/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import serverapplication.entities.Category;
import serverapplication.entities.Document;
import serverapplication.exceptions.CategoryNameAlreadyExistsException;
import serverapplication.exceptions.CategoryNotFoundException;
import serverapplication.exceptions.DocumenttNotFoundException;
import serverapplication.interfaces.CategoryEJBLocal;

/**
 *
 * @author Adrian
 */
@Stateless
public class CategoryEJB implements CategoryEJBLocal {

    /**
     * Logger for the class.
     */
    private static final Logger LOGGER = Logger.getLogger("serverapplication.ejbs.CategoryEJB");

    /**
     * Entity manager object.
     */
    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    private EntityManager em; 

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
    @Override
    public Category findCategoryById(Long id) throws CategoryNotFoundException, Exception {
        Category category = null;
        category = em.find(Category.class, id);
        if (category == null) {
            throw new CategoryNotFoundException();
        }
        return em.find(Category.class, id);

    }

    /**
     * Finds any {@link Category} that his name match with the parameter name
     *
     * @param name Parameter used for search in database a category name that
     * contains this name parameter
     * @return Set of {@link Category} with all the categories that match with
     * the parameter
     * @throws Exception Throws this exception if something unusual happens
     */
    @Override
    public Set<Category> findCategoryByName(String name) throws Exception {
        Set<Category> categories = null;
        categories = new HashSet<>(em.createNamedQuery("findCategoryByName").setParameter("name", "%" + name + "%").getResultList());
        return categories;
    }

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
     * @throws serverapplication.exceptions.DocumenttNotFoundException Throw this
     * exception when can't find the id of the {@link Document}
     * @throws Exception Throws this exception if something unusual happens
     */
    @Override
    public Document findDocumentsByCategory(String catName, String docName) throws CategoryNotFoundException, DocumenttNotFoundException, Exception {
        Category category = null;
        category = (Category) em.createNamedQuery("findDocumentsByCategory").setParameter("name", catName).getSingleResult();
        if (category != null) {
            for (Document aux : category.getDocuments()) {
                if (aux.getName().equalsIgnoreCase(docName)) {
                    return aux;
                }
            }
            throw new DocumenttNotFoundException();
        } else {
            throw new CategoryNotFoundException();
        }
    }

    /**
     * Find all the {@link Category} that are in the data base
     * 
     * @return A Set with all the {@link Category} from the data base
     * @throws Exception Throws this exception if something unusual happens
     */
    @Override
    public Set<Category> findAllCategories() throws Exception {
        return new HashSet<>(em.createNamedQuery("findAllCategories").getResultList());
    }

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
    @Override
    public void createCategory(Category category) throws CategoryNameAlreadyExistsException, Exception {
        Set<Category> auxCategory = null;
        auxCategory = findCategoryByName(category.getName());
        if (auxCategory != null) {
            em.persist(category);
        } else {
            throw new CategoryNameAlreadyExistsException();
        }
    }

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
    @Override
    public void modifyCategory(Category category) throws CategoryNameAlreadyExistsException, CategoryNotFoundException, Exception {
        Category checkCategoryId = null;
        Set<Category> checkCategoryName = null;
        checkCategoryId = findCategoryById(category.getId());
        if (checkCategoryId != null) {
            checkCategoryName = findCategoryByName(category.getName());
            if (checkCategoryName != null) {
                em.merge(category);
                em.flush();
            } else {
                throw new CategoryNameAlreadyExistsException();
            }
        } else {
            throw new CategoryNotFoundException();
        }

    }

    /**
     * Delete the {@link Category} with the same id from the data base
     *
     * @param id Parameter used for search in database a category with the same
     * id
     * @throws CategoryNotFoundException Throw this exception when can't find
     * the id of the {@link Category} that we want delete.
     * @throws Exception Throws this exception if something unusual happens
     */
    @Override
    public void deleteCategory(Long id) throws CategoryNotFoundException, Exception {
        Category checkCategoryId = null;
        checkCategoryId = findCategoryById(id);
        if (checkCategoryId != null) {
            Query q = em.createQuery("DELETE FROM Category c WHERE c.id = :id");
            q.setParameter("id", id);
            q.executeUpdate();
        } else {
            throw new CategoryNotFoundException();
        }
    }

}
