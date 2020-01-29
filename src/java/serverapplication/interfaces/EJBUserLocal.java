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

    /**
     * This method creates a new user and stores it in the database.
     * @param user A User object.
     * @throws LoginAlreadyExistsException If the login already exists.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public void createUser(User user) throws LoginAlreadyExistsException, 
            GenericServerErrorException;
    /**
     * This method modifies an existing user's email and full name and stores 
     * them in the database.
     * @param user A User object.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public void modifyUserData(User user) throws GenericServerErrorException;
    /**
     * This method deletes an existing user from the database.
     * @param user A User object.
     * @throws UserNotFoundException If the user searched doesn't exist.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public void deleteUser(User user) throws UserNotFoundException, 
            GenericServerErrorException;
    /**
     * This method searches a user in the database by its Id.
     * @param id A Long that contains the id to search by.
     * @return An User object with the user.
     * @throws UserNotFoundException If the user searched doesn't exist.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public User findUserById(Long id) throws UserNotFoundException, 
            GenericServerErrorException;
    /**
     * This method finds an User by its login.
     * @param login A string that contains the users login.
     * @return An User object with the user. 
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public User findUserByLogin(String login) throws LoginNotFoundException, 
            GenericServerErrorException;
    /**
     * This method finds all the users in the database.
     * @return a Set with all the users.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public Set<User> findAllUsers() throws GenericServerErrorException;
    /**
     * This method deletes a Free from the persistency context and adds a new 
     * entry as Premium with its unique attributes.
     * @param premium A Premium object with all the new data.
     * @throws LoginNotFoundException If the login does not exist.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public void modifyFreeToPremium(Premium premium) throws LoginNotFoundException, 
            GenericServerErrorException;
    /**
     * This method deletes a Free from the persistency context and adds a new 
     * entry as Admin with its unique attributes.
     * @param user A User object.
     * @throws LoginNotFoundException If the login does not exist.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public void modifyFreeToAdmin(User user) throws LoginNotFoundException, 
            GenericServerErrorException;
    /**
     * This method deletes a Premium from the persistency context and adds a new 
     * entry as Free with its unique attributes.
     * @param user A User object.
     * @throws LoginNotFoundException If the login does not exist.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public void modifyPremiumToFree(User user) throws LoginNotFoundException, 
            GenericServerErrorException;
    /**
     * This method deletes a Premium from the persistency context and adds a new 
     * entry as Admin with its unique attributes.
     * @param user A User object.
     * @throws LoginNotFoundException If the login does not exist.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public void modifyPremiumToAdmin(User user) throws LoginNotFoundException, 
            GenericServerErrorException;
    /**
     * This method deletes a Premium from the persistency context and adds a new 
     * entry as Admin with its unique attributes.
     * @param user A User object.
     * @throws LoginNotFoundException
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public void modifyAdminToFree(User user) throws LoginNotFoundException, 
            GenericServerErrorException;
    /**
     * 
     * @param id
     * @return 
     */
    public Set<Rating> findRatingsOfUser(Long id);
    
      
    public Set<Document> findDocumentsOfUser(Long id); 
    /**
     * 
     * @param user A User object.
     * @return 
     */
    public Set<Group> findGroupsOfUser(Long id);
    /**
     * 
     * @param user A User object.
     * @return 
     */
    public Set<Group> findGroupsRuledByUser(Long id);
    
    public User checkPassword(String login, String password) 
            throws UserPasswordNotFoundException, GenericServerErrorException;
    
    public void savePaymentMethod(Premium premium);
    
    public String findPrivilegeOfUserByLogin(String login);
  
    /**
     * 
     * @param user A User object.
     * @return
     * @throws LoginAlreadyExistsException
     * @throws GenericServerErrorException If there's an error in the server.
     */
    public User signUp(User user) throws LoginAlreadyExistsException, 
            GenericServerErrorException;
    /**
     * 
     */
    public void logOut();

}
