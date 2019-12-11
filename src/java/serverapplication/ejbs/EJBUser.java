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
import serverapplication.entities.User;

/**
 *
 * @author aimar
 */
@Stateless
public class EJBUser {
    
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

    public User findUserByName(String name) {
        return em.
    }

    public List<User> findAllUsers() {
        return em.createNamedQuery("findAllUsers").getResultList();
    }

}
