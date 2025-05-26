package com.banquito.sistema.originacion.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "SolicitudesCredito")
public class SolicitudCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSolicitud", nullable = false)
    private Long id;

    @Column(name = "NumeroSolicitud", nullable = false, length = 20, unique = true)
    private String numeroSolicitud;

    @Column(name = "IdClienteProspecto", nullable = false)
    private Long idClienteProspecto;

    @Column(name = "IdVehiculo", nullable = false)
    private Long idVehiculo;

    @Column(name = "MontoSolicitado", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoSolicitado;

    @Column(name = "PlazoMeses", nullable = false)
    private Integer plazoMeses;

    @Column(name = "TasaAnual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaAnual;

    @Column(name = "CuotaMensual", nullable = false, precision = 10, scale = 2)
    private BigDecimal cuotaMensual;

    @Column(name = "TotalPagar", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPagar;

    @Column(name = "ScoreInterno", precision = 5, scale = 2)
    private BigDecimal scoreInterno;

    @Column(name = "ScoreExterno", precision = 5, scale = 2)
    private BigDecimal scoreExterno;

    @Column(name = "RelacionCuotaIngreso", precision = 5, scale = 2)
    private BigDecimal relacionCuotaIngreso;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @Version
    @Column(name = "Version", nullable = false)
    private Long version;

    @OneToMany(mappedBy = "solicitudCredito")
    private List<Contrato> contratos;

    @OneToMany(mappedBy = "solicitudCredito")
    private List<DocumentoAdjunto> documentosAdjuntos;

    @OneToMany(mappedBy = "solicitudCredito")
    private List<Pagare> pagares;

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

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public Long getIdClienteProspecto() {
        return idClienteProspecto;
    }

    public void setIdClienteProspecto(Long idClienteProspecto) {
        this.idClienteProspecto = idClienteProspecto;
    }

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
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

    public List<Contrato> getContratos() {
        return contratos;
    }

    public void setContratos(List<Contrato> contratos) {
        this.contratos = contratos;
    }

    public List<DocumentoAdjunto> getDocumentosAdjuntos() {
        return documentosAdjuntos;
    }

    public void setDocumentosAdjuntos(List<DocumentoAdjunto> documentosAdjuntos) {
        this.documentosAdjuntos = documentosAdjuntos;
    }

    public List<Pagare> getPagares() {
        return pagares;
    }

    public void setPagares(List<Pagare> pagares) {
        this.pagares = pagares;
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
               ", montoSolicitado=" + montoSolicitado + ", plazoMeses=" + plazoMeses + 
               ", tasaAnual=" + tasaAnual + ", cuotaMensual=" + cuotaMensual + 
               ", totalPagar=" + totalPagar + ", estado=" + estado + "]";
    }
}
