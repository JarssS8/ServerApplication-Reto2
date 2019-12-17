/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.HashSet;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import serverapplication.entities.Category;
import serverapplication.entities.Document;
import serverapplication.exceptions.CategoryNameAlreadyExistsException;
import serverapplication.exceptions.CategoryNotFoundException;
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
    public void createCategory(Category category) throws CategoryNameAlreadyExistsException,Exception {
        em.persist(category);
    }

    @Override
    public void modifyCategory(Category category) throws CategoryNameAlreadyExistsException, CategoryNotFoundException,Exception {
        em.merge(category);
    }

    @Override
    public void deleteCategory(Long id) throws CategoryNotFoundException,Exception {
        em.remove(em.merge(id));
    }

    @Override
    public Set<Category> findCategoryByName(String name) throws Exception{
        return new HashSet<>( em.createNamedQuery("findCategoryByName").setParameter("name", "%" + name + "%").getResultList());
    }
    
    @Override
    public Document findDocumentsByCategory(String catName, String docName){
        Category category=null;
        category=(Category) em.createNamedQuery("findDocumentsByCategory").setParameter("name", catName).getSingleResult();
        for(Document aux : category.getDocuments()){
            if(aux.getName().equalsIgnoreCase(docName)){               
                return aux;
            }
        }
        return null;
    }
    
    @Override
    public Set<Category> findAllCategories() throws Exception{
        return new HashSet<>( em.createNamedQuery("findAllCategories").getResultList());
    }

}
