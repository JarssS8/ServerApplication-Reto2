/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import serverapplication.interfaces.EJBUserLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import serverapplication.entities.User;

/**
 *
 * @author aimar
 */
@Path("user")
public class RESTUser {

    @EJB
    private EJBUserLocal ejb;

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createUser(User user) {
        ejb.createUser(user);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void modifyUserData(User user) {
        ejb.modifyUserData(user);
    }
    
    @DELETE
    public void deleteUser(User user) {
        ejb.deleteUser(user);
    }

    @GET
    @Path("{name}")
    @Produces({MediaType.APPLICATION_XML})
    public User findUserByLogin(String name) {
        return ejb.findUserByLogin(name);
    }

    @GET
    @Path("{find all users}")
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAllUsers() {
        return ejb.findAllUsers();
    }

}
