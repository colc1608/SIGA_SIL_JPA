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
import entidades.Nota;
import entidades.Tipoevaluacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author CesarLopez
 */
public class TipoevaluacionJpaController implements Serializable {

    public TipoevaluacionJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipoevaluacion tipoevaluacion) {
        if (tipoevaluacion.getNotaList() == null) {
            tipoevaluacion.setNotaList(new ArrayList<Nota>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Nota> attachedNotaList = new ArrayList<Nota>();
            for (Nota notaListNotaToAttach : tipoevaluacion.getNotaList()) {
                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getId());
                attachedNotaList.add(notaListNotaToAttach);
            }
            tipoevaluacion.setNotaList(attachedNotaList);
            em.persist(tipoevaluacion);
            for (Nota notaListNota : tipoevaluacion.getNotaList()) {
                Tipoevaluacion oldIdTipoEvaluacionOfNotaListNota = notaListNota.getIdTipoEvaluacion();
                notaListNota.setIdTipoEvaluacion(tipoevaluacion);
                notaListNota = em.merge(notaListNota);
                if (oldIdTipoEvaluacionOfNotaListNota != null) {
                    oldIdTipoEvaluacionOfNotaListNota.getNotaList().remove(notaListNota);
                    oldIdTipoEvaluacionOfNotaListNota = em.merge(oldIdTipoEvaluacionOfNotaListNota);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipoevaluacion tipoevaluacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoevaluacion persistentTipoevaluacion = em.find(Tipoevaluacion.class, tipoevaluacion.getId());
            List<Nota> notaListOld = persistentTipoevaluacion.getNotaList();
            List<Nota> notaListNew = tipoevaluacion.getNotaList();
            List<String> illegalOrphanMessages = null;
            for (Nota notaListOldNota : notaListOld) {
                if (!notaListNew.contains(notaListOldNota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Nota " + notaListOldNota + " since its idTipoEvaluacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
            for (Nota notaListNewNotaToAttach : notaListNew) {
                notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getId());
                attachedNotaListNew.add(notaListNewNotaToAttach);
            }
            notaListNew = attachedNotaListNew;
            tipoevaluacion.setNotaList(notaListNew);
            tipoevaluacion = em.merge(tipoevaluacion);
            for (Nota notaListNewNota : notaListNew) {
                if (!notaListOld.contains(notaListNewNota)) {
                    Tipoevaluacion oldIdTipoEvaluacionOfNotaListNewNota = notaListNewNota.getIdTipoEvaluacion();
                    notaListNewNota.setIdTipoEvaluacion(tipoevaluacion);
                    notaListNewNota = em.merge(notaListNewNota);
                    if (oldIdTipoEvaluacionOfNotaListNewNota != null && !oldIdTipoEvaluacionOfNotaListNewNota.equals(tipoevaluacion)) {
                        oldIdTipoEvaluacionOfNotaListNewNota.getNotaList().remove(notaListNewNota);
                        oldIdTipoEvaluacionOfNotaListNewNota = em.merge(oldIdTipoEvaluacionOfNotaListNewNota);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoevaluacion.getId();
                if (findTipoevaluacion(id) == null) {
                    throw new NonexistentEntityException("The tipoevaluacion with id " + id + " no longer exists.");
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
            Tipoevaluacion tipoevaluacion;
            try {
                tipoevaluacion = em.getReference(Tipoevaluacion.class, id);
                tipoevaluacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoevaluacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Nota> notaListOrphanCheck = tipoevaluacion.getNotaList();
            for (Nota notaListOrphanCheckNota : notaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipoevaluacion (" + tipoevaluacion + ") cannot be destroyed since the Nota " + notaListOrphanCheckNota + " in its notaList field has a non-nullable idTipoEvaluacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoevaluacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipoevaluacion> findTipoevaluacionEntities() {
        return findTipoevaluacionEntities(true, -1, -1);
    }

    public List<Tipoevaluacion> findTipoevaluacionEntities(int maxResults, int firstResult) {
        return findTipoevaluacionEntities(false, maxResults, firstResult);
    }

    private List<Tipoevaluacion> findTipoevaluacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipoevaluacion.class));
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

    public Tipoevaluacion findTipoevaluacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipoevaluacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoevaluacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipoevaluacion> rt = cq.from(Tipoevaluacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
