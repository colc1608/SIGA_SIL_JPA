/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entidades.Constantes;
import entidades.Curso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Cursoporgrado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author CesarLopez
 */
public class CursoJpaController implements Serializable {

    public CursoJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Curso curso) {
        if (curso.getCursoporgradoList() == null) {
            curso.setCursoporgradoList(new ArrayList<Cursoporgrado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cursoporgrado> attachedCursoporgradoList = new ArrayList<Cursoporgrado>();
            for (Cursoporgrado cursoporgradoListCursoporgradoToAttach : curso.getCursoporgradoList()) {
                cursoporgradoListCursoporgradoToAttach = em.getReference(cursoporgradoListCursoporgradoToAttach.getClass(), cursoporgradoListCursoporgradoToAttach.getId());
                attachedCursoporgradoList.add(cursoporgradoListCursoporgradoToAttach);
            }
            curso.setCursoporgradoList(attachedCursoporgradoList);
            em.persist(curso);
            for (Cursoporgrado cursoporgradoListCursoporgrado : curso.getCursoporgradoList()) {
                Curso oldIdCursoOfCursoporgradoListCursoporgrado = cursoporgradoListCursoporgrado.getIdCurso();
                cursoporgradoListCursoporgrado.setIdCurso(curso);
                cursoporgradoListCursoporgrado = em.merge(cursoporgradoListCursoporgrado);
                if (oldIdCursoOfCursoporgradoListCursoporgrado != null) {
                    oldIdCursoOfCursoporgradoListCursoporgrado.getCursoporgradoList().remove(cursoporgradoListCursoporgrado);
                    oldIdCursoOfCursoporgradoListCursoporgrado = em.merge(oldIdCursoOfCursoporgradoListCursoporgrado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Curso curso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso persistentCurso = em.find(Curso.class, curso.getId());
            List<Cursoporgrado> cursoporgradoListOld = persistentCurso.getCursoporgradoList();
            List<Cursoporgrado> cursoporgradoListNew = curso.getCursoporgradoList();
            List<String> illegalOrphanMessages = null;
            for (Cursoporgrado cursoporgradoListOldCursoporgrado : cursoporgradoListOld) {
                if (!cursoporgradoListNew.contains(cursoporgradoListOldCursoporgrado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cursoporgrado " + cursoporgradoListOldCursoporgrado + " since its idCurso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cursoporgrado> attachedCursoporgradoListNew = new ArrayList<Cursoporgrado>();
            for (Cursoporgrado cursoporgradoListNewCursoporgradoToAttach : cursoporgradoListNew) {
                cursoporgradoListNewCursoporgradoToAttach = em.getReference(cursoporgradoListNewCursoporgradoToAttach.getClass(), cursoporgradoListNewCursoporgradoToAttach.getId());
                attachedCursoporgradoListNew.add(cursoporgradoListNewCursoporgradoToAttach);
            }
            cursoporgradoListNew = attachedCursoporgradoListNew;
            curso.setCursoporgradoList(cursoporgradoListNew);
            curso = em.merge(curso);
            for (Cursoporgrado cursoporgradoListNewCursoporgrado : cursoporgradoListNew) {
                if (!cursoporgradoListOld.contains(cursoporgradoListNewCursoporgrado)) {
                    Curso oldIdCursoOfCursoporgradoListNewCursoporgrado = cursoporgradoListNewCursoporgrado.getIdCurso();
                    cursoporgradoListNewCursoporgrado.setIdCurso(curso);
                    cursoporgradoListNewCursoporgrado = em.merge(cursoporgradoListNewCursoporgrado);
                    if (oldIdCursoOfCursoporgradoListNewCursoporgrado != null && !oldIdCursoOfCursoporgradoListNewCursoporgrado.equals(curso)) {
                        oldIdCursoOfCursoporgradoListNewCursoporgrado.getCursoporgradoList().remove(cursoporgradoListNewCursoporgrado);
                        oldIdCursoOfCursoporgradoListNewCursoporgrado = em.merge(oldIdCursoOfCursoporgradoListNewCursoporgrado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = curso.getId();
                if (findCurso(id) == null) {
                    throw new NonexistentEntityException("The curso with id " + id + " no longer exists.");
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
            Curso curso;
            try {
                curso = em.getReference(Curso.class, id);
                curso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The curso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cursoporgrado> cursoporgradoListOrphanCheck = curso.getCursoporgradoList();
            for (Cursoporgrado cursoporgradoListOrphanCheckCursoporgrado : cursoporgradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Cursoporgrado " + cursoporgradoListOrphanCheckCursoporgrado + " in its cursoporgradoList field has a non-nullable idCurso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(curso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Curso> findCursoEntities() {
        return findCursoEntities(true, -1, -1);
    }

    public List<Curso> findCursoEntities(int maxResults, int firstResult) {
        return findCursoEntities(false, maxResults, firstResult);
    }

    private List<Curso> findCursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Curso.class));
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

    public Curso findCurso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Curso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Curso> rt = cq.from(Curso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
