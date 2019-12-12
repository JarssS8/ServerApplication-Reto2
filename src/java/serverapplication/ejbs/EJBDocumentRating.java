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
    
    /**
     * Method to search a specific document by his name.
     * @param name the name of the document 
     * @return The document with the specified name
     */
    @Override
    public Document findDocumentByName(String name){
        return (Document) em.createNamedQuery("findDocumentByName").setParameter("name", name).getSingleResult();
    }
    /**
     * Method to search the names of documents by a name
     * @param name the name to search by
     * @return A documents names list who contains the send name.
     */
    @Override
    public List<String> findDocumentNameByName(String name) {
       return em.createNamedQuery("findDocumentNameByName").setParameter("name", name).getResultList();
    }
    /**
     * Method to search the names of documents by a category
     * @param category the category to search by
     * @return A documents names list who contains the send category.
     */
    @Override
    public List<String> findDocumentNameByCategory(String category) {
        return em.createNamedQuery("findDocumentNameByCategory").setParameter("category", category).getResultList();
    }
    /**
     * Method to search the names of documents by a date
     * @param uploadDate the date to search by
     * @return A documents names list who contains the send date.
     */
    @Override
    public List<String> findDocumentNameByDate(Date uploadDate) {
        return em.createNamedQuery("findDocumentNameByDate").setParameter("uploadDate", uploadDate).getResultList();
    }
    /**
     * Method to search the names of documents by a name and category.
     * @param name the name to search by
     * @param category the category to search by
     * @return A documents names list who contains the send name and category.
     */
    @Override
    public List<String> findDocumentNameByNameAndCategory(String name, String category) {
        return em.createNamedQuery("findDocumentNameByNameAndCategory").setParameter("name", name).setParameter("category", category).getResultList();
    }
    /**
     * Method to search the names of documents by a name and date.
     * @param name the name to search by
     * @param uploadDate the date to search by
     * @return A documents names list who contains the send name and date.
     */
    @Override
    public List<String> findDocumentNameByNameAndDate(String name, Date uploadDate) {
       return em.createNamedQuery("findDocumentNameByNameAndDate").setParameter("name", name).setParameter("uploadDate", uploadDate).getResultList();
    }
    /**
     * Method to search the names of documents by a category and date.
     * @param category the category to search by
     * @param uploadDate the date to search by
     * @return A documents names list who contains the send category and date.
     */
    @Override
    public List<String> findDocumentNameByCategoryAndDate(String category, Date uploadDate) {
        return em.createNamedQuery("findDocumentNameByCategoryAndDate").setParameter("category", category).setParameter("uploadDate", uploadDate).getResultList();
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
    /**
     * Method to search all the ratings of a specific document
     * @param name the name of the document to search by
     * @return A ratings list of the specified document
     */
    @Override
    public List<Rating> findRatingsOfDocument(String name) {
        return (List<Rating>)em.find(Document.class, name).getRatings();
    }

}
