/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import serverapplication.entities.Group;
import serverapplication.entities.User;
import serverapplication.exceptions.GroupIdNotFoundException;
import serverapplication.interfaces.EJBGroupLocal;
import serverapplication.exceptions.GroupPasswordNotFoundException;
import serverapplication.exceptions.GroupNameNotFoundException;
import serverapplication.exceptions.LoginNotFoundException;



/**
 *
 * @author Diego Urraca
 */
@Path("group")
public class GroupFacadeREST{

    private static final Logger LOGGER = Logger.getLogger("serverapplication.rest.GroupFacadeREST");

    @EJB
    private EJBGroupLocal ejb;

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createGroup(Group group){//The admin comes inside the group
        try {
            ejb.createGroup(group);
        }catch(LoginNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREst: "+ ex.getMessage());
        }
        
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void modifyGroup(Group group) {
        try{
            ejb.modifyGroup(group);
        }catch(LoginNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREst: "+ ex.getMessage());
        }
    }
 
    @PUT
    @Path("groupName/{groupName}/password/{password}")
    @Consumes({MediaType.APPLICATION_XML})
    public void joinGroup(@PathParam("groupName") String groupName,@PathParam("password") String password, User user){
         try{
            ejb.joinGroup(groupName, password, user);
        }catch(GroupPasswordNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(LoginNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(GroupNameNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREst: "+ ex.getMessage());
        }
    }
    
    @PUT
    @Path("id/{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void leaveGroup(@PathParam("id") Long id,User user){
        try{
            ejb.leaveGroup(id, user);
        }catch(LoginNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(GroupNameNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREst: "+ ex.getMessage());
        }
    }
    
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

    @GET
    @Path("user")
    @Produces({MediaType.APPLICATION_XML})
    public List<Group> findAllGroups(String login) {
        List<Group> groups = null;
        try{
            groups = ejb.findAllGroups(login);
        }catch(LoginNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch(Exception ex){
            LOGGER.warning("GroupFacadeREst: "+ ex.getMessage());
        }
        return groups;
    }
    
    @DELETE
    @Consumes({MediaType.APPLICATION_XML})
    public void deleteGroup (Long id){
        try {
            ejb.deleteGroup(id);
        }catch(GroupIdNotFoundException ex){
            LOGGER.warning("GroupFacadeREST: " + ex.getMessage());
        }catch (Exception ex) {
            Logger.getLogger(GroupFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
