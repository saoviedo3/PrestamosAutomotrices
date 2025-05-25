package com.banquito.sistema.originacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "TiposDocumentos")
public class TipoDocumento {

    @Id
    @Column(name = "IdTipoDocumento", nullable = false)
    private Integer idTipoDocumento;

    @Column(name = "Nombre", length = 40, nullable = false)
    private String nombre;

    @Column(name = "Descripcion", length = 150, nullable = false)
    private String descripcion;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    @Version
    private Long version;

    public TipoDocumento() {
    }

    public TipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idTipoDocumento == null) ? 0 : idTipoDocumento.hashCode());
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
        TipoDocumento other = (TipoDocumento) obj;
        if (idTipoDocumento == null) {
            if (other.idTipoDocumento != null)
                return false;
        } else if (!idTipoDocumento.equals(other.idTipoDocumento))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TipoDocumento [idTipoDocumento=" + idTipoDocumento + ", nombre=" + nombre + ", descripcion="
                + descripcion + ", estado=" + estado + ", version=" + version + "]";
    }
} 