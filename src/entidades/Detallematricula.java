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
@Table(name = "detallematricula")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallematricula.findAll", query = "SELECT d FROM Detallematricula d"),
    @NamedQuery(name = "Detallematricula.findById", query = "SELECT d FROM Detallematricula d WHERE d.id = :id"),
    @NamedQuery(name = "Detallematricula.findByEstado", query = "SELECT d FROM Detallematricula d WHERE d.estado = :estado")})
public class Detallematricula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "estado")
    private Character estado;
    @JoinColumn(name = "idClase", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Clase idClase;
    @JoinColumn(name = "idMatricula", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Matricula idMatricula;

    public Detallematricula() {
    }

    public Detallematricula(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Clase getIdClase() {
        return idClase;
    }

    public void setIdClase(Clase idClase) {
        this.idClase = idClase;
    }

    public Matricula getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(Matricula idMatricula) {
        this.idMatricula = idMatricula;
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
        if (!(object instanceof Detallematricula)) {
            return false;
        }
        Detallematricula other = (Detallematricula) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Detallematricula[ id=" + id + " ]";
    }
    
}
