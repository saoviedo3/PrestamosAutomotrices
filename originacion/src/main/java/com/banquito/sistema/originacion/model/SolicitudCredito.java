package com.banquito.sistema.originacion.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "SolicitudesCreditos")
public class SolicitudCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSolicitud", nullable = false)
    private Long id;

    @Column(name = "NumeroSolicitud", length = 50, nullable = false, unique = true)
    private String numeroSolicitud;

    @Column(name = "IdVehiculo", nullable = false)
    private Long idVehiculo;

    @Column(name = "MontoSolicitado", precision = 12, scale = 2, nullable = false)
    private BigDecimal montoSolicitado;

    @Column(name = "PlazoMeses", nullable = false)
    private Short plazoMeses;

    @Column(name = "FechaSolicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "ScoreInterno", precision = 6, scale = 2)
    private BigDecimal scoreInterno;

    @Column(name = "ScoreExterno", precision = 6, scale = 2)
    private BigDecimal scoreExterno;

    @Column(name = "RelacionCuotaIngreso", precision = 5, scale = 2)
    private BigDecimal relacionCuotaIngreso;

    @Column(name = "TasaAnual", precision = 5, scale = 2, nullable = false)
    private BigDecimal tasaAnual;

    @Column(name = "CuotaMensual", precision = 8, scale = 2, nullable = false)
    private BigDecimal cuotaMensual;

    @Column(name = "TotalPagar", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalPagar;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "Entrada", precision = 12, scale = 2, nullable = false)
    private BigDecimal entrada;

    @Version
    @Column(name = "Version", nullable = false)
    private Long version;

    @ManyToOne(optional = false)
    @JoinColumn(name = "IdClienteProspecto", nullable = false, insertable = false, updatable = false)
    private ClienteProspecto clienteProspecto;

    @Column(name = "IdClienteProspecto", nullable = false)
    private Long idClienteProspecto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "IdVendedor", nullable = false, insertable = false, updatable = false)
    private Vendedor vendedor;

    @Column(name = "IdVendedor", nullable = false)
    private Long idVendedor;

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

    public Short getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(Short plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
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

    public ClienteProspecto getClienteProspecto() {
        return clienteProspecto;
    }

    public void setClienteProspecto(ClienteProspecto clienteProspecto) {
        this.clienteProspecto = clienteProspecto;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public BigDecimal getEntrada() {
        return entrada;
    }

    public void setEntrada(BigDecimal entrada) {
        this.entrada = entrada;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getIdClienteProspecto() {
        return idClienteProspecto;
    }

    public void setIdClienteProspecto(Long idClienteProspecto) {
        this.idClienteProspecto = idClienteProspecto;
    }

    public Long getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Long idVendedor) {
        this.idVendedor = idVendedor;
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
        return "SolicitudCredito [id=" + id + ", numeroSolicitud=" + numeroSolicitud + 
               ", idVehiculo=" + idVehiculo + ", montoSolicitado=" + montoSolicitado + 
               ", plazoMeses=" + plazoMeses + ", fechaSolicitud=" + fechaSolicitud + 
               ", scoreInterno=" + scoreInterno + ", scoreExterno=" + scoreExterno + 
               ", relacionCuotaIngreso=" + relacionCuotaIngreso + ", tasaAnual=" + tasaAnual + 
               ", cuotaMensual=" + cuotaMensual + ", totalPagar=" + totalPagar + 
               ", estado=" + estado + ", entrada=" + entrada + 
               ", version=" + version + ", idClienteProspecto=" + idClienteProspecto + 
               ", idVendedor=" + idVendedor + "]";
    }
}
