/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import serverapplication.interfaces.EJBDocumentRatingLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
import serverapplication.entities.Rating;
import serverapplication.entities.RatingId;
import serverapplication.exceptions.RatingNotFoundException;
import serverapplication.exceptions.ServerConnectionErrorException;

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
    public void newDocumentRating(Rating rating){
        ejb.newDocumentRating(rating);
    }
    /**
     * Method who use the ejb to search all the ratings
     * @throws RatingNotFoundException exception if are no rating 
     * @throws ServerConnectionErrorException exception if are a problem with 
     * the server
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Rating> findAllRatings(){
        List<Rating> ratings = null;
        try {
            ratings = ejb.findAllRatings();
        } catch (RatingNotFoundException ex) {
           LOGGER.severe(ex.getMessage());
           throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return ratings;
    }
    
    /**
     * Method who use the ejb to mofify the Rating
     * @param rating the rating will be modified
     * @throws RatingNotFoundException exception if are no rating 
     * @throws ServerConnectionErrorException exception if are a problem with 
     * the server
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public void updateRating(Rating rating) {
        try {
            ejb.updateRating(rating);
        } catch (RatingNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
    
    /**
     * Method who use the ejb to delete a Rating
     * @param rating the rating will be deleted
     * @throws RatingNotFoundException exception if are no rating 
     * @throws ServerConnectionErrorException exception if are a problem with 
     * the server
     */
    @DELETE
    @Path("{id}")
    public void deleteRating(RatingId ratingid) {
        try {
            ejb.deleteRating(ejb.findRatingById(ratingid));
        } catch (RatingNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
    @GET
    @Path("/documentRatings/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public List<Rating> DocumentsRating(@PathParam("id") Long id){
        List<Rating> ratings;
        ratings = ejb.DocumentsRating(id);
        return ratings;
    }
 
}
