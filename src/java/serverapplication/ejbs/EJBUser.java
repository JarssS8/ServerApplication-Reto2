/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.ejb.Stateless;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import serverapplication.entities.Admin;
import serverapplication.entities.Document;
import serverapplication.entities.Free;
import serverapplication.entities.Group;
import serverapplication.entities.Premium;
import serverapplication.entities.Privilege;
import serverapplication.entities.Rating;
import serverapplication.entities.Status;
import serverapplication.entities.User;
import serverapplication.exceptions.DocumentNotFoundException;
import serverapplication.exceptions.LoginAlreadyExistsException;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.exceptions.GenericServerErrorException;
import serverapplication.exceptions.GroupNameNotFoundException;
import serverapplication.exceptions.UserPasswordNotFoundException;
import serverapplication.exceptions.UserNotFoundException;
import serverapplication.interfaces.EJBDocumentRatingLocal;
import serverapplication.interfaces.EJBUserLocal;
import serverapplication.utilities.EmailSender;
import serverapplication.utilities.EncriptationAsymmetric;
import serverapplication.utilities.EncryptationLocal;

/**
 * EJB class for managing User operations.
 * @author aimar
 */
@Stateless
public class EJBUser implements EJBUserLocal {

    private static final Logger LOGGER = Logger.getLogger("serverapplication.ejbs.EJBUser");

    private final String[] method = new String[]{"FORGOT_PASSWORD", "MODIFY_PASSWORD"};

    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    private EntityManager em;

    public void setEM(EntityManager em) {
        this.em = em;
    }

