/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.util.Set;
import javax.ejb.Local;
import serverapplication.entities.Category;
import serverapplication.entities.Document;
import serverapplication.exceptions.CategoryNameAlreadyExistsException;
import serverapplication.exceptions.CategoryNotFoundException;

/**
 *
 * @author Adrian
 */
@Local
public interface CategoryEJBLocal {
    
    public void createCategory(Category category) throws CategoryNameAlreadyExistsException,Exception;
    public void modifyCategory(Category category) throws CategoryNameAlreadyExistsException,CategoryNotFoundException,Exception;
    public void deleteCategory(Long id) throws CategoryNotFoundException,Exception;
    public Category findCategoryByName(String name)throws Exception;
    public Document findDocumentsByCategory(String catName, String docName)throws Exception;
    public Set<Category> findAllCategories()throws Exception;

}
