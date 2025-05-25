package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.SolicitudCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SolicitudCreditoRepository extends JpaRepository<SolicitudCredito, Integer> {

    List<SolicitudCredito> findByIdClienteProspecto(Integer idClienteProspecto);
    
    List<SolicitudCredito> findByEstado(String estado);
    
    List<SolicitudCredito> findByIdClienteProspectoAndEstado(Integer idClienteProspecto, String estado);
    
    List<SolicitudCredito> findByFechaSolicitudBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<SolicitudCredito> findByMontoSolicitadoBetween(BigDecimal montoMinimo, BigDecimal montoMaximo);
    
    List<SolicitudCredito> findByIdVendedor(Integer idVendedor);
    
    long countByEstado(String estado);
    
    boolean existsByIdClienteProspectoAndEstado(Integer idClienteProspecto, String estado);

    SolicitudCredito findByNumeroSolicitud(String numeroSolicitud);
} 