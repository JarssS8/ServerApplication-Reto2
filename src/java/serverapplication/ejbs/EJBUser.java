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
        em.remove(em.merge(user));
    }

    @Override
    public User findUserByLogin(String name) {
        return (User) em.createNamedQuery("findUserByLogin").setParameter(
                "name", name).getSingleResult();
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
     * @param free A Free object.
     * @return A Premium object.
     */
    @Override
    public Premium modifyFreeToPremium(Free free, Long cardNumber, int month, 
            int year, int cvc) throws LoginNotFoundException, 
            ServerConnectionErrorException {
        free.setTimeOnline(0);
        Premium premium = new Premium();
        /*auxUser = (Free) em.createNamedQuery("findUserByLogin")
        .setParameter("login", free.getLogin()).getSingleResult();
        */
        
        //(( (User) (Free) premium)).setTimeOnline(0);
        premium.setLogin(free.getLogin());
        premium.setEmail(free.getEmail());
        premium.setFullName(free.getFullName());
        premium.setStatus(free.getStatus());
        premium.setPrivilege(free.getPrivilege());
        premium.setPassword(free.getPassword());
        premium.setProfilePicture(free.getProfilePicture());
        premium.setLastAccess(free.getLastAccess());
        premium.setLastPasswordChange(free.getLastPasswordChange());
        premium.setRatings(free.getRatings());
        premium.setDocuments(free.getDocuments());
        premium.setGroups(free.getGroups());
        premium.setAdminGroups(free.getAdminGroups());
        // New data for the premium user
        premium.setCardNumber(cardNumber);
        premium.setExpirationMonth(month);
        premium.setExpirationYear(year);
        premium.setCvc(cvc);
        modifyUserData(premium);
        /*
        em.createNamedQuery("updateFreeToPremium").setParameter(
        "cardNumber", cardNumber).setParameter("month", month)
        .setParameter("year", year).setParameter("cvc", cvc)
        .setParameter("login", free.getLogin());
        */
        return premium;
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
    public User logIn(User user) throws LoginNotFoundException, UserPasswordNotFoundException, ServerConnectionErrorException {
        User auxUser = null;
        try {
            auxUser = (User) em.createNamedQuery("findUserByLogin").setParameter("login", user.getLogin()).getSingleResult();
            if (auxUser != null) {
                String passw = (String) em.createNamedQuery("findPasswordByLogin").setParameter("login", user.getLogin()).getSingleResult();
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
