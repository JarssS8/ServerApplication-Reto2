/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.interfaces;

import java.util.Set;
import javax.ejb.Local;
import serverapplication.entities.Category;
import serverapplication.exceptions.CategoryNameAlreadyExists;
import serverapplication.exceptions.CategoryNotFound;

/**
 *
 * @author Adrian
 */
@Local
public interface CategoryEJBLocal {
    public void createCategory(Category category) throws CategoryNameAlreadyExists;
    public void modifyCategory(Category category) throws CategoryNameAlreadyExists,CategoryNotFound;
    public void deleteCategory(Category category) throws CategoryNotFound;
    public Set<Category> findCategoryByName(String catName);
    public Set<Category> findAllCategories();

}
