/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entidades.Constantes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Grado;
import entidades.Niveleducacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author CesarLopez
 */
public class NiveleducacionJpaController implements Serializable {

    public NiveleducacionJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Niveleducacion niveleducacion) {
        if (niveleducacion.getGradoList() == null) {
            niveleducacion.setGradoList(new ArrayList<Grado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Grado> attachedGradoList = new ArrayList<Grado>();
            for (Grado gradoListGradoToAttach : niveleducacion.getGradoList()) {
                gradoListGradoToAttach = em.getReference(gradoListGradoToAttach.getClass(), gradoListGradoToAttach.getId());
                attachedGradoList.add(gradoListGradoToAttach);
            }
            niveleducacion.setGradoList(attachedGradoList);
            em.persist(niveleducacion);
            for (Grado gradoListGrado : niveleducacion.getGradoList()) {
                Niveleducacion oldIdnivelEducacionOfGradoListGrado = gradoListGrado.getIdnivelEducacion();
                gradoListGrado.setIdnivelEducacion(niveleducacion);
                gradoListGrado = em.merge(gradoListGrado);
                if (oldIdnivelEducacionOfGradoListGrado != null) {
                    oldIdnivelEducacionOfGradoListGrado.getGradoList().remove(gradoListGrado);
                    oldIdnivelEducacionOfGradoListGrado = em.merge(oldIdnivelEducacionOfGradoListGrado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Niveleducacion niveleducacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Niveleducacion persistentNiveleducacion = em.find(Niveleducacion.class, niveleducacion.getId());
            List<Grado> gradoListOld = persistentNiveleducacion.getGradoList();
            List<Grado> gradoListNew = niveleducacion.getGradoList();
            List<String> illegalOrphanMessages = null;
            for (Grado gradoListOldGrado : gradoListOld) {
                if (!gradoListNew.contains(gradoListOldGrado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Grado " + gradoListOldGrado + " since its idnivelEducacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Grado> attachedGradoListNew = new ArrayList<Grado>();
            for (Grado gradoListNewGradoToAttach : gradoListNew) {
                gradoListNewGradoToAttach = em.getReference(gradoListNewGradoToAttach.getClass(), gradoListNewGradoToAttach.getId());
                attachedGradoListNew.add(gradoListNewGradoToAttach);
            }
            gradoListNew = attachedGradoListNew;
            niveleducacion.setGradoList(gradoListNew);
            niveleducacion = em.merge(niveleducacion);
            for (Grado gradoListNewGrado : gradoListNew) {
                if (!gradoListOld.contains(gradoListNewGrado)) {
                    Niveleducacion oldIdnivelEducacionOfGradoListNewGrado = gradoListNewGrado.getIdnivelEducacion();
                    gradoListNewGrado.setIdnivelEducacion(niveleducacion);
                    gradoListNewGrado = em.merge(gradoListNewGrado);
                    if (oldIdnivelEducacionOfGradoListNewGrado != null && !oldIdnivelEducacionOfGradoListNewGrado.equals(niveleducacion)) {
                        oldIdnivelEducacionOfGradoListNewGrado.getGradoList().remove(gradoListNewGrado);
                        oldIdnivelEducacionOfGradoListNewGrado = em.merge(oldIdnivelEducacionOfGradoListNewGrado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = niveleducacion.getId();
                if (findNiveleducacion(id) == null) {
                    throw new NonexistentEntityException("The niveleducacion with id " + id + " no longer exists.");
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
            Niveleducacion niveleducacion;
            try {
                niveleducacion = em.getReference(Niveleducacion.class, id);
                niveleducacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The niveleducacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Grado> gradoListOrphanCheck = niveleducacion.getGradoList();
            for (Grado gradoListOrphanCheckGrado : gradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Niveleducacion (" + niveleducacion + ") cannot be destroyed since the Grado " + gradoListOrphanCheckGrado + " in its gradoList field has a non-nullable idnivelEducacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(niveleducacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Niveleducacion> findNiveleducacionEntities() {
        return findNiveleducacionEntities(true, -1, -1);
    }

    public List<Niveleducacion> findNiveleducacionEntities(int maxResults, int firstResult) {
        return findNiveleducacionEntities(false, maxResults, firstResult);
    }

    private List<Niveleducacion> findNiveleducacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Niveleducacion.class));
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

    public Niveleducacion findNiveleducacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Niveleducacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getNiveleducacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Niveleducacion> rt = cq.from(Niveleducacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
