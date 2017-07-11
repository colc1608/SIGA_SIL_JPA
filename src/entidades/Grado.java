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
@Table(name = "grado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grado.findAll", query = "SELECT g FROM Grado g"),
    @NamedQuery(name = "Grado.findById", query = "SELECT g FROM Grado g WHERE g.id = :id"),
    @NamedQuery(name = "Grado.findByNumeroGrado", query = "SELECT g FROM Grado g WHERE g.numeroGrado = :numeroGrado"),
    @NamedQuery(name = "Grado.findByEstado", query = "SELECT g FROM Grado g WHERE g.estado = :estado")})
public class Grado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "numeroGrado")
    private String numeroGrado;
    @Column(name = "estado")
    private Character estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGrado")
    private List<Cursoporgrado> cursoporgradoList;
    @JoinColumn(name = "idSeccion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seccion idSeccion;
    @JoinColumn(name = "idnivelEducacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Niveleducacion idnivelEducacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGrado")
    private List<Matricula> matriculaList;

    public Grado() {
    }

    public Grado(Integer id) {
        this.id = id;
    }

    public Grado(Integer id, String numeroGrado) {
        this.id = id;
        this.numeroGrado = numeroGrado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroGrado() {
        return numeroGrado;
    }

    public void setNumeroGrado(String numeroGrado) {
        this.numeroGrado = numeroGrado;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Cursoporgrado> getCursoporgradoList() {
        return cursoporgradoList;
    }

    public void setCursoporgradoList(List<Cursoporgrado> cursoporgradoList) {
        this.cursoporgradoList = cursoporgradoList;
    }

    public Seccion getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(Seccion idSeccion) {
        this.idSeccion = idSeccion;
    }

    public Niveleducacion getIdnivelEducacion() {
        return idnivelEducacion;
    }

    public void setIdnivelEducacion(Niveleducacion idnivelEducacion) {
        this.idnivelEducacion = idnivelEducacion;
    }

    @XmlTransient
    public List<Matricula> getMatriculaList() {
        return matriculaList;
    }

    public void setMatriculaList(List<Matricula> matriculaList) {
        this.matriculaList = matriculaList;
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
        if (!(object instanceof Grado)) {
            return false;
        }
        Grado other = (Grado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Grado[ id=" + id + " ]";
    }
    
}
