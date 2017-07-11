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
@Table(name = "cursoporgrado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cursoporgrado.findAll", query = "SELECT c FROM Cursoporgrado c"),
    @NamedQuery(name = "Cursoporgrado.findById", query = "SELECT c FROM Cursoporgrado c WHERE c.id = :id"),
    @NamedQuery(name = "Cursoporgrado.findByDescripcion", query = "SELECT c FROM Cursoporgrado c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Cursoporgrado.findByEstado", query = "SELECT c FROM Cursoporgrado c WHERE c.estado = :estado")})
public class Cursoporgrado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Character estado;
    @JoinColumn(name = "idCurso", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Curso idCurso;
    @JoinColumn(name = "idGrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Grado idGrado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCursoPorGrado")
    private List<Clase> claseList;

    public Cursoporgrado() {
    }

    public Cursoporgrado(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Curso getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Curso idCurso) {
        this.idCurso = idCurso;
    }

    public Grado getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(Grado idGrado) {
        this.idGrado = idGrado;
    }

    @XmlTransient
    public List<Clase> getClaseList() {
        return claseList;
    }

    public void setClaseList(List<Clase> claseList) {
        this.claseList = claseList;
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
        if (!(object instanceof Cursoporgrado)) {
            return false;
        }
        Cursoporgrado other = (Cursoporgrado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Cursoporgrado[ id=" + id + " ]";
    }
    
}
