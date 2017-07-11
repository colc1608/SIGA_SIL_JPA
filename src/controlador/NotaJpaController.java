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
import entidades.Alumno;
import entidades.Periodo;
import entidades.Tipoevaluacion;
import entidades.Clase;
import entidades.Constantes;
import entidades.Nota;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author CesarLopez
 */
public class NotaJpaController implements Serializable {

    public NotaJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nota nota) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno idAlumno = nota.getIdAlumno();
            if (idAlumno != null) {
                idAlumno = em.getReference(idAlumno.getClass(), idAlumno.getId());
                nota.setIdAlumno(idAlumno);
            }
            Periodo idPeriodo = nota.getIdPeriodo();
            if (idPeriodo != null) {
                idPeriodo = em.getReference(idPeriodo.getClass(), idPeriodo.getId());
                nota.setIdPeriodo(idPeriodo);
            }
            Tipoevaluacion idTipoEvaluacion = nota.getIdTipoEvaluacion();
            if (idTipoEvaluacion != null) {
                idTipoEvaluacion = em.getReference(idTipoEvaluacion.getClass(), idTipoEvaluacion.getId());
                nota.setIdTipoEvaluacion(idTipoEvaluacion);
            }
            Clase idClase = nota.getIdClase();
            if (idClase != null) {
                idClase = em.getReference(idClase.getClass(), idClase.getId());
                nota.setIdClase(idClase);
            }
            em.persist(nota);
            if (idAlumno != null) {
                idAlumno.getNotaList().add(nota);
                idAlumno = em.merge(idAlumno);
            }
            if (idPeriodo != null) {
                idPeriodo.getNotaList().add(nota);
                idPeriodo = em.merge(idPeriodo);
            }
            if (idTipoEvaluacion != null) {
                idTipoEvaluacion.getNotaList().add(nota);
                idTipoEvaluacion = em.merge(idTipoEvaluacion);
            }
            if (idClase != null) {
                idClase.getNotaList().add(nota);
                idClase = em.merge(idClase);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nota nota) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nota persistentNota = em.find(Nota.class, nota.getId());
            Alumno idAlumnoOld = persistentNota.getIdAlumno();
            Alumno idAlumnoNew = nota.getIdAlumno();
            Periodo idPeriodoOld = persistentNota.getIdPeriodo();
            Periodo idPeriodoNew = nota.getIdPeriodo();
            Tipoevaluacion idTipoEvaluacionOld = persistentNota.getIdTipoEvaluacion();
            Tipoevaluacion idTipoEvaluacionNew = nota.getIdTipoEvaluacion();
            Clase idClaseOld = persistentNota.getIdClase();
            Clase idClaseNew = nota.getIdClase();
            if (idAlumnoNew != null) {
                idAlumnoNew = em.getReference(idAlumnoNew.getClass(), idAlumnoNew.getId());
                nota.setIdAlumno(idAlumnoNew);
            }
            if (idPeriodoNew != null) {
                idPeriodoNew = em.getReference(idPeriodoNew.getClass(), idPeriodoNew.getId());
                nota.setIdPeriodo(idPeriodoNew);
            }
            if (idTipoEvaluacionNew != null) {
                idTipoEvaluacionNew = em.getReference(idTipoEvaluacionNew.getClass(), idTipoEvaluacionNew.getId());
                nota.setIdTipoEvaluacion(idTipoEvaluacionNew);
            }
            if (idClaseNew != null) {
                idClaseNew = em.getReference(idClaseNew.getClass(), idClaseNew.getId());
                nota.setIdClase(idClaseNew);
            }
            nota = em.merge(nota);
            if (idAlumnoOld != null && !idAlumnoOld.equals(idAlumnoNew)) {
                idAlumnoOld.getNotaList().remove(nota);
                idAlumnoOld = em.merge(idAlumnoOld);
            }
            if (idAlumnoNew != null && !idAlumnoNew.equals(idAlumnoOld)) {
                idAlumnoNew.getNotaList().add(nota);
                idAlumnoNew = em.merge(idAlumnoNew);
            }
            if (idPeriodoOld != null && !idPeriodoOld.equals(idPeriodoNew)) {
                idPeriodoOld.getNotaList().remove(nota);
                idPeriodoOld = em.merge(idPeriodoOld);
            }
            if (idPeriodoNew != null && !idPeriodoNew.equals(idPeriodoOld)) {
                idPeriodoNew.getNotaList().add(nota);
                idPeriodoNew = em.merge(idPeriodoNew);
            }
            if (idTipoEvaluacionOld != null && !idTipoEvaluacionOld.equals(idTipoEvaluacionNew)) {
                idTipoEvaluacionOld.getNotaList().remove(nota);
                idTipoEvaluacionOld = em.merge(idTipoEvaluacionOld);
            }
            if (idTipoEvaluacionNew != null && !idTipoEvaluacionNew.equals(idTipoEvaluacionOld)) {
                idTipoEvaluacionNew.getNotaList().add(nota);
                idTipoEvaluacionNew = em.merge(idTipoEvaluacionNew);
            }
            if (idClaseOld != null && !idClaseOld.equals(idClaseNew)) {
                idClaseOld.getNotaList().remove(nota);
                idClaseOld = em.merge(idClaseOld);
            }
            if (idClaseNew != null && !idClaseNew.equals(idClaseOld)) {
                idClaseNew.getNotaList().add(nota);
                idClaseNew = em.merge(idClaseNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nota.getId();
                if (findNota(id) == null) {
                    throw new NonexistentEntityException("The nota with id " + id + " no longer exists.");
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
            Nota nota;
            try {
                nota = em.getReference(Nota.class, id);
                nota.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nota with id " + id + " no longer exists.", enfe);
            }
            Alumno idAlumno = nota.getIdAlumno();
            if (idAlumno != null) {
                idAlumno.getNotaList().remove(nota);
                idAlumno = em.merge(idAlumno);
            }
            Periodo idPeriodo = nota.getIdPeriodo();
            if (idPeriodo != null) {
                idPeriodo.getNotaList().remove(nota);
                idPeriodo = em.merge(idPeriodo);
            }
            Tipoevaluacion idTipoEvaluacion = nota.getIdTipoEvaluacion();
            if (idTipoEvaluacion != null) {
                idTipoEvaluacion.getNotaList().remove(nota);
                idTipoEvaluacion = em.merge(idTipoEvaluacion);
            }
            Clase idClase = nota.getIdClase();
            if (idClase != null) {
                idClase.getNotaList().remove(nota);
                idClase = em.merge(idClase);
            }
            em.remove(nota);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Nota> findNotaEntities() {
        return findNotaEntities(true, -1, -1);
    }

    public List<Nota> findNotaEntities(int maxResults, int firstResult) {
        return findNotaEntities(false, maxResults, firstResult);
    }

    private List<Nota> findNotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nota.class));
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

    public Nota findNota(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nota.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nota> rt = cq.from(Nota.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
