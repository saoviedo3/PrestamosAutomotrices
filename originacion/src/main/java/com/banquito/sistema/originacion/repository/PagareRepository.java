package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.Pagare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagareRepository extends JpaRepository<Pagare, Integer> {

    List<Pagare> findByIdSolicitud(Integer idSolicitud);
    
    List<Pagare> findByIdSolicitudOrderByNumeroCuota(Integer idSolicitud);
    
    Optional<Pagare> findByIdSolicitudAndNumeroCuota(Integer idSolicitud, Integer numeroCuota);
    
    boolean existsByIdSolicitudAndNumeroCuota(Integer idSolicitud, Integer numeroCuota);
    
    long countByIdSolicitud(Integer idSolicitud);
} 