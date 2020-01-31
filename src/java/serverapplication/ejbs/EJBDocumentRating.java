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
import serverapplication.entities.Category;
import serverapplication.entities.Document;
import serverapplication.entities.Rating;
import serverapplication.entities.RatingId;
import serverapplication.exceptions.DocumentNotFoundException;
import serverapplication.exceptions.GenericServerErrorException;
import serverapplication.exceptions.RatingNotFoundException;
import serverapplication.exceptions.UserNotFoundException;
import serverapplication.interfaces.EJBDocumentRatingLocal;

/**
 * EJB class for managing Document and Rating operations.
 *
 * @author Gaizka Andrés
 */
@Stateless
public class EJBDocumentRating implements EJBDocumentRatingLocal {

    private static final Logger LOGGER = Logger.getLogger("serverapplication.ejbs.EJBDocumentRating");
    /**
     * Entity manager object.
     */
    @PersistenceContext(unitName = "ServerApplication-Reto2PU")
    private EntityManager em;

    /**
     * Setter to the Entity Manager
     *
     * @param em a Entity Manager
     */
    public void setEm(EntityManager em) {
        this.em = em;
    }
    /**
     * A new UserEJB
     */
    private EJBUser userejb = new EJBUser();

    //----------------------------Document------------------------------------\\
    /**
     * Method to create a new document
     *
     * @param document the object will be created.
     * @throws UserNotFoundException If user's not found.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public void newDocument(Document document) throws UserNotFoundException, GenericServerErrorException {
        try {
            userejb.setEM(em);
            document.setUser(userejb.findUserById(Long.valueOf(document.getRatingCount())));
            document.setRatingCount(0);
            em.persist(document);
        } catch (UserNotFoundException ex) {
            LOGGER.warning("EJBDocumentRating: " + ex.getMessage());
            throw new UserNotFoundException(ex.getMessage());
        } catch (GenericServerErrorException ex) {
            LOGGER.warning("EJBDocumentRating: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warning("EJBDocumentRating: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }

    }

    /**
     * Method to modify a specific document
     *
     * @param document the document will be modificated.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public void modifyDocument(Document document) throws GenericServerErrorException {
        try {
            if (!em.contains(document)) {
                em.merge(document);
                em.flush();
            }
        } catch (Exception ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }

    }

    /**
     * Method to remove a specific document
     *
     * @param document the object will be removed.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public void deleteDocument(Document document) throws GenericServerErrorException {
        try {
            Query q = em.createQuery("DELETE FROM Document d WHERE d.id = :id");
            q.setParameter("id", document.getId());
            q.executeUpdate();
        } catch (Exception ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
    }

    /**
     * Method to search a specific document by his id.
     *
     * @param id the id of the document
     * @return The document with the specified id
     * @throws DocumentNotFoundException If the document is not found.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public Document findDocumentById(Long id) throws DocumentNotFoundException, GenericServerErrorException {
        Document document = null;
        try {
            document = em.find(Document.class, id);
            if (document == null) {
                throw new DocumentNotFoundException();
            }
        } catch (DocumentNotFoundException ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new DocumentNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return document;
    }

    /**
     * Method who give all the documents
     *
     * @return All the documents list
     * @throws DocumentNotFoundException If the document is not found.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public List<Document> findAllDocuments() throws DocumentNotFoundException, GenericServerErrorException {
        List<Document> allDocs = null;
        try {
            allDocs = em.createNamedQuery("findAllDocuments").getResultList();
            if (allDocs == null) {
                LOGGER.severe("EJBDocumentandRating: Document Not Found");
                throw new DocumentNotFoundException();
            }
        } catch (DocumentNotFoundException ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new DocumentNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return allDocs;
    }

    /**
     * Method who search by document name
     *
     * @param name document name
     * @return list of document with that name
     * @throws DocumentNotFoundException If the document is not found.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public List<Document> findDocumentNameByName(String name) throws DocumentNotFoundException, GenericServerErrorException {
        List<Document> documents = null;
        try {
            documents = em.createNamedQuery("findDocumentNamebyName")
                    .setParameter("name", name).getResultList();
        } catch (NoResultException ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new DocumentNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return documents;
    }

    /**
     * Method who search by various parameters
     *
     * @param name the name of the document
     * @param category the category of the document
     * @return A list of documents
     * @throws DocumentNotFoundException If the document is not found.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public List<Document> findDocumentNameByParameters(String name, Category category)
            throws DocumentNotFoundException, GenericServerErrorException {
        List<Document> docNames = null;
        try {
            docNames = em.createNamedQuery("findDocumentNameByParameters").
                    setParameter("name", "%" + name + "%").
                    setParameter("category", category).getResultList();
        } catch (NoResultException ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new DocumentNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return docNames;
    }

    /**
     * Method to search all the ratings of a specific document
     *
     * @param id
     * @return A ratings list of the specified document
     * @throws DocumentNotFoundException If the document is not found.
     * @throws GenericServerErrorException If there's an error in the server.
     */
    @Override
    public Set<Rating> findRatingsOfDocument(Long id)
            throws DocumentNotFoundException, GenericServerErrorException {
        Document document = null;
        try {
            document = (Document) em.createNamedQuery("findRatingsOfDocument")
                    .setParameter("id", id).getSingleResult();
        } catch (NoResultException ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new DocumentNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.severe("EJBDocumentRating: " + ex.getMessage());
            throw new GenericServerErrorException(ex.getMessage());
        }
        return new HashSet<>(document.getRatings());
    }
    //--------------------------------Rating----------------------------------\\

