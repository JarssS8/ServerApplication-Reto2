                            /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import serverapplication.entities.Document;
import serverapplication.interfaces.CategoryEJBLocal;

/**
 *
 * @author Adrian
 */
@Stateless
@Path("category")
public class RESTCategory {

    /**
     * Logger for class methods.
     */
    private static final Logger LOGGER = Logger.getLogger(
            "serverapplication.rest.RESTCategory");

    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    private EntityManager em;

    /**
     * EJB reference for business logic object.
     */
    @EJB
    private CategoryEJBLocal eJBLocal;

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void create(Category category) {
        try {
            eJBLocal.createCategory(category);
        } catch (Exception ex) {
            LOGGER.warning("REST Category: The name of the category alredy exists " + ex.getMessage());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyCategory(Category category) {
        try {
            eJBLocal.modifyCategory(category);
        } catch (Exception ex) {
            LOGGER.warning("REST Category: The name of the category alredy exists on modifyCategory " + ex.getMessage());
        }

    }

    @DELETE
    @Path("{id}")
    public void deleteCategory(@PathParam("id") Long id) {
        try {
            eJBLocal.deleteCategory(id);
        } catch (Exception ex) {
            LOGGER.warning("REST Category: The category not found on deleteCategory " + ex.getMessage());
        }

    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_XML)
    public Set<Category> findCategoryByName(@PathParam("name") String name) {
        Set<Category> categories = null;
        try {
            categories = eJBLocal.findCategoryByName(name);
        } catch (Exception ex) {
            LOGGER.warning("REST Category: Exception creating " + ex.getMessage());
        }
        return categories;
    }

    @GET
    @Path("{catName}/{docName}")
    @Produces(MediaType.APPLICATION_XML)
    public Document findDocumentsByCategory(@PathParam("catName") String catName,@PathParam("docName") String docName) {
        Document doc = null;
        try {
            doc=eJBLocal.findDocumentsByCategory(catName, docName);
        } catch (Exception ex) {
            LOGGER.warning("REST Category: Exception creating " + ex.getMessage());
        }
            return doc;
        
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Category> findAllCategories() {
        Set<Category> categories = null;
        try {
            categories = eJBLocal.findAllCategories();
        } catch (Exception ex) {
            LOGGER.warning("REST Category: Exception creating " + ex.getMessage());
        }
        return categories;
    }

}
