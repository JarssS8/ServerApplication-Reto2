/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.rest;

import java.util.Date;
import serverapplication.interfaces.EJBDocumentRatingLocal;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
import javax.ws.rs.core.PathSegment;
import serverapplication.entities.Category;
import serverapplication.entities.Document;
import serverapplication.entities.Rating;
import serverapplication.entities.RatingId;
import serverapplication.exceptions.DocumentNotFoundException;
import serverapplication.exceptions.RatingNotFoundException;
import serverapplication.exceptions.ServerConnectionErrorException;
import serverapplication.interfaces.CategoryEJBLocal;

/**
 * RESTful web service class exposing CRUD operations for Document.
 *
 * @author Gaizka Andrés
 */
@Path("document")
public class DocumentFacadeREST {

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
     * Injection of the Docuement & Rating ejb
     */
    @EJB
    private EJBDocumentRatingLocal ejb;

    /**
     * Injection of the Category ejb
     */
    @EJB
    private CategoryEJBLocal ejbCat;

    /**
     * Method who use the ejb to create a Document
     *
     * @param document the document will be created
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void newDocument(Document document) {
        document.setId(null);
        ejb.newDocument(document);
    }

    /**
     * Method who use the ejb to modify a document
     *
     * @param document the document will be modifie
     * @throws DocumentNotFoundException exception if are no document
     * @throws ServerConnectionErrorException exception if are a problem with
     * the server
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public void modifyDocument(Document document) {
        try {
            ejb.modifyDocument(document);
        } catch (DocumentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    /**
     * Method who use the ejb to delete a document
     *
     * @param document the document will be deleted
     * @throws DocumentNotFoundException exception if are no document
     * @throws ServerConnectionErrorException exception if are a problem with
     * the server
     */
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public void deleteDocument(@PathParam("id") Long id) {
        try {
            ejb.deleteDocument(ejb.findDocumentById(id));
        } catch (DocumentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    /**
     * Method who use the ejb to search all the documents
     *
     * @return All the documents list
     * @throws DocumentNotFoundException exception if are no document
     * @throws ServerConnectionErrorException exception if are a problem with
     * the server
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Document> findAllDocuments() {
        List<Document> documents = null;
        try {
            documents = ejb.findAllDocuments();
        } catch (DocumentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return documents;

    }

    /**
     * Method who use the ejb to search a document by his id
     *
     * @param id the id to search by
     * @return the document with the specified id
     * @throws DocumentNotFoundException exception if are no document
     * @throws ServerConnectionErrorException exception if are a problem with
     * the server
     */
    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Document findDocument(@PathParam("id") Long id) {
        Document document = null;
        try {
            document = ejb.findDocumentById(id);
        } catch (DocumentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return document;
    }

    /**
     * Method who use the ejb to search a document by his name
     *
     * @param name the name to search by
     * @return List of documents with that name or contains that name
     */
    @GET
    @Path("name/{name}")
    @Produces(MediaType.APPLICATION_XML)
    public List<Document> findDocumentNamebyName(@PathParam("name") String name) {
        List<Document> documentNoFile = new Vector<Document>();
        List<Document> documents = null;
        documents = ejb.findDocumentNameByName(name);
        for (Document auxDocu : documents) {
            documentNoFile.add(new Document(
                    auxDocu.getId(),
                    auxDocu.getName(),
                    auxDocu.getCategory(),
                    auxDocu.getUploadDate(),
                    auxDocu.getTotalRating(),
                    auxDocu.getRatingCount()));
        }
        return documentNoFile;
    }

    /**
     * Method who use the ejb to search a document by various parameters
     *
     * @param name the name to search by
     * @param category the category to search by
     * @return A list of names of documents
     */
    @GET
    @Path("/parameters/{name}/{category}")
    @Produces(MediaType.APPLICATION_XML)
    public List<Document> findDocumentNameByParameters(
            @PathParam("name") String name,
            @PathParam("category") String category) {

        List<Document> documentNoFile = new Vector<Document>();
        List<Document> documents = null;
        try {
            documents = ejb.findDocumentNameByParameters(name,
                    (Category) ejbCat.findCategoryByName(category));
            for (Document auxDocu : documents) {
                documentNoFile.add(new Document(
                        auxDocu.getId(),
                        auxDocu.getName(),
                        auxDocu.getCategory(),
                        auxDocu.getUploadDate(),
                        auxDocu.getTotalRating(),
                        auxDocu.getRatingCount()));
            }
        } catch (DocumentNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(DocumentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documentNoFile;
    }

    /**
     * Method who use the ejb to search Rating of a document
     *
     * @param name the name of the document
     * @throws documentNotFoundException exception if are no document
     * @throws ServerConnectionErrorException exception if are a problem with
     * the server
     */
    @GET
    @Path("/ratings/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Set<Rating> findRatingsOfDocument(@PathParam("id") Long id) {

        Set<Rating> ratingsDocument = null;
        try {
            ratingsDocument = (Set<Rating>) ejb.findRatingsOfDocument(id);
        } catch (RatingNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ServerConnectionErrorException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return ratingsDocument;
    }

}
