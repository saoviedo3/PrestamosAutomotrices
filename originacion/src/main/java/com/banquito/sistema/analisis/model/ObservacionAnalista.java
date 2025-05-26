package com.banquito.sistema.analisis.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.banquito.sistema.originacion.model.SolicitudCredito;

@Entity
@Table(name = "ObservacionAnalistas")
public class ObservacionAnalista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdObservacionAnalista", nullable = false)
    private Integer id;

    @NotNull(message = "El ID de la solicitud es obligatorio")
    @Column(name = "IdSolicitud", nullable = false)
    private Long idSolicitud;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario debe tener máximo 50 caracteres")
    @Column(name = "Usuario", length = 50, nullable = false)
    private String usuario;

    @Column(name = "FechaHora", nullable = false)
    private LocalDateTime fechaHora;

    @NotBlank(message = "La razón de intervención es obligatoria")
    @Size(max = 500, message = "La razón de intervención debe tener máximo 500 caracteres")
    @Column(name = "RazonIntervencion", length = 500, nullable = false)
    private String razonIntervencion;

    @Version
    @Column(name = "Version", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "IdSolicitud", referencedColumnName = "IdSolicitud", insertable = false, updatable = false)
    private SolicitudCredito solicitudCredito;

    public ObservacionAnalista() {
    }

    public ObservacionAnalista(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getRazonIntervencion() {
        return razonIntervencion;
    }

    public void setRazonIntervencion(String razonIntervencion) {
        this.razonIntervencion = razonIntervencion;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public SolicitudCredito getSolicitudCredito() {
        return solicitudCredito;
    }

    public void setSolicitudCredito(SolicitudCredito solicitudCredito) {
        this.solicitudCredito = solicitudCredito;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "ObservacionAnalista [id=" + id + ", idSolicitud=" + idSolicitud + ", usuario=" + usuario
                + ", fechaHora=" + fechaHora + "]";
    }
}
