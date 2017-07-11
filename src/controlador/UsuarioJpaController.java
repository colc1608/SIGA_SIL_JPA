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
import entidades.Alumno;
import entidades.Constantes;
import java.util.ArrayList;
import java.util.List;
import entidades.Docente;
import entidades.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author CesarLopez
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getAlumnoList() == null) {
            usuario.setAlumnoList(new ArrayList<Alumno>());
        }
        if (usuario.getDocenteList() == null) {
            usuario.setDocenteList(new ArrayList<Docente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Alumno> attachedAlumnoList = new ArrayList<Alumno>();
            for (Alumno alumnoListAlumnoToAttach : usuario.getAlumnoList()) {
                alumnoListAlumnoToAttach = em.getReference(alumnoListAlumnoToAttach.getClass(), alumnoListAlumnoToAttach.getId());
                attachedAlumnoList.add(alumnoListAlumnoToAttach);
            }
            usuario.setAlumnoList(attachedAlumnoList);
            List<Docente> attachedDocenteList = new ArrayList<Docente>();
            for (Docente docenteListDocenteToAttach : usuario.getDocenteList()) {
                docenteListDocenteToAttach = em.getReference(docenteListDocenteToAttach.getClass(), docenteListDocenteToAttach.getId());
                attachedDocenteList.add(docenteListDocenteToAttach);
            }
            usuario.setDocenteList(attachedDocenteList);
            em.persist(usuario);
            for (Alumno alumnoListAlumno : usuario.getAlumnoList()) {
                Usuario oldIdUsuarioOfAlumnoListAlumno = alumnoListAlumno.getIdUsuario();
                alumnoListAlumno.setIdUsuario(usuario);
                alumnoListAlumno = em.merge(alumnoListAlumno);
                if (oldIdUsuarioOfAlumnoListAlumno != null) {
                    oldIdUsuarioOfAlumnoListAlumno.getAlumnoList().remove(alumnoListAlumno);
                    oldIdUsuarioOfAlumnoListAlumno = em.merge(oldIdUsuarioOfAlumnoListAlumno);
                }
            }
            for (Docente docenteListDocente : usuario.getDocenteList()) {
                Usuario oldIdUsuarioOfDocenteListDocente = docenteListDocente.getIdUsuario();
                docenteListDocente.setIdUsuario(usuario);
                docenteListDocente = em.merge(docenteListDocente);
                if (oldIdUsuarioOfDocenteListDocente != null) {
                    oldIdUsuarioOfDocenteListDocente.getDocenteList().remove(docenteListDocente);
                    oldIdUsuarioOfDocenteListDocente = em.merge(oldIdUsuarioOfDocenteListDocente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            List<Alumno> alumnoListOld = persistentUsuario.getAlumnoList();
            List<Alumno> alumnoListNew = usuario.getAlumnoList();
            List<Docente> docenteListOld = persistentUsuario.getDocenteList();
            List<Docente> docenteListNew = usuario.getDocenteList();
            List<String> illegalOrphanMessages = null;
            for (Alumno alumnoListOldAlumno : alumnoListOld) {
                if (!alumnoListNew.contains(alumnoListOldAlumno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alumno " + alumnoListOldAlumno + " since its idUsuario field is not nullable.");
                }
            }
            for (Docente docenteListOldDocente : docenteListOld) {
                if (!docenteListNew.contains(docenteListOldDocente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Docente " + docenteListOldDocente + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Alumno> attachedAlumnoListNew = new ArrayList<Alumno>();
            for (Alumno alumnoListNewAlumnoToAttach : alumnoListNew) {
                alumnoListNewAlumnoToAttach = em.getReference(alumnoListNewAlumnoToAttach.getClass(), alumnoListNewAlumnoToAttach.getId());
                attachedAlumnoListNew.add(alumnoListNewAlumnoToAttach);
            }
            alumnoListNew = attachedAlumnoListNew;
            usuario.setAlumnoList(alumnoListNew);
            List<Docente> attachedDocenteListNew = new ArrayList<Docente>();
            for (Docente docenteListNewDocenteToAttach : docenteListNew) {
                docenteListNewDocenteToAttach = em.getReference(docenteListNewDocenteToAttach.getClass(), docenteListNewDocenteToAttach.getId());
                attachedDocenteListNew.add(docenteListNewDocenteToAttach);
            }
            docenteListNew = attachedDocenteListNew;
            usuario.setDocenteList(docenteListNew);
            usuario = em.merge(usuario);
            for (Alumno alumnoListNewAlumno : alumnoListNew) {
                if (!alumnoListOld.contains(alumnoListNewAlumno)) {
                    Usuario oldIdUsuarioOfAlumnoListNewAlumno = alumnoListNewAlumno.getIdUsuario();
                    alumnoListNewAlumno.setIdUsuario(usuario);
                    alumnoListNewAlumno = em.merge(alumnoListNewAlumno);
                    if (oldIdUsuarioOfAlumnoListNewAlumno != null && !oldIdUsuarioOfAlumnoListNewAlumno.equals(usuario)) {
                        oldIdUsuarioOfAlumnoListNewAlumno.getAlumnoList().remove(alumnoListNewAlumno);
                        oldIdUsuarioOfAlumnoListNewAlumno = em.merge(oldIdUsuarioOfAlumnoListNewAlumno);
                    }
                }
            }
            for (Docente docenteListNewDocente : docenteListNew) {
                if (!docenteListOld.contains(docenteListNewDocente)) {
                    Usuario oldIdUsuarioOfDocenteListNewDocente = docenteListNewDocente.getIdUsuario();
                    docenteListNewDocente.setIdUsuario(usuario);
                    docenteListNewDocente = em.merge(docenteListNewDocente);
                    if (oldIdUsuarioOfDocenteListNewDocente != null && !oldIdUsuarioOfDocenteListNewDocente.equals(usuario)) {
                        oldIdUsuarioOfDocenteListNewDocente.getDocenteList().remove(docenteListNewDocente);
                        oldIdUsuarioOfDocenteListNewDocente = em.merge(oldIdUsuarioOfDocenteListNewDocente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Alumno> alumnoListOrphanCheck = usuario.getAlumnoList();
            for (Alumno alumnoListOrphanCheckAlumno : alumnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Alumno " + alumnoListOrphanCheckAlumno + " in its alumnoList field has a non-nullable idUsuario field.");
            }
            List<Docente> docenteListOrphanCheck = usuario.getDocenteList();
            for (Docente docenteListOrphanCheckDocente : docenteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Docente " + docenteListOrphanCheckDocente + " in its docenteList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }
    
    public Usuario validaLogin(Usuario user) {
        EntityManager em = getEntityManager();
        Usuario usuario = null;
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u where u.user = :user and u.clave = :clave ", Usuario.class);
            query.setParameter("user", user.getUser());
            query.setParameter("clave", user.getClave());
            return usuario = query.getSingleResult();
        }catch(NoResultException ex){
            return usuario;
        }finally {
            em.close();
        }
        
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
