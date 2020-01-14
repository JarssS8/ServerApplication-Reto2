/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.ejb.Stateless;
import java.util.logging.Logger;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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

/**
 *
 * @author aimar
 */
@Stateless
public class EJBUser implements EJBUserLocal {

    private static final Logger LOGGER = Logger.getLogger("serverapplication.ejbs.EJBUser");

    //Necesitamos esta anotacion para injectar el EntityManager
    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    
    private EntityManager em;

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
    public void createUser(User user) throws LoginAlreadyExistsException,
            GenericServerErrorException {
        // El metodo recibe un user de inicio y nosotros le cargamos los datos de free que necesitemos.
        Free free = null;
        try {
            // Instanciamos un nuevo free y le pasamos el user en el constructor.
            free = new Free(user);
            // Cargamos el atributo propio de free.
            Random random = new Random();
            Long randId = random.nextLong();
            while (randId < 1 || em.find(User.class, randId) != null) {
                randId = random.nextLong();
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
        } catch (NoResultException ex) { // Login is not taken
            // Login disponible, procedemos a crear el usuario.
            LOGGER.warning("Login available... Creating new user...");
            em.persist(free);
            LOGGER.warning("New user created successfully...");
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ex) {
            LOGGER.warning(ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
    }

    // NECESITAMOS UN METODO @PUT EN RESTFUL PARA CADA TIPO DE USUARIO. EL METODO RECIBIRA EL TIPO DE USER QUE VAYA A SER.
    // public void mod(Free free), public void mod(Premium premium), public void mod(Admin admin).
    @Override
    public void modifyUserData(User user) throws GenericServerErrorException {
        try {
            em.createNamedQuery("modifyUserData")
                    .setParameter("email", user.getEmail())
                    .setParameter("fullName", user.getFullName())
                    .setParameter("id", user.getId())
                    .executeUpdate();
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }

    }

    @Override
    public void deleteUser(User user) throws UserNotFoundException {
        try {
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
    public Object findUserByLogin(String login
    ) {
        Object user = null;
        Object auxUser = null;
        try {
            user = em.createNamedQuery("findUserByLogin").setParameter(
                    "login", login).getSingleResult();
            if (user instanceof Premium) {
                auxUser = (Premium) user;
            }

            if (user instanceof Free) {
                auxUser = (Free) user;
            }

            if (user instanceof Admin) {
                auxUser = (Admin) user;
            }
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
        return auxUser;
    }

    @Override
    public Set<User> findAllUsers() {
        return new HashSet<>(em.createNamedQuery("findAllUsers").getResultList());
    }
    
    @Override
    public void modifyFreeToPremium(Premium premium)
            throws LoginNotFoundException, GenericServerErrorException {
        try {
            Free free = em.find(Free.class, premium.getId());
            premium.setPrivilege(Privilege.PREMIUM);
            premium.setBeginSub(Timestamp.valueOf(LocalDateTime.now()));
            if (em.contains(free)) {
                em.remove(free);
                em.merge(premium);
                em.flush();
            }
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }

    }

    @Override
    public void modifyFreeToAdmin(User user)
            throws LoginNotFoundException, GenericServerErrorException {
        try {
            Free free = em.find(Free.class, user.getId());
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
        }

    }

    @Override
    public void modifyPremiumToFree(User user)
            throws LoginNotFoundException, GenericServerErrorException {
        try {
            Premium premium = em.find(Premium.class, user.getId());
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
        }

    }

    @Override
    public void modifyPremiumToAdmin(User user)
            throws LoginNotFoundException, GenericServerErrorException {
        try {
            Premium premium = em.find(Premium.class, user.getId());
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
        }

    }

    @Override
    public void modifyAdminToFree(User user)
            throws LoginNotFoundException, GenericServerErrorException {
        try {
            Admin admin = em.find(Admin.class, user.getId());
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
        }

    }
    
    @Override
    public Set<Rating> findRatingsOfUser(Long id) {
        Set<Rating> ratings = null;
        try {
            ratings = new HashSet<Rating>(em.createQuery("findRatingsOfUser").setParameter("id", id).getResultList());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
        return ratings;
    }
    
    @Override
    public Set<Document> findDocumentsOfUser(Long id) {
        Set<Document> documents = null;
        try {
            documents = new HashSet<Document>(em.createQuery("findDocumentsOfUser").setParameter("id", id).getResultList());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
        return documents;
    }
    
    @Override
    public Set<Group> findGroupsOfUser(Long id) {
        Set<Group> groups = null;
        try {
            groups = new HashSet<Group>(em.createQuery("findGroupsOfUser").setParameter("id", id).getResultList());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
        return groups;
    }
    
    @Override
    public Set<Group> findGroupsRuledByUser(Long id) {
        User user = null;
        try {
            user = em.find(User.class, id);
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
        return new HashSet<> (user.getGroups());
    }

    @Override
    public User logIn(User user) throws LoginNotFoundException,
            UserPasswordNotFoundException, GenericServerErrorException {
        User auxUser = null;
        try {
            auxUser = (User) em.createNamedQuery(
                    "findUserByLogin").setParameter("login", user.getLogin())
                    .getSingleResult();
            if (auxUser != null) {
                String passw = (String) em.createNamedQuery(
                        "findPasswordByLogin").setParameter(
                                "login", user.getLogin()).setParameter(
                        "password", user.getPassword())
                        .getSingleResult();
                if (!user.getPassword().equals(passw)) {
                    LOGGER.warning("EJBUser: Password does not match...");
                    throw new UserPasswordNotFoundException();
                } else {
                    LOGGER.info("Both login and password match");
                }
            } else {
                LOGGER.warning("EJBUser: Login not found...");
                throw new LoginNotFoundException();
            }
        } catch (LoginNotFoundException ex) {
            LOGGER.warning(ex.getMessage());
            throw new LoginNotFoundException();
        } catch (UserPasswordNotFoundException ex) {
            LOGGER.warning(ex.getMessage());
            throw new UserPasswordNotFoundException();
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
        return auxUser;
    }
    
    @Override
    public User signUp(User user) throws LoginAlreadyExistsException, 
            GenericServerErrorException {
        return user;
        
    }

    @Override
    public void logOut() {
    }

}
