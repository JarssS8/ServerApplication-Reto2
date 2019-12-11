/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.util.List;
import serverapplication.entities.User;

/**
 *
 * @author aimar
 */
public interface EJBUserLocal {

    public void createUser(User user);

    public void modifyUserData(User user);

    public void deleteUser(User user);

    public User findUserByName(String name);

    public List<User> findAllUsers();

}
