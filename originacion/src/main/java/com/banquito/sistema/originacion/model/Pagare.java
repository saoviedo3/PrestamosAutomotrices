package com.banquito.sistema.originacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "Pagares")
public class Pagare {

    @Id
    @Column(name = "IdPagare", nullable = false)
    private Integer idPagare;

    @Column(name = "IdSolicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "NumeroCuota", nullable = false)
    private Integer numeroCuota;

    @Column(name = "RutaArchivo", length = 150, nullable = false)
    private String rutaArchivo;

    @Column(name = "FechaGenerado", nullable = false)
    private java.time.LocalDateTime fechaGenerado;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "IdSolicitud", referencedColumnName = "IdSolicitud")
    private SolicitudCredito solicitudCredito;

    public Pagare() {
    }

    public Pagare(Integer idPagare) {
        this.idPagare = idPagare;
    }

    public Integer getIdPagare() {
        return idPagare;
    }

    public void setIdPagare(Integer idPagare) {
        this.idPagare = idPagare;
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Integer getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(Integer numeroCuota) {
        this.numeroCuota = numeroCuota;
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
        result = prime * result + ((idPagare == null) ? 0 : idPagare.hashCode());
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
        Pagare other = (Pagare) obj;
        if (idPagare == null) {
            if (other.idPagare != null)
                return false;
        } else if (!idPagare.equals(other.idPagare))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Pagare [idPagare=" + idPagare + ", idSolicitud=" + idSolicitud + ", numeroCuota=" + numeroCuota
                + ", rutaArchivo=" + rutaArchivo + ", fechaGenerado=" + fechaGenerado + ", version=" + version
                + ", solicitudCredito=" + solicitudCredito + "]";
    }
} 