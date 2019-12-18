/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import serverapplication.interfaces.EJBUserLocal;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import serverapplication.entities.Free;
import serverapplication.entities.User;
import serverapplication.exceptions.LoginAlreadyExistsException;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.exceptions.ServerConnectionErrorException;
import serverapplication.exceptions.UserPasswordNotFoundException;

/**
 *
 * @author aimar
 */
@Path("user")
public class RESTUser {

    private static final Logger LOGGER = Logger.getLogger("serverapplication.rest.RESTUser");

    @EJB
    private EJBUserLocal ejb;

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void createUser(User user) {
        try {
            ejb.createUser(user);
        } catch (LoginAlreadyExistsException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            // AQUI HACE FALTA UNA EXCEPCION RESTful
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyUserData(User user) {
        try {
            ejb.modifyUserData(user);
        } catch (LoginAlreadyExistsException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            // AQUI HACE FALTA UNA EXCEPCION RESTful
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @DELETE
    @Path("{login}")
    public void deleteUser(@PathParam("login") String login) {
        try {
            ejb.deleteUser(ejb.findUserByLogin(login));
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("/logIn")
    public User logIn(User user) {
        User auxUser = null;
        try {
            auxUser = ejb.logIn(user);
        } catch (LoginNotFoundException ex) {
            LOGGER.warning(ex.getMessage());
        } catch (UserPasswordNotFoundException ex) {
            LOGGER.warning(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return auxUser;
    }
    /*
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public User findUserById(@PathParam("id") Long id) {
        return ejb.findUserById(id); 
    }
    */
    @GET
    @Path("{login}")
    @Produces(MediaType.APPLICATION_XML)
    public User findUserByLogin(@PathParam("login") String login) {
        User user = null;
        try {
            user = ejb.findUserByLogin(login);
            if (user == null) {
                LOGGER.warning("RESTUser: Login not found...");
                throw new NotFoundException();
            }
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return user;
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<User> findAllUsers() {
        List<User> users = null;
        try {
            users = ejb.findAllUsers();
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return users;
    }

    @PUT
    @Path("f/{par1}/{par2}")
    public void f(@FormParam("par1") Free par1, @PathParam("par2") int par2) {
        LOGGER.info("Hola mundo");
    }
    
    @PUT
    @Path("/premium/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyFreeToPremium(User user) {
        try { 
            ejb.modifyFreeToPremium(user);
        } catch (LoginNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

}
