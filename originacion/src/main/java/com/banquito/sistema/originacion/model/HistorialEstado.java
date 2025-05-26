package com.banquito.sistema.originacion.model;

import java.sql.Timestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "HistorialEstados")
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdHistorialEstado")
    private Long id;

    @NotNull(message = "El ID de la solicitud es obligatorio")
    @Column(name = "IdSolicitud", nullable = false)
    private Long idSolicitud;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 12, message = "El estado no puede tener más de 12 caracteres")
    @Column(name = "Estado", nullable = false, length = 12)
    private String estado;

    @Column(name = "FechaHora", nullable = false)
    private Timestamp fechaHora;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 100, message = "El usuario no puede tener más de 100 caracteres")
    @Column(name = "Usuario", nullable = false, length = 100)
    private String usuario;

    @Size(max = 500, message = "El motivo no puede tener más de 500 caracteres")
    @Column(name = "Motivo", length = 500)
    private String motivo;

    @Version
    @Column(name = "version")
    private Long version;


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