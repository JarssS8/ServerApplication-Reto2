/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import java.util.logging.Level;
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
import serverapplication.exceptions.ratingNotFoundException;

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
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void newDocumentRating(Rating rating) {
        ejb.newDocumentRating(rating);
    }
    /**
     * Method who use the ejb to search all the ratings
     */
    @GET
    @Path("findAllRatings")
    @Produces(MediaType.APPLICATION_XML)
    public void findAllRatings(){
        try {
            ejb.findAllRatings();
        } catch (ratingNotFoundException ex) {
           LOGGER.severe(ex.getMessage());
        }
    }
    
    /**
     * Method who use the ejb to mofify the Rating
     * @param rating the rating will be modified
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updateRating(Rating rating) {
        try {
            ejb.updateRating(rating);
        } catch (ratingNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
    
    /**
     * Method who use the ejb to delete a Rating
     * @param rating the rating will be deleted
     */
    @DELETE
    @Path("{id}")
    public void deleteRating(Rating rating) {
        try {
            ejb.deleteRating(rating);
        } catch (ratingNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
    
    /**
     * Method who use the ejb to search Rating of a document
     * @param name the name of the document
     */
    @GET
    @Path("RatingsOfDocument")
    @Produces(MediaType.APPLICATION_XML)
    public void findRatingsOfDocument(String name){
        try {
            ejb.findRatingsOfDocument(name);
        } catch (ratingNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
    
}
