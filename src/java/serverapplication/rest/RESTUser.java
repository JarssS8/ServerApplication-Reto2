/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import serverapplication.interfaces.EJBUserLocal;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import serverapplication.entities.Document;
import serverapplication.entities.Free;
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
@Path("user")
public class RESTUser {

    private static final Logger LOGGER = Logger.getLogger("serverapplication.rest.RESTUser");

    @EJB
    private EJBUserLocal ejb;

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Free createUser(User user) {
        Free free;
        try {
            free = ejb.createUser(user);
        } catch (LoginAlreadyExistsException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            // We use ForbiddenException 403 if login already exists
            throw new ForbiddenException(ex.getMessage());
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return free;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyUserData(User user) {
        try {
            ejb.modifyUserData(user);
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        // AQUI HACE FALTA UNA EXCEPCION RESTful

    }

    @DELETE
    @Path("id/{id}")
    public void deleteUser(@PathParam("id") Long id) {
        try {
            ejb.deleteUser((User) ejb.findUserById(id));
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("/logIn/{login}/{password}")
    @Produces(MediaType.APPLICATION_XML)
    public User logIn(@PathParam("login") String login, @PathParam("password") String password) {
        User user = null;
        try {
            String auxLogin = ejb.findPrivilegeOfUserByLogin(login);
            user = ejb.checkPassword(login,password);
        } catch (LoginNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (UserPasswordNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotAuthorizedException(ex.getMessage());
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return user;
    }

    @GET
    @Path("id/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public User findUserById(@PathParam("id") Long id) {
        User user = null;
        try {
            user = (User) ejb.findUserById(id);
            if (user == null) {
                LOGGER.warning("RESTUser: User not found...");
                throw new NotFoundException();
            }
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (UserNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        }
        return user;
    }

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
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return user;
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<User> findAllUsers() {
        Set<User> users = null;
        try {
            users = ejb.findAllUsers();
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return users;
    }

    @PUT
    @Path("/FreeToPremium/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyFreeToPremium(Premium premium) {
        try {
            ejb.modifyFreeToPremium(premium);
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Path("/FreeToAdmin/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyFreeToAdmin(User user) {
        try {
            ejb.modifyFreeToAdmin(user);
        } catch (LoginNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Path("/PremiumToFree/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyPremiumToFree(User user) {
        try {
            ejb.modifyPremiumToFree(user);
        } catch (LoginNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Path("/PremiumToAdmin/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyPremiumToAdmin(User user) {
        try {
            ejb.modifyPremiumToAdmin(user);
        } catch (LoginNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Path("/AdminToFree/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyAdminToFree(User user) {
        try {
            ejb.modifyAdminToFree(user);
        } catch (LoginNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("/findRatingsOfUser/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Set<Rating> findRatingsOfUser(@PathParam("id") Long id) {
        Set<Rating> ratings = null;
        try {
            ratings = ejb.findRatingsOfUser(id);
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return ratings;
    }

     @GET
    @Path("/findDocumentsOfUser/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Set<Document> findDocumentsOfUser(@PathParam("id") Long id) {
        Set<Document> documents = null;
        try {
            documents = ejb.findDocumentsOfUser(id);
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return documents;
    }
    
    @GET
    @Path("/findGroupsOfUser/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Set<Group> findGroupsOfUser(@PathParam("id") Long id) {
        Set<Group> groups = null;
        try {
            groups = ejb.findGroupsOfUser(id);
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return groups;
    }
    
    @PUT
    @Path("/savePaymentMethod")
    @Consumes(MediaType.APPLICATION_XML)
    public void savePaymentMethod(Premium premium) {
        try {
            ejb.savePaymentMethod(premium);
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
    
    @GET
    @Path("/findUserPrivilege/{login}")
    @Produces(MediaType.APPLICATION_XML)
    public String findPrivilegeOfUserByLogin(@PathParam("login") String login) {
        String auxLogin = null;
        try {
            auxLogin = ejb.findPrivilegeOfUserByLogin(login);
        } catch (LoginNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getCause());
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getCause());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getCause());
        }
        return auxLogin;
    }
    
    @GET
    @Path("/checkPasswordByLogin/{login}/{password}")
    @Produces(MediaType.APPLICATION_XML)
    public User checkPassword(@PathParam("login") String login, @PathParam("password") String password) {
        User user = null;
        try {
            user = ejb.checkPassword(login, password);
        } catch (UserPasswordNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotAuthorizedException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return user;
    }
    
    @GET
    @Path("/restorePassword/{email}")
    public void restorePassword(@PathParam("email") String email) {
        try {
            ejb.restorePassword(email);
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        }
    }
    
    @GET
    @Path("/getPublicKey")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPublicKey(){
        String publicKey;
        try {
           publicKey = ejb.getPublicKey();
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return publicKey;
    }
    
}
