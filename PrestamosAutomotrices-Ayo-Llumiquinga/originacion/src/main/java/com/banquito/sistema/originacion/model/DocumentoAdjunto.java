package com.banquito.sistema.originacion.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DocumentosAdjuntos")
public class DocumentoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDocumentoAdjunto", nullable = false)
    private Long id;

    @Column(name = "NombreArchivo", length = 255, nullable = false)
    private String nombreArchivo;

    @Column(name = "RutaArchivo", length = 500, nullable = false)
    private String rutaArchivo;

    @Column(name = "TipoMime", length = 100)
    private String tipoMime;

    @Column(name = "Tamaño")
    private Long tamaño;

    @Column(name = "FechaSubida", nullable = false)
    private LocalDateTime fechaSubida;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    public DocumentoAdjunto() {
    }

    public DocumentoAdjunto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public Long getTamaño() {
        return tamaño;
    }

    public void setTamaño(Long tamaño) {
        this.tamaño = tamaño;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
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
        DocumentoAdjunto other = (DocumentoAdjunto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DocumentoAdjunto [id=" + id + ", nombreArchivo=" + nombreArchivo + 
               ", rutaArchivo=" + rutaArchivo + ", tipoMime=" + tipoMime + 
               ", tamaño=" + tamaño + ", fechaSubida=" + fechaSubida + ", estado=" + estado + "]";
    }
}
