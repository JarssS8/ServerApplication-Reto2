/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import serverapplication.interfaces.EJBUserLocal;
import java.util.Set;
import java.util.logging.Level;
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
import serverapplication.exceptions.DocumentNotFoundException;
import serverapplication.exceptions.LoginAlreadyExistsException;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.exceptions.GenericServerErrorException;
import serverapplication.exceptions.GroupNameNotFoundException;
import serverapplication.exceptions.UserNotFoundException;
import serverapplication.exceptions.UserPasswordNotFoundException;

/**
 *
 * @author aimar
 */
@Path("user")
public class RESTUser {

    /**
     * Logger for class methods.
     */
    private static final Logger LOGGER = Logger.getLogger("serverapplication.rest.RESTUser");

    /**
     * EJB reference for business logic object.
     */
    @EJB
    private EJBUserLocal ejb;

    /**
     * RESTful POST method for create new {@link  Free} objects from an XML
     * representation
     *
     * @param user User who is going to be created
     * @return A Free user.
     */
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

    /**
     * RESTful PUT method for update {@link  User} objects from an XML
     * representation
     *
     * @param user User with the new data for be updated
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyUserData(User user) {
        try {
            ejb.modifyUserData(user);
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }

    }

    /**
     * RESTful DELETE method for remove {@link  User} objects using the Path id
     *
     * @param id Long used for find the {@link User} that is going to be removed
     * from the data base
     */
    @DELETE
    @Path("id/{id}")
    public void deleteUser(@PathParam("id") Long id) {
        try {
            ejb.deleteUser((User) ejb.findUserById(id));
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (UserNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    /**
     * RESTful GET method for find {@link  User} object where both parameters
     * match with it
     *
     * @param login String with the login of the user
     * @param password String with the password of the user
     * @return User that match with that parameters
     */
    @GET
    @Path("/logIn/{login}/{password}")
    @Produces(MediaType.APPLICATION_XML)
    public User logIn(@PathParam("login") String login, @PathParam("password") String password) {
        User user = null;
        try {
            user = ejb.checkPassword(login, password);
        } catch (UserPasswordNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotAuthorizedException(ex.getMessage());
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return user;
    }

    /**
     * RESTful GET method for find {@link  User} object using his id
     *
     * @param id Long with the id of one user
     * @return User that match with that id
     */
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

    /**
     * RESTful GET method for find {@link  User} object who match with the login
     * parameter
     *
     * @param login String login of one user
     * @return User that login match with the parameter
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
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return user;
    }

    /**
     * RESTful GET method for find {@link  User} objects for our database
     *
     * @return Set with all the users from our database
     */
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

    /**
     * RESTful PUT method for update {@link  User} object privilege from Free to
     * Premium
     *
     * @param premium Premium object with the data for transform one Free user
     * to premium
     */
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

    /**
     * RESTful PUT method for update {@link  User} object privilege from Free to
     * Admin
     *
     * @param user User object with the data for transform one Free user to
     * Admin
     */
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

    /**
     * RESTful PUT method for update {@link  User} object privilege from Premium
     * to Free
     *
     * @param user User object with the data for transform one Premium user to
     * Free
     */
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

    /**
     * RESTful PUT method for update {@link  User} object privilege from Premium
     * to Admin
     *
     * @param user User object with the data for transform one Premium user to
     * Admin
     */
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

    /**
     * RESTful PUT method for update {@link  User} object privilege from Admin to
     * Free
     *
     * @param user User object with the data for transform one Admin user to
     * Free
     */
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

    /**
     * RESTful GET method for find {@link  Rating} objects that made the user
     * with that id
     *
     * @param id Long with the id of one user
     * @return Set with all the ratings made for one user
     */
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

    /**
     * RESTful GET method for find {@link  Document} objects that upload the user
     * with that id
     *
     * @param id Long with the id of one user
     * @return Set with all the documents upload for that user
     */
    @GET
    @Path("/findDocumentsOfUser/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Set<Document> findDocumentsOfUser(@PathParam("id") Long id) {
        Set<Document> documents = null;
        try {
            documents = ejb.findDocumentsOfUser(id);
        } catch (DocumentNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return documents;
    }

    /**
     * RESTful GET method for find {@link  Group} objects that belong the user
     * with that id
     *
     * @param id Long with the id of one user
     * @return Set with all the groups that the user belong
     */
    @GET
    @Path("/findGroupsOfUser/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Set<Group> findGroupsOfUser(@PathParam("id") Long id) {
        Set<Group> groups = null;
        try {
            groups = ejb.findGroupsOfUser(id);
        } catch (GroupNameNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return groups;
    }

    /**
     * RESTful GET method update the payment method for one Premium user
     * @param premium Premium who payment method is going to be updated
     */
    @PUT
    @Path("/savePaymentMethod")
    @Consumes(MediaType.APPLICATION_XML)
    public void savePaymentMethod(Premium premium) {
        try {
            ejb.savePaymentMethod(premium);
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    /**
     * RESTful GET method find the privilege of one user using his login
     * @param login String with the login of one user
     * @return String with the privilege of the user
     */
    @GET
    @Path("/findUserPrivilegeByLogin/{login}")
    @Produces(MediaType.APPLICATION_XML)
    public String findPrivilegeOfUserByLogin(@PathParam("login") String login) {
        String privilege = null;
        try {
            privilege = ejb.findPrivilegeOfUserByLogin(login);
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
        return privilege;
    }

    /**
     * RESTful GET method find the privilege of one user using his id
     * @param id Long with the id of one user
     * @return String with the privilege of the user
     */
    @GET
    @Path("/findUserPrivilegeById/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public String findPrivilegeOfUserById(@PathParam("id") Long id) {
        String privilege = null;
        try {
            privilege = ejb.findPrivilegeOfUserById(id);
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
        return privilege;
    }

    /**
     * RESTful GET method find one user that match with the login and password parameters
     * @param login String with the login of one user
     * @param password String with the password of one user
     * @return User that match with that parameters
     */
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

    /**
     * RESTful GET method for restore the password of one user
     * @param email String with the email for restore the password of one user
     */
    @GET
    @Path("/restorePassword/{email}")
    @Produces(MediaType.APPLICATION_XML)
    public void restorePassword(@PathParam("email") String email) {
        try {
            ejb.restorePassword(email);
        } catch (Exception ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        }
    }

    /**
     * RESTful GET method for find the public key for encrypt
     *
     * @return String with the public key in hexadecimal
     */
    @GET
    @Path("/getPublicKey")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPublicKey() {
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
