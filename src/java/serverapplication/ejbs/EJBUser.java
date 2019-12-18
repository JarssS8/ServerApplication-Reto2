/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import serverapplication.entities.Admin;
import serverapplication.entities.Free;
import serverapplication.entities.Premium;
import serverapplication.entities.User;
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

    @Override
    public void createUser(User user) {
        em.persist(user);
    }

    @Override
    public void modifyUserData(User user) {
        em.merge(user);
        //Con flush hacemos que los cambios se reflejen al instante
        em.flush();
    }

    @Override
    public void deleteUser(User user) {
        Query query = em.createQuery("DELETE FROM User U WHERE U.login = :login");
        query.setParameter("login", user.getLogin());
        query.executeUpdate();
    }
    
    @Override
    public User findUserById(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public User findUserByLogin(String login) {
        return (User) em.createNamedQuery("findUserByLogin").setParameter(
                "login", login).getSingleResult();
    }

    @Override
    public List<User> findAllUsers() {
        return em.createNamedQuery("findAllUsers").getResultList();
    }

    @Override
    public void banUser(User user) {
        // ¿LO QUITAMOS?
    }

    /**
     * This method 
     *
     */
    @Override
    public void modifyFreeToPremium(User user) 
            throws LoginNotFoundException, ServerConnectionErrorException {
        
        user = new Premium();
        em.merge(user);
        em.flush();
    }

    @Override
    public Free modifyPremiumToFree(Premium premium) {

        return new Free();
    }

    @Override
    public Free modifyAdminToFree(Admin admin) {

        return new Free();
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
