/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import serverapplication.interfaces.EJBUserLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import serverapplication.entities.User;

/**
 *
 * @author aimar
 */
@Path("user")
public class UserFacadeREST {

    @EJB
    private EJBUserLocal ejb;

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(User user) {
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
    public User findUserByName(String name) {
        return ejb.findUserByName(name);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
