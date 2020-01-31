/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import serverapplication.entities.Category;
import serverapplication.entities.Document;
import serverapplication.entities.Rating;
import serverapplication.entities.RatingId;
import serverapplication.exceptions.DocumentNotFoundException;
import serverapplication.exceptions.GenericServerErrorException;
import serverapplication.exceptions.RatingNotFoundException;
import serverapplication.exceptions.ServerConnectionErrorException;
import serverapplication.exceptions.UserNotFoundException;

/**
 *
 * @author Gaizka Andrés
 */
@Local
public interface EJBDocumentRatingLocal {
    
    //----------------------Document-----------------------------
    
    public void newDocument(Document document) throws UserNotFoundException, GenericServerErrorException;

    public void modifyDocument(Document document) throws GenericServerErrorException;
    
    public void deleteDocument(Document document) throws GenericServerErrorException;
    
    public List<Document> findAllDocuments() throws DocumentNotFoundException,GenericServerErrorException;
    
    public Document findDocumentById(Long id) throws DocumentNotFoundException,GenericServerErrorException;
    
    public List<Document> findDocumentNameByParameters(String name,Category category) throws DocumentNotFoundException, GenericServerErrorException;
    
    public Set<Rating> findRatingsOfDocument(Long id) throws DocumentNotFoundException, GenericServerErrorException;
    
    public List<Document> findDocumentNameByName(String name) throws DocumentNotFoundException, GenericServerErrorException;
    
 
    //----------------------Rating-----------------------------
    
    public void newDocumentRating(Rating Rating);
    
    public List<Rating> findAllRatings() throws RatingNotFoundException,ServerConnectionErrorException;
    
    public Rating findRatingById(RatingId id) throws RatingNotFoundException,ServerConnectionErrorException;
    
    public void updateRating(Rating Rating) throws RatingNotFoundException,ServerConnectionErrorException;
    
    public void deleteRating(Rating rating) throws RatingNotFoundException,ServerConnectionErrorException; 
    
    public List<Rating> DocumentsRating(Long id);

}
