/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.InternalServerErrorException;
import serverapplication.entities.Category;
import serverapplication.entities.Document;
import serverapplication.exceptions.CategoryNameAlreadyExistsException;
import serverapplication.exceptions.CategoryNameNotFoundException;
import serverapplication.exceptions.CategoryNotFoundException;
import serverapplication.exceptions.DocumentNotFoundException;
import serverapplication.exceptions.GenericServerErrorException;
import serverapplication.interfaces.CategoryEJBLocal;

/**
 * EJB class for managing Category operations.
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
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public Category findCategoryById(Long id) throws CategoryNotFoundException, GenericServerErrorException {
        Category category = null;
        try {
            category = em.find(Category.class, id);
            if (category == null) {
                throw new CategoryNotFoundException();
            }
        } catch (CategoryNotFoundException ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new CategoryNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
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
     * @throws CategoryNameNotFoundException If the category is not found.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public Category findCategoryByName(String name) throws CategoryNameNotFoundException, GenericServerErrorException {
        Category category = null;
        try {
            category = (Category) em.createNamedQuery("findCategoryByName").setParameter("name", name).getSingleResult();
        } catch (NoResultException ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new CategoryNameNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return category;
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
     * @throws serverapplication.exceptions.DocumentNotFoundException Throw this
     * exception when can't find the id of the {@link Document}
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public Document findDocumentsByCategory(String catName, String docName) throws CategoryNotFoundException, DocumentNotFoundException, GenericServerErrorException {
        Category category = null;
        try {
            category = (Category) em.createNamedQuery("findDocumentsByCategory").setParameter("name", catName).getSingleResult();
            if (category != null) {
                for (Document aux : category.getDocuments()) {
                    if (aux.getName().equalsIgnoreCase(docName)) {
                        return aux;
                    }
                }
                throw new DocumentNotFoundException();
            } else {
                throw new CategoryNotFoundException();
            }
        } catch (DocumentNotFoundException ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new DocumentNotFoundException(ex.getMessage());
        } catch (CategoryNotFoundException ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new CategoryNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }

    }

    /**
     * Find all the {@link Category} that are in the data base
     *
     * @return A Set with all the {@link Category} from the data base
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public List<Category> findAllCategories() throws GenericServerErrorException {
        List<Category> categories = null;
        try {
            categories = (List<Category>) em.createNamedQuery("findAllCategories").getResultList();
        } catch (Exception ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return categories;
    }

    /**
     * Create a new {@link Category} with the {@link Category} object, get it as
     * parameter category
     *
     * @param category A {@link Category} with the data of the new
     * {@link Category}
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public void createCategory(Category category) throws GenericServerErrorException {
        try {
            Category auxCategory = null;

            auxCategory = findCategoryByName(category.getName());

        } catch (CategoryNameNotFoundException ex) {
            em.persist(category);
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
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
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public void modifyCategory(Category category) throws CategoryNameAlreadyExistsException, CategoryNotFoundException, GenericServerErrorException {
        try {
            Category checkCategoryId = null;
            Category checkCategoryName = null;
            checkCategoryId = findCategoryById(category.getId());
            if (checkCategoryId != null) {
                try {
                    checkCategoryName = findCategoryByName(category.getName());
                    if (checkCategoryName == null) {
                        LOGGER.info("Category name available...");
                    } else {
                        throw new CategoryNameAlreadyExistsException();
                    }
                } catch (CategoryNameNotFoundException ex) {

                    if (!em.contains(category)) {
                        em.merge(category);
                        em.flush();
                    }

                }
            } else {
                throw new CategoryNotFoundException();
            }

        } catch (CategoryNameAlreadyExistsException ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new CategoryNameAlreadyExistsException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }

    }

    /**
     * Delete the {@link Category} with the same id from the data base
     *
     * @throws CategoryNotFoundException Throw this exception when can't find
     * the id of the {@link Category} that we want delete.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public void deleteCategory(Category category) throws CategoryNotFoundException, GenericServerErrorException {
        try {
            Query qDoc = em.createQuery("DELETE FROM Document d WHERE d.category = :cat");
            qDoc.setParameter("cat", findCategoryById(category.getId()));
            qDoc.executeUpdate();

            Query q = em.createQuery("DELETE FROM Category c WHERE c.id = :id");
            q.setParameter("id", category.getId());
            q.executeUpdate();
        } catch (CategoryNotFoundException ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new CategoryNotFoundException(ex.getMessage());
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("CategoryEJB: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }

    }

}
