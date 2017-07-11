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
import entidades.Curso;
import entidades.Grado;
import entidades.Clase;
import entidades.Constantes;
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
public class CursoporgradoJpaController implements Serializable {

    public CursoporgradoJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cursoporgrado cursoporgrado) {
        if (cursoporgrado.getClaseList() == null) {
            cursoporgrado.setClaseList(new ArrayList<Clase>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso idCurso = cursoporgrado.getIdCurso();
            if (idCurso != null) {
                idCurso = em.getReference(idCurso.getClass(), idCurso.getId());
                cursoporgrado.setIdCurso(idCurso);
            }
            Grado idGrado = cursoporgrado.getIdGrado();
            if (idGrado != null) {
                idGrado = em.getReference(idGrado.getClass(), idGrado.getId());
                cursoporgrado.setIdGrado(idGrado);
            }
            List<Clase> attachedClaseList = new ArrayList<Clase>();
            for (Clase claseListClaseToAttach : cursoporgrado.getClaseList()) {
                claseListClaseToAttach = em.getReference(claseListClaseToAttach.getClass(), claseListClaseToAttach.getId());
                attachedClaseList.add(claseListClaseToAttach);
            }
            cursoporgrado.setClaseList(attachedClaseList);
            em.persist(cursoporgrado);
            if (idCurso != null) {
                idCurso.getCursoporgradoList().add(cursoporgrado);
                idCurso = em.merge(idCurso);
            }
            if (idGrado != null) {
                idGrado.getCursoporgradoList().add(cursoporgrado);
                idGrado = em.merge(idGrado);
            }
            for (Clase claseListClase : cursoporgrado.getClaseList()) {
                Cursoporgrado oldIdCursoPorGradoOfClaseListClase = claseListClase.getIdCursoPorGrado();
                claseListClase.setIdCursoPorGrado(cursoporgrado);
                claseListClase = em.merge(claseListClase);
                if (oldIdCursoPorGradoOfClaseListClase != null) {
                    oldIdCursoPorGradoOfClaseListClase.getClaseList().remove(claseListClase);
                    oldIdCursoPorGradoOfClaseListClase = em.merge(oldIdCursoPorGradoOfClaseListClase);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cursoporgrado cursoporgrado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cursoporgrado persistentCursoporgrado = em.find(Cursoporgrado.class, cursoporgrado.getId());
            Curso idCursoOld = persistentCursoporgrado.getIdCurso();
            Curso idCursoNew = cursoporgrado.getIdCurso();
            Grado idGradoOld = persistentCursoporgrado.getIdGrado();
            Grado idGradoNew = cursoporgrado.getIdGrado();
            List<Clase> claseListOld = persistentCursoporgrado.getClaseList();
            List<Clase> claseListNew = cursoporgrado.getClaseList();
            List<String> illegalOrphanMessages = null;
            for (Clase claseListOldClase : claseListOld) {
                if (!claseListNew.contains(claseListOldClase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Clase " + claseListOldClase + " since its idCursoPorGrado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCursoNew != null) {
                idCursoNew = em.getReference(idCursoNew.getClass(), idCursoNew.getId());
                cursoporgrado.setIdCurso(idCursoNew);
            }
            if (idGradoNew != null) {
                idGradoNew = em.getReference(idGradoNew.getClass(), idGradoNew.getId());
                cursoporgrado.setIdGrado(idGradoNew);
            }
            List<Clase> attachedClaseListNew = new ArrayList<Clase>();
            for (Clase claseListNewClaseToAttach : claseListNew) {
                claseListNewClaseToAttach = em.getReference(claseListNewClaseToAttach.getClass(), claseListNewClaseToAttach.getId());
                attachedClaseListNew.add(claseListNewClaseToAttach);
            }
            claseListNew = attachedClaseListNew;
            cursoporgrado.setClaseList(claseListNew);
            cursoporgrado = em.merge(cursoporgrado);
            if (idCursoOld != null && !idCursoOld.equals(idCursoNew)) {
                idCursoOld.getCursoporgradoList().remove(cursoporgrado);
                idCursoOld = em.merge(idCursoOld);
            }
            if (idCursoNew != null && !idCursoNew.equals(idCursoOld)) {
                idCursoNew.getCursoporgradoList().add(cursoporgrado);
                idCursoNew = em.merge(idCursoNew);
            }
            if (idGradoOld != null && !idGradoOld.equals(idGradoNew)) {
                idGradoOld.getCursoporgradoList().remove(cursoporgrado);
                idGradoOld = em.merge(idGradoOld);
            }
            if (idGradoNew != null && !idGradoNew.equals(idGradoOld)) {
                idGradoNew.getCursoporgradoList().add(cursoporgrado);
                idGradoNew = em.merge(idGradoNew);
            }
            for (Clase claseListNewClase : claseListNew) {
                if (!claseListOld.contains(claseListNewClase)) {
                    Cursoporgrado oldIdCursoPorGradoOfClaseListNewClase = claseListNewClase.getIdCursoPorGrado();
                    claseListNewClase.setIdCursoPorGrado(cursoporgrado);
                    claseListNewClase = em.merge(claseListNewClase);
                    if (oldIdCursoPorGradoOfClaseListNewClase != null && !oldIdCursoPorGradoOfClaseListNewClase.equals(cursoporgrado)) {
                        oldIdCursoPorGradoOfClaseListNewClase.getClaseList().remove(claseListNewClase);
                        oldIdCursoPorGradoOfClaseListNewClase = em.merge(oldIdCursoPorGradoOfClaseListNewClase);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cursoporgrado.getId();
                if (findCursoporgrado(id) == null) {
                    throw new NonexistentEntityException("The cursoporgrado with id " + id + " no longer exists.");
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
            Cursoporgrado cursoporgrado;
            try {
                cursoporgrado = em.getReference(Cursoporgrado.class, id);
                cursoporgrado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cursoporgrado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Clase> claseListOrphanCheck = cursoporgrado.getClaseList();
            for (Clase claseListOrphanCheckClase : claseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cursoporgrado (" + cursoporgrado + ") cannot be destroyed since the Clase " + claseListOrphanCheckClase + " in its claseList field has a non-nullable idCursoPorGrado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Curso idCurso = cursoporgrado.getIdCurso();
            if (idCurso != null) {
                idCurso.getCursoporgradoList().remove(cursoporgrado);
                idCurso = em.merge(idCurso);
            }
            Grado idGrado = cursoporgrado.getIdGrado();
            if (idGrado != null) {
                idGrado.getCursoporgradoList().remove(cursoporgrado);
                idGrado = em.merge(idGrado);
            }
            em.remove(cursoporgrado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cursoporgrado> findCursoporgradoEntities() {
        return findCursoporgradoEntities(true, -1, -1);
    }

    public List<Cursoporgrado> findCursoporgradoEntities(int maxResults, int firstResult) {
        return findCursoporgradoEntities(false, maxResults, firstResult);
    }

    private List<Cursoporgrado> findCursoporgradoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cursoporgrado.class));
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

    public Cursoporgrado findCursoporgrado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cursoporgrado.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursoporgradoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cursoporgrado> rt = cq.from(Cursoporgrado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
