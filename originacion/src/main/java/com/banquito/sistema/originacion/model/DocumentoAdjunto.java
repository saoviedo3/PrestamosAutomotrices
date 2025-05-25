package com.banquito.sistema.originacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "DocumentosAdjuntos")
public class DocumentoAdjunto {

    @Id
    @Column(name = "IdDocumento", nullable = false)
    private Integer idDocumento;

    @Column(name = "IdSolicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "IdTipoDocumento", nullable = false)
    private Integer idTipoDocumento;

    @Column(name = "RutaArchivo", length = 150, nullable = false)
    private String rutaArchivo;

    @Column(name = "FechaCargado", nullable = false)
    private java.time.LocalDateTime fechaCargado;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "IdSolicitud", referencedColumnName = "IdSolicitud")
    private SolicitudCredito solicitudCredito;

    @ManyToOne
    @JoinColumn(name = "IdTipoDocumento", referencedColumnName = "IdTipoDocumento")
    private TipoDocumento tipoDocumento;

    public DocumentoAdjunto() {
    }

    public DocumentoAdjunto(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Integer getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public java.time.LocalDateTime getFechaCargado() {
        return fechaCargado;
    }

    public void setFechaCargado(java.time.LocalDateTime fechaCargado) {
        this.fechaCargado = fechaCargado;
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

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idDocumento == null) ? 0 : idDocumento.hashCode());
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
        DocumentoAdjunto other = (DocumentoAdjunto) obj;
        if (idDocumento == null) {
            if (other.idDocumento != null)
                return false;
        } else if (!idDocumento.equals(other.idDocumento))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DocumentoAdjunto [idDocumento=" + idDocumento + ", idSolicitud=" + idSolicitud + ", idTipoDocumento="
                + idTipoDocumento + ", rutaArchivo=" + rutaArchivo + ", fechaCargado=" + fechaCargado + ", version="
                + version + ", solicitudCredito=" + solicitudCredito + ", tipoDocumento=" + tipoDocumento + "]";
    }
} 