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
import serverapplication.exceptions.LoginAlreadyExistsException;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.exceptions.GenericServerErrorException;
import serverapplication.exceptions.UserPasswordNotFoundException;
import serverapplication.exceptions.UserNotFoundException;
import serverapplication.interfaces.EJBUserLocal;
import serverapplication.utilities.EmailSender;
import serverapplication.utilities.EncriptationAsymmetric;
import serverapplication.utilities.EncryptationLocal;

/**
 *
 * @author aimar
 */
@Stateless
public class EJBUser implements EJBUserLocal {

    private static final Logger LOGGER = Logger.getLogger("serverapplication.ejbs.EJBUser");

    //Necesitamos esta anotacion para injectar el EntityManager
    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    
    public void setEm(EntityManager em) {
        this.em = em;
    }
    
    private final String[] method = new String[]{"FORGOT_PASSWORD", "MODIFY_PASSWORD"};
    
    private EntityManager em;
    
    private EJBDocumentRating ejbDocument = new EJBDocumentRating();

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
        }
    }


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
            ex.printStackTrace();
            LOGGER.warning(ex.getMessage());
            throw new UserNotFoundException();
        }

    }

    @Override
    public User findUserById(Long id) throws UserNotFoundException {
        User user = null;
        user = em.find(User.class, id);
        if (user == null) {
            LOGGER.warning("EJBUser: User not found...");
            throw new UserNotFoundException();
        }
        return user;
    }

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

    @Override
    public Set<User> findAllUsers() {
        return new HashSet<>(em.createNamedQuery("findAllUsers").getResultList());
    }

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
                /*for (Rating rating : auxPremium.getRatings()) {
                    if (!em.contains(rating)) {
                        ejbDocument.setEm(em);
                        rating.setDocument(ejbDocument.findDocumentById(rating.getId().getIdDocument()));
                        rating.setUser((User) premium);
                        em.merge(rating);
                    }
                }*/
                em.merge(auxPremium);
                em.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GenericServerErrorException(ex.getMessage());
        }

    }

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

    @Override
    public Set<Rating> findRatingsOfUser(Long id) throws GenericServerErrorException {
        Set<Rating> ratings = null;
        try {
            ratings = new HashSet<Rating>(
                em.createNamedQuery("findRatingsOfUser")
                    .setParameter("id", id)
                    .getResultList());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return ratings;
    }

    @Override
    public Set<Document> findDocumentsOfUser(Long id) throws GenericServerErrorException {
        Set<Document> documents = null;
        try {
            documents = new HashSet<Document>(
                em.createNamedQuery("findDocumentsOfUser")
                    .setParameter("id", id)
                    .getResultList());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
        return documents;
    }

    @Override
    public Set<Group> findGroupsOfUser(Long id) throws GenericServerErrorException {
        Set<Group> groups = null;
        try {
            groups = new HashSet<Group>(
                em.createNamedQuery("findGroupsOfUser")
                    .setParameter("id", id)
                    .getResultList());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
        return groups;
    }

    @Override
    public Set<Group> findGroupsRuledByUser(Long id) throws GenericServerErrorException {
        User user = null;

        try {
            user = em.find(User.class,
                    id);
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
        return new HashSet<>(user.getGroups());
    }

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
        }
    }

    @Override
    public String findPrivilegeOfUserByLogin(String login)
            throws LoginNotFoundException, GenericServerErrorException {
        String privilege = null;
        try {
            privilege = em.createQuery("SELECT u.privilege FROM User u WHERE u.login = :login")
                .setParameter("login", login).getSingleResult().toString();
        } catch (NoResultException ex) {
            LOGGER.warning(privilege);
            LOGGER.warning(ex.getMessage());
            throw new LoginNotFoundException();
        } catch (Exception ex) {
            LOGGER.warning(privilege);
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException();
        }
        return privilege;
    }
    
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
    
    
    @Override
    public String getPublicKey() throws GenericServerErrorException{
        String publicKey="";
        try {
            publicKey = EncriptationAsymmetric.getPublic();
        } catch (IOException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return publicKey;
    }

    private String generateRandomPassword() {
        String NUMBERS = "0123456789";
        String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
        String pswd = "", value = "";
        String key = NUMBERS + UPPERCASE + LOWERCASE;
        boolean upper = false, numbers = false;
        do {
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

    private void sendEmail(String method, String password, String email) throws MessagingException {
        String message = "";
        String subject = "";

        switch (method) {
            case "FORGOT_PASSWORD":
                subject = "Forgotten password in toLearn() Application";
                message = "From this moment you have 30 minutes to access with the "
                        + "generated password. \n If you don't access in the next 30 minutes "
                        + "you couldn't access.\n Generated Password: " + password;
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