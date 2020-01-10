/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.util.Set;
import serverapplication.entities.Document;
import serverapplication.entities.Group;
import serverapplication.entities.Premium;
import serverapplication.entities.Rating;
import serverapplication.entities.User;
import serverapplication.exceptions.LoginAlreadyExistsException;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.exceptions.GenericServerErrorException;
import serverapplication.exceptions.UserNotFoundException;
import serverapplication.exceptions.UserPasswordNotFoundException;

/**
 *
 * @author aimar
 */
public interface EJBUserLocal {

    public void createUser(User user) throws LoginAlreadyExistsException, 
            GenericServerErrorException;
    
    public void modifyUserData(User user) throws GenericServerErrorException;
    
    public void deleteUser(User user) throws UserNotFoundException, GenericServerErrorException;
    
    public Object findUserById(Long id) throws UserNotFoundException, GenericServerErrorException;
    
    public Object findUserByLogin(String login) throws GenericServerErrorException;
    
    public Set<User> findAllUsers() throws GenericServerErrorException;
    
    public void modifyFreeToPremium(Premium premium) throws LoginNotFoundException, GenericServerErrorException;
    
    public void modifyFreeToAdmin(User user) throws LoginNotFoundException, GenericServerErrorException;
    
    public void modifyPremiumToFree(User user) throws LoginNotFoundException, GenericServerErrorException;
    
    public void modifyPremiumToAdmin(User user) throws LoginNotFoundException, GenericServerErrorException;
    
    public void modifyAdminToFree(User user) throws LoginNotFoundException, GenericServerErrorException;
    
    public Set<Rating> findRatingsOfUser(User user);
    
    public Set<Document> findDocumentsOfUser(User user);
    
    public Set<Group> findGroupsOfUser(User user);
    
    public Set<Group> findGroupsRuledByUser(User user);
    
    public User logIn(User user) throws LoginNotFoundException, 
            UserPasswordNotFoundException, GenericServerErrorException;
    
    public User signUp(User user) throws LoginAlreadyExistsException, GenericServerErrorException;
    
    public void logOut();

}
