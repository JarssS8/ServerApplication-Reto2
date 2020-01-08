/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.HashSet;
import java.util.Set;
import javax.ejb.Stateless;
import java.util.logging.Logger;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import serverapplication.entities.Admin;
import serverapplication.entities.Free;
import serverapplication.entities.Premium;
import serverapplication.entities.User;
import serverapplication.exceptions.LoginAlreadyExistsException;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.exceptions.ServerConnectionErrorException;
import serverapplication.exceptions.UserPasswordNotFoundException;
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
     * @throws ServerConnectionErrorException When there's an error at the
     * server.
     */
    @Override
    public void createUser(User user) throws LoginAlreadyExistsException,
            ServerConnectionErrorException {
        // El metodo recibe un user de inicio y nosotros le cargamos los datos de free que necesitemos.
        Free free = null;
        try {
            // Instanciamos un nuevo free y le pasamos el user en el constructor.
            free = new Free(user);
            // Cargamos el atributo propio de free.
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
            throw new ServerConnectionErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
    }

    // NECESITAMOS UN METODO @PUT EN RESTFUL PARA CADA TIPO DE USUARIO. EL METODO RECIBIRA EL TIPO DE USER QUE VAYA A SER.
    // public void mod(Free free), public void mod(Premium premium), public void mod(Admin admin).
    @Override
    public void modifyUserData(Free free) throws ServerConnectionErrorException {
        try {
            // FALTA METODO PARA CAMBIAR LA CONTRASEÑA
            em.merge(free);
            em.flush();

        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }

    }

    @Override
    public void modifyUserData(Premium premium) throws ServerConnectionErrorException {
        try {
            // FALTA METODO PARA CAMBIAR LA CONTRASEÑA
            em.merge(premium);
            em.flush();
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
    }

    @Override
    public void modifyUserData(Admin admin) throws ServerConnectionErrorException {
        try {
            // FALTA METODO PARA CAMBIAR LA CONTRASEÑA
            em.merge(admin);
            em.flush();
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
    }

    @Override
    public void deleteFree(Free free) {
        Query query = em.createQuery("DELETE FROM User U WHERE U.login = :login");
        query.setParameter("login", free.getLogin() );
        query.executeUpdate();
    }
    
    @Override
    public void deletePremium(Premium premium) {
        Query query = em.createQuery("DELETE FROM User U WHERE U.login = :login");
        query.setParameter("login", premium.getLogin() );
        query.executeUpdate();
    }
    
    @Override
    public void deleteAdmin(Admin admin) {
        Query query = em.createQuery("DELETE FROM User U WHERE U.login = :login");
        query.setParameter("login", admin.getLogin() );
        query.executeUpdate();
    }

    @Override
    public Object findUserById(Long id) {
        Object user = null;
        Object auxUser = null;
        user = (Object) em.find(User.class, id);
        if (user instanceof Premium) {
            auxUser = (Premium) user;
        }

        if (user instanceof Free) {
            auxUser = (Free) user;
        }

        if (user instanceof Admin) {
            auxUser = (Admin) user;
        }
        return auxUser;
    }

    @Override
    public Object findUserByLogin(String login) {
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
    public void banUser(User user) {
        // ¿LO QUITAMOS?
    }

    @Override
    public void modifyUserToFree(Free free)
            throws LoginNotFoundException, ServerConnectionErrorException {

        em.merge(free);
        em.flush();
    }
    
    @Override
    public void modifyUserToPremium(Premium premium) 
            throws LoginNotFoundException, ServerConnectionErrorException {
        
        em.merge(premium);
        em.flush();
    }

    @Override
    public void modifyUserToAdmin(Admin admin)
            throws LoginNotFoundException, ServerConnectionErrorException {

        em.merge(admin);
        em.flush();
    }

    @Override
    public User logIn(User user) throws LoginNotFoundException,
            UserPasswordNotFoundException,
            ServerConnectionErrorException {
        User auxUser = null;
        try {
            auxUser = (User) em.createNamedQuery(
                    "findUserByLogin").setParameter("login", user.getLogin())
                    .getSingleResult();
            if (auxUser != null) {
                String passw = (String) em.createNamedQuery(
                        "findPasswordByLogin").setParameter(
                                "login", user.getLogin()).getSingleResult();
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
        }
        return auxUser;
    }

    @Override
    public User signUp(User user) {

        return user;
    }

    @Override
    public void logOut() {
    }

}
