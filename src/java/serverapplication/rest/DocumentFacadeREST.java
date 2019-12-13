/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import serverapplication.interfaces.EJBDocumentRatingLocal;
import java.util.Date;
import java.util.List;
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
import javax.ws.rs.core.PathSegment;
import serverapplication.entities.Document;
import serverapplication.entities.Rating;
import serverapplication.entities.RatingId;
import serverapplication.exceptions.documentNotFoundException;
import serverapplication.exceptions.ratingNotFoundException;

/**
 *
 * @author Gaizka Andrés
 */
@Path("document")
public class DocumentFacadeREST{
    private static final Logger LOGGER = Logger.getLogger("rest");
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
    
    /**
     * Injection of the ejb
     */
    @EJB
    private EJBDocumentRatingLocal ejb;
    
    /**
     * Method who use the ejb to create a Document 
     * @param document the document will be created
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void newDocument(Document document) {
        ejb.newDocument(document);
    }
    
    /**
     * Method who use the ejb to modify a document
     * @param document the document will be modified
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyDocument(Document document) {
        try {
            ejb.modifyDocument(document);
        } catch (documentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
    
    /**
     * Method who use the ejb to delete a document
     * @param document the document will be deleted
     */
    @DELETE
    @Path("{id}")
    public void deleteDocument(Document document) {
        try {
            ejb.deleteDocument(document);
        } catch (documentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
    
    /**
     * Method who use the ejb to search all the documents
     * @return All the documents list
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Document> findAllDocuments() {
        List<Document> documents = null;
        try {
            documents = ejb.findAllDocuments();
        } catch (documentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return documents;
        
    }
    
    /**
     * Method who use the ejb to search a document by his id
     * @param id the id to search by
     * @return the document with the specified id
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Document findDocument(@PathParam("id") Long id) {
        Document document = null;
        try {
            document = ejb.findDocumentById(id);
        } catch (documentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return document;
    }
    
    @GET
    @Path("{name}/{category}/{uploadDate}")
    @Produces(MediaType.APPLICATION_XML)
    public Document findDocumentNameByParameters(@PathParam("name") String name
        ,@PathParam("category") String category
        ,@PathParam("uploadDate") Date uploadDate){
        Document document = null;
        try {
            document=(Document) ejb.findDocumentNameByParameters(name, category, uploadDate);
        } catch (documentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return document;
    }
    
    /**
     * Method who use the ejb to search Rating of a document
     * @param name the name of the document
     */
    @GET
    @Path("{id}1")
    @Produces(MediaType.APPLICATION_XML)
    public Document findRatingsOfDocument(@PathParam("id") Long id){
        Document document = null;
        List<Rating> ratingsDocument = null;
        try {
            document = ejb.findRatingsOfDocument(id);
            ratingsDocument = (List<Rating>) document.getRatings();
        } catch (ratingNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return (Document) ratingsDocument;
    }
}
