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
import javax.persistence.Query;
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
        }else
            throw new GroupPasswordNotFoundException();
    }
    
    //TODO paths
    @Override
    public void leaveGroup(Group group,User user) throws LoginNotFoundException, GroupNameNotFoundException, Exception{
        Group auxGroup = (Group) em.createNamedQuery("findGroupByName").
                setParameter("name",group.getName());
        if(auxGroup!=null){
            ArrayList<User> userList = (ArrayList<User>) auxGroup.getUsers();
            for(int pos=0;pos<userList.size();pos++){
                if(userList.get(pos).getLogin().equals(user.getLogin())){
                    userList.remove(pos);
                    auxGroup.setUsers((Set<User>) userList);
                    if(auxGroup.getUsers().size()>0 && auxGroup.getGroupAdmin().getLogin().equals(user.getLogin())){
                        auxGroup.setGroupAdmin(userList.get(0));
                    }
                    if(auxGroup.getUsers().size()==0){
                        Query query = em.createQuery("DELETE FROM groups g WHERE g.groupName = :groupName");
                        query.setParameter("groupName", auxGroup.getName());
                        query.executeUpdate();
                    }else
                        em.merge(auxGroup);
                    em.flush();
                    break;
                }
            }
        }else
            throw new GroupNameNotFoundException();
        
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
    
    /*@Override
    public Group findGroupByName (String groupName){
        return (Group) em.createNamedQuery("findGroupByName").
                setParameter("groupName", groupName).getSingleResult();
    }*/
}