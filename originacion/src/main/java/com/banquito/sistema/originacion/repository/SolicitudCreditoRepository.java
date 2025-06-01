package com.banquito.sistema.originacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.SolicitudCredito;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudCreditoRepository extends JpaRepository<SolicitudCredito, Long> {

    Optional<SolicitudCredito> findByNumeroSolicitud(String numeroSolicitud);

    //List<SolicitudCredito> findByIdClienteProspecto(Long idClienteProspecto);

    List<SolicitudCredito> findByEstado(String estado);

    //List<SolicitudCredito> findByIdVendedor(Long idVendedor);

    boolean existsByNumeroSolicitud(String numeroSolicitud);

    boolean existsByVehiculo_Id(Long idVehiculo);
} 