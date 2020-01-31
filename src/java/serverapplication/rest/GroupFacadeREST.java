/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import serverapplication.entities.Group;
import serverapplication.entities.User;
import serverapplication.exceptions.GroupIdNotFoundException;
import serverapplication.exceptions.GroupNameAlreadyExistException;
import serverapplication.interfaces.EJBGroupLocal;
import serverapplication.exceptions.GroupPasswordNotFoundException;
import serverapplication.exceptions.GroupNameNotFoundException;
import serverapplication.exceptions.LoginNotFoundException;
import serverapplication.exceptions.UserNotFoundException;



/**
 *
 * @author Diego Urraca
 */ 
@Path("group")
public class GroupFacadeREST{
    private static final Logger LOGGER = Logger.getLogger("serverapplication.rest.GroupFacadeREST");

    @EJB
    private EJBGroupLocal ejb;

    /**
     * Method that creates a group
     * @param group 
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createGroup(Group group){//The admin comes inside the group
        try {
            ejb.createGroup(group);
        }catch(LoginNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(GroupNameAlreadyExistException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREST: "+ ex.getMessage());
        }
    }

    /**
     * Method that modify the group
     * @param group 
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void modifyGroup(Group group) {
        try{
            ejb.modifyGroup(group);
        }catch(GroupNameNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREst: "+ ex.getMessage());
        }
    }
 
    /**
     * Method that joins a user to a group
     * @param groupName
     * @param password
     * @param usr_id 
     */
    @PUT
    @Path("join/{groupName}/{password}/{usr_id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void joinGroup(@PathParam("groupName") String groupName,@PathParam("password") String password, @PathParam("usr_id") Long usr_id){
         try{
            ejb.joinGroup(groupName, password, usr_id);
        }catch(UserNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(GroupPasswordNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(GroupNameNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREst: "+ ex.getMessage());
        }
    }
    
    /**
     * Method that kicks a user out of a group
     * @param id
     * @param usr_id 
     */
    @PUT
    @Path("leave/{id}/{usr_id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void leaveGroup(@PathParam("id") Long id,@PathParam("usr_id") Long usr_id){
        try{
            ejb.leaveGroup(id, usr_id);
        }catch(GroupIdNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREst: "+ ex.getMessage());
        }
    }
    
    /**
     * Method that gets all the groups
     * @return list of groups
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Group> findGroups() {
        List<Group> auxGroups = null;
        try{
            auxGroups = ejb.findGroups();
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREst: "+ ex.getMessage());
        }
        return auxGroups;
    }
    
    /**
     * Method that gets a group by name
     * @param groupName
     * @return list of groups
     */
    @GET
    @Path("{groupName}")
    @Produces({MediaType.APPLICATION_XML})
    public Group findGroupByName(@PathParam("groupName") String groupName){
        Group group = null;
        try{
            group = ejb.findGroupByName(groupName);
        }catch(GroupNameNotFoundException ex){
            LOGGER.warning("GroupFacadeREST" + ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREST" + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return group;
    }
    
    
    /**
     * Method that gets the users of a group
     * @param gid group id
     * @return list of users
    
    @GET
    @Path("gid/{gid}")
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findUsersOfGroup(Long gid) {
        List<User> users = null;
        try{
            users = ejb.findUsersOfGroup(gid);
        }catch(GroupIdNotFoundException ex){
            LOGGER.warning("GroupFacadeREST:" + ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREST: "+ ex.getMessage());
        }
        return users;
    }
     */
    /**
     * Method that gets a group by id
     * @param id
     * @return list of groups
     */
    @GET
    @Path("id/{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Group findGroupById(@PathParam("id") Long id){
        Group group = null;
        try{
            group = ejb.findGroupById(id);
        }catch(GroupIdNotFoundException ex){
            LOGGER.warning("GroupFacadeREST" + ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREST" + ex.getMessage());
        }
        return group;
    }
    
    /**
     * Method that deletes a group
     * @param id 
     */
    @DELETE
    @Path("id/{id}")
    public void deleteGroup (@PathParam("id") Long id){
        try {
            ejb.deleteGroup(ejb.findGroupById(id));
        }catch(GroupIdNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch (Exception ex) {
            LOGGER.warning("GroupFacadeREst: "+ ex.getMessage());
        }
    }
}