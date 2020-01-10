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
import serverapplication.entities.Group;
import serverapplication.entities.User;
import serverapplication.exceptions.GroupIdNotFoundException;
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
    public void createGroup(Group group) throws GroupNameAlreadyExistException, LoginNotFoundException, Exception {
        em.persist(group);
    }

    @Override
    public void modifyGroup(Group group) throws LoginNotFoundException, Exception {
        em.merge(group);
        em.flush();
    }

    @Override
    public void joinGroup(String groupName, String password, User user) throws GroupPasswordNotFoundException, LoginNotFoundException, GroupNameNotFoundException, Exception {
        Group group = null;
        group = (Group) em.createNamedQuery("findGroupByNameAndPass").
                setParameter("groupName", groupName).
                setParameter("password", password).getSingleResult();
        if (group != null) {
            group.getUsers().add(user);
            em.merge(group);
            em.flush();
        } else {
            throw new GroupPasswordNotFoundException();
        }
    }
    
    @Override
    public void leaveGroup(Long id, User user) throws LoginNotFoundException, GroupNameNotFoundException, Exception {
        Group group = (Group) em.createNamedQuery("findGroupByid").
                setParameter("id", id);
        
        if (null != group) {//Group is empty?
            ArrayList<User> userList = (ArrayList<User>) group.getUsers();
            for (User usr : userList) {
                if (usr.getLogin().equals(user.getLogin())) {//If the user is found
                    userList.remove(usr);
                    group.setUsers((Set<User>) userList);
                    if (group.getUsers().size() > 0 && group.getGroupAdmin().getLogin().equals(user.getLogin())) {//Admin changes
                        group.setGroupAdmin(userList.get(0));
                    }
                    if (group.getUsers().isEmpty()) {//If group is empty of users, delete it
                        em.createQuery("deleteGroup").setParameter("id",group.getId());
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

    @Override
    public List<Group> findGroups() throws Exception {
        return em.createNamedQuery("findGroups").getResultList();
    }
    
    public Group findGroupByName(String groupName) throws GroupNameNotFoundException, Exception{
        return (Group) em.createNamedQuery("findGroupByName").getSingleResult();
    }

    @Override
    public List<Group> findAllGroups(String login) throws LoginNotFoundException, Exception {
        return em.createNamedQuery("findAllGroups").setParameter(
                "login", login).getResultList();
    }

    @Override
    public void deleteGroup(Long id) {
        em.createNamedQuery("deleteGroup").
                setParameter("id", id);
    }

}
