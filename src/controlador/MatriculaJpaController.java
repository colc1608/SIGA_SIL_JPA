/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Grado;
import entidades.Alumno;
import entidades.Constantes;
import entidades.Detallematricula;
import entidades.Matricula;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author CesarLopez
 */
public class MatriculaJpaController implements Serializable {

    public MatriculaJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Matricula matricula) {
        if (matricula.getDetallematriculaList() == null) {
            matricula.setDetallematriculaList(new ArrayList<Detallematricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grado idGrado = matricula.getIdGrado();
            if (idGrado != null) {
                idGrado = em.getReference(idGrado.getClass(), idGrado.getId());
                matricula.setIdGrado(idGrado);
            }
            Alumno idAlumno = matricula.getIdAlumno();
            if (idAlumno != null) {
                idAlumno = em.getReference(idAlumno.getClass(), idAlumno.getId());
                matricula.setIdAlumno(idAlumno);
            }
            List<Detallematricula> attachedDetallematriculaList = new ArrayList<Detallematricula>();
            for (Detallematricula detallematriculaListDetallematriculaToAttach : matricula.getDetallematriculaList()) {
                detallematriculaListDetallematriculaToAttach = em.getReference(detallematriculaListDetallematriculaToAttach.getClass(), detallematriculaListDetallematriculaToAttach.getId());
                attachedDetallematriculaList.add(detallematriculaListDetallematriculaToAttach);
            }
            matricula.setDetallematriculaList(attachedDetallematriculaList);
            em.persist(matricula);
            if (idGrado != null) {
                idGrado.getMatriculaList().add(matricula);
                idGrado = em.merge(idGrado);
            }
            if (idAlumno != null) {
                idAlumno.getMatriculaList().add(matricula);
                idAlumno = em.merge(idAlumno);
            }
            for (Detallematricula detallematriculaListDetallematricula : matricula.getDetallematriculaList()) {
                Matricula oldIdMatriculaOfDetallematriculaListDetallematricula = detallematriculaListDetallematricula.getIdMatricula();
                detallematriculaListDetallematricula.setIdMatricula(matricula);
                detallematriculaListDetallematricula = em.merge(detallematriculaListDetallematricula);
                if (oldIdMatriculaOfDetallematriculaListDetallematricula != null) {
                    oldIdMatriculaOfDetallematriculaListDetallematricula.getDetallematriculaList().remove(detallematriculaListDetallematricula);
                    oldIdMatriculaOfDetallematriculaListDetallematricula = em.merge(oldIdMatriculaOfDetallematriculaListDetallematricula);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Matricula matricula) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Matricula persistentMatricula = em.find(Matricula.class, matricula.getId());
            Grado idGradoOld = persistentMatricula.getIdGrado();
            Grado idGradoNew = matricula.getIdGrado();
            Alumno idAlumnoOld = persistentMatricula.getIdAlumno();
            Alumno idAlumnoNew = matricula.getIdAlumno();
            List<Detallematricula> detallematriculaListOld = persistentMatricula.getDetallematriculaList();
            List<Detallematricula> detallematriculaListNew = matricula.getDetallematriculaList();
            List<String> illegalOrphanMessages = null;
            for (Detallematricula detallematriculaListOldDetallematricula : detallematriculaListOld) {
                if (!detallematriculaListNew.contains(detallematriculaListOldDetallematricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallematricula " + detallematriculaListOldDetallematricula + " since its idMatricula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idGradoNew != null) {
                idGradoNew = em.getReference(idGradoNew.getClass(), idGradoNew.getId());
                matricula.setIdGrado(idGradoNew);
            }
            if (idAlumnoNew != null) {
                idAlumnoNew = em.getReference(idAlumnoNew.getClass(), idAlumnoNew.getId());
                matricula.setIdAlumno(idAlumnoNew);
            }
            List<Detallematricula> attachedDetallematriculaListNew = new ArrayList<Detallematricula>();
            for (Detallematricula detallematriculaListNewDetallematriculaToAttach : detallematriculaListNew) {
                detallematriculaListNewDetallematriculaToAttach = em.getReference(detallematriculaListNewDetallematriculaToAttach.getClass(), detallematriculaListNewDetallematriculaToAttach.getId());
                attachedDetallematriculaListNew.add(detallematriculaListNewDetallematriculaToAttach);
            }
            detallematriculaListNew = attachedDetallematriculaListNew;
            matricula.setDetallematriculaList(detallematriculaListNew);
            matricula = em.merge(matricula);
            if (idGradoOld != null && !idGradoOld.equals(idGradoNew)) {
                idGradoOld.getMatriculaList().remove(matricula);
                idGradoOld = em.merge(idGradoOld);
            }
            if (idGradoNew != null && !idGradoNew.equals(idGradoOld)) {
                idGradoNew.getMatriculaList().add(matricula);
                idGradoNew = em.merge(idGradoNew);
            }
            if (idAlumnoOld != null && !idAlumnoOld.equals(idAlumnoNew)) {
                idAlumnoOld.getMatriculaList().remove(matricula);
                idAlumnoOld = em.merge(idAlumnoOld);
            }
            if (idAlumnoNew != null && !idAlumnoNew.equals(idAlumnoOld)) {
                idAlumnoNew.getMatriculaList().add(matricula);
                idAlumnoNew = em.merge(idAlumnoNew);
            }
            for (Detallematricula detallematriculaListNewDetallematricula : detallematriculaListNew) {
                if (!detallematriculaListOld.contains(detallematriculaListNewDetallematricula)) {
                    Matricula oldIdMatriculaOfDetallematriculaListNewDetallematricula = detallematriculaListNewDetallematricula.getIdMatricula();
                    detallematriculaListNewDetallematricula.setIdMatricula(matricula);
                    detallematriculaListNewDetallematricula = em.merge(detallematriculaListNewDetallematricula);
                    if (oldIdMatriculaOfDetallematriculaListNewDetallematricula != null && !oldIdMatriculaOfDetallematriculaListNewDetallematricula.equals(matricula)) {
                        oldIdMatriculaOfDetallematriculaListNewDetallematricula.getDetallematriculaList().remove(detallematriculaListNewDetallematricula);
                        oldIdMatriculaOfDetallematriculaListNewDetallematricula = em.merge(oldIdMatriculaOfDetallematriculaListNewDetallematricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = matricula.getId();
                if (findMatricula(id) == null) {
                    throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Matricula matricula;
            try {
                matricula = em.getReference(Matricula.class, id);
                matricula.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallematricula> detallematriculaListOrphanCheck = matricula.getDetallematriculaList();
            for (Detallematricula detallematriculaListOrphanCheckDetallematricula : detallematriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Matricula (" + matricula + ") cannot be destroyed since the Detallematricula " + detallematriculaListOrphanCheckDetallematricula + " in its detallematriculaList field has a non-nullable idMatricula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Grado idGrado = matricula.getIdGrado();
            if (idGrado != null) {
                idGrado.getMatriculaList().remove(matricula);
                idGrado = em.merge(idGrado);
            }
            Alumno idAlumno = matricula.getIdAlumno();
            if (idAlumno != null) {
                idAlumno.getMatriculaList().remove(matricula);
                idAlumno = em.merge(idAlumno);
            }
            em.remove(matricula);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Matricula> findMatriculaEntities() {
        return findMatriculaEntities(true, -1, -1);
    }

    public List<Matricula> findMatriculaEntities(int maxResults, int firstResult) {
        return findMatriculaEntities(false, maxResults, firstResult);
    }

    private List<Matricula> findMatriculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Matricula.class));
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

    public Matricula findMatricula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Matricula.class, id);
        } finally {
            em.close();
        }
    }

    public int getMatriculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Matricula> rt = cq.from(Matricula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
