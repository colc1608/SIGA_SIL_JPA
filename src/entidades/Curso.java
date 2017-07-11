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
@Table(name = "curso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Curso.findAll", query = "SELECT c FROM Curso c"),
    @NamedQuery(name = "Curso.findById", query = "SELECT c FROM Curso c WHERE c.id = :id"),
    @NamedQuery(name = "Curso.findByNombreCorto", query = "SELECT c FROM Curso c WHERE c.nombreCorto = :nombreCorto"),
    @NamedQuery(name = "Curso.findByNombreLargo", query = "SELECT c FROM Curso c WHERE c.nombreLargo = :nombreLargo"),
    @NamedQuery(name = "Curso.findByHorasTecnicas", query = "SELECT c FROM Curso c WHERE c.horasTecnicas = :horasTecnicas"),
    @NamedQuery(name = "Curso.findByHorasPracticas", query = "SELECT c FROM Curso c WHERE c.horasPracticas = :horasPracticas"),
    @NamedQuery(name = "Curso.findByDescripcion", query = "SELECT c FROM Curso c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Curso.findByEstado", query = "SELECT c FROM Curso c WHERE c.estado = :estado")})
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombreCorto")
    private String nombreCorto;
    @Basic(optional = false)
    @Column(name = "nombreLargo")
    private String nombreLargo;
    @Basic(optional = false)
    @Column(name = "horasTecnicas")
    private int horasTecnicas;
    @Column(name = "horasPracticas")
    private Integer horasPracticas;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Character estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCurso")
    private List<Cursoporgrado> cursoporgradoList;

    public Curso() {
    }

    public Curso(Integer id) {
        this.id = id;
    }

    public Curso(Integer id, String nombreCorto, String nombreLargo, int horasTecnicas) {
        this.id = id;
        this.nombreCorto = nombreCorto;
        this.nombreLargo = nombreLargo;
        this.horasTecnicas = horasTecnicas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getNombreLargo() {
        return nombreLargo;
    }

    public void setNombreLargo(String nombreLargo) {
        this.nombreLargo = nombreLargo;
    }

    public int getHorasTecnicas() {
        return horasTecnicas;
    }

    public void setHorasTecnicas(int horasTecnicas) {
        this.horasTecnicas = horasTecnicas;
    }

    public Integer getHorasPracticas() {
        return horasPracticas;
    }

    public void setHorasPracticas(Integer horasPracticas) {
        this.horasPracticas = horasPracticas;
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

    @XmlTransient
    public List<Cursoporgrado> getCursoporgradoList() {
        return cursoporgradoList;
    }

    public void setCursoporgradoList(List<Cursoporgrado> cursoporgradoList) {
        this.cursoporgradoList = cursoporgradoList;
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
        if (!(object instanceof Curso)) {
            return false;
        }
        Curso other = (Curso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Curso[ id=" + id + " ]";
    }
    
}
