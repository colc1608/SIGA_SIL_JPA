/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entidades.Alumno;
import entidades.Constantes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Usuario;
import entidades.Parentesco;
import java.util.ArrayList;
import java.util.List;
import entidades.Nota;
import entidades.Matricula;
import java.sql.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author CesarLopez
 */
public class AlumnoJpaController implements Serializable {

    public AlumnoJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.PERSISTENCE_NAME);
        
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alumno alumno) {
        if (alumno.getParentescoList() == null) {
            alumno.setParentescoList(new ArrayList<Parentesco>());
        }
        if (alumno.getNotaList() == null) {
            alumno.setNotaList(new ArrayList<Nota>());
        }
        if (alumno.getMatriculaList() == null) {
            alumno.setMatriculaList(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idUsuario = alumno.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getId());
                alumno.setIdUsuario(idUsuario);
            }
            List<Parentesco> attachedParentescoList = new ArrayList<Parentesco>();
            for (Parentesco parentescoListParentescoToAttach : alumno.getParentescoList()) {
                parentescoListParentescoToAttach = em.getReference(parentescoListParentescoToAttach.getClass(), parentescoListParentescoToAttach.getId());
                attachedParentescoList.add(parentescoListParentescoToAttach);
            }
            alumno.setParentescoList(attachedParentescoList);
            List<Nota> attachedNotaList = new ArrayList<Nota>();
            for (Nota notaListNotaToAttach : alumno.getNotaList()) {
                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getId());
                attachedNotaList.add(notaListNotaToAttach);
            }
            alumno.setNotaList(attachedNotaList);
            List<Matricula> attachedMatriculaList = new ArrayList<Matricula>();
            for (Matricula matriculaListMatriculaToAttach : alumno.getMatriculaList()) {
                matriculaListMatriculaToAttach = em.getReference(matriculaListMatriculaToAttach.getClass(), matriculaListMatriculaToAttach.getId());
                attachedMatriculaList.add(matriculaListMatriculaToAttach);
            }
            alumno.setMatriculaList(attachedMatriculaList);
            em.persist(alumno);
            if (idUsuario != null) {
                idUsuario.getAlumnoList().add(alumno);
                idUsuario = em.merge(idUsuario);
            }
            for (Parentesco parentescoListParentesco : alumno.getParentescoList()) {
                Alumno oldIdAlumnoOfParentescoListParentesco = parentescoListParentesco.getIdAlumno();
                parentescoListParentesco.setIdAlumno(alumno);
                parentescoListParentesco = em.merge(parentescoListParentesco);
                if (oldIdAlumnoOfParentescoListParentesco != null) {
                    oldIdAlumnoOfParentescoListParentesco.getParentescoList().remove(parentescoListParentesco);
                    oldIdAlumnoOfParentescoListParentesco = em.merge(oldIdAlumnoOfParentescoListParentesco);
                }
            }
            for (Nota notaListNota : alumno.getNotaList()) {
                Alumno oldIdAlumnoOfNotaListNota = notaListNota.getIdAlumno();
                notaListNota.setIdAlumno(alumno);
                notaListNota = em.merge(notaListNota);
                if (oldIdAlumnoOfNotaListNota != null) {
                    oldIdAlumnoOfNotaListNota.getNotaList().remove(notaListNota);
                    oldIdAlumnoOfNotaListNota = em.merge(oldIdAlumnoOfNotaListNota);
                }
            }
            for (Matricula matriculaListMatricula : alumno.getMatriculaList()) {
                Alumno oldIdAlumnoOfMatriculaListMatricula = matriculaListMatricula.getIdAlumno();
                matriculaListMatricula.setIdAlumno(alumno);
                matriculaListMatricula = em.merge(matriculaListMatricula);
                if (oldIdAlumnoOfMatriculaListMatricula != null) {
                    oldIdAlumnoOfMatriculaListMatricula.getMatriculaList().remove(matriculaListMatricula);
                    oldIdAlumnoOfMatriculaListMatricula = em.merge(oldIdAlumnoOfMatriculaListMatricula);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alumno alumno) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno persistentAlumno = em.find(Alumno.class, alumno.getId());
            Usuario idUsuarioOld = persistentAlumno.getIdUsuario();
            Usuario idUsuarioNew = alumno.getIdUsuario();
            List<Parentesco> parentescoListOld = persistentAlumno.getParentescoList();
            List<Parentesco> parentescoListNew = alumno.getParentescoList();
            List<Nota> notaListOld = persistentAlumno.getNotaList();
            List<Nota> notaListNew = alumno.getNotaList();
            List<Matricula> matriculaListOld = persistentAlumno.getMatriculaList();
            List<Matricula> matriculaListNew = alumno.getMatriculaList();
            List<String> illegalOrphanMessages = null;
            for (Parentesco parentescoListOldParentesco : parentescoListOld) {
                if (!parentescoListNew.contains(parentescoListOldParentesco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Parentesco " + parentescoListOldParentesco + " since its idAlumno field is not nullable.");
                }
            }
            for (Nota notaListOldNota : notaListOld) {
                if (!notaListNew.contains(notaListOldNota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Nota " + notaListOldNota + " since its idAlumno field is not nullable.");
                }
            }
            for (Matricula matriculaListOldMatricula : matriculaListOld) {
                if (!matriculaListNew.contains(matriculaListOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaListOldMatricula + " since its idAlumno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getId());
                alumno.setIdUsuario(idUsuarioNew);
            }
            List<Parentesco> attachedParentescoListNew = new ArrayList<Parentesco>();
            for (Parentesco parentescoListNewParentescoToAttach : parentescoListNew) {
                parentescoListNewParentescoToAttach = em.getReference(parentescoListNewParentescoToAttach.getClass(), parentescoListNewParentescoToAttach.getId());
                attachedParentescoListNew.add(parentescoListNewParentescoToAttach);
            }
            parentescoListNew = attachedParentescoListNew;
            alumno.setParentescoList(parentescoListNew);
            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
            for (Nota notaListNewNotaToAttach : notaListNew) {
                notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getId());
                attachedNotaListNew.add(notaListNewNotaToAttach);
            }
            notaListNew = attachedNotaListNew;
            alumno.setNotaList(notaListNew);
            List<Matricula> attachedMatriculaListNew = new ArrayList<Matricula>();
            for (Matricula matriculaListNewMatriculaToAttach : matriculaListNew) {
                matriculaListNewMatriculaToAttach = em.getReference(matriculaListNewMatriculaToAttach.getClass(), matriculaListNewMatriculaToAttach.getId());
                attachedMatriculaListNew.add(matriculaListNewMatriculaToAttach);
            }
            matriculaListNew = attachedMatriculaListNew;
            alumno.setMatriculaList(matriculaListNew);
            alumno = em.merge(alumno);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getAlumnoList().remove(alumno);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getAlumnoList().add(alumno);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (Parentesco parentescoListNewParentesco : parentescoListNew) {
                if (!parentescoListOld.contains(parentescoListNewParentesco)) {
                    Alumno oldIdAlumnoOfParentescoListNewParentesco = parentescoListNewParentesco.getIdAlumno();
                    parentescoListNewParentesco.setIdAlumno(alumno);
                    parentescoListNewParentesco = em.merge(parentescoListNewParentesco);
                    if (oldIdAlumnoOfParentescoListNewParentesco != null && !oldIdAlumnoOfParentescoListNewParentesco.equals(alumno)) {
                        oldIdAlumnoOfParentescoListNewParentesco.getParentescoList().remove(parentescoListNewParentesco);
                        oldIdAlumnoOfParentescoListNewParentesco = em.merge(oldIdAlumnoOfParentescoListNewParentesco);
                    }
                }
            }
            for (Nota notaListNewNota : notaListNew) {
                if (!notaListOld.contains(notaListNewNota)) {
                    Alumno oldIdAlumnoOfNotaListNewNota = notaListNewNota.getIdAlumno();
                    notaListNewNota.setIdAlumno(alumno);
                    notaListNewNota = em.merge(notaListNewNota);
                    if (oldIdAlumnoOfNotaListNewNota != null && !oldIdAlumnoOfNotaListNewNota.equals(alumno)) {
                        oldIdAlumnoOfNotaListNewNota.getNotaList().remove(notaListNewNota);
                        oldIdAlumnoOfNotaListNewNota = em.merge(oldIdAlumnoOfNotaListNewNota);
                    }
                }
            }
            for (Matricula matriculaListNewMatricula : matriculaListNew) {
                if (!matriculaListOld.contains(matriculaListNewMatricula)) {
                    Alumno oldIdAlumnoOfMatriculaListNewMatricula = matriculaListNewMatricula.getIdAlumno();
                    matriculaListNewMatricula.setIdAlumno(alumno);
                    matriculaListNewMatricula = em.merge(matriculaListNewMatricula);
                    if (oldIdAlumnoOfMatriculaListNewMatricula != null && !oldIdAlumnoOfMatriculaListNewMatricula.equals(alumno)) {
                        oldIdAlumnoOfMatriculaListNewMatricula.getMatriculaList().remove(matriculaListNewMatricula);
                        oldIdAlumnoOfMatriculaListNewMatricula = em.merge(oldIdAlumnoOfMatriculaListNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alumno.getId();
                if (findAlumno(id) == null) {
                    throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.");
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
            Alumno alumno;
            try {
                alumno = em.getReference(Alumno.class, id);
                alumno.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Parentesco> parentescoListOrphanCheck = alumno.getParentescoList();
            for (Parentesco parentescoListOrphanCheckParentesco : parentescoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Parentesco " + parentescoListOrphanCheckParentesco + " in its parentescoList field has a non-nullable idAlumno field.");
            }
            List<Nota> notaListOrphanCheck = alumno.getNotaList();
            for (Nota notaListOrphanCheckNota : notaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Nota " + notaListOrphanCheckNota + " in its notaList field has a non-nullable idAlumno field.");
            }
            List<Matricula> matriculaListOrphanCheck = alumno.getMatriculaList();
            for (Matricula matriculaListOrphanCheckMatricula : matriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Matricula " + matriculaListOrphanCheckMatricula + " in its matriculaList field has a non-nullable idAlumno field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario idUsuario = alumno.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getAlumnoList().remove(alumno);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(alumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alumno> findAlumnoEntities() {
        return findAlumnoEntities(true, -1, -1);
    }

    public List<Alumno> findAlumnoEntities(int maxResults, int firstResult) {
        return findAlumnoEntities(false, maxResults, firstResult);
    }

    private List<Alumno> findAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumno.class));
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

    public Alumno findAlumno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumno> rt = cq.from(Alumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public boolean createAlumno(Alumno alumno){
        EntityManager em = getEntityManager();
        try{
        // Create call stored procedure
        em.getTransaction().begin();
        StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("sp_addAlumno");
        // set parameters
        storedProcedure.registerStoredProcedureParameter(0, String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter(7, Date.class, ParameterMode.IN);
        
        storedProcedure.setParameter(0, alumno.getNombre());
        storedProcedure.setParameter(1, alumno.getApellidoPaterno());
        storedProcedure.setParameter(2, alumno.getApellidoPaterno());
        storedProcedure.setParameter(3, alumno.getDni());
        storedProcedure.setParameter(4, alumno.getTelefono());
        storedProcedure.setParameter(5, alumno.getMovil());
        storedProcedure.setParameter(6, alumno.getEmail());
        storedProcedure.setParameter(7, new java.sql.Date(alumno.getFechadenacimiento().getTime()));
        // execute SP
        storedProcedure.execute();
        
        em.getTransaction().commit();
        em.close();
        return true;
        } catch (Exception e) {
            System.out.println("createAlumno : getMessage --> " + e.getMessage());
            return false;
        }

    }
    
}
