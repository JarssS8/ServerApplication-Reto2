/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import serverapplication.entities.Category;
import serverapplication.exceptions.CategoryNameAlreadyExists;
import serverapplication.exceptions.CategoryNotFound;
import serverapplication.interfaces.CategoryEJBLocal;

/**
 *
 * @author Adrian
 */
@Stateless
@Path("category")
public class RESTCategory {

    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    private EntityManager em;
    
    @EJB
    private CategoryEJBLocal eJBLocal;
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void create(Category category) {
        try {
            eJBLocal.createCategory(category);
        } catch (CategoryNameAlreadyExists ex) {
            Logger.getLogger(RESTCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyCategory(Category category) {
        try {
            eJBLocal.modifyCategory(category);
        } catch (CategoryNameAlreadyExists ex) {
            Logger.getLogger(RESTCategory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CategoryNotFound ex) {
            Logger.getLogger(RESTCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_XML)
    public void deleteCategory(Category category) {
        try {
            eJBLocal.deleteCategory(category);
        } catch (CategoryNotFound ex) {
            Logger.getLogger(RESTCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_XML)
    public Set<Category> findCategoryByName(@PathParam("name") String name) {
       return eJBLocal.findCategoryByName(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Category> findAllCategories() {
        return eJBLocal.findAllCategories();
    }

}
