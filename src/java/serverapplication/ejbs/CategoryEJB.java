/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import serverapplication.entities.Category;
import serverapplication.exceptions.CategoryNameAlreadyExists;
import serverapplication.exceptions.CategoryNotFound;
import serverapplication.interfaces.CategoryEJBLocal;

/**
 *
 * @author Adrian
 */
@Stateless
public class CategoryEJB implements CategoryEJBLocal {

    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    private EntityManager em;

    @Override
    public void createCategory(Category category) throws CategoryNameAlreadyExists {
        em.persist(category);
    }

    @Override
    public void modifyCategory(Category category) throws CategoryNameAlreadyExists, CategoryNotFound {
        em.merge(category);
    }

    @Override
    public void deleteCategory(Category category) throws CategoryNotFound {
        em.remove(em.merge(category));
    }

    @Override
    public Set<Category> findCategoryByName(String catName) {
        return (Set<Category>) em.createNamedQuery("findCategoryByName").getResultList();
    }

    @Override
    public Set<Category> findAllCategories() {
        return (Set<Category>) em.createNamedQuery("findAllCategories").getResultList();
    }

}
