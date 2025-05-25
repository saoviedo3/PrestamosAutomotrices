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
@Table(name = "Pagares")
public class Pagare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPagare", nullable = false)
    private Long id;

    @Column(name = "NumeroPagare", length = 50, nullable = false, unique = true)
    private String numeroPagare;

    @Column(name = "Monto", precision = 15, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "FechaEmision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "FechaVencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "Observaciones", length = 500)
    private String observaciones;

    public Pagare() {
    }

    public Pagare(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPagare() {
        return numeroPagare;
    }

    public void setNumeroPagare(String numeroPagare) {
        this.numeroPagare = numeroPagare;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
        Pagare other = (Pagare) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Pagare [id=" + id + ", numeroPagare=" + numeroPagare + ", monto=" + monto + 
               ", fechaEmision=" + fechaEmision + ", fechaVencimiento=" + fechaVencimiento + 
               ", estado=" + estado + ", observaciones=" + observaciones + "]";
    }
} 