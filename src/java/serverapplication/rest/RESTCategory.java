/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import java.util.Set;
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
import serverapplication.exceptions.CategoryNameAlreadyExistsException;
import serverapplication.exceptions.CategoryNotFoundException;
import serverapplication.interfaces.CategoryEJBLocal;

/**
 *
 * @author Adrian
 */
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

    /**
     * RESTful POST method for create new {@link  Category} objects from an XML
     * representation
     *
     * @param category
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void create(Category category) {
        try {
            category.setId(null);
            eJBLocal.createCategory(category);
        } catch (CategoryNameAlreadyExistsException ex) {
            LOGGER.warning("REST Category: The name of the category alredy exists " + ex.getMessage());
        } catch (Exception e) {
            LOGGER.warning("REST Category: Exception creating the category" + e.getMessage());
        }
    }

    /**
     * RESTful PUT method for update {@link  Category} objects from an XML
     * representation
     *
     * @param category
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyCategory(Category category) {
        try {
            eJBLocal.modifyCategory(category);
        } catch (CategoryNameAlreadyExistsException ex) {
            LOGGER.warning("REST Category: The name of the category alredy exists on modifyCategory " + ex.getMessage());
        } catch (CategoryNotFoundException ex) {
            LOGGER.warning("REST Category: The category not found on modifyCategory " + ex.getMessage());
        } catch (Exception e) {
            LOGGER.warning("REST Category: Exception modifying the category " + e.getMessage());
        }

    }

    /**
     * RESTful GET method for find {@link  Category} objects using the Path id through an XML
     * representation
     * @param id Long used for find one Category
     * @return A {@link Category} object with the category for that id
     */
    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Category findCategoryById(@PathParam("id") Long id) {
        Category category = null;
        try {
            category = eJBLocal.findCategoryById(id);
        } catch (CategoryNotFoundException ex) {
            LOGGER.warning("REST Category: The category not found on findCategoryById " + ex.getMessage());
        } catch (Exception e) {
            LOGGER.warning("REST Category: Exception finding the category" + e.getMessage());
        }
        return category;

    }

    /**
     * RESTful DELETE method for remove {@link  Category} objects using the Path id
     * @param id Long used for find the {@link Category} that is going to be removed from the data base
     */
    @DELETE
    @Path("{id}")
    public void deleteCategory(@PathParam("id") Long id) {
        try {
            eJBLocal.deleteCategory(eJBLocal.findCategoryById(id));
        } catch (CategoryNotFoundException ex) {
            LOGGER.warning("REST Category: The category not found on deleteCategory " + ex.getMessage());
        } catch (Exception e) {
            LOGGER.warning("REST Category: Exception deleting the category" + e.getMessage());
        }

    }

     /**
     * RESTful GET method for find every {@link  Category} objects that contains the Path name and through an XML
     * representation
     * @param name String used for find Set of  {@link Category} that contains the string on his name attribute
     * @return A Set of {@link Category} object with the categories that contains the string on his name attribute
     */
    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_XML)
    public Category findCategoryByName(@PathParam("name") String name) {
        Category category = null;
        try {
            category = eJBLocal.findCategoryByName(name);
        } catch (Exception ex) {
            LOGGER.warning("REST Category: Exception creating " + ex.getMessage());
        }
        return category;
    }

    /**
     * RESTful GET method for find a {@link  Document} object using the {@link Category} name and the {@link Document} name and through an XML
     * representation
     * @param catName String with the literal name of the category
     * @param docName String with the literal name of the document
     * @return A {@link Document} object with the document that match with the parameters
     */
    @GET
    @Path("{catName}/{docName}")
    @Produces(MediaType.APPLICATION_XML)
    public Document findDocumentsByCategory(@PathParam("catName") String catName, @PathParam("docName") String docName) {
        Document doc = null;
        try {
            doc = eJBLocal.findDocumentsByCategory(catName, docName);
        } catch (Exception ex) {
            LOGGER.warning("REST Category: Exception creating " + ex.getMessage());
        }
        return doc;

    }

    /**
     * RESTful GET method for find every {@link  Category} objects and through an XML
     * representation
     * @return A Set of {@link Category} object with all the categories in the data base 
     */
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
