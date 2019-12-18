/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import java.util.List;
import java.util.logging.Logger;
import serverapplication.interfaces.EJBDocumentRatingLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import serverapplication.entities.Rating;
import serverapplication.exceptions.DocumentNotFoundException;
import serverapplication.exceptions.RatingNotFoundException;

/**
 *
 * @author Gaizka Andrés
 */
@Stateless
@Path("rating")
public class RatingFacadeREST{
    
    private static final Logger LOGGER = Logger.getLogger("rest");
    
    /**
     * Injection of the ejb
     */
    @EJB
    private EJBDocumentRatingLocal ejb;
    
    /**
     * Method who use the ejb to create a Rating
     * @param rating the rating will be created
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void newDocumentRating(Rating rating) {
        ejb.newDocumentRating(rating);
    }
    /**
     * Method who use the ejb to search all the ratings
     * @throws RatingNotFoundException exception if are no rating 
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Rating> findAllRatings(){
        List<Rating> ratings = null;
        try {
            ratings = ejb.findAllRatings();
        } catch (RatingNotFoundException ex) {
           LOGGER.severe(ex.getMessage());
        }
        return ratings;
    }
    
    /**
     * Method who use the ejb to search a document by his id
     * @param id the id to search by
     * @return the document with the specified id
     * @throws DocumentNotFoundException exception if are no document 
     */
    
    /*
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Rating findRating(Long id) {
        Rating rating = null;
        try {
            rating = ejb.findRatingById(id);
        } catch (RatingNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return rating;
    }
    */
    
    /**
     * Method who use the ejb to mofify the Rating
     * @param rating the rating will be modified
     * @throws RatingNotFoundException exception if are no rating 
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public void updateRating(Rating rating) {
        try {
            ejb.updateRating(rating);
        } catch (RatingNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
    
    /**
     * Method who use the ejb to delete a Rating
     * @param rating the rating will be deleted
     * @throws RatingNotFoundException exception if are no rating 
     */
    @DELETE
    @Path("{id}")
    public void deleteRating(Long id) {
        try {
            ejb.deleteRating(ejb.findRatingById(id));
        } catch (RatingNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
 
}
