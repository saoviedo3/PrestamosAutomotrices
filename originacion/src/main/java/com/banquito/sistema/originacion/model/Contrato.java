package com.banquito.sistema.originacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;

@Entity
@Table(name = "Contratos")
public class Contrato {

    @Id
    @Column(name = "IdContrato", nullable = false)
    private Integer idContrato;

    @Column(name = "IdSolicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "RutaArchivo", length = 150, nullable = false)
    private String rutaArchivo;

    @Column(name = "FechaGenerado", nullable = false)
    private LocalDateTime fechaGenerado;

    @Column(name = "FechaFirma", nullable = false)
    private LocalDateTime fechaFirma;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "CondicionEspecial", length = 120)
    private String condicionEspecial;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "IdSolicitud", referencedColumnName = "IdSolicitud")
    private SolicitudCredito solicitudCredito;

    public Contrato() {
    }

    public Contrato(Integer idContrato) {
        this.idContrato = idContrato;
    }

    public Integer getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Integer idContrato) {
        this.idContrato = idContrato;
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public java.time.LocalDateTime getFechaGenerado() {
        return fechaGenerado;
    }

    public void setFechaGenerado(java.time.LocalDateTime fechaGenerado) {
        this.fechaGenerado = fechaGenerado;
    }

    public java.time.LocalDateTime getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(java.time.LocalDateTime fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCondicionEspecial() {
        return condicionEspecial;
    }

    public void setCondicionEspecial(String condicionEspecial) {
        this.condicionEspecial = condicionEspecial;
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
        result = prime * result + ((idContrato == null) ? 0 : idContrato.hashCode());
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
        Contrato other = (Contrato) obj;
        if (idContrato == null) {
            if (other.idContrato != null)
                return false;
        } else if (!idContrato.equals(other.idContrato))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Contrato [idContrato=" + idContrato + ", idSolicitud=" + idSolicitud + ", rutaArchivo=" + rutaArchivo
                + ", fechaGenerado=" + fechaGenerado + ", fechaFirma=" + fechaFirma + ", estado=" + estado
                + ", condicionEspecial=" + condicionEspecial + ", version=" + version + ", solicitudCredito="
                + solicitudCredito + "]";
    }
} 