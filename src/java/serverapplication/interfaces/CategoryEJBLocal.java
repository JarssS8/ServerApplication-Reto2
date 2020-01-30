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
import serverapplication.exceptions.CategoryNameAlreadyExistsException;
import serverapplication.exceptions.CategoryNameNotFoundException;
import serverapplication.exceptions.CategoryNotFoundException;
import serverapplication.exceptions.DocumentNotFoundException;
import serverapplication.exceptions.GenericServerErrorException;

/**
 *
 * @author Adrian
 */
@Local
public interface CategoryEJBLocal {
        /**
     * Finds a {@link Category} using a parameter id
     * 
     * @param id Parameter used for search in database a category with the same
     * id
     * @return A {@link Category} object
     * @throws CategoryNotFoundException If don't found the id of the
     * {@link Category} throw this exception
     * @throws Exception Throws this exception if something unusual happens
     */
    public Category findCategoryById(Long id)throws CategoryNotFoundException,GenericServerErrorException;
    
      /**
     * Finds any {@link Category} that his name match with the parameter name
     * 
     * @param name Parameter used for search in database a category name that
     * contains this name parameter
     * @return Set of {@link Category} with all the categories that match with
     * the parameter
     * @throws Exception Throws this exception if something unusual happens
     */
    public Category findCategoryByName(String name)throws CategoryNameNotFoundException, GenericServerErrorException;
    
    /**
     * Find a {@link Document} into a {@link Category} that the name of the
     * category match with the parameter catName and the document name match
     * with the parameter docName
     * 
     * @param catName String with the complete name of the category
     * @param docName String with the complete name of the document
     * @return A {@link Document} that match with the parameter docName and is
     * into {@link Category} object that match with catName
     * @throws serverapplication.exceptions.CategoryNotFoundException Throw this
     * exception when can't find the id of the {@link Category}
     * @throws serverapplication.exceptions.DocumentNotFoundException Throw this
     * exception when can't find the id of the {@link Document}
     * @throws Exception Throws this exception if something unusual happens
     */
    public Document findDocumentsByCategory(String catName, String docName)throws CategoryNotFoundException,DocumentNotFoundException,GenericServerErrorException;
    
        /**
     * Find all the {@link Category} that are in the data base
     * 
     * @return A Set with all the {@link Category} from the data base
     * @throws Exception Throws this exception if something unusual happens
     */
    public List<Category> findAllCategories()throws GenericServerErrorException;
    
        /**
     * Create a new {@link Category} with the {@link Category} object, get it as
     * parameter category
     * 
     * @param category A {@link Category} with the data of the new
     * {@link Category}
     * @throws CategoryNameAlreadyExistsException Throw this exception with the
     * name of the category exists on the data base
     * @throws Exception Throws this exception if something unusual happens
     */
    public void createCategory(Category category) throws CategoryNameAlreadyExistsException,GenericServerErrorException;
    
     /**
     * Modify a {@link Category} changing only his name
     *
     * @param category A {@link Category} with the new name of the category
     * @throws CategoryNameAlreadyExistsException Throw this exception with the
     * new name of the category exists on the data base
     * @throws CategoryNotFoundException Throw this exception when can't find
     * the id of the {@link Category} that we want modify
     * @throws Exception Throws this exception if something unusual happens
     */
    public void modifyCategory(Category category) throws CategoryNameAlreadyExistsException,CategoryNotFoundException,GenericServerErrorException;
    
      /**
     * Delete the {@link Category} with the same id from the data base
     *
     * @param id Parameter used for search in database a category with the same
     * id
     * @throws CategoryNotFoundException Throw this exception when can't find
     * the id of the {@link Category} that we want delete.
     * @throws Exception Throws this exception if something unusual happens
     */
    public void deleteCategory(Category category) throws CategoryNotFoundException,GenericServerErrorException;

}
