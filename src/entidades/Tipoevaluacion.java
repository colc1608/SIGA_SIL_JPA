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
@Table(name = "tipoevaluacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipoevaluacion.findAll", query = "SELECT t FROM Tipoevaluacion t"),
    @NamedQuery(name = "Tipoevaluacion.findById", query = "SELECT t FROM Tipoevaluacion t WHERE t.id = :id"),
    @NamedQuery(name = "Tipoevaluacion.findByDescripcion", query = "SELECT t FROM Tipoevaluacion t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Tipoevaluacion.findByPeso", query = "SELECT t FROM Tipoevaluacion t WHERE t.peso = :peso"),
    @NamedQuery(name = "Tipoevaluacion.findByObservacion", query = "SELECT t FROM Tipoevaluacion t WHERE t.observacion = :observacion"),
    @NamedQuery(name = "Tipoevaluacion.findByEstado", query = "SELECT t FROM Tipoevaluacion t WHERE t.estado = :estado")})
public class Tipoevaluacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "peso")
    private double peso;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "estado")
    private Character estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoEvaluacion")
    private List<Nota> notaList;

    public Tipoevaluacion() {
    }

    public Tipoevaluacion(Integer id) {
        this.id = id;
    }

    public Tipoevaluacion(Integer id, String descripcion, double peso) {
        this.id = id;
        this.descripcion = descripcion;
        this.peso = peso;
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

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoevaluacion)) {
            return false;
        }
        Tipoevaluacion other = (Tipoevaluacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Tipoevaluacion[ id=" + id + " ]";
    }
    
}
