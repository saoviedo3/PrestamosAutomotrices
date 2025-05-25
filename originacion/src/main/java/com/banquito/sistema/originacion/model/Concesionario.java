package com.banquito.sistema.originacion.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "concesionarios")
public class Concesionario {

    @Id                              
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdConcesionario", nullable = false)
    private Long id;

    @Column(name = "RazonSocial")        
    private String RazonSocial;

    @Column(name = "Direccion")        // tu columna nombre
    private String Direccion;

    @Column(name = "Telefono")        // tu columna nombre
    private String Telefono;

    @Column(name = "EmailContacto")        // tu columna nombre
    private String EmailContacto;

    @Column(name = "Estado")        // tu columna nombre
    private String Estado;

    @Version
    private Long version;

    @OneToMany(mappedBy = "concesionario")
    private List<Vehiculo> vehiculos;
    
    public Concesionario() {
    }

    public Concesionario(Long id) {
        this.id = id;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getEmailContacto() {
        return EmailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        EmailContacto = emailContacto;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Concesionario other = (Concesionario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Concesionario [id=" + id + ", RazonSocial=" + RazonSocial + ", Direccion=" + Direccion + ", Telefono="
                + Telefono + ", EmailContacto=" + EmailContacto + ", Estado=" + Estado + ", version=" + version + "]";
    }

    
}