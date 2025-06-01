package com.banquito.sistema.originacion.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "Concesionarios")
public class Concesionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdConcesionario")
    private Long idConcesionario;

    @Column(name = "RazonSocial", nullable = false, length = 50)
    private String razonSocial;


    @Column(name = "Direccion", nullable = false, length = 120)
    private String direccion;


    @Column(name = "Telefono", length = 20)
    private String telefono;

    @Column(name = "EmailContacto", length = 50)
    private String emailContacto;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    @OneToMany(mappedBy  = "concesionario")
    private List<Vehiculo> vehiculos;

    @Version
    private Long version; 

    public Concesionario() {
    }

    public Concesionario(Long idConcesionario) {
        this.idConcesionario = idConcesionario;
    }
    
    public Long getIdConcesionario() {
        return idConcesionario;
    }

    public void setIdConcesionario(Long idConcesionario) {
        this.idConcesionario = idConcesionario;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getVersion() {
        return version;
    }

        public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concesionario that)) return false;
        return Objects.equals(idConcesionario, that.idConcesionario);
    }
    @Override
    public int hashCode() {
        return Objects.hash(idConcesionario);
    }
    @Override
    public String toString() {
        return "Concesionario{" +
                "idConcesionario=" + idConcesionario +
                ", razonSocial='" + razonSocial + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", emailContacto='" + emailContacto + '\'' +
                ", estado='" + estado + '\'' +
                ", version=" + version +
                '}';
    }


 
}