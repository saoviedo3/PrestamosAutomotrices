package com.banquito.sistema.analisis.model;

import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ObservacionAnalistas")
public class ObservacionAnalista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdObservacionAnalista", nullable = false)
    private Long id;

    @NotNull(message = "El ID de la solicitud es obligatorio")
    @Column(name = "IdSolicitud", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
    @JsonIgnoreProperties({
        "clienteProspecto", "vehiculo", "vendedor", "montoSolicitado", "plazoMeses", "fechaSolicitud",
        "scoreInterno", "scoreExterno", "relacionCuotaIngreso", "tasaAnual", "cuotaMensual", "totalPagar",
        "entrada", "version"
    })
    private SolicitudCredito solicitudCredito;

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
                + ", fechaHora=" + fechaHora + ", razonIntervencion=" + razonIntervencion + ", version=" + version + "]";
    }
}
