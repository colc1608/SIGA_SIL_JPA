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
import entidades.Usuario;
import entidades.Especialidad;
import entidades.Clase;
import entidades.Constantes;
import entidades.Docente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author CesarLopez
 */
public class DocenteJpaController implements Serializable {

    public DocenteJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Docente docente) {
        if (docente.getClaseList() == null) {
            docente.setClaseList(new ArrayList<Clase>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idUsuario = docente.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getId());
                docente.setIdUsuario(idUsuario);
            }
            Especialidad idEspecialidad = docente.getIdEspecialidad();
            if (idEspecialidad != null) {
                idEspecialidad = em.getReference(idEspecialidad.getClass(), idEspecialidad.getId());
                docente.setIdEspecialidad(idEspecialidad);
            }
            List<Clase> attachedClaseList = new ArrayList<Clase>();
            for (Clase claseListClaseToAttach : docente.getClaseList()) {
                claseListClaseToAttach = em.getReference(claseListClaseToAttach.getClass(), claseListClaseToAttach.getId());
                attachedClaseList.add(claseListClaseToAttach);
            }
            docente.setClaseList(attachedClaseList);
            em.persist(docente);
            if (idUsuario != null) {
                idUsuario.getDocenteList().add(docente);
                idUsuario = em.merge(idUsuario);
            }
            if (idEspecialidad != null) {
                idEspecialidad.getDocenteList().add(docente);
                idEspecialidad = em.merge(idEspecialidad);
            }
            for (Clase claseListClase : docente.getClaseList()) {
                Docente oldIdDocenteOfClaseListClase = claseListClase.getIdDocente();
                claseListClase.setIdDocente(docente);
                claseListClase = em.merge(claseListClase);
                if (oldIdDocenteOfClaseListClase != null) {
                    oldIdDocenteOfClaseListClase.getClaseList().remove(claseListClase);
                    oldIdDocenteOfClaseListClase = em.merge(oldIdDocenteOfClaseListClase);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Docente docente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Docente persistentDocente = em.find(Docente.class, docente.getId());
            Usuario idUsuarioOld = persistentDocente.getIdUsuario();
            Usuario idUsuarioNew = docente.getIdUsuario();
            Especialidad idEspecialidadOld = persistentDocente.getIdEspecialidad();
            Especialidad idEspecialidadNew = docente.getIdEspecialidad();
            List<Clase> claseListOld = persistentDocente.getClaseList();
            List<Clase> claseListNew = docente.getClaseList();
            List<String> illegalOrphanMessages = null;
            for (Clase claseListOldClase : claseListOld) {
                if (!claseListNew.contains(claseListOldClase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Clase " + claseListOldClase + " since its idDocente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getId());
                docente.setIdUsuario(idUsuarioNew);
            }
            if (idEspecialidadNew != null) {
                idEspecialidadNew = em.getReference(idEspecialidadNew.getClass(), idEspecialidadNew.getId());
                docente.setIdEspecialidad(idEspecialidadNew);
            }
            List<Clase> attachedClaseListNew = new ArrayList<Clase>();
            for (Clase claseListNewClaseToAttach : claseListNew) {
                claseListNewClaseToAttach = em.getReference(claseListNewClaseToAttach.getClass(), claseListNewClaseToAttach.getId());
                attachedClaseListNew.add(claseListNewClaseToAttach);
            }
            claseListNew = attachedClaseListNew;
            docente.setClaseList(claseListNew);
            docente = em.merge(docente);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getDocenteList().remove(docente);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getDocenteList().add(docente);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            if (idEspecialidadOld != null && !idEspecialidadOld.equals(idEspecialidadNew)) {
                idEspecialidadOld.getDocenteList().remove(docente);
                idEspecialidadOld = em.merge(idEspecialidadOld);
            }
            if (idEspecialidadNew != null && !idEspecialidadNew.equals(idEspecialidadOld)) {
                idEspecialidadNew.getDocenteList().add(docente);
                idEspecialidadNew = em.merge(idEspecialidadNew);
            }
            for (Clase claseListNewClase : claseListNew) {
                if (!claseListOld.contains(claseListNewClase)) {
                    Docente oldIdDocenteOfClaseListNewClase = claseListNewClase.getIdDocente();
                    claseListNewClase.setIdDocente(docente);
                    claseListNewClase = em.merge(claseListNewClase);
                    if (oldIdDocenteOfClaseListNewClase != null && !oldIdDocenteOfClaseListNewClase.equals(docente)) {
                        oldIdDocenteOfClaseListNewClase.getClaseList().remove(claseListNewClase);
                        oldIdDocenteOfClaseListNewClase = em.merge(oldIdDocenteOfClaseListNewClase);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = docente.getId();
                if (findDocente(id) == null) {
                    throw new NonexistentEntityException("The docente with id " + id + " no longer exists.");
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
            Docente docente;
            try {
                docente = em.getReference(Docente.class, id);
                docente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Clase> claseListOrphanCheck = docente.getClaseList();
            for (Clase claseListOrphanCheckClase : claseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Docente (" + docente + ") cannot be destroyed since the Clase " + claseListOrphanCheckClase + " in its claseList field has a non-nullable idDocente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario idUsuario = docente.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getDocenteList().remove(docente);
                idUsuario = em.merge(idUsuario);
            }
            Especialidad idEspecialidad = docente.getIdEspecialidad();
            if (idEspecialidad != null) {
                idEspecialidad.getDocenteList().remove(docente);
                idEspecialidad = em.merge(idEspecialidad);
            }
            em.remove(docente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Docente> findDocenteEntities() {
        return findDocenteEntities(true, -1, -1);
    }

    public List<Docente> findDocenteEntities(int maxResults, int firstResult) {
        return findDocenteEntities(false, maxResults, firstResult);
    }

    private List<Docente> findDocenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Docente.class));
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

    public Docente findDocente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Docente.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Docente> rt = cq.from(Docente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
