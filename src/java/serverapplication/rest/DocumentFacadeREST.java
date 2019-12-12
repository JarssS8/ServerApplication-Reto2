/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import serverapplication.interfaces.EJBDocumentRatingLocal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
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
import serverapplication.entities.RatingId;
import serverapplication.exceptions.documentNotFoundException;

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
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void newDocument(Document document) {
        ejb.newDocument(document);
    }
    
    /**
     * Method who use the ejb to modify a document
     * @param document the document will be modified
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Document findDocument(@PathParam("id") Long id) {
        Document document = null;
        try {
            document = ejb.findDocumentById(id);
        } catch (documentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return document;
    }
    
    /**
     * Method who use the ejb to search a specific document by his name.
     * @param name the name of the document
     * @return The document with the specified name
     */
    @GET
    @Path("DocumentByName")
    @Produces(MediaType.APPLICATION_XML)
    public List<String> findDocumentByName(@PathParam("name") String name){
        List<String> documents = null;
        try {
            documents = ejb.findDocumentNameByName(name);
        } catch (documentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return documents;
    }
    
    /**
     * Method who use the ejb to search the names of documents by a name
     * @param name the name to search by
     * @return A documents names list who contains the send name.
     */
    @GET
    @Path("DocumentByName")
    @Produces(MediaType.APPLICATION_XML)
    public List<String> findDocumentNameByName(@PathParam("name") String name){
        List<String> documents = null;
        try {
            documents = ejb.findDocumentNameByName(name);
        } catch (documentNotFoundException ex) {
           LOGGER.severe(ex.getMessage());
        }
        return documents;
    }
    
    /**
     * Method who use the ejb to search the names of documents by a category
     * @param category the category to search by
     * @return A documents names list who contains the send category.
     */
    @GET
    @Path("DocumentByCategory")
    @Produces(MediaType.APPLICATION_XML)
    public List<String> findDocumentNameByCategory(@PathParam("category") String category){
        List<String> documents = null;
        try {
            documents = ejb.findDocumentNameByCategory(category);
        } catch (documentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return documents;
    }
    
    /**
     * Method who use the ejb to search the names of documents by a date
     * @param uploadDate the date to search by
     * @return A documents names list who contains the send date.
     */
    @GET
    @Path("DocumentByDate")
    @Produces(MediaType.APPLICATION_XML)
    public List<String> findDocumentNameByDate(@PathParam("uploadDate") Date uploadDate){
        List<String> documents = null;
        try {
            documents = ejb.findDocumentNameByDate(uploadDate);
        } catch (documentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return documents;
    }
    
    /**
     * Method who use the ejb to search the names of documents by a name and category.
     * @param name the name to search by
     * @param category the category to search by
     * @return A documents names list who contains the send name and category.
     */
    @GET
    @Path("DocumentByNameAndCategory")
    @Produces(MediaType.APPLICATION_XML)
    public List<String> findDocumentNameByNameAndCategory(@PathParam("name") String name, @PathParam("category") String category){
        List<String> documents = null;
        try {
            documents = ejb.findDocumentNameByNameAndCategory(name,category);
        } catch (documentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return documents;
    }
    
    /**
     * Method who use the ejb to search the names of documents by a name and date.
     * @param name the name to search by
     * @param uploadDate the date to search by
     * @return A documents names list who contains the send name and date.
     */
    @GET
    @Path("DocumentByNameAndDate")
    @Produces(MediaType.APPLICATION_XML)
    public List<String> findDocumentNameByNameAndDate(@PathParam("name") String name, @PathParam("uploadDate") Date uploadDate){
        List<String> documents = null;
        try {
            documents = ejb.findDocumentNameByNameAndDate(name,uploadDate);
        } catch (documentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return documents;
    }
    
    /**
     * Method who use the ejb to search the names of documents by a category and date.
     * @param category category the category to search by
     * @param uploadDate the date to search by
     * @return A documents names list who contains the send category and date.
     */
    @GET
    @Path("DocumentByCategoryAndDate")
    @Produces(MediaType.APPLICATION_XML)
    public List<String> findDocumentNameByCategoryAndDate(@PathParam("category") String category, @PathParam("uploadDate") Date uploadDate){
        List<String> documents = null;
        try {
            documents = ejb.findDocumentNameByCategoryAndDate(category,uploadDate);
        } catch (documentNotFoundException ex) {
           LOGGER.severe(ex.getMessage());
        }
        return documents;
    }
    
}
