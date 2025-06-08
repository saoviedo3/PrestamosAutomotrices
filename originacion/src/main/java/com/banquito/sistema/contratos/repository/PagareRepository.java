package com.banquito.sistema.contratos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import com.banquito.sistema.contratos.model.Pagare;

public interface PagareRepository extends JpaRepository<Pagare, Long> {

    List<Pagare> findByIdSolicitud(Long idSolicitud);
    
    List<Pagare> findByIdSolicitudOrderByNumeroCuota(Long idSolicitud);
    
    Optional<Pagare> findByIdSolicitudAndNumeroCuota(Long idSolicitud, Integer numeroCuota);
    
    List<Pagare> findByNumeroCuota(Integer numeroCuota);
    
    boolean existsByIdSolicitud(Long idSolicitud);
    
    Long countByIdSolicitud(Long idSolicitud);
    
    void deleteByIdSolicitud(Long idSolicitud);
} 