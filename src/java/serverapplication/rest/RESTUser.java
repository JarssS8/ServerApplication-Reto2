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
            // We use ForbiddenException 403 if login already exists
            throw new ForbiddenException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage()+"aaa");
        }
    }

    @PUT
    @Path("/modifyFree/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyUserData(Free free) {
        try {
            ejb.modifyUserData(free);
        }catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        // AQUI HACE FALTA UNA EXCEPCION RESTful
        
    }
    
    @PUT
    @Path("/modifyPremium/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyUserData(Premium premium) {
        try {
            ejb.modifyUserData(premium);
        }catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        // AQUI HACE FALTA UNA EXCEPCION RESTful
        
    }
    
    @PUT
    @Path("/modifyAdmin/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyUserData(Admin admin) {
        try {
            ejb.modifyUserData(admin);
        }catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        // AQUI HACE FALTA UNA EXCEPCION RESTful
        
    }

    @DELETE
    @Path("{login}")
    public void deleteUser(@PathParam("login") String login) {
        try {
            Object auxUser = ejb.findUserByLogin(login);
            if (auxUser instanceof Free) {
                ejb.deleteFree((Free) ejb.findUserByLogin(login));
            }
            if (auxUser instanceof Premium) {
                ejb.deletePremium((Premium) ejb.findUserByLogin(login));
            }
            if (auxUser instanceof Admin) {
                ejb.deleteAdmin((Admin) ejb.findUserByLogin(login));
            }
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
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

    @GET
    @Path("id/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Object findUserById(@PathParam("id") Long id)
            throws ServerConnectionErrorException {
        Object user = null;
        try {
            user = ejb.findUserById(id);
            if (user == null) {
                LOGGER.warning("RESTUser: Login not found...");
                throw new NotFoundException();
            }
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return user;
    }

    @GET
    @Path("{login}")
    @Produces(MediaType.APPLICATION_XML)
    public Object findUserByLogin(@PathParam("login") String login) {
        Object user = null;
        try {
            user = ejb.findUserByLogin(login);
            if (user == null) {
                LOGGER.warning("RESTUser: Login not found...");
                throw new NotFoundException();
            }
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
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

    // ESTO ES UNA PRUEBA, BORRAR
    @PUT
    @Path("f/{par1}/{par2}")
    public void f(@FormParam("par1") Free par1, @PathParam("par2") int par2) {
        LOGGER.info("Hola mundo");
    }

    @PUT
    @Path("/goFree/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyUserToFree(Free free) {
        try {
            ejb.modifyUserToFree(free);
        } catch (LoginNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
    
    @PUT
    @Path("/goPremium/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyUserToPremium(Premium premium) {
        try {
            ejb.modifyUserToPremium(premium);
        } catch (LoginNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
    
    @PUT
    @Path("/goAdmin/")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyUserToAdmin(Admin admin) {
        try {
            ejb.modifyUserToAdmin(admin);
        } catch (LoginNotFoundException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

}
