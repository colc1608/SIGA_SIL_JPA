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
import entidades.Seccion;
import entidades.Niveleducacion;
import entidades.Cursoporgrado;
import entidades.Grado;
import java.util.ArrayList;
import java.util.List;
import entidades.Matricula;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author CesarLopez
 */
public class GradoJpaController implements Serializable {

    public GradoJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grado grado) {
        if (grado.getCursoporgradoList() == null) {
            grado.setCursoporgradoList(new ArrayList<Cursoporgrado>());
        }
        if (grado.getMatriculaList() == null) {
            grado.setMatriculaList(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Seccion idSeccion = grado.getIdSeccion();
            if (idSeccion != null) {
                idSeccion = em.getReference(idSeccion.getClass(), idSeccion.getId());
                grado.setIdSeccion(idSeccion);
            }
            Niveleducacion idnivelEducacion = grado.getIdnivelEducacion();
            if (idnivelEducacion != null) {
                idnivelEducacion = em.getReference(idnivelEducacion.getClass(), idnivelEducacion.getId());
                grado.setIdnivelEducacion(idnivelEducacion);
            }
            List<Cursoporgrado> attachedCursoporgradoList = new ArrayList<Cursoporgrado>();
            for (Cursoporgrado cursoporgradoListCursoporgradoToAttach : grado.getCursoporgradoList()) {
                cursoporgradoListCursoporgradoToAttach = em.getReference(cursoporgradoListCursoporgradoToAttach.getClass(), cursoporgradoListCursoporgradoToAttach.getId());
                attachedCursoporgradoList.add(cursoporgradoListCursoporgradoToAttach);
            }
            grado.setCursoporgradoList(attachedCursoporgradoList);
            List<Matricula> attachedMatriculaList = new ArrayList<Matricula>();
            for (Matricula matriculaListMatriculaToAttach : grado.getMatriculaList()) {
                matriculaListMatriculaToAttach = em.getReference(matriculaListMatriculaToAttach.getClass(), matriculaListMatriculaToAttach.getId());
                attachedMatriculaList.add(matriculaListMatriculaToAttach);
            }
            grado.setMatriculaList(attachedMatriculaList);
            em.persist(grado);
            if (idSeccion != null) {
                idSeccion.getGradoList().add(grado);
                idSeccion = em.merge(idSeccion);
            }
            if (idnivelEducacion != null) {
                idnivelEducacion.getGradoList().add(grado);
                idnivelEducacion = em.merge(idnivelEducacion);
            }
            for (Cursoporgrado cursoporgradoListCursoporgrado : grado.getCursoporgradoList()) {
                Grado oldIdGradoOfCursoporgradoListCursoporgrado = cursoporgradoListCursoporgrado.getIdGrado();
                cursoporgradoListCursoporgrado.setIdGrado(grado);
                cursoporgradoListCursoporgrado = em.merge(cursoporgradoListCursoporgrado);
                if (oldIdGradoOfCursoporgradoListCursoporgrado != null) {
                    oldIdGradoOfCursoporgradoListCursoporgrado.getCursoporgradoList().remove(cursoporgradoListCursoporgrado);
                    oldIdGradoOfCursoporgradoListCursoporgrado = em.merge(oldIdGradoOfCursoporgradoListCursoporgrado);
                }
            }
            for (Matricula matriculaListMatricula : grado.getMatriculaList()) {
                Grado oldIdGradoOfMatriculaListMatricula = matriculaListMatricula.getIdGrado();
                matriculaListMatricula.setIdGrado(grado);
                matriculaListMatricula = em.merge(matriculaListMatricula);
                if (oldIdGradoOfMatriculaListMatricula != null) {
                    oldIdGradoOfMatriculaListMatricula.getMatriculaList().remove(matriculaListMatricula);
                    oldIdGradoOfMatriculaListMatricula = em.merge(oldIdGradoOfMatriculaListMatricula);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grado grado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grado persistentGrado = em.find(Grado.class, grado.getId());
            Seccion idSeccionOld = persistentGrado.getIdSeccion();
            Seccion idSeccionNew = grado.getIdSeccion();
            Niveleducacion idnivelEducacionOld = persistentGrado.getIdnivelEducacion();
            Niveleducacion idnivelEducacionNew = grado.getIdnivelEducacion();
            List<Cursoporgrado> cursoporgradoListOld = persistentGrado.getCursoporgradoList();
            List<Cursoporgrado> cursoporgradoListNew = grado.getCursoporgradoList();
            List<Matricula> matriculaListOld = persistentGrado.getMatriculaList();
            List<Matricula> matriculaListNew = grado.getMatriculaList();
            List<String> illegalOrphanMessages = null;
            for (Cursoporgrado cursoporgradoListOldCursoporgrado : cursoporgradoListOld) {
                if (!cursoporgradoListNew.contains(cursoporgradoListOldCursoporgrado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cursoporgrado " + cursoporgradoListOldCursoporgrado + " since its idGrado field is not nullable.");
                }
            }
            for (Matricula matriculaListOldMatricula : matriculaListOld) {
                if (!matriculaListNew.contains(matriculaListOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaListOldMatricula + " since its idGrado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idSeccionNew != null) {
                idSeccionNew = em.getReference(idSeccionNew.getClass(), idSeccionNew.getId());
                grado.setIdSeccion(idSeccionNew);
            }
            if (idnivelEducacionNew != null) {
                idnivelEducacionNew = em.getReference(idnivelEducacionNew.getClass(), idnivelEducacionNew.getId());
                grado.setIdnivelEducacion(idnivelEducacionNew);
            }
            List<Cursoporgrado> attachedCursoporgradoListNew = new ArrayList<Cursoporgrado>();
            for (Cursoporgrado cursoporgradoListNewCursoporgradoToAttach : cursoporgradoListNew) {
                cursoporgradoListNewCursoporgradoToAttach = em.getReference(cursoporgradoListNewCursoporgradoToAttach.getClass(), cursoporgradoListNewCursoporgradoToAttach.getId());
                attachedCursoporgradoListNew.add(cursoporgradoListNewCursoporgradoToAttach);
            }
            cursoporgradoListNew = attachedCursoporgradoListNew;
            grado.setCursoporgradoList(cursoporgradoListNew);
            List<Matricula> attachedMatriculaListNew = new ArrayList<Matricula>();
            for (Matricula matriculaListNewMatriculaToAttach : matriculaListNew) {
                matriculaListNewMatriculaToAttach = em.getReference(matriculaListNewMatriculaToAttach.getClass(), matriculaListNewMatriculaToAttach.getId());
                attachedMatriculaListNew.add(matriculaListNewMatriculaToAttach);
            }
            matriculaListNew = attachedMatriculaListNew;
            grado.setMatriculaList(matriculaListNew);
            grado = em.merge(grado);
            if (idSeccionOld != null && !idSeccionOld.equals(idSeccionNew)) {
                idSeccionOld.getGradoList().remove(grado);
                idSeccionOld = em.merge(idSeccionOld);
            }
            if (idSeccionNew != null && !idSeccionNew.equals(idSeccionOld)) {
                idSeccionNew.getGradoList().add(grado);
                idSeccionNew = em.merge(idSeccionNew);
            }
            if (idnivelEducacionOld != null && !idnivelEducacionOld.equals(idnivelEducacionNew)) {
                idnivelEducacionOld.getGradoList().remove(grado);
                idnivelEducacionOld = em.merge(idnivelEducacionOld);
            }
            if (idnivelEducacionNew != null && !idnivelEducacionNew.equals(idnivelEducacionOld)) {
                idnivelEducacionNew.getGradoList().add(grado);
                idnivelEducacionNew = em.merge(idnivelEducacionNew);
            }
            for (Cursoporgrado cursoporgradoListNewCursoporgrado : cursoporgradoListNew) {
                if (!cursoporgradoListOld.contains(cursoporgradoListNewCursoporgrado)) {
                    Grado oldIdGradoOfCursoporgradoListNewCursoporgrado = cursoporgradoListNewCursoporgrado.getIdGrado();
                    cursoporgradoListNewCursoporgrado.setIdGrado(grado);
                    cursoporgradoListNewCursoporgrado = em.merge(cursoporgradoListNewCursoporgrado);
                    if (oldIdGradoOfCursoporgradoListNewCursoporgrado != null && !oldIdGradoOfCursoporgradoListNewCursoporgrado.equals(grado)) {
                        oldIdGradoOfCursoporgradoListNewCursoporgrado.getCursoporgradoList().remove(cursoporgradoListNewCursoporgrado);
                        oldIdGradoOfCursoporgradoListNewCursoporgrado = em.merge(oldIdGradoOfCursoporgradoListNewCursoporgrado);
                    }
                }
            }
            for (Matricula matriculaListNewMatricula : matriculaListNew) {
                if (!matriculaListOld.contains(matriculaListNewMatricula)) {
                    Grado oldIdGradoOfMatriculaListNewMatricula = matriculaListNewMatricula.getIdGrado();
                    matriculaListNewMatricula.setIdGrado(grado);
                    matriculaListNewMatricula = em.merge(matriculaListNewMatricula);
                    if (oldIdGradoOfMatriculaListNewMatricula != null && !oldIdGradoOfMatriculaListNewMatricula.equals(grado)) {
                        oldIdGradoOfMatriculaListNewMatricula.getMatriculaList().remove(matriculaListNewMatricula);
                        oldIdGradoOfMatriculaListNewMatricula = em.merge(oldIdGradoOfMatriculaListNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grado.getId();
                if (findGrado(id) == null) {
                    throw new NonexistentEntityException("The grado with id " + id + " no longer exists.");
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
            Grado grado;
            try {
                grado = em.getReference(Grado.class, id);
                grado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cursoporgrado> cursoporgradoListOrphanCheck = grado.getCursoporgradoList();
            for (Cursoporgrado cursoporgradoListOrphanCheckCursoporgrado : cursoporgradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grado (" + grado + ") cannot be destroyed since the Cursoporgrado " + cursoporgradoListOrphanCheckCursoporgrado + " in its cursoporgradoList field has a non-nullable idGrado field.");
            }
            List<Matricula> matriculaListOrphanCheck = grado.getMatriculaList();
            for (Matricula matriculaListOrphanCheckMatricula : matriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grado (" + grado + ") cannot be destroyed since the Matricula " + matriculaListOrphanCheckMatricula + " in its matriculaList field has a non-nullable idGrado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Seccion idSeccion = grado.getIdSeccion();
            if (idSeccion != null) {
                idSeccion.getGradoList().remove(grado);
                idSeccion = em.merge(idSeccion);
            }
            Niveleducacion idnivelEducacion = grado.getIdnivelEducacion();
            if (idnivelEducacion != null) {
                idnivelEducacion.getGradoList().remove(grado);
                idnivelEducacion = em.merge(idnivelEducacion);
            }
            em.remove(grado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grado> findGradoEntities() {
        return findGradoEntities(true, -1, -1);
    }

    public List<Grado> findGradoEntities(int maxResults, int firstResult) {
        return findGradoEntities(false, maxResults, firstResult);
    }

    private List<Grado> findGradoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grado.class));
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

    public Grado findGrado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grado.class, id);
        } finally {
            em.close();
        }
    }

    public int getGradoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grado> rt = cq.from(Grado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
