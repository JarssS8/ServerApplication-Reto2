/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import serverapplication.interfaces.EJBDocumentRatingLocal;
import java.util.Date;
import java.util.List;
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
import javax.ws.rs.core.PathSegment;
import serverapplication.entities.Document;
import serverapplication.entities.RatingId;

/**
 *
 * @author Gaizka Andrés
 */
@Path("document")
public class DocumentFacadeREST{
    
    private RatingId getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;idDocument=idDocumentValue;idUser=idUserValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        serverapplication.entities.RatingId key = new serverapplication.entities.RatingId();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> idDocument = map.get("idDocument");
        if (idDocument != null && !idDocument.isEmpty()) {
            key.setIdDocument(new java.lang.Long(idDocument.get(0)));
        }
        java.util.List<String> idUser = map.get("idUser");
        if (idUser != null && !idUser.isEmpty()) {
            key.setIdUser(new java.lang.Long(idUser.get(0)));
        }
        return key;
    }
    
    @EJB
    private EJBDocumentRatingLocal ejb;
    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Document document) {
        ejb.createDocument(document);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(Document document) {
        ejb.createDocument(document);
    }

    @DELETE
    @Path("{id}")
    public void remove(Document document) {
        ejb.removeDocument(document);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Document findDocument(@PathParam("id") Long id) {
        return ejb.findDocument(id);
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Document> findAll() {
        return ejb.findAll();
    }
    
    /*
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Document> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    */

    @GET
    @Path("DocumentByName")
    @Produces(MediaType.APPLICATION_XML)
    public List<Document> findDocumentByName(@PathParam("name") String name){
        return ejb.findDocumentByName(name);
    }
    
    @GET
    @Path("DocumentByCategory")
    @Produces(MediaType.APPLICATION_XML)
    public List<Document> findDocumentByCategory(@PathParam("category") String category){
        return ejb.findDocumentByCategory(category);
    }
    
    @GET
    @Path("DocumentByDate")
    @Produces(MediaType.APPLICATION_XML)
    public List<Document> findDocumentByDate(@PathParam("uploadDate") Date uploadDate){
        return ejb.findDocumentByDate(uploadDate);
    }
    
    @GET
    @Path("DocumentByNameAndCategory")
    @Produces(MediaType.APPLICATION_XML)
    public List<Document> findDocumentByNameAndCategory(@PathParam("name") String name, @PathParam("category") String category){
        return ejb.findDocumentByNameAndCategory(name,category);
    }
    
    @GET
    @Path("DocumentByNameAndDate")
    @Produces(MediaType.APPLICATION_XML)
    public List<Document> findDocumentByNameAndDate(@PathParam("name") String name, @PathParam("uploadDate") Date uploadDate){
        return ejb.findDocumentByNameAndDate(name,uploadDate);
    }
    
    @GET
    @Path("DocumentByCategoryAndDate")
    @Produces(MediaType.APPLICATION_XML)
    public List<Document> findDocumentByCategoryAndDate(@PathParam("category") String category, @PathParam("uploadDate") Date uploadDate){
        return ejb.findDocumentByCategoryAndDate(category,uploadDate);
    }
    
}
