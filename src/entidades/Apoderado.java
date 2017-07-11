/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author CesarLopez
 */
@Entity
@Table(name = "apoderado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Apoderado.findAll", query = "SELECT a FROM Apoderado a"),
    @NamedQuery(name = "Apoderado.findById", query = "SELECT a FROM Apoderado a WHERE a.id = :id"),
    @NamedQuery(name = "Apoderado.findByNombre", query = "SELECT a FROM Apoderado a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Apoderado.findByApellidoPaterno", query = "SELECT a FROM Apoderado a WHERE a.apellidoPaterno = :apellidoPaterno"),
    @NamedQuery(name = "Apoderado.findByApellidoMaterno", query = "SELECT a FROM Apoderado a WHERE a.apellidoMaterno = :apellidoMaterno"),
    @NamedQuery(name = "Apoderado.findByDni", query = "SELECT a FROM Apoderado a WHERE a.dni = :dni"),
    @NamedQuery(name = "Apoderado.findByTelefono", query = "SELECT a FROM Apoderado a WHERE a.telefono = :telefono"),
    @NamedQuery(name = "Apoderado.findByMovil", query = "SELECT a FROM Apoderado a WHERE a.movil = :movil"),
    @NamedQuery(name = "Apoderado.findByFechaNacimiento", query = "SELECT a FROM Apoderado a WHERE a.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Apoderado.findByEmail", query = "SELECT a FROM Apoderado a WHERE a.email = :email"),
    @NamedQuery(name = "Apoderado.findByEstado", query = "SELECT a FROM Apoderado a WHERE a.estado = :estado")})
public class Apoderado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "apellidoPaterno")
    private String apellidoPaterno;
    @Basic(optional = false)
    @Column(name = "apellidoMaterno")
    private String apellidoMaterno;
    @Basic(optional = false)
    @Column(name = "DNI")
    private String dni;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "movil")
    private String movil;
    @Column(name = "fechaNacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "email")
    private String email;
    @Column(name = "estado")
    private Character estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idApoderado")
    private List<Parentesco> parentescoList;

    public Apoderado() {
    }

    public Apoderado(Integer id) {
        this.id = id;
    }

    public Apoderado(Integer id, String nombre, String apellidoPaterno, String apellidoMaterno, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.dni = dni;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Parentesco> getParentescoList() {
        return parentescoList;
    }

    public void setParentescoList(List<Parentesco> parentescoList) {
        this.parentescoList = parentescoList;
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
        if (!(object instanceof Apoderado)) {
            return false;
        }
        Apoderado other = (Apoderado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Apoderado[ id=" + id + " ]";
    }
    
}
