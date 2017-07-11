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
import entidades.Apoderado;
import entidades.Alumno;
import entidades.Constantes;
import entidades.Parentesco;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author CesarLopez
 */
public class ParentescoJpaController implements Serializable {

    public ParentescoJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Parentesco parentesco) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Apoderado idApoderado = parentesco.getIdApoderado();
            if (idApoderado != null) {
                idApoderado = em.getReference(idApoderado.getClass(), idApoderado.getId());
                parentesco.setIdApoderado(idApoderado);
            }
            Alumno idAlumno = parentesco.getIdAlumno();
            if (idAlumno != null) {
                idAlumno = em.getReference(idAlumno.getClass(), idAlumno.getId());
                parentesco.setIdAlumno(idAlumno);
            }
            em.persist(parentesco);
            if (idApoderado != null) {
                idApoderado.getParentescoList().add(parentesco);
                idApoderado = em.merge(idApoderado);
            }
            if (idAlumno != null) {
                idAlumno.getParentescoList().add(parentesco);
                idAlumno = em.merge(idAlumno);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Parentesco parentesco) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Parentesco persistentParentesco = em.find(Parentesco.class, parentesco.getId());
            Apoderado idApoderadoOld = persistentParentesco.getIdApoderado();
            Apoderado idApoderadoNew = parentesco.getIdApoderado();
            Alumno idAlumnoOld = persistentParentesco.getIdAlumno();
            Alumno idAlumnoNew = parentesco.getIdAlumno();
            if (idApoderadoNew != null) {
                idApoderadoNew = em.getReference(idApoderadoNew.getClass(), idApoderadoNew.getId());
                parentesco.setIdApoderado(idApoderadoNew);
            }
            if (idAlumnoNew != null) {
                idAlumnoNew = em.getReference(idAlumnoNew.getClass(), idAlumnoNew.getId());
                parentesco.setIdAlumno(idAlumnoNew);
            }
            parentesco = em.merge(parentesco);
            if (idApoderadoOld != null && !idApoderadoOld.equals(idApoderadoNew)) {
                idApoderadoOld.getParentescoList().remove(parentesco);
                idApoderadoOld = em.merge(idApoderadoOld);
            }
            if (idApoderadoNew != null && !idApoderadoNew.equals(idApoderadoOld)) {
                idApoderadoNew.getParentescoList().add(parentesco);
                idApoderadoNew = em.merge(idApoderadoNew);
            }
            if (idAlumnoOld != null && !idAlumnoOld.equals(idAlumnoNew)) {
                idAlumnoOld.getParentescoList().remove(parentesco);
                idAlumnoOld = em.merge(idAlumnoOld);
            }
            if (idAlumnoNew != null && !idAlumnoNew.equals(idAlumnoOld)) {
                idAlumnoNew.getParentescoList().add(parentesco);
                idAlumnoNew = em.merge(idAlumnoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = parentesco.getId();
                if (findParentesco(id) == null) {
                    throw new NonexistentEntityException("The parentesco with id " + id + " no longer exists.");
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
            Parentesco parentesco;
            try {
                parentesco = em.getReference(Parentesco.class, id);
                parentesco.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parentesco with id " + id + " no longer exists.", enfe);
            }
            Apoderado idApoderado = parentesco.getIdApoderado();
            if (idApoderado != null) {
                idApoderado.getParentescoList().remove(parentesco);
                idApoderado = em.merge(idApoderado);
            }
            Alumno idAlumno = parentesco.getIdAlumno();
            if (idAlumno != null) {
                idAlumno.getParentescoList().remove(parentesco);
                idAlumno = em.merge(idAlumno);
            }
            em.remove(parentesco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Parentesco> findParentescoEntities() {
        return findParentescoEntities(true, -1, -1);
    }

    public List<Parentesco> findParentescoEntities(int maxResults, int firstResult) {
        return findParentescoEntities(false, maxResults, firstResult);
    }

    private List<Parentesco> findParentescoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Parentesco.class));
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

    public Parentesco findParentesco(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Parentesco.class, id);
        } finally {
            em.close();
        }
    }

    public int getParentescoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Parentesco> rt = cq.from(Parentesco.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
