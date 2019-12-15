/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import serverapplication.entities.Document;
import serverapplication.entities.Rating;
import serverapplication.exceptions.documentNotFoundException;
import serverapplication.exceptions.ratingNotFoundException;
import serverapplication.interfaces.EJBDocumentRatingLocal;

/**
 *
 * @author Gaizka Andrés
 */
@Stateless
public class EJBDocumentRating implements EJBDocumentRatingLocal{

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
    * @throws documentNotFoundException exception if are no document 
    */
    @Override
    public void modifyDocument(Document document) {
        em.merge(document);
        em.flush();
    }
 
    /**
     * Method to remove a specific document
     * @param document the object will be removed.
     * @throws documentNotFoundException exception if are no document 
     */
    @Override
    public void deleteDocument(Document document) {
       Query q = em.createQuery("DELETE FROM Document d WHERE d.id = :id");
       q.setParameter("id", document.getId());
       int delete = q.executeUpdate();
    }
    /**
     * Method to search a specific document by his id.
     * @param id the id of the document
     * @return The document with the specified id
     */
   @Override
    public Document findDocumentById(Long id) {
        return em.find(Document.class, id);
    }
    
    /**
     * Method who give all the documents
     * @return All the documents list
     * @throws documentNotFoundException exception if are no document 
     */
    @Override
    public List<Document> findAllDocuments() {
        return em.createNamedQuery("findAllDocuments").getResultList();
    }
    /**
     * Method who search by various parameters
     * @param name the name of the document
     * @param category the category of the document
     * @param uploadDate the date of the upload date
     * @return A list of documents 
     * @throws documentNotFoundException exception if are no document 
     */
    @Override
    public List<String> findDocumentNameByParameters(Document document) throws documentNotFoundException {
        List<String> documents;
        Query q = em.createQuery("SELECT d FROM Document d WHERE UPPER(d.name) LIKE UPPER(:name) AND UPPER(d.category) LIKE UPPER(:category) AND UPPER(d.uploadDate) LIKE UPPER(:uploadDate)");
        q.setParameter("name", document.getName());
        q.setParameter("category", document.getCategory());
        q.setParameter("uploadDate", document.getUploadDate());
        documents = q.getResultList();

        return documents;
    }
    
    /**
     * Method to search all the ratings of a specific document
     * @param name the name of the document to search by
     * @return A ratings list of the specified document
     * @throws documentNotFoundException exception if are no document 
     */
    /*
    @Override
    public Document findRatingsOfDocument(Long id) {
        return em.find(Document.class, id);
    }
    */
    //--------------------------------Rating----------------------------------\\
    
    /**
     * Method to create a new Rating
     * @param Rating the rating will be created
     * @throws ratingNotFoundException exception if are no rating 
     */
    @Override
    public void newDocumentRating(Rating Rating) {
        em.persist(Rating);
    }
    
    @Override
    public Rating findRatingById(Long id) throws ratingNotFoundException {
        return em.find(Rating.class, id);
    }

    
    /**
     * Method to modify a specific rating
     * @param Rating 
     * @throws ratingNotFoundException exception if are no rating 
     */
    @Override
    public void updateRating(Rating Rating) {
       em.merge(Rating);
       em.flush();
    }
    
    /**
     * Method to search all the ratings
     * @return all ratings list
     * @throws ratingNotFoundException exception if are no rating 
     */
    @Override
    public List<Rating> findAllRatings(){
        return em.createNamedQuery("findAllRating").getResultList();
    }
    
    /**
     * Method to remove a specific rating
     * @param rating
     * @throws ratingNotFoundException exception if are no rating 
     */
    @Override
    public void deleteRating(Rating rating) {
        Query q = em.createQuery("DELETE FROM Document d WHERE d.id = :id");
       q.setParameter("id", rating.getId());
       int delete = q.executeUpdate();
    }

    
}
