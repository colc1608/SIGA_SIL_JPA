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
@Table(name = "niveleducacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Niveleducacion.findAll", query = "SELECT n FROM Niveleducacion n"),
    @NamedQuery(name = "Niveleducacion.findById", query = "SELECT n FROM Niveleducacion n WHERE n.id = :id"),
    @NamedQuery(name = "Niveleducacion.findByNombreCorto", query = "SELECT n FROM Niveleducacion n WHERE n.nombreCorto = :nombreCorto"),
    @NamedQuery(name = "Niveleducacion.findByNombreLargo", query = "SELECT n FROM Niveleducacion n WHERE n.nombreLargo = :nombreLargo"),
    @NamedQuery(name = "Niveleducacion.findByEstado", query = "SELECT n FROM Niveleducacion n WHERE n.estado = :estado")})
public class Niveleducacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombreCorto")
    private String nombreCorto;
    @Column(name = "nombreLargo")
    private String nombreLargo;
    @Column(name = "estado")
    private Character estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idnivelEducacion")
    private List<Grado> gradoList;

    public Niveleducacion() {
    }

    public Niveleducacion(Integer id) {
        this.id = id;
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

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Grado> getGradoList() {
        return gradoList;
    }

    public void setGradoList(List<Grado> gradoList) {
        this.gradoList = gradoList;
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
        if (!(object instanceof Niveleducacion)) {
            return false;
        }
        Niveleducacion other = (Niveleducacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Niveleducacion[ id=" + id + " ]";
    }
    
}
