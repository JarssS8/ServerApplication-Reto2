/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import serverapplication.entities.Category;
import serverapplication.entities.Document;
import serverapplication.entities.Rating;
import serverapplication.exceptions.DocumentNotFoundException;
import serverapplication.exceptions.RatingNotFoundException;

/**
 *
 * @author Gaizka Andrés
 */
@Local
public interface EJBDocumentRatingLocal {
    
    //----------------------Document-----------------------------
    
    public void newDocument(Document document);

    public void modifyDocument(Document document) throws DocumentNotFoundException;
    
    public void deleteDocument(Document document) throws DocumentNotFoundException;
    
    public List<Document> findAllDocuments() throws DocumentNotFoundException;
    
    public Document findDocumentById(Long id) throws DocumentNotFoundException;
    
    public List<Document> findDocumentNameByParameters(String name,Category category) throws DocumentNotFoundException;
    
    public Set<Rating> findRatingsOfDocument(Long id) throws RatingNotFoundException;
    
    //----------------------Rating-----------------------------
    
    public void newDocumentRating(Rating Rating);
    
    public List<Rating> findAllRatings() throws RatingNotFoundException;
    
    public Rating findRatingById(Long id) throws RatingNotFoundException;
    
    public void updateRating(Rating Rating) throws RatingNotFoundException;
    
    public void deleteRating(Rating rating) throws RatingNotFoundException; 
    
   

}
