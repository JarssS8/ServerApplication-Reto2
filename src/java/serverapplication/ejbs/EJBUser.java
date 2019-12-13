/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.List;
import javax.ejb.Stateless;
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
    
    //Necesitamos esta anotacion para injectar el EntityManager
    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    private EntityManager em;

    public void createUser(User user) {
        em.persist(user);
    }

    public void modifyUserData(User user) {
        em.merge(user);
        //Con flush hacemos que los cambios se reflejen al instante
        em.flush();
    }

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
     * @param free A Free object.
     * @return A Premium object.
     */
    @Override
    public Premium modifyFreeToPremium(Free free) {
        
        return new Premium();
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
    public User logIn(User user) throws LoginNotFoundException, UserPasswordNotFoundException , ServerConnectionErrorException {
        User auxUser = (User) em.createNamedQuery("findUserByLogin").setParameter("login", user.getLogin()).getSingleResult();
        if (auxUser != null) {
            String passw = (String) em.createNamedQuery("findPasswordByLogin").setParameter("login", user.getLogin()).getSingleResult();
            if (!user.getPassword().equals(passw)) {
                throw new UserPasswordNotFoundException();
            } else {
                
            }
        } else {
            throw new LoginNotFoundException();
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
