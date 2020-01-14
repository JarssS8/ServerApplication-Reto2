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
import serverapplication.exceptions.GroupIdNotFoundException;
import serverapplication.exceptions.GroupNameAlreadyExistException;
import serverapplication.exceptions.GroupNameNotFoundException;
import serverapplication.exceptions.GroupPasswordNotFoundException;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.exceptions.UserNotFoundException;

/**
 *
 * @author Diego Urraca
 */
@Local
public interface EJBGroupLocal {
    
    public void createGroup(Group group) throws GroupNameAlreadyExistException, LoginNotFoundException, Exception;
    
    public void modifyGroup(Group group) throws GroupNameNotFoundException, Exception;

    public void joinGroup(String groupName,String password, Long usr_id) throws UserNotFoundException, GroupPasswordNotFoundException, LoginNotFoundException, GroupNameNotFoundException, Exception;
    
    public void leaveGroup(Long id,Long usr_id) throws GroupIdNotFoundException, Exception;
    
    public List<Group> findGroups() throws Exception;
    
    public Group findGroupByName(String groupName) throws GroupNameNotFoundException, Exception; 
    
    public List<User> findUsersOfGroup(Long id) throws GroupIdNotFoundException, Exception;
    
    public Group findGroupById(Long id) throws GroupIdNotFoundException, Exception; 
    
    public void deleteGroup(Group group) throws GroupIdNotFoundException, Exception;
}
