package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.Pagare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagareRepository extends JpaRepository<Pagare, Integer> {

    List<Pagare> findBySolicitudCredito_IdSolicitud(Integer idSolicitud);
    
    List<Pagare> findBySolicitudCredito_IdSolicitudOrderByNumeroCuota(Integer idSolicitud);
    
    Optional<Pagare> findBySolicitudCredito_IdSolicitudAndNumeroCuota(Integer idSolicitud, Integer numeroCuota);
    
    boolean existsBySolicitudCredito_IdSolicitudAndNumeroCuota(Integer idSolicitud, Integer numeroCuota);
    
    long countBySolicitudCredito_IdSolicitud(Integer idSolicitud);
} 