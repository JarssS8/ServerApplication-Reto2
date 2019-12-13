/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import serverapplication.entities.Group;
import serverapplication.ejbs.EJBUser;
import serverapplication.entities.User;

/**
 *
 * @author Diego Urraca
 */
@Stateless
public class EJBGroups<T> {
    
    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    private EntityManager em;

    public void createGroup(Group group,String login) {
        em.persist(group);
    }
    
    public void modifyGroup(Group group, String login){
        
    }
    
    public void deleteGroup(Group group, String login){
        
    }
    
    public void joinGroup(String groupName,String pass, String login){
        
    }
    
    public void leaveGroup(String groupName,String login){
        
    }
    
    public Group findGroup(String groupName){
        
        return null;
    }
    
    public List<Group> finAllGroups(String login){
        User user=findUserByLogin(login);
        
        
        return groupsList;
    }
    
    
    
    
    
    
    
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
