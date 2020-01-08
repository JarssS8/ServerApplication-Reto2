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
import serverapplication.exceptions.ServerConnectionErrorException;
import serverapplication.exceptions.UserPasswordNotFoundException;

/**
 *
 * @author aimar
 */
public interface EJBUserLocal {

    public void createUser(User user) throws LoginAlreadyExistsException, 
            ServerConnectionErrorException;
    
    public void modifyUserData(Free free) throws ServerConnectionErrorException;
    
    public void modifyUserData(Premium premium) throws ServerConnectionErrorException;
    
    public void modifyUserData(Admin admin) throws ServerConnectionErrorException;
    
    //public void deleteUser(User user) throws ServerConnectionErrorException;
    
    public void deleteFree(Free free) throws ServerConnectionErrorException;
    
    public void deletePremium(Premium premium) throws ServerConnectionErrorException;
    
    public void deleteAdmin(Admin admin) throws ServerConnectionErrorException;
    
    public Object findUserById(Long id) throws ServerConnectionErrorException;
    
    public Object findUserByLogin(String login) throws ServerConnectionErrorException;
    
    public Set<User> findAllUsers() throws ServerConnectionErrorException;
    
    public void banUser(User user);
    
    public void modifyUserToFree(Free free) throws LoginNotFoundException, 
            ServerConnectionErrorException;
    
    public void modifyUserToPremium(Premium premium) throws LoginNotFoundException, 
            ServerConnectionErrorException;
    
    public void modifyUserToAdmin(Admin admin) throws LoginNotFoundException, 
            ServerConnectionErrorException;
    
    public User logIn(User user) throws LoginNotFoundException, 
            UserPasswordNotFoundException, ServerConnectionErrorException;
    
    public User signUp(User user);
    
    public void logOut();

}