    /**
     * Method to create a new Rating
     *
     * @param Rating the rating will be created
     * @throws RatingNotFoundException exception if are no rating
     */
    @Override
    public void newDocumentRating(Rating rating) {
        try {
            userejb.setEM(em);
            rating.setDocument(findDocumentById(rating.getId().getIdDocument()));
            rating.setUser(userejb.findUserById(rating.getId().getIdUser()));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(EJBDocumentRating.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserNotFoundException ex) {
            Logger.getLogger(EJBDocumentRating.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GenericServerErrorException ex) {
            Logger.getLogger(EJBDocumentRating.class.getName()).log(Level.SEVERE, null, ex);
        }
        em.persist(rating);
    }

    /**
     * Method to search a Rating by his ID
     *
     * @param id id to search by
     * @return a rating with the send id
     * @throws RatingNotFoundException if are no rating with that id
     */
    @Override
    public Rating findRatingById(RatingId id) throws RatingNotFoundException {
        Rating rating = null;
        try {
            rating = em.find(Rating.class, id);
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }
        return rating;
    }

    /**
     * Method to modify a specific rating
     *
     * @param Rating modified rating
     * @throws RatingNotFoundException exception if are no rating
     */
    @Override
    public void updateRating(Rating Rating) throws RatingNotFoundException {
        try {
            em.merge(Rating);
            em.flush();
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    /**
     * Method to search all the ratings
     *
     * @return all ratings list
     * @throws RatingNotFoundException exception if are no rating
     */
    @Override
    public List<Rating> findAllRatings() throws RatingNotFoundException {
        List<Rating> ratings = null;
        try {
            ratings = em.createNamedQuery("findAllRating").getResultList();
            if (ratings == null) {
                throw new RatingNotFoundException();
            }
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }

        return ratings;
    }

    /**
     * Method to remove a specific rating
     *
     * @param rating a rating to delete
     * @throws RatingNotFoundException exception if are no rating
     */
    @Override
    public void deleteRating(Rating rating) throws RatingNotFoundException {
        try {
            Query q = em.createQuery("DELETE FROM Document d WHERE d.id = :id");
            q.setParameter("id", rating.getId());
            q.executeUpdate();
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }

    }

    /**
     * Method who return all the ratings of a document
     *
     * @param id Id of the document from which we will get the ratings
     * @return List of ratings of that document
     */
    @Override
    public List<Rating> DocumentsRating(Long id) {
        List<Rating> ratings;
        Query q = em.createQuery("SELECT r FROM Rating r WHERE r.document.id = :id");
        q.setParameter("id", id);
        ratings = (List<Rating>) q.getResultList();
        return ratings;
    }

}
