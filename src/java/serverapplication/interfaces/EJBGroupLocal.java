/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.util.List;
import javax.ejb.Local;
import serverapplication.entities.Group;
import serverapplication.entities.User;
import serverapplication.exceptions.GroupNameAlreadyExistException;
import serverapplication.exceptions.GroupNameNotFoundException;
import serverapplication.exceptions.GroupPasswordNotFoundException;
import serverapplication.exceptions.LoginNotFoundException;

/**
 *
 * @author Diego Urraca
 */
@Local
public interface EJBGroupLocal {
    
    public void createGroup(Group group) throws GroupNameAlreadyExistException, LoginNotFoundException, Exception;
    
    public void modifyGroup(Group group) throws LoginNotFoundException, Exception;

    public void joinGroup(String groupName,String password, User user) throws GroupPasswordNotFoundException, LoginNotFoundException, GroupNameNotFoundException, Exception;
    
    public void leaveGroup(Group group,User user) throws LoginNotFoundException, GroupNameNotFoundException, Exception;
    
    public List<Group> findGroups() throws Exception;
    
    public List<Group> findAllGroups(String login) throws LoginNotFoundException, Exception;
}
