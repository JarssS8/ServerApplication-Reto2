/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import serverapplication.entities.Group;
import serverapplication.entities.User;
import serverapplication.exceptions.GroupNameAlreadyExistException;
import serverapplication.exceptions.GroupNameNotFoundException;
import serverapplication.exceptions.GroupPasswordNotFoundException;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.interfaces.EJBGroupLocal;

/**
 *
 * @author Diego Urraca
 */
@Stateless
public class EJBGroups implements EJBGroupLocal {

    private static final Logger LOGGER = Logger.getLogger("serverapplication.ejbs.EJBGroups");
    
    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    private EntityManager em;

    @Override
    public void createGroup(Group group)throws GroupNameAlreadyExistException, LoginNotFoundException, Exception {
        em.persist(group);
    }
    
    @Override
    public void modifyGroup(Group group) throws LoginNotFoundException, Exception{
        em.merge(group);
        em.flush();
    }
    
    @Override
    public void joinGroup(String groupName,String password, User user) throws GroupPasswordNotFoundException, LoginNotFoundException, GroupNameNotFoundException, Exception{
        Group auxGroup = null;
        auxGroup = (Group) em.createNamedQuery("findGroupByNameAndPass").
                setParameter("groupName", groupName).
                setParameter("password", password).getSingleResult();
        if(auxGroup!=null){
            auxGroup.getUsers().add(user);
            em.merge(auxGroup);
            em.flush();
        }else{
            throw new GroupPasswordNotFoundException();
        }
    }
    
    @Override
    public void leaveGroup(Group group,User user) throws LoginNotFoundException, GroupNameNotFoundException, Exception{
        //TODO Modify group to delete a user from a group
    }
    
    @Override
    public List<Group> findGroups() throws Exception{
        return em.createNamedQuery("findGroups").getResultList();
    }
    
    @Override
    public List<Group> findAllGroups(String login) throws LoginNotFoundException, Exception{
       return em.createNamedQuery("findAllGroups").setParameter(
                "login", login).getResultList();
    }
}