package com.banquito.sistema.originacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.SolicitudCredito;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudCreditoRepository extends JpaRepository<SolicitudCredito, Integer> {
    
    Optional<SolicitudCredito> findByNumeroSolicitud(String numeroSolicitud);
    
    List<SolicitudCredito> findByIdClienteProspecto(Integer idClienteProspecto);
    
    List<SolicitudCredito> findByEstado(String estado);
    
    List<SolicitudCredito> findByIdVendedor(Integer idVendedor);
    
    boolean existsByIdVehiculo(Integer idVehiculo);
} 