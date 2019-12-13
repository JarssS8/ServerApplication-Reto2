/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.util.List;
import serverapplication.entities.Group;

/**
 *
 * @author Diego Urraca
 */
public interface EJBGroupLocal {
    
    public void createGroup(Group group, String login);
    
    public void modifyGroup(Group group, String login);
    
    public void deleteGroup(Group group, String login);
    
    public void joinGroup(String groupName,String pass, String login);
    
    public void leaveGroup(String groupName,String login);
    
    public Group findGroup(String groupName);
    
    public List<Group> finAllGroups(String login);
}
