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
import serverapplication.exceptions.DocumenttNotFoundException;
import serverapplication.exceptions.RatinggNotFoundException;
import serverapplication.exceptions.ServerConnectionErrorException;

/**
 *
 * @author Gaizka Andrés
 */
@Local
public interface EJBDocumentRatingLocal {
    
    //----------------------Document-----------------------------
    
    public void newDocument(Document document);

    public void modifyDocument(Document document) throws DocumenttNotFoundException,ServerConnectionErrorException;
    
    public void deleteDocument(Document document);
    
    public List<Document> findAllDocuments() throws DocumenttNotFoundException,ServerConnectionErrorException;
    
    public Document findDocumentById(Long id) throws DocumenttNotFoundException,ServerConnectionErrorException;
    
    public List<Document> findDocumentNameByParameters(String name,Category category,Date uploadDate) throws DocumenttNotFoundException,ServerConnectionErrorException;
    
    public Set<Rating> findRatingsOfDocument(Long id) throws RatinggNotFoundException,ServerConnectionErrorException;
    
    //----------------------Rating-----------------------------
    
    public void newDocumentRating(Rating Rating);
    
    public List<Rating> findAllRatings() throws RatinggNotFoundException,ServerConnectionErrorException;
    
    public Rating findRatingById(Long id) throws RatinggNotFoundException,ServerConnectionErrorException;
    
    public void updateRating(Rating Rating) throws RatinggNotFoundException,ServerConnectionErrorException;
    
    public void deleteRating(Rating rating) throws RatinggNotFoundException,ServerConnectionErrorException; 
    
   

}
