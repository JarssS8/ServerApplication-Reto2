/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.util.Set;
import serverapplication.entities.Admin;
import serverapplication.entities.Free;
import serverapplication.entities.Premium;
import serverapplication.entities.User;
import serverapplication.exceptions.LoginAlreadyExistsException;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.exceptions.GenericServerErrorException;
import serverapplication.exceptions.UserPasswordNotFoundException;

/**
 *
 * @author aimar
 */
public interface EJBUserLocal {

    public void createUser(User user) throws LoginAlreadyExistsException, 
            GenericServerErrorException;
    
    public void modifyUserData(Free free) throws GenericServerErrorException;
    
    public void modifyUserData(Premium premium) throws GenericServerErrorException;
    
    public void modifyUserData(Admin admin) throws GenericServerErrorException;
    
    public void deleteUserById(Long id) throws GenericServerErrorException;
    
    public void deleteFree(Free free) throws GenericServerErrorException;
    
    public void deletePremium(Premium premium) throws GenericServerErrorException;
    
    public void deleteAdmin(Admin admin) throws GenericServerErrorException;
    
    public Object findUserById(Long id) throws GenericServerErrorException;
    
    public Object findUserByLogin(String login) throws GenericServerErrorException;
    
    public Set<User> findAllUsers() throws GenericServerErrorException;
    
    public void banUser(User user);
    
    public void modifyPrivilege(Premium premium, String privilege) throws LoginNotFoundException, 
            GenericServerErrorException;
    
    /*
    public void modifyUserToPremium(Premium premium) throws LoginNotFoundException, 
            GenericServerErrorException;
    
    public void modifyUserToAdmin(User user) throws LoginNotFoundException, 
            GenericServerErrorException;
    */
    
    public User logIn(User user) throws LoginNotFoundException, 
            UserPasswordNotFoundException, GenericServerErrorException;
    
    public void logOut();

}
