package com.banquito.sistema.originacion.model;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "SolicitudesCreditos")
public class SolicitudCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSolicitud", nullable = false)
    private Integer id;

    @NotNull(message = "El ID del cliente prospecto es obligatorio")
    @Column(name = "IdClienteProspecto", nullable = false)
    private Integer idClienteProspecto;

    @NotNull(message = "El ID del vehículo es obligatorio")
    @Column(name = "IdVehiculo", nullable = false)
    private Integer idVehiculo;

    @NotNull(message = "El ID del vendedor es obligatorio")
    @Column(name = "IdVendedor", nullable = false)
    private Integer idVendedor;

    @NotBlank(message = "El número de solicitud es obligatorio")
    @Size(min = 5, max = 20, message = "El número de solicitud debe tener entre 5 y 20 caracteres")
    @Column(name = "NumeroSolicitud", length = 20, nullable = false)
    private String numeroSolicitud;

    @NotNull(message = "El monto solicitado es obligatorio")
    @DecimalMin(value = "1000.0", message = "El monto solicitado debe ser al menos 1000")
    @Column(name = "MontoSolicitado", precision = 12, scale = 2, nullable = false)
    private BigDecimal montoSolicitado;

    @NotNull(message = "El plazo en meses es obligatorio")
    @Min(value = 12, message = "El plazo mínimo es de 12 meses")
    @Column(name = "PlazoMeses", nullable = false)
    private Integer plazoMeses;

    @NotNull(message = "La entrada es obligatoria")
    @DecimalMin(value = "0.0", message = "La entrada debe ser mayor o igual a 0")
    @Column(name = "Entrada", precision = 12, scale = 2, nullable = false)
    private BigDecimal entrada;

    @Column(name = "ScoreInterno", precision = 6, scale = 2, nullable = false)
    private BigDecimal scoreInterno;

    @Column(name = "ScoreExterno", precision = 6, scale = 2, nullable = false)
    private BigDecimal scoreExterno;

    @Column(name = "RelacionCuotaIngreso", precision = 5, scale = 2, nullable = false)
    private BigDecimal relacionCuotaIngreso;

    @Column(name = "TasaAnual", precision = 5, scale = 2, nullable = false)
    private BigDecimal tasaAnual;

    @Column(name = "CuotaMensual", precision = 8, scale = 2, nullable = false)
    private BigDecimal cuotaMensual;

    @Column(name = "TotalPagar", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalPagar;

    @Pattern(regexp = "Borrador|EnRevision|Aprobada|Rechazada|Cancelada", 
            message = "El estado debe ser uno de: Borrador, EnRevision, Aprobada, Rechazada, Cancelada")
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

    public SolicitudCredito(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(Integer plazoMeses) {
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
        return Objects.hash(id);
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
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "SolicitudCredito [id=" + id + ", numeroSolicitud=" + numeroSolicitud + ", montoSolicitado="
                + montoSolicitado + ", plazoMeses=" + plazoMeses + ", estado=" + estado + "]";
    }
}
