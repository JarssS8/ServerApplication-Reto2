/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import serverapplication.interfaces.EJBDocumentRatingLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import serverapplication.entities.Rating;

/**
 *
 * @author Gaizka Andrés
 */
@Stateless
@Path("rating")
public class RatingFacadeREST{

    @EJB
    private EJBDocumentRatingLocal ejb;
    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Rating entity) {
        ejb.createRating(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(Rating rating) {
        ejb.editRating(rating);
    }

    @DELETE
    @Path("{id}")
    public void removeRating(Rating rating) {
        ejb.removeRating(rating);
    }
    
}
