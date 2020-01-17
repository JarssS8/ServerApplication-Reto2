/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import serverapplication.entities.Category;
import serverapplication.entities.Document;
import serverapplication.entities.Rating;
import serverapplication.exceptions.DocumentNotFoundException;
import serverapplication.exceptions.RatingNotFoundException;
import serverapplication.exceptions.ServerConnectionErrorException;

/**
 *
 * @author Gaizka Andrés
 */
@Local
public interface EJBDocumentRatingLocal {
    
    //----------------------Document-----------------------------
    
    public void newDocument(Document document);

    public void modifyDocument(Document document) throws DocumentNotFoundException,ServerConnectionErrorException;
    
    public void deleteDocument(Document document);
    
    public List<Document> findAllDocuments() throws DocumentNotFoundException,ServerConnectionErrorException;
    
    public Document findDocumentById(Long id) throws DocumentNotFoundException,ServerConnectionErrorException;
    
    public List<Document> findDocumentNameByParameters(String name,Category category,Date uploadDate) throws DocumentNotFoundException,ServerConnectionErrorException;
    
    public Set<Rating> findRatingsOfDocument(Long id) throws RatingNotFoundException,ServerConnectionErrorException;
    
 
    //----------------------Rating-----------------------------
    
    public void newDocumentRating(Rating Rating);
    
    public List<Rating> findAllRatings() throws RatingNotFoundException,ServerConnectionErrorException;
    
    public Rating findRatingById(Long id) throws RatingNotFoundException,ServerConnectionErrorException;
    
    public void updateRating(Rating Rating) throws RatingNotFoundException,ServerConnectionErrorException;
    
    public void deleteRating(Rating rating) throws RatingNotFoundException,ServerConnectionErrorException; 
    
   

}
