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
@Table(name = "SolicitudesCreditos")
public class SolicitudCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSolicitudCredito", nullable = false)
    private Long id;

    @Column(name = "NumeroSolicitud", length = 50, nullable = false, unique = true)
    private String numeroSolicitud;

    @Column(name = "IdVehiculo", nullable = false)
    private Long idVehiculo;

    @Column(name = "MontoSolicitado", precision = 15, scale = 2, nullable = false)
    private BigDecimal montoSolicitado;

    @Column(name = "PlazoMeses", nullable = false)
    private Integer plazoMeses;

    @Column(name = "FechaSolicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Column(name = "ScoreInterno", precision = 10, scale = 2)
    private BigDecimal scoreInterno;

    @Column(name = "ScoreExterno", precision = 10, scale = 2)
    private BigDecimal scoreExterno;

    @Column(name = "RelacionCuotaIngreso", precision = 10, scale = 2)
    private BigDecimal relacionCuotaIngreso;

    @Column(name = "TasaAnual", precision = 10, scale = 6, nullable = false)
    private BigDecimal tasaAnual;

    @Column(name = "CuotaMensual", precision = 15, scale = 2, nullable = false)
    private BigDecimal cuotaMensual;

    @Column(name = "TotalPagar", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalPagar;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    public SolicitudCredito() {
    }

    public SolicitudCredito(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(BigDecimal montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }
    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public Long getIdVehiculo() {
        return idVehiculo;
    }
    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public BigDecimal getScoreInterno() {
        return scoreInterno;
    }
    public void setScoreInterno(BigDecimal scoreInterno) {
        this.scoreInterno = scoreInterno;
    }

    public BigDecimal getScoreExterno() {
        return scoreExterno;
    }
    public void setScoreExterno(BigDecimal scoreExterno) {
        this.scoreExterno = scoreExterno;
    }

    public BigDecimal getRelacionCuotaIngreso() {
        return relacionCuotaIngreso;
    }
    public void setRelacionCuotaIngreso(BigDecimal relacionCuotaIngreso) {
        this.relacionCuotaIngreso = relacionCuotaIngreso;
    }

    public BigDecimal getTasaAnual() { return tasaAnual; }
    public void setTasaAnual(BigDecimal tasaAnual) { this.tasaAnual = tasaAnual; }

    public BigDecimal getCuotaMensual() { return cuotaMensual; }
    public void setCuotaMensual(BigDecimal cuotaMensual) { this.cuotaMensual = cuotaMensual; }

    public BigDecimal getTotalPagar() { return totalPagar; }
    public void setTotalPagar(BigDecimal totalPagar) { this.totalPagar = totalPagar; }


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
        SolicitudCredito other = (SolicitudCredito) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SolicitudCredito [id=" + id + ", montoSolicitado=" + montoSolicitado + 
               ", plazoMeses=" + plazoMeses + ", fechaSolicitud=" + fechaSolicitud + 
               ", estado=" + estado + "]";
    }
}
