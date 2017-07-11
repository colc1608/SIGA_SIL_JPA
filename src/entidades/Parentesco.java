/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author CesarLopez
 */
@Entity
@Table(name = "parentesco")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parentesco.findAll", query = "SELECT p FROM Parentesco p"),
    @NamedQuery(name = "Parentesco.findById", query = "SELECT p FROM Parentesco p WHERE p.id = :id"),
    @NamedQuery(name = "Parentesco.findByParentesco", query = "SELECT p FROM Parentesco p WHERE p.parentesco = :parentesco"),
    @NamedQuery(name = "Parentesco.findByObservacion", query = "SELECT p FROM Parentesco p WHERE p.observacion = :observacion")})
public class Parentesco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "parentesco")
    private String parentesco;
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "idApoderado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Apoderado idApoderado;
    @JoinColumn(name = "idAlumno", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Alumno idAlumno;

    public Parentesco() {
    }

    public Parentesco(Integer id) {
        this.id = id;
    }

    public Parentesco(Integer id, String parentesco) {
        this.id = id;
        this.parentesco = parentesco;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Apoderado getIdApoderado() {
        return idApoderado;
    }

    public void setIdApoderado(Apoderado idApoderado) {
        this.idApoderado = idApoderado;
    }

    public Alumno getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Alumno idAlumno) {
        this.idAlumno = idAlumno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parentesco)) {
            return false;
        }
        Parentesco other = (Parentesco) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Parentesco[ id=" + id + " ]";
    }
    
}
