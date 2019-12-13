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
import serverapplication.entities.Document;
import serverapplication.entities.Rating;
import serverapplication.exceptions.documentNotFoundException;
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
    */
    @Override
    public void modifyDocument(Document document) {
        em.merge(document);
        em.flush();
    }
 
    /**
     * Method to remove a specific document
     * @param document the object will be removed.
     */
    @Override
    public void deleteDocument(Document document) {
        em.remove(em.merge(document));
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
     */
    @Override
    public List<Document> findAllDocuments() {
        return em.createNamedQuery("findAllDocuments").getResultList();
    }
   
    @Override
    public List<String> findDocumentNameByParameters(String name, String category, Date uploadDate) throws documentNotFoundException {
        return (List<String>) em.createNamedQuery("findDocumentNameByParameters")
                .setParameter("name","%" + name + "%")
                .setParameter("user","%" + category + "%")
                .setParameter("uploadDate", uploadDate ).getResultList();
    }
    
    /**
     * Method to search all the ratings of a specific document
     * @param name the name of the document to search by
     * @return A ratings list of the specified document
     */
    @Override
    public Document findRatingsOfDocument(Long id) {
        return em.find(Document.class, id);
    }
    
    //--------------------------------Rating----------------------------------\\
    
    /**
     * Method to create a new Rating
     * @param Rating the rating will be created
     */
    @Override
    public void newDocumentRating(Rating Rating) {
        em.persist(Rating);
    }
    
    /**
     * Method to modify a specific rating
     * @param Rating 
     */
    @Override
    public void updateRating(Rating Rating) {
       em.merge(Rating);
       em.flush();
    }
    
    /**
     * Method to search all the ratings
     * @return all ratings list
     */
    @Override
    public List<Rating> findAllRatings(){
        return em.createNamedQuery("findAllRating").getResultList();
    }
    
    /**
     * Method to remove a specific rating
     * @param rating 
     */
    @Override
    public void deleteRating(Rating rating) {
        em.remove(em.merge(rating));
    }

}
