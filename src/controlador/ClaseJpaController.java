/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entidades.Clase;
import entidades.Constantes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Docente;
import entidades.Cursoporgrado;
import entidades.Nota;
import java.util.ArrayList;
import java.util.List;
import entidades.Detallematricula;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author CesarLopez
 */
public class ClaseJpaController implements Serializable {

    public ClaseJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clase clase) {
        if (clase.getNotaList() == null) {
            clase.setNotaList(new ArrayList<Nota>());
        }
        if (clase.getDetallematriculaList() == null) {
            clase.setDetallematriculaList(new ArrayList<Detallematricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Docente idDocente = clase.getIdDocente();
            if (idDocente != null) {
                idDocente = em.getReference(idDocente.getClass(), idDocente.getId());
                clase.setIdDocente(idDocente);
            }
            Cursoporgrado idCursoPorGrado = clase.getIdCursoPorGrado();
            if (idCursoPorGrado != null) {
                idCursoPorGrado = em.getReference(idCursoPorGrado.getClass(), idCursoPorGrado.getId());
                clase.setIdCursoPorGrado(idCursoPorGrado);
            }
            List<Nota> attachedNotaList = new ArrayList<Nota>();
            for (Nota notaListNotaToAttach : clase.getNotaList()) {
                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getId());
                attachedNotaList.add(notaListNotaToAttach);
            }
            clase.setNotaList(attachedNotaList);
            List<Detallematricula> attachedDetallematriculaList = new ArrayList<Detallematricula>();
            for (Detallematricula detallematriculaListDetallematriculaToAttach : clase.getDetallematriculaList()) {
                detallematriculaListDetallematriculaToAttach = em.getReference(detallematriculaListDetallematriculaToAttach.getClass(), detallematriculaListDetallematriculaToAttach.getId());
                attachedDetallematriculaList.add(detallematriculaListDetallematriculaToAttach);
            }
            clase.setDetallematriculaList(attachedDetallematriculaList);
            em.persist(clase);
            if (idDocente != null) {
                idDocente.getClaseList().add(clase);
                idDocente = em.merge(idDocente);
            }
            if (idCursoPorGrado != null) {
                idCursoPorGrado.getClaseList().add(clase);
                idCursoPorGrado = em.merge(idCursoPorGrado);
            }
            for (Nota notaListNota : clase.getNotaList()) {
                Clase oldIdClaseOfNotaListNota = notaListNota.getIdClase();
                notaListNota.setIdClase(clase);
                notaListNota = em.merge(notaListNota);
                if (oldIdClaseOfNotaListNota != null) {
                    oldIdClaseOfNotaListNota.getNotaList().remove(notaListNota);
                    oldIdClaseOfNotaListNota = em.merge(oldIdClaseOfNotaListNota);
                }
            }
            for (Detallematricula detallematriculaListDetallematricula : clase.getDetallematriculaList()) {
                Clase oldIdClaseOfDetallematriculaListDetallematricula = detallematriculaListDetallematricula.getIdClase();
                detallematriculaListDetallematricula.setIdClase(clase);
                detallematriculaListDetallematricula = em.merge(detallematriculaListDetallematricula);
                if (oldIdClaseOfDetallematriculaListDetallematricula != null) {
                    oldIdClaseOfDetallematriculaListDetallematricula.getDetallematriculaList().remove(detallematriculaListDetallematricula);
                    oldIdClaseOfDetallematriculaListDetallematricula = em.merge(oldIdClaseOfDetallematriculaListDetallematricula);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clase clase) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clase persistentClase = em.find(Clase.class, clase.getId());
            Docente idDocenteOld = persistentClase.getIdDocente();
            Docente idDocenteNew = clase.getIdDocente();
            Cursoporgrado idCursoPorGradoOld = persistentClase.getIdCursoPorGrado();
            Cursoporgrado idCursoPorGradoNew = clase.getIdCursoPorGrado();
            List<Nota> notaListOld = persistentClase.getNotaList();
            List<Nota> notaListNew = clase.getNotaList();
            List<Detallematricula> detallematriculaListOld = persistentClase.getDetallematriculaList();
            List<Detallematricula> detallematriculaListNew = clase.getDetallematriculaList();
            List<String> illegalOrphanMessages = null;
            for (Nota notaListOldNota : notaListOld) {
                if (!notaListNew.contains(notaListOldNota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Nota " + notaListOldNota + " since its idClase field is not nullable.");
                }
            }
            for (Detallematricula detallematriculaListOldDetallematricula : detallematriculaListOld) {
                if (!detallematriculaListNew.contains(detallematriculaListOldDetallematricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallematricula " + detallematriculaListOldDetallematricula + " since its idClase field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDocenteNew != null) {
                idDocenteNew = em.getReference(idDocenteNew.getClass(), idDocenteNew.getId());
                clase.setIdDocente(idDocenteNew);
            }
            if (idCursoPorGradoNew != null) {
                idCursoPorGradoNew = em.getReference(idCursoPorGradoNew.getClass(), idCursoPorGradoNew.getId());
                clase.setIdCursoPorGrado(idCursoPorGradoNew);
            }
            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
            for (Nota notaListNewNotaToAttach : notaListNew) {
                notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getId());
                attachedNotaListNew.add(notaListNewNotaToAttach);
            }
            notaListNew = attachedNotaListNew;
            clase.setNotaList(notaListNew);
            List<Detallematricula> attachedDetallematriculaListNew = new ArrayList<Detallematricula>();
            for (Detallematricula detallematriculaListNewDetallematriculaToAttach : detallematriculaListNew) {
                detallematriculaListNewDetallematriculaToAttach = em.getReference(detallematriculaListNewDetallematriculaToAttach.getClass(), detallematriculaListNewDetallematriculaToAttach.getId());
                attachedDetallematriculaListNew.add(detallematriculaListNewDetallematriculaToAttach);
            }
            detallematriculaListNew = attachedDetallematriculaListNew;
            clase.setDetallematriculaList(detallematriculaListNew);
            clase = em.merge(clase);
            if (idDocenteOld != null && !idDocenteOld.equals(idDocenteNew)) {
                idDocenteOld.getClaseList().remove(clase);
                idDocenteOld = em.merge(idDocenteOld);
            }
            if (idDocenteNew != null && !idDocenteNew.equals(idDocenteOld)) {
                idDocenteNew.getClaseList().add(clase);
                idDocenteNew = em.merge(idDocenteNew);
            }
            if (idCursoPorGradoOld != null && !idCursoPorGradoOld.equals(idCursoPorGradoNew)) {
                idCursoPorGradoOld.getClaseList().remove(clase);
                idCursoPorGradoOld = em.merge(idCursoPorGradoOld);
            }
            if (idCursoPorGradoNew != null && !idCursoPorGradoNew.equals(idCursoPorGradoOld)) {
                idCursoPorGradoNew.getClaseList().add(clase);
                idCursoPorGradoNew = em.merge(idCursoPorGradoNew);
            }
            for (Nota notaListNewNota : notaListNew) {
                if (!notaListOld.contains(notaListNewNota)) {
                    Clase oldIdClaseOfNotaListNewNota = notaListNewNota.getIdClase();
                    notaListNewNota.setIdClase(clase);
                    notaListNewNota = em.merge(notaListNewNota);
                    if (oldIdClaseOfNotaListNewNota != null && !oldIdClaseOfNotaListNewNota.equals(clase)) {
                        oldIdClaseOfNotaListNewNota.getNotaList().remove(notaListNewNota);
                        oldIdClaseOfNotaListNewNota = em.merge(oldIdClaseOfNotaListNewNota);
                    }
                }
            }
            for (Detallematricula detallematriculaListNewDetallematricula : detallematriculaListNew) {
                if (!detallematriculaListOld.contains(detallematriculaListNewDetallematricula)) {
                    Clase oldIdClaseOfDetallematriculaListNewDetallematricula = detallematriculaListNewDetallematricula.getIdClase();
                    detallematriculaListNewDetallematricula.setIdClase(clase);
                    detallematriculaListNewDetallematricula = em.merge(detallematriculaListNewDetallematricula);
                    if (oldIdClaseOfDetallematriculaListNewDetallematricula != null && !oldIdClaseOfDetallematriculaListNewDetallematricula.equals(clase)) {
                        oldIdClaseOfDetallematriculaListNewDetallematricula.getDetallematriculaList().remove(detallematriculaListNewDetallematricula);
                        oldIdClaseOfDetallematriculaListNewDetallematricula = em.merge(oldIdClaseOfDetallematriculaListNewDetallematricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clase.getId();
                if (findClase(id) == null) {
                    throw new NonexistentEntityException("The clase with id " + id + " no longer exists.");
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
            Clase clase;
            try {
                clase = em.getReference(Clase.class, id);
                clase.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clase with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Nota> notaListOrphanCheck = clase.getNotaList();
            for (Nota notaListOrphanCheckNota : notaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clase (" + clase + ") cannot be destroyed since the Nota " + notaListOrphanCheckNota + " in its notaList field has a non-nullable idClase field.");
            }
            List<Detallematricula> detallematriculaListOrphanCheck = clase.getDetallematriculaList();
            for (Detallematricula detallematriculaListOrphanCheckDetallematricula : detallematriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clase (" + clase + ") cannot be destroyed since the Detallematricula " + detallematriculaListOrphanCheckDetallematricula + " in its detallematriculaList field has a non-nullable idClase field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Docente idDocente = clase.getIdDocente();
            if (idDocente != null) {
                idDocente.getClaseList().remove(clase);
                idDocente = em.merge(idDocente);
            }
            Cursoporgrado idCursoPorGrado = clase.getIdCursoPorGrado();
            if (idCursoPorGrado != null) {
                idCursoPorGrado.getClaseList().remove(clase);
                idCursoPorGrado = em.merge(idCursoPorGrado);
            }
            em.remove(clase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clase> findClaseEntities() {
        return findClaseEntities(true, -1, -1);
    }

    public List<Clase> findClaseEntities(int maxResults, int firstResult) {
        return findClaseEntities(false, maxResults, firstResult);
    }

    private List<Clase> findClaseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clase.class));
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

    public Clase findClase(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clase.class, id);
        } finally {
            em.close();
        }
    }

    public int getClaseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clase> rt = cq.from(Clase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
