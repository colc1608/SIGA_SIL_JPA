/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author CesarLopez
 */
@Entity
@Table(name = "clase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clase.findAll", query = "SELECT c FROM Clase c"),
    @NamedQuery(name = "Clase.findById", query = "SELECT c FROM Clase c WHERE c.id = :id"),
    @NamedQuery(name = "Clase.findByCantidadAlumnos", query = "SELECT c FROM Clase c WHERE c.cantidadAlumnos = :cantidadAlumnos"),
    @NamedQuery(name = "Clase.findByObservacion", query = "SELECT c FROM Clase c WHERE c.observacion = :observacion"),
    @NamedQuery(name = "Clase.findByEstado", query = "SELECT c FROM Clase c WHERE c.estado = :estado")})
public class Clase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "cantidadAlumnos")
    private int cantidadAlumnos;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "estado")
    private Character estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idClase")
    private List<Nota> notaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idClase")
    private List<Detallematricula> detallematriculaList;
    @JoinColumn(name = "idDocente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Docente idDocente;
    @JoinColumn(name = "idCursoPorGrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cursoporgrado idCursoPorGrado;

    public Clase() {
    }

    public Clase(Integer id) {
        this.id = id;
    }

    public Clase(Integer id, int cantidadAlumnos) {
        this.id = id;
        this.cantidadAlumnos = cantidadAlumnos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCantidadAlumnos() {
        return cantidadAlumnos;
    }

    public void setCantidadAlumnos(int cantidadAlumnos) {
        this.cantidadAlumnos = cantidadAlumnos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Nota> getNotaList() {
        return notaList;
    }

    public void setNotaList(List<Nota> notaList) {
        this.notaList = notaList;
    }

    @XmlTransient
    public List<Detallematricula> getDetallematriculaList() {
        return detallematriculaList;
    }

    public void setDetallematriculaList(List<Detallematricula> detallematriculaList) {
        this.detallematriculaList = detallematriculaList;
    }

    public Docente getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Docente idDocente) {
        this.idDocente = idDocente;
    }

    public Cursoporgrado getIdCursoPorGrado() {
        return idCursoPorGrado;
    }

    public void setIdCursoPorGrado(Cursoporgrado idCursoPorGrado) {
        this.idCursoPorGrado = idCursoPorGrado;
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
        if (!(object instanceof Clase)) {
            return false;
        }
        Clase other = (Clase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Clase[ id=" + id + " ]";
    }
    
}
