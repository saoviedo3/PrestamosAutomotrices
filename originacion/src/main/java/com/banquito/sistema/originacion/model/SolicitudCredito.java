package com.banquito.sistema.originacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;

@Entity
@Table(name = "SolicitudesCreditos")
public class SolicitudCredito {

    @Id
    @Column(name = "IdSolicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "IdClienteProspecto", nullable = false)
    private Integer idClienteProspecto;

    @Column(name = "IdVehiculo", nullable = false)
    private Integer idVehiculo;

    @Column(name = "IdVendedor", nullable = false)
    private Integer idVendedor;

    @Column(name = "NumeroSolicitud", length = 20, nullable = false, unique = true)
    private String numeroSolicitud;

    @Column(name = "MontoSolicitado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoSolicitado;

    @Column(name = "PlazoMeses", nullable = false, precision = 3)
    private BigDecimal plazoMeses;

    @Column(name = "Entrada", nullable = false, precision = 12, scale = 2)
    private BigDecimal entrada;

    @Column(name = "ScoreInterno", nullable = false, precision = 6, scale = 2)
    private BigDecimal scoreInterno;

    @Column(name = "ScoreExterno", nullable = false, precision = 6, scale = 2)
    private BigDecimal scoreExterno;

    @Column(name = "RelacionCuotaIngreso", nullable = false, precision = 5, scale = 2)
    private BigDecimal relacionCuotaIngreso;

    @Column(name = "TasaAnual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaAnual;

    @Column(name = "CuotaMensual", nullable = false, precision = 8, scale = 2)
    private BigDecimal cuotaMensual;

    @Column(name = "TotalPagar", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPagar;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    @Version
    @Column(name = "Version", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "IdClienteProspecto", referencedColumnName = "IdClienteProspecto", insertable = false, updatable = false)
    private ClienteProspecto clienteProspecto;

    @ManyToOne
    @JoinColumn(name = "IdVehiculo", referencedColumnName = "IdVehiculo", insertable = false, updatable = false)
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "IdVendedor", referencedColumnName = "IdVendedor", insertable = false, updatable = false)
    private Vendedor vendedor;

    public SolicitudCredito() {
    }

    public SolicitudCredito(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    // Getters y Setters
    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Integer getIdClienteProspecto() {
        return idClienteProspecto;
    }

    public void setIdClienteProspecto(Integer idClienteProspecto) {
        this.idClienteProspecto = idClienteProspecto;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(BigDecimal montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public BigDecimal getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(BigDecimal plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public BigDecimal getEntrada() {
        return entrada;
    }

    public void setEntrada(BigDecimal entrada) {
        this.entrada = entrada;
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

    public BigDecimal getTasaAnual() {
        return tasaAnual;
    }

    public void setTasaAnual(BigDecimal tasaAnual) {
        this.tasaAnual = tasaAnual;
    }

    public BigDecimal getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(BigDecimal cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public ClienteProspecto getClienteProspecto() {
        return clienteProspecto;
    }

    public void setClienteProspecto(ClienteProspecto clienteProspecto) {
        this.clienteProspecto = clienteProspecto;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idSolicitud == null) ? 0 : idSolicitud.hashCode());
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
        if (idSolicitud == null) {
            if (other.idSolicitud != null)
                return false;
        } else if (!idSolicitud.equals(other.idSolicitud))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SolicitudCredito [idSolicitud=" + idSolicitud + ", numeroSolicitud=" + numeroSolicitud
                + ", montoSolicitado=" + montoSolicitado + ", estado=" + estado + ", version=" + version + "]";
    }
} 