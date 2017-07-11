/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Clase;
import entidades.Constantes;
import entidades.Detallematricula;
import entidades.Matricula;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author CesarLopez
 */
public class DetallematriculaJpaController implements Serializable {

    public DetallematriculaJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallematricula detallematricula) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clase idClase = detallematricula.getIdClase();
            if (idClase != null) {
                idClase = em.getReference(idClase.getClass(), idClase.getId());
                detallematricula.setIdClase(idClase);
            }
            Matricula idMatricula = detallematricula.getIdMatricula();
            if (idMatricula != null) {
                idMatricula = em.getReference(idMatricula.getClass(), idMatricula.getId());
                detallematricula.setIdMatricula(idMatricula);
            }
            em.persist(detallematricula);
            if (idClase != null) {
                idClase.getDetallematriculaList().add(detallematricula);
                idClase = em.merge(idClase);
            }
            if (idMatricula != null) {
                idMatricula.getDetallematriculaList().add(detallematricula);
                idMatricula = em.merge(idMatricula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallematricula detallematricula) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detallematricula persistentDetallematricula = em.find(Detallematricula.class, detallematricula.getId());
            Clase idClaseOld = persistentDetallematricula.getIdClase();
            Clase idClaseNew = detallematricula.getIdClase();
            Matricula idMatriculaOld = persistentDetallematricula.getIdMatricula();
            Matricula idMatriculaNew = detallematricula.getIdMatricula();
            if (idClaseNew != null) {
                idClaseNew = em.getReference(idClaseNew.getClass(), idClaseNew.getId());
                detallematricula.setIdClase(idClaseNew);
            }
            if (idMatriculaNew != null) {
                idMatriculaNew = em.getReference(idMatriculaNew.getClass(), idMatriculaNew.getId());
                detallematricula.setIdMatricula(idMatriculaNew);
            }
            detallematricula = em.merge(detallematricula);
            if (idClaseOld != null && !idClaseOld.equals(idClaseNew)) {
                idClaseOld.getDetallematriculaList().remove(detallematricula);
                idClaseOld = em.merge(idClaseOld);
            }
            if (idClaseNew != null && !idClaseNew.equals(idClaseOld)) {
                idClaseNew.getDetallematriculaList().add(detallematricula);
                idClaseNew = em.merge(idClaseNew);
            }
            if (idMatriculaOld != null && !idMatriculaOld.equals(idMatriculaNew)) {
                idMatriculaOld.getDetallematriculaList().remove(detallematricula);
                idMatriculaOld = em.merge(idMatriculaOld);
            }
            if (idMatriculaNew != null && !idMatriculaNew.equals(idMatriculaOld)) {
                idMatriculaNew.getDetallematriculaList().add(detallematricula);
                idMatriculaNew = em.merge(idMatriculaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detallematricula.getId();
                if (findDetallematricula(id) == null) {
                    throw new NonexistentEntityException("The detallematricula with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detallematricula detallematricula;
            try {
                detallematricula = em.getReference(Detallematricula.class, id);
                detallematricula.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallematricula with id " + id + " no longer exists.", enfe);
            }
            Clase idClase = detallematricula.getIdClase();
            if (idClase != null) {
                idClase.getDetallematriculaList().remove(detallematricula);
                idClase = em.merge(idClase);
            }
            Matricula idMatricula = detallematricula.getIdMatricula();
            if (idMatricula != null) {
                idMatricula.getDetallematriculaList().remove(detallematricula);
                idMatricula = em.merge(idMatricula);
            }
            em.remove(detallematricula);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detallematricula> findDetallematriculaEntities() {
        return findDetallematriculaEntities(true, -1, -1);
    }

    public List<Detallematricula> findDetallematriculaEntities(int maxResults, int firstResult) {
        return findDetallematriculaEntities(false, maxResults, firstResult);
    }

    private List<Detallematricula> findDetallematriculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallematricula.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Detallematricula findDetallematricula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallematricula.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallematriculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallematricula> rt = cq.from(Detallematricula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
