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
import serverapplication.entities.RatingId;
import serverapplication.exceptions.documentNotFoundException;
import serverapplication.exceptions.ratingNotFoundException;

/**
 *
 * @author Gaizka Andrés
 */
@Local
public interface EJBDocumentRatingLocal {
    
    //----------------------Document-----------------------------
    
    public void newDocument(Document document);

    public void modifyDocument(Document document) throws documentNotFoundException;
    
    public void deleteDocument(Document document) throws documentNotFoundException;
    
    public List<Document> findAllDocuments() throws documentNotFoundException;
    
    public Document findDocumentById(Long id) throws documentNotFoundException;
    
    public List<String> findDocumentNameByParameters(Document document) throws documentNotFoundException;
    
   // public Document findRatingsOfDocument(Long id) throws ratingNotFoundException;
    
    //----------------------Rating-----------------------------
    
    public void newDocumentRating(Rating Rating);
    
    public List<Rating> findAllRatings() throws ratingNotFoundException;
    
    public Rating findRatingById(Long id) throws ratingNotFoundException;
    
    public void updateRating(Rating Rating) throws ratingNotFoundException;
    
    public void deleteRating(Rating rating) throws ratingNotFoundException; 
    
   

}
