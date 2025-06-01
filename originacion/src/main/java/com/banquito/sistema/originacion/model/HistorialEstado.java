package com.banquito.sistema.originacion.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "HistorialEstados")
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdHistorialEstado")
    private Long id;

    @Column(name = "IdSolicitud", nullable = false)
    private Long idSolicitud;

    @Column(name = "Estado", nullable = false, length = 12)
    private String estado;

    @Column(name = "FechaHora", nullable = false)
    private Timestamp fechaHora;

    @Column(name = "Usuario", nullable = false, length = 100)
    private String usuario;

    @Column(name = "Motivo", length = 500)
    private String motivo;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "IdSolicitud", insertable = false, updatable = false)
    @JsonIgnoreProperties({
        "clienteProspecto",
        "vehiculo",
        "vendedor",
        "montoSolicitado",
        "plazoMeses",
        "fechaSolicitud",
        "scoreInterno",
        "scoreExterno",
        "relacionCuotaIngreso",
        "tasaAnual",
        "cuotaMensual",
        "totalPagar",
        "estado",
        "entrada",
        "version"
    })
    private SolicitudCredito solicitudCredito;

    public HistorialEstado() {
 
    }

    public HistorialEstado(Long id) {
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

    

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
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
        HistorialEstado other = (HistorialEstado) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "HistorialEstado [id=" + id + ", idSolicitud=" + idSolicitud + ", estado=" + estado + ", fechaHora="
                + fechaHora + ", usuario=" + usuario + ", motivo=" + motivo + ", version=" + version + "]";
    }

}