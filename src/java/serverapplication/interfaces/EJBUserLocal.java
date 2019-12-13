/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.util.List;
import serverapplication.entities.Admin;
import serverapplication.entities.Free;
import serverapplication.entities.Premium;
import serverapplication.entities.User;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.exceptions.ServerConnectionErrorException;
import serverapplication.exceptions.UserPasswordNotFoundException;

/**
 *
 * @author aimar
 */
public interface EJBUserLocal {

    public void createUser(User user);
    public void modifyUserData(User user);
    public void deleteUser(User user);
    public User findUserByLogin(String login);
    public List<User> findAllUsers();
    public void banUser(User user);
    public Premium modifyFreeToPremium(Free free);
    public Free modifyPremiumToFree(Premium premium);
    public Free modifyAdminToFree(Admin admin);
    public User logIn(User user) throws LoginNotFoundException, UserPasswordNotFoundException, ServerConnectionErrorException;
    public User signUp(User user);
    public void logOut();

}
