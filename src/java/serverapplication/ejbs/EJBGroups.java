/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import serverapplication.entities.Document;
import serverapplication.entities.Group;
import serverapplication.entities.User;
import serverapplication.exceptions.GroupIdNotFoundException;
import serverapplication.exceptions.GroupNameAlreadyExistException;
import serverapplication.exceptions.GroupNameNotFoundException;
import serverapplication.exceptions.GroupPasswordNotFoundException;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.exceptions.UserNotFoundException;
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

    /**
     * Method to create a group
     * @param group
     * @throws GroupNameAlreadyExistException
     * @throws LoginNotFoundException
     * @throws Exception 
     */
    @Override
    public void createGroup(Group group) throws GroupNameAlreadyExistException, LoginNotFoundException, Exception {
        em.persist(group);
    }

    /**
     * Method to modify a group
     * @param group
     * @throws GroupNameNotFoundException
     * @throws Exception 
     */
    @Override
    public void modifyGroup(Group group) throws GroupNameNotFoundException, Exception {
        em.merge(group);
        em.flush();
    }

    /**
     * Methpd to join a user to a group
     * @param groupName
     * @param password
     * @param usr_id
     * @throws GroupPasswordNotFoundException
     * @throws GroupNameNotFoundException
     * @throws Exception 
     */
    @Override
    public void joinGroup(String groupName, String password, Long usr_id) throws UserNotFoundException,GroupPasswordNotFoundException, GroupNameNotFoundException, Exception {
        Group group = null;
        User user = null;
        group = (Group) em.createNamedQuery("findGroupByNameAndPass").
                setParameter("groupName", groupName).
                setParameter("password", password).getSingleResult();
        if (null != group) {
            ArrayList <User> userList= null;
            user = em.find(User.class, usr_id);
            userList = (ArrayList<User>) em.createNamedQuery("findUsersOfGroup").setParameter("id", group.getId());
            userList.add(user);
            group.setUsers((Set<User>) userList);
            em.merge(group);
            em.flush();
        } else {
            throw new GroupPasswordNotFoundException();
        }
    }
    
    /**
     * Method to kick a user out of a group, if was the last user, the group 
     * deletes itself, otherwise, if there is another user, it will be the 
     * next group admin
     * @param id
     * @param usr_id
     * @throws GroupIdNotFoundException
     * @throws Exception 
     */
    @Override
    public void leaveGroup(Long id, Long usr_id) throws GroupIdNotFoundException, Exception {
        Group group = (Group) em.find(Group.class, id);
        
        if (null != group) {//Group is empty?
            ArrayList<User> userList = (ArrayList<User>) group.getUsers();
            for (User usr : userList) {
                if (usr.getId().equals(usr_id)) {//If the user is found
                    userList.remove(usr);
                    group.setUsers((Set<User>) userList);
                    if (group.getUsers().size() > 0 && group.getGroupAdmin().getId().equals(usr_id)) {//Admin changes
                        group.setGroupAdmin(userList.get(0));
                    }
                    if (group.getUsers().isEmpty()) {//If group is empty of users, delete it
                        em.remove(id);
                    } else {
                        em.merge(group);
                    }
                    em.flush();
                    break;
                }
            }
        } else {//Is empty
            throw new GroupIdNotFoundException();
        }

    }

    /**
     * Method to get all the groups
     * @return
     * @throws Exception 
     */
    @Override
    public List<Group> findGroups() throws Exception {
        return em.createNamedQuery("findGroups").getResultList();
    }
    
    /**
     * Method to get a group by name
     * @param groupName
     * @return
     * @throws GroupNameNotFoundException
     * @throws Exception 
     */
    public Group findGroupByName(String groupName) throws GroupNameNotFoundException, Exception{
        return (Group) em.createNamedQuery("findGroupByName").getSingleResult();
    }

    /**
     * Method to get all the users of a group
     * @param id
     * @return
     * @throws GroupIdNotFoundException
     * @throws Exception 
     */
    @Override
    public List<User> findUsersOfGroup(Long id) throws GroupIdNotFoundException, Exception {
        return em.createNamedQuery("findUsersOfGroup").setParameter(
                "id", id).getResultList();
    }
    
     /**
     * Method to get all the documents of a group
     * @param id
     * @return
     * @throws GroupIdNotFoundException
     * @throws Exception 
     */
    @Override
    public List<Document> findDocsOfGroup(Long id) throws GroupIdNotFoundException, Exception {
        return em.createNamedQuery("findDocsOfGroup").setParameter(
                "id", id).getResultList();
    } 
    
    /**
     * Method to get a group by id
     * @param id
     * @return
     * @throws GroupIdNotFoundException
     * @throws Exception 
     */
    @Override
    public Group findGroupById(Long id) throws GroupIdNotFoundException, Exception{
        return em.find(Group.class, id);
    }

    /**
     * Method to delete a group
     * @param group 
     */
    @Override
    public void deleteGroup(Group group) {
        em.remove(em.find(Group.class, group.getId()));
    }

}