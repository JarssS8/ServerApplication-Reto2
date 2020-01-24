/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import serverapplication.entities.Category;
import serverapplication.entities.Document;
import serverapplication.entities.Rating;
import serverapplication.exceptions.DocumentNotFoundException;
import serverapplication.exceptions.RatingNotFoundException;
import serverapplication.interfaces.EJBDocumentRatingLocal;

/**
 *
 * @author Gaizka Andrés
 */
@Stateless
public class EJBDocumentRating implements EJBDocumentRatingLocal{
    private static final Logger LOGGER = Logger.getLogger("rest");
    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    private EntityManager em;
    
    //----------------------------Document------------------------------------\\
    
    /**
     * Method to create a new document
     * @param document the object will be created.
     */
    @Override
    public void newDocument(Document document) {
        em.persist(document);
    }
    /**
    * Method to modify a specific document
    * @param document the document will be modificated.
    * @throws DocumentNotFoundException exception if are no document 
    */
    @Override
    public void modifyDocument(Document document) throws DocumentNotFoundException{
        try{
            em.merge(document);
            em.flush(); 
        }catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }
        
    }
 
    /**
     * Method to remove a specific document
     * @param document the object will be removed.
     * @throws DocumentNotFoundException exception if are no document 
     */
    @Override
    public void deleteDocument(Document document) {
       try{
       Query q = em.createQuery("DELETE FROM Document d WHERE d.id = :id");
       q.setParameter("id", document.getId());
       q.executeUpdate();
       }catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
       }
    }
    /**
     * Method to search a specific document by his id.
     * @param id the id of the document
     * @return The document with the specified id
     */
   @Override
    public Document findDocumentById(Long id) throws DocumentNotFoundException{
        Document document= null;
        try{
            document = em.find(Document.class, id);
        }catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }
        
        return document;
    }
    
    /**
     * Method who give all the documents
     * @return All the documents list
     * @throws DocumentNotFoundException exception if are no document 
     */
    @Override
    public List<Document> findAllDocuments() throws DocumentNotFoundException{
        List<Document> allDocs = null;
        try{
            allDocs = em.createNamedQuery("findAllDocuments").getResultList();
            if(allDocs == null){
                LOGGER.severe("EJBDocumentandRating: Document Not Found");
                throw new DocumentNotFoundException();
            }
        }catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }
         return allDocs;
    }
    /**
     * Method who search by document name
     * @param name document name
     * @return list of document with that name
     */
    public List<Document> findDocumentNameByName(String name){
        List<Document> documents = null;
        
        documents = em.createNamedQuery("findDocumentNamebyName")
            .setParameter("name", name).getResultList();
        
        return documents;
    }
    /**
     * Method who search by various parameters
     * @param name the name of the document
     * @param category the category of the document
     * @param uploadDate the date of the upload date
     * @return A list of documents 
     * @throws DocumentNotFoundException exception if are no document 
     */
    @Override
    public List<Document> findDocumentNameByParameters(String name, Category category, Date uploadDate) throws DocumentNotFoundException {
        List<Document> docNames = null;
        try{
                docNames = em.createNamedQuery("findDocumentNameByParameters").
                setParameter("name",name).
                setParameter("category",category).
                setParameter("uploadDate",uploadDate).getResultList();
                if(docNames==null){
                    LOGGER.severe("EJBDocumentandRating: Document Not Found");
                    throw new DocumentNotFoundException();
                }
        }catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }
        return docNames;
    }
    
    /**
     * Method to search all the ratings of a specific document
     * @param name the name of the document to search by
     * @return A ratings list of the specified document
     * @throws documentNotFoundException exception if are no document 
     */
    
    @Override
    public Set<Rating> findRatingsOfDocument(Long id) throws RatingNotFoundException{
        Document document = null;
        try{
            document = (Document) em.createNamedQuery("findRatingsOfDocument").setParameter("id", id).getSingleResult();
            if(document == null){
                LOGGER.severe("EJBDocumentandRating: Document Not Found");
                throw new DocumentNotFoundException();
            }
        }catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }
        
        return new HashSet<> (document.getRatings());
    }
    //--------------------------------Rating----------------------------------\\
    
    /**
     * Method to create a new Rating
     * @param Rating the rating will be created
     * @throws RatingNotFoundException exception if are no rating 
     */
    @Override
    public void newDocumentRating(Rating Rating) {
        em.persist(Rating);
    }
    
    @Override
    public Rating findRatingById(Long id) throws RatingNotFoundException {
        Rating rating= null;
        try{
           rating =  em.find(Rating.class, id);
        }catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }
        return rating;
    }

    
    /**
     * Method to modify a specific rating
     * @param Rating 
     * @throws RatingNotFoundException exception if are no rating 
     */
    @Override
    public void updateRating(Rating Rating) throws RatingNotFoundException{
       try{
            em.merge(Rating);
            em.flush();
       }catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
       }
    }
    
    /**
     * Method to search all the ratings
     * @return all ratings list
     * @throws RatingNotFoundException exception if are no rating 
     */
    @Override
    public List<Rating> findAllRatings()throws RatingNotFoundException{
        List<Rating> ratings = null;
        try{
           ratings =em.createNamedQuery("findAllRating").getResultList();
           if(ratings == null){
               throw new RatingNotFoundException();
           }
        }catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }

        return ratings;
    }
    
    /**
     * Method to remove a specific rating
     * @param rating
     * @throws RatingNotFoundException exception if are no rating 
     */
    @Override
    public void deleteRating(Rating rating) throws RatingNotFoundException{
       try{
            Query q = em.createQuery("DELETE FROM Document d WHERE d.id = :id");
            q.setParameter("id", rating.getId());
            q.executeUpdate(); 
       }catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
       }
       
    }
    @Override
    public List<Rating> DocumentsRating(Long id){
        List<Rating> ratings;
        Query q = em.createQuery("SELECT r FROM Rating r WHERE r.document.id = :id");
        q.setParameter("id", id);
        ratings = (List<Rating>) q.getResultList();
        return ratings; 
    }

    
}
