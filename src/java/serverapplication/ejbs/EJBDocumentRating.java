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

/**
 *
 * @author Gaizka Andrés
 */
@Stateless
public class EJBDocumentRating{

    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    private EntityManager em;
    
    //----------------------Document-----------------------------
    
    public void createDocument(Document document) {
        em.persist(document);
    }
   
    public void editDocument(Document document) {
       em.merge(document);
       em.flush();
    }
 
    public void removeDocument(Document document) {
        em.remove(em.merge(document));
    }

    public Document findDocument(Long id) {
        return em.find(Document.class, id);
    }

    public List<Document> findAll() {
        return em.createQuery("findAll").getResultList();
    }
  
    public List<Document> findDocumentByName(String name) {
        return (List<Document>)em.createNamedQuery("findDocumentByName").setParameter("name", name).getResultList();
        
    }

    public List<Document> findDocumentByCategory(String category) {
        return (List<Document>)em.createNamedQuery("findDocumentByCategory").setParameter("category", category).getResultList();
    }

    public List<Document> findDocumentByDate(Date uploadDate) {
        return (List<Document>)em.createNamedQuery("findDocumentByDate").setParameter("uploadDate", uploadDate).getResultList();
    }

    public List<Document> findDocumentByNameAndCategory(String name, String category) {
        return (List<Document>)em.createNamedQuery("findDocumentByNameAndCategory").setParameter("name", name).setParameter("category", category).getResultList();
    }

    public List<Document> findDocumentByNameAndDate(String name, Date uploadDate) {
       return (List<Document>)em.createNamedQuery("findDocumentByNameAndDate").setParameter("name", name).setParameter("uploadDate", uploadDate).getResultList();
    }

    public List<Document> findDocumentByCategoryAndDate(String category, Date uploadDate) {
        return (List<Document>)em.createNamedQuery("findDocumentByNameAndDate").setParameter("category", category).setParameter("uploadDate", uploadDate).getResultList();
    }
    
    //----------------------Rating-----------------------------
    
    public void createRating(Rating Rating) {
        em.persist(Rating);
    }
    
    public void editRating(Rating Rating) {
       em.merge(Rating);
       em.flush();
    }
    
    public void removeRating(Rating rating) {
        em.remove(em.merge(rating));
    }

    List<Rating> findRatingsOfDocument(String name) {
        return (List<Rating>)em.find(Document.class, name).getRatings();
    }

}