    /**
     * This method creates a new Free user. Checks if the login's taken and if
     * it's not, inserts the user via EntityManager.
     *
     * @param user The user object.
     * @throws LoginAlreadyExistsException When login is already taken by
     * another user.
     * @throws GenericServerErrorException When there's an error at the server.
     */
    @Override
    public Free createUser(User user) throws LoginAlreadyExistsException,
        GenericServerErrorException {
        // El metodo recibe un user de inicio y nosotros le cargamos los datos de free que necesitemos.
        Free free = null;
        try {
            // Instanciamos un nuevo free y le pasamos el user en el constructor.
            free = new Free(user);
            // Cargamos el atributo propio de free.
            Random random = new Random();
            Long randId = new Long(random.nextInt());
            while (randId < 1 || em.find(User.class, randId) != null) {
                randId++;
            }
            free.setId(randId);
            free.setPrivilege(Privilege.FREE);
            free.setStatus(Status.ENABLED);
            free.setTimeOnline(0);
            String newPassword = free.getPassword();
            newPassword = EncriptationAsymmetric.decrypt(newPassword);
            newPassword = EncryptationLocal.encryptPass(newPassword);
            free.setPassword(newPassword);
            // Guardamos en un objeto User el resultado de la busqueda del login.
            // Si existe lanzamos excepcion LoginAlreadyExists.
            // Si no existe capturamos la excepcion NoFoundException y dentro hacemos el em.persist(free).
            User auxUser = (User) em.createNamedQuery("findUserByLogin")
                .setParameter("login", user.getLogin()).getSingleResult();
            if (auxUser.getLogin().equals(user.getLogin())) {
                LOGGER.warning("Login already exists...");
                // El login ya existe en la base de datos.
                throw new LoginAlreadyExistsException("Login Already Exists...");
            }
        } catch (NoResultException ex) { // Login is AVAILABLE
            // Login disponible, procedemos a crear el usuario.
            LOGGER.warning("Login available... Creating new user...");
            em.persist(free);
            em.flush();
            LOGGER.warning("New user created successfully...");
        } catch (LoginAlreadyExistsException ex) {
            LOGGER.warning("EJBUser: " + ex.getMessage());
            throw new LoginAlreadyExistsException(ex.getMessage());
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ex) {
            LOGGER.warning("EJBUser: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("EJBUser: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return free;
    }

    /**
     * This method modify the common user data for all users of the app. Checks if the password
     * is being modified for send and email.
     * @param user User with new data
     * @throws GenericServerErrorException 
     */
    @Override
    public void modifyUserData(User user) throws GenericServerErrorException {
        User auxUser;
        String password;
        try {
            em.createNamedQuery("modifyUserData")
                .setParameter("email", user.getEmail())
                .setParameter("fullName", user.getFullName())
                .setParameter("id", user.getId())
                .executeUpdate();

            auxUser = findUserById(user.getId());
            password = user.getPassword();
            password = EncriptationAsymmetric.decrypt(password);
            password = EncryptationLocal.encryptPass(password);
            if (!auxUser.getPassword().equalsIgnoreCase(password)) {
                em.createNamedQuery("modifyUserPassword")
                    .setParameter("password", user.getPassword())
                    .setParameter("id", user.getId())
                    .executeUpdate();
                sendEmail(method[1], null, user.getEmail());
            }

        } catch (Exception ex) {
            LOGGER.warning("EJBUser: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
    }

    /**
     * Delete one user of the application
     * @param user User who is going to be removed
     * @throws UserNotFoundException 
     */
    @Override
    public void deleteUser(User user) throws UserNotFoundException {
        try {
            Query q = em.createQuery("DELETE FROM Document d WHERE d.user = :user");
            q.setParameter("user", user);
            q.executeUpdate();
            switch (user.getPrivilege().toString()) {
                case "FREE": {

                    em.remove(em.find(Free.class, user.getId()));
                    em.flush();
                    em.remove(user);
                    break;

                }
                case "PREMIUM": {
                    em.remove(em.find(Premium.class, user.getId()));
                    em.flush();
                    em.remove(user);
                    break;

                }
                case "ADMIN": {
                    em.remove(em.find(Admin.class, user.getId()));
                    em.flush();
                    em.remove(user);
                    break;
                }
            }
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new UserNotFoundException();
        }

    }

    /**
     * Find one user using his id
     * @param id Long for search the user
     * @return User find with that id
     * @throws UserNotFoundException 
     */
    @Override
    public User findUserById(Long id) throws UserNotFoundException, GenericServerErrorException {
        User user = null;
        try {
            user = em.find(User.class, id);
            if (user == null) {
                LOGGER.warning("EJBUser: User not found...");
                throw new UserNotFoundException();
            }
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return user;
    }

    /**
     * Find one user using his login
     * @param login String for search the user by login
     * @return User find with that login
     * @throws LoginNotFoundException
     * @throws GenericServerErrorException 
     */
    @Override
    public User findUserByLogin(String login) throws LoginNotFoundException,
        GenericServerErrorException {
        User user = null;
        try {
            user = (User) em.createNamedQuery("findUserByLogin").setParameter(
                "login", login).getSingleResult();
        } catch (NoResultException ex) {
            LOGGER.warning("EJBUser: Login not found..." + ex.getMessage());
            throw new LoginNotFoundException();
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
        return user;
    }

    /**
     * Find all user on the Database
     * @return Set with all the users from the database
     */
    @Override
    public Set<User> findAllUsers() {
        return new HashSet<>(em.createNamedQuery("findAllUsers").getResultList());
    }

    /**
     * Change privilege from one free user and become in one Premium
     * @param premium Premium object with the new data required for become in premium
     * @throws GenericServerErrorException 
     */
    @Override
    public void modifyFreeToPremium(Premium premium) throws GenericServerErrorException {
        try {
            Free free = em.find(Free.class, premium.getId());
            Premium auxPremium = new Premium((User) premium);
            auxPremium.setAutorenovation(true);
            auxPremium.setPrivilege(Privilege.PREMIUM);
            auxPremium.setBeginSub(Timestamp.valueOf(LocalDateTime.now()));
            auxPremium.setCardNumber(premium.getCardNumber());
            auxPremium.setCvc(premium.getCvc());
            auxPremium.setExpirationMonth(premium.getExpirationMonth());
            auxPremium.setExpirationYear(premium.getExpirationYear());
            if (em.contains(free)) {
                em.remove(free);
                em.flush();
                em.merge(auxPremium);
                em.flush();
            }
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }

    }

    /**
     * Change privilege from one free user and become in one Admin
     * @param user User object who is going to be Admin
     * @throws GenericServerErrorException 
     */
    @Override
    public void modifyFreeToAdmin(User user) throws GenericServerErrorException {
        try {
            Free free = em.find(Free.class,
                user.getId());
            Admin admin = new Admin(user);
            admin.setPrivilege(Privilege.ADMIN);
            admin.setAdminDate(Timestamp.valueOf(LocalDateTime.now()));
            if (em.contains(free)) {
                em.remove(free);
                em.merge(admin);
                em.flush();
            }
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }

    }

    /**
     * Change privilege from one Prrmium user and become in one Free
     * @param user User object who is going to be Free
     * @throws GenericServerErrorException 
     */
    @Override
    public void modifyPremiumToFree(User user) throws GenericServerErrorException {
        try {
            Premium premium = em.find(Premium.class,
                user.getId());
            Free free = new Free(user);
            free.setPrivilege(Privilege.FREE);
            free.setTimeOnline(0);
            if (em.contains(premium)) {
                em.remove(premium);
                em.merge(free);
                em.flush();
            }
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }

    }

    /**
     * Change privilege from one Prrmium user and become in one Admin
     * @param user User object who is going to be Admin
     * @throws GenericServerErrorException 
     */
    @Override
    public void modifyPremiumToAdmin(User user) throws GenericServerErrorException {
        try {
            Premium premium = em.find(Premium.class,
                user.getId());
            Admin admin = new Admin(user);
            admin.setPrivilege(Privilege.ADMIN);
            admin.setAdminDate(Timestamp.valueOf(LocalDateTime.now()));
            if (em.contains(premium)) {
                em.remove(premium);
                em.merge(admin);
                em.flush();
            }
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }

    }

    /**
     * Change privilege from one Admin user and become in one Free
     * @param user User object who is going to be Free
     * @throws GenericServerErrorException 
     */
    @Override
    public void modifyAdminToFree(User user) throws GenericServerErrorException {
        try {
            Admin admin = em.find(Admin.class,
                user.getId());
            Free free = new Free(user);
            free.setPrivilege(Privilege.FREE);
            free.setTimeOnline(0);
            if (em.contains(admin)) {
                em.remove(admin);
                em.merge(free);
                em.flush();
            }
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }

    }

    /**
     * Find all ratings did it by that user
     * @param id Long of the user id used for find the Ratings
     * @return Set with all the ratings made it from that user id
     * @throws GenericServerErrorException 
     */
    @Override
    public Set<Rating> findRatingsOfUser(Long id) throws GenericServerErrorException {
        String privilege;
        Premium premium = null;
        Admin admin = null;
        Free free = null;
        Set<Rating> ratings = null;
        try {
            privilege = em.createNamedQuery("findUserPrivilegeById")
                .setParameter("id", id).getSingleResult().toString();
            switch (privilege) {
                case "PREMIUM": {
                    premium = em.find(Premium.class, id);
                    ratings = premium.getRatings();
                    break;
                }
                case "ADMIN": {
                    admin = em.find(Admin.class, id);
                    ratings = admin.getRatings();
                    break;
                }
                default: {
                    free = em.find(Free.class, id);
                    ratings = free.getRatings();
                }
            }
        } catch (NoResultException ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return ratings;
    }

    /**
     * Find all the documents uploaded by one user
     * @param id Long with the id of the user
     * @return Set of documents from the user with that id
     * @throws GenericServerErrorException 
     */
    @Override
    public Set<Document> findDocumentsOfUser(Long id) throws DocumentNotFoundException, GenericServerErrorException {
        String privilege;
        Premium premium = null;
        Admin admin = null;
        Free free = null;
        Set<Document> documents = null;
        try {
            privilege = em.createNamedQuery("findUserPrivilegeById")
                .setParameter("id", id).getSingleResult().toString();
            switch (privilege) {
                case "PREMIUM": {
                    premium = em.find(Premium.class, id);
                    documents = premium.getDocuments();
                    break;
                }
                case "ADMIN": {
                    admin = em.find(Admin.class, id);
                    documents = admin.getDocuments();
                    break;
                }
                default: {
                    free = em.find(Free.class, id);
                    documents = free.getDocuments();
                }
            }
        } catch (NoResultException ex) {
            LOGGER.warning(ex.getMessage());
            throw new DocumentNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return documents;
    }

    /**
     * Find all the groups from one user by id
     * @param id Long id from the user
     * @return Set with the groups of that user
     * @throws GenericServerErrorException 
     */
    @Override
    public Set<Group> findGroupsOfUser(Long id) throws GroupNameNotFoundException, GenericServerErrorException {
        String privilege;
        Premium premium = null;
        Admin admin = null;
        Free free = null;
        Set<Group> groups = null;
        try {
            privilege = em.createNamedQuery("findUserPrivilegeById")
                .setParameter("id", id).getSingleResult().toString();
            switch (privilege) {
                case "PREMIUM": {
                    premium = em.find(Premium.class, id);
                    groups = premium.getGroups();
                    break;
                }
                case "ADMIN": {
                    admin = em.find(Admin.class, id);
                    groups = admin.getGroups();
                    break;
                }
                default: {
                    free = em.find(Free.class, id);
                    groups = free.getGroups();
                }
            }
        } catch (NoResultException ex) {
            LOGGER.warning(ex.getMessage());
            throw new GroupNameNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return groups;
    }

    /**
     * Method that checks if the password is correct checking with the database
     * @param login String with the login of the user
     * @param password String with the password of the user
     * @return User that have that both parameters
     * @throws UserPasswordNotFoundException
     * @throws GenericServerErrorException 
     */
    @Override
    public User checkPassword(String login, String password)
        throws UserPasswordNotFoundException, GenericServerErrorException {
        User user = null;
        try {
            password = EncriptationAsymmetric.decrypt(password);
            password = EncryptationLocal.encryptPass(password);
            user = (User) em.createNamedQuery("findPasswordByLogin")
                .setParameter("login", login)
                .setParameter("password", password)
                .getSingleResult();
        } catch (NoResultException ex) {
            LOGGER.warning("EJBUser: Password not found..." + ex.getMessage());
            throw new UserPasswordNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException();
        }
        return user;
    }

    /**
     * Method that save the payment method for one Premium user
     * @param premium The user with the payment data
     * @throws GenericServerErrorException 
     */
    @Override
    public void savePaymentMethod(Premium premium) throws GenericServerErrorException {
        try {
            Query q = em.createQuery("UPDATE Premium p SET p.cardNumber = :cardNumber, "
                + "p.cvc = :cvc, p.expirationMonth = :expirationMonth, "
                + "p.expirationYear = :expirationYear WHERE p.id = :id");
            q.setParameter("cardNumber", premium.getCardNumber());
            q.setParameter("cvc", premium.getCvc());
            q.setParameter("expirationMonth", premium.getExpirationMonth());
            q.setParameter("expirationYear", premium.getExpirationYear());
            q.setParameter("id", premium.getId());
            q.executeUpdate();
            em.flush();
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
    }

    /**
     * Find the privilege of one user using his login
     * @param login String with the login of one user
     * @return String with the privilege of the found user
     * @throws LoginNotFoundException
     * @throws GenericServerErrorException 
     */
    @Override
    public String findPrivilegeOfUserByLogin(String login)
        throws LoginNotFoundException, GenericServerErrorException {
        String privilege = null;
        try {
            privilege = em.createNamedQuery("findUserPrivilegeByLogin")
                .setParameter("login", login).getSingleResult().toString();
        } catch (NoResultException ex) {
            LOGGER.warning(ex.getMessage());
            throw new LoginNotFoundException();
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException();
        }
        return privilege;
    }
    
    /**
     * Find the privilege of one user using his id
     * @param id Long with the id of one user
     * @return String with the privilege of the found user
     * @throws LoginNotFoundException
     * @throws GenericServerErrorException 
     */
    @Override
    public String findPrivilegeOfUserById(Long id)
        throws LoginNotFoundException, GenericServerErrorException {
        String privilege = null;
        try {
            privilege = em.createNamedQuery("findUserPrivilegeById")
                .setParameter("id", id).getSingleResult().toString();
        } catch (NoResultException ex) {
            LOGGER.warning(ex.getMessage());
            throw new LoginNotFoundException();
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException();
        }
        return privilege;
    }
    
    /**
     * Method that restore the password for someone who forgot it and send it by email
     * @param email String email from user where is going to be send the email
     * @throws UserNotFoundException
     * @throws GenericServerErrorException 
     */
    @Override
    public void restorePassword(String email) throws UserNotFoundException, GenericServerErrorException {
        User user;
        String password = generateRandomPassword();

        try {
            user = (User) em.createNamedQuery("findUserByEmail")
                .setParameter("email", email)
                .getSingleResult();

            em.createNamedQuery("modifyUserPassword")
                .setParameter("password", EncryptationLocal.encryptPass(password))
                .setParameter("id", user.getId())
                .executeUpdate();
            sendEmail(method[0], password, email);
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new UserNotFoundException(ex.getMessage());
        }
    }
    
    
   /**
    * Method that send the public key from the Encryptation class 
    * @return String with the public key in hexadecimal
    * @throws GenericServerErrorException 
    */
    @Override
    public String getPublicKey() throws GenericServerErrorException {
        String publicKey = "";
        try {
            publicKey = EncriptationAsymmetric.getPublic();
        } catch (IOException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return publicKey;
    }

    /**
     * Generate a new random password with at least one uppercase and one number for a user who forget it.
     * The length of the new password always is going to be 10
     * @return String the new password generated
     */
    private String generateRandomPassword() {
        String NUMBERS = "0123456789";
        String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
        String pswd = "", value = "";
        String key = NUMBERS + UPPERCASE + LOWERCASE;
        boolean upper = false, numbers = false;
        do {
            pswd="";
            for (int i = 0; i < 10; i++) {
                value = String.valueOf(key.charAt((int) (Math.random() * key.length())));
                if (UPPERCASE.contains(value)) {
                    upper = true;
                } else if (NUMBERS.contains(value)) {
                    numbers = true;
                }
                pswd += value;
            }
        } while (!upper || !numbers);
        return pswd;
    }

    /**
     * Method that execute the action to send the information to the class who send the emails
     * @param method String with who email type is going to be send
     * @param password String with new generated password for one user in case that was a forgot password email
     * @param email String with the email of the user where the email is going to be send
     * @throws MessagingException
     * @throws IOException
     * @throws Exception 
     */
    private void sendEmail(String method, String password, String email) 
            throws MessagingException, IOException, Exception {
        String message = "";
        String subject = "";

        switch (method) {
            case "FORGOT_PASSWORD":
                subject = "Forgotten password in toLearn() Application";
                message = "Now you can access with the "
                        + "generated password.\n Generated Password: " + password;
                break;
            case "MODIFY_PASSWORD":
                subject = "Succesfully modified password in toLearn() Application";
                message = "The password was successfully modified, now you can access to your account with the new password";
                break;
        }
        EmailSender emailSender = new EmailSender();
        emailSender.sendMail(email, subject, message);
    }

}
