package com.banquito.sistema.auditoria.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Auditorias")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdAuditoria", nullable = false)
    private Integer id;

    @Column(name = "Tabla", length = 40, nullable = false)
    private String tabla;

    @Column(name = "Accion", length = 6, nullable = false)
    private String accion;

    @Column(name = "FechaHora", nullable = false)
    private Timestamp fechaHora;

    public Auditoria() {
    }

    public Auditoria(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public java.sql.Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(java.sql.Timestamp fechaHora) {
        this.fechaHora = fechaHora;
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
        Auditoria other = (Auditoria) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Auditoria [id=" + id + ", tabla=" + tabla + ", accion=" + accion + ", fechaHora=" + fechaHora + "]";
    }

}
