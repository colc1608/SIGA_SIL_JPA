/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entidades.Apoderado;
import entidades.Constantes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Parentesco;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author CesarLopez
 */
public class ApoderadoJpaController implements Serializable {

    public ApoderadoJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Apoderado apoderado) {
        if (apoderado.getParentescoList() == null) {
            apoderado.setParentescoList(new ArrayList<Parentesco>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Parentesco> attachedParentescoList = new ArrayList<Parentesco>();
            for (Parentesco parentescoListParentescoToAttach : apoderado.getParentescoList()) {
                parentescoListParentescoToAttach = em.getReference(parentescoListParentescoToAttach.getClass(), parentescoListParentescoToAttach.getId());
                attachedParentescoList.add(parentescoListParentescoToAttach);
            }
            apoderado.setParentescoList(attachedParentescoList);
            em.persist(apoderado);
            for (Parentesco parentescoListParentesco : apoderado.getParentescoList()) {
                Apoderado oldIdApoderadoOfParentescoListParentesco = parentescoListParentesco.getIdApoderado();
                parentescoListParentesco.setIdApoderado(apoderado);
                parentescoListParentesco = em.merge(parentescoListParentesco);
                if (oldIdApoderadoOfParentescoListParentesco != null) {
                    oldIdApoderadoOfParentescoListParentesco.getParentescoList().remove(parentescoListParentesco);
                    oldIdApoderadoOfParentescoListParentesco = em.merge(oldIdApoderadoOfParentescoListParentesco);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Apoderado apoderado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Apoderado persistentApoderado = em.find(Apoderado.class, apoderado.getId());
            List<Parentesco> parentescoListOld = persistentApoderado.getParentescoList();
            List<Parentesco> parentescoListNew = apoderado.getParentescoList();
            List<String> illegalOrphanMessages = null;
            for (Parentesco parentescoListOldParentesco : parentescoListOld) {
                if (!parentescoListNew.contains(parentescoListOldParentesco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Parentesco " + parentescoListOldParentesco + " since its idApoderado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Parentesco> attachedParentescoListNew = new ArrayList<Parentesco>();
            for (Parentesco parentescoListNewParentescoToAttach : parentescoListNew) {
                parentescoListNewParentescoToAttach = em.getReference(parentescoListNewParentescoToAttach.getClass(), parentescoListNewParentescoToAttach.getId());
                attachedParentescoListNew.add(parentescoListNewParentescoToAttach);
            }
            parentescoListNew = attachedParentescoListNew;
            apoderado.setParentescoList(parentescoListNew);
            apoderado = em.merge(apoderado);
            for (Parentesco parentescoListNewParentesco : parentescoListNew) {
                if (!parentescoListOld.contains(parentescoListNewParentesco)) {
                    Apoderado oldIdApoderadoOfParentescoListNewParentesco = parentescoListNewParentesco.getIdApoderado();
                    parentescoListNewParentesco.setIdApoderado(apoderado);
                    parentescoListNewParentesco = em.merge(parentescoListNewParentesco);
                    if (oldIdApoderadoOfParentescoListNewParentesco != null && !oldIdApoderadoOfParentescoListNewParentesco.equals(apoderado)) {
                        oldIdApoderadoOfParentescoListNewParentesco.getParentescoList().remove(parentescoListNewParentesco);
                        oldIdApoderadoOfParentescoListNewParentesco = em.merge(oldIdApoderadoOfParentescoListNewParentesco);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = apoderado.getId();
                if (findApoderado(id) == null) {
                    throw new NonexistentEntityException("The apoderado with id " + id + " no longer exists.");
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
            Apoderado apoderado;
            try {
                apoderado = em.getReference(Apoderado.class, id);
                apoderado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The apoderado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Parentesco> parentescoListOrphanCheck = apoderado.getParentescoList();
            for (Parentesco parentescoListOrphanCheckParentesco : parentescoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Apoderado (" + apoderado + ") cannot be destroyed since the Parentesco " + parentescoListOrphanCheckParentesco + " in its parentescoList field has a non-nullable idApoderado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(apoderado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Apoderado> findApoderadoEntities() {
        return findApoderadoEntities(true, -1, -1);
    }

    public List<Apoderado> findApoderadoEntities(int maxResults, int firstResult) {
        return findApoderadoEntities(false, maxResults, firstResult);
    }

    private List<Apoderado> findApoderadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Apoderado.class));
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

    public Apoderado findApoderado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Apoderado.class, id);
        } finally {
            em.close();
        }
    }

    public int getApoderadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Apoderado> rt = cq.from(Apoderado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
