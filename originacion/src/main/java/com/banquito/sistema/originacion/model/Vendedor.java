package com.banquito.sistema.originacion.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "Vendedores")
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdVendedor")
    private Long id;

    @Column(name = "IdConcesionario", nullable = false)
    private Long idConcesionario;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    @Column(name = "Email", length = 60)
    private String email;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    @Version
    @Column(name = "Version")
    private Long version;

    //relacion concesionario
    @ManyToOne
    @JoinColumn(name = "IdConcesionario", referencedColumnName = "IdConcesionario", insertable = false, updatable = false)
    @JsonIgnoreProperties({
        "direccion",
        "telefono",
        "emailContacto",
        "version"
    })
    private Concesionario concesionario;


    public Vendedor() {
    }
    public Vendedor(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getIdConcesionario() {
        return idConcesionario;
    }
    public void setIdConcesionario(Long idConcesionario) {
        this.idConcesionario = idConcesionario;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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
    public void setVersion(Long version) {
        this.version = version;
    }
    public Concesionario getConcesionario() {
        return concesionario;
    }
    public void setConcesionario(Concesionario concesionario) {
        this.concesionario = concesionario;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vendedor)) return false;
        Vendedor vendedor = (Vendedor) o;
        return Objects.equals(id, vendedor.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public String toString() {
        return "Vendedor{" +
                "id=" + id +
                ", idConcesionario=" + idConcesionario +
                ", nombres='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", estado='" + estado + '\'' +
                ", version=" + version +
                ", concesionario=" + (concesionario != null ? concesionario.getIdConcesionario() : null) +
                '}';
    }
}
