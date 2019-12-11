/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import serverapplication.entities.Document;
import serverapplication.entities.Rating;

/**
 *
 * @author Gaizka Andrés
 */
@Local
public interface EJBDocumentRatingLocal {
    
    //----------------------Document-----------------------------
    
    public void createDocument(Document document);

    public void editDocument(Document document);
    
    public void removeDocument(Document document);
    
    public Document findDocument(Long id);

    public List<Document> findAll();

    /*public List<T> findRange(int[] range);
    
    public int count();
    */
    public List<Document> findDocumentByName(String name);
    
    public List<Document> findDocumentByCategory(String category);
    
    public List<Document> findDocumentByDate(Date uploadDate);
    
    public List<Document> findDocumentByNameAndCategory(String name, String category);
    
    public List<Document> findDocumentByNameAndDate(String name, Date uploadDate);
    
    public List<Document> findDocumentByCategoryAndDate(String category, Date uploadDate);
    
    //----------------------Rating-----------------------------
    
    public void createRating(Rating Rating);
    
    public void editRating(Rating Rating);
    
    public void removeRating(Rating rating); 
    
    List<Document> findRatingsOfDocument(String rating);
    
    List<Document> findReviewsOfDocument(String review);
    
    
}
