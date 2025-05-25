package com.banquito.sistema.originacion.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ObservacionAnalistas")
public class ObservacionAnalista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdObservacionAnalista", nullable = false)
    private Long id;

    @Column(name = "EntidadTipo", length = 50, nullable = false)
    private String entidadTipo;

    @Column(name = "EntidadId", nullable = false)
    private Long entidadId;

    @Column(name = "Observacion", length = 1000, nullable = false)
    private String observacion;

    @Column(name = "UsuarioAnalista", length = 100, nullable = false)
    private String usuarioAnalista;

    @Column(name = "FechaObservacion", nullable = false)
    private LocalDateTime fechaObservacion;

    @Column(name = "TipoObservacion", length = 50, nullable = false)
    private String tipoObservacion;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    public ObservacionAnalista() {
    }

    public ObservacionAnalista(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntidadTipo() {
        return entidadTipo;
    }

    public void setEntidadTipo(String entidadTipo) {
        this.entidadTipo = entidadTipo;
    }

    public Long getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(Long entidadId) {
        this.entidadId = entidadId;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuarioAnalista() {
        return usuarioAnalista;
    }

    public void setUsuarioAnalista(String usuarioAnalista) {
        this.usuarioAnalista = usuarioAnalista;
    }

    public LocalDateTime getFechaObservacion() {
        return fechaObservacion;
    }

    public void setFechaObservacion(LocalDateTime fechaObservacion) {
        this.fechaObservacion = fechaObservacion;
    }

    public String getTipoObservacion() {
        return tipoObservacion;
    }

    public void setTipoObservacion(String tipoObservacion) {
        this.tipoObservacion = tipoObservacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
        ObservacionAnalista other = (ObservacionAnalista) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ObservacionAnalista [id=" + id + ", entidadTipo=" + entidadTipo + 
               ", entidadId=" + entidadId + ", observacion=" + observacion + 
               ", usuarioAnalista=" + usuarioAnalista + ", fechaObservacion=" + fechaObservacion + 
               ", tipoObservacion=" + tipoObservacion + ", estado=" + estado + "]";
    }
} 