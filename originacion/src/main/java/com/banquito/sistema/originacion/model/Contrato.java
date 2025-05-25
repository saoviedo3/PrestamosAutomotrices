package com.banquito.sistema.originacion.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdContrato", nullable = false)
    private Long id;

    @Column(name = "NumeroContrato", length = 50, nullable = false, unique = true)
    private String numeroContrato;

    @Column(name = "MontoFinanciado", precision = 15, scale = 2, nullable = false)
    private BigDecimal montoFinanciado;

    @Column(name = "TasaInteres", precision = 5, scale = 4, nullable = false)
    private BigDecimal tasaInteres;

    @Column(name = "PlazoMeses", nullable = false)
    private Integer plazoMeses;

    @Column(name = "FechaContrato", nullable = false)
    private LocalDate fechaContrato;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    public Contrato() {
    }

    public Contrato(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public BigDecimal getMontoFinanciado() {
        return montoFinanciado;
    }

    public void setMontoFinanciado(BigDecimal montoFinanciado) {
        this.montoFinanciado = montoFinanciado;
    }

    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public LocalDate getFechaContrato() {
        return fechaContrato;
    }

    public void setFechaContrato(LocalDate fechaContrato) {
        this.fechaContrato = fechaContrato;
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
        Contrato other = (Contrato) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Contrato [id=" + id + ", numeroContrato=" + numeroContrato + 
               ", montoFinanciado=" + montoFinanciado + ", tasaInteres=" + tasaInteres + 
               ", plazoMeses=" + plazoMeses + ", fechaContrato=" + fechaContrato + 
               ", estado=" + estado + "]";
    }
} 