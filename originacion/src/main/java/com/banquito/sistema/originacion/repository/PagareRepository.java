package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.Pagare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagareRepository extends JpaRepository<Pagare, Integer> {

    List<Pagare> findBySolicitudCredito_IdSolicitud(Integer idSolicitud);
    
    List<Pagare> findByNumeroCuota(Integer numeroCuota);
    
    Optional<Pagare> findBySolicitudCredito_IdSolicitudAndNumeroCuota(Integer idSolicitud, Integer numeroCuota);
    
    List<Pagare> findByFechaGeneradoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Pagare> findByRutaArchivoContainingIgnoreCase(String rutaArchivo);
    
    long countBySolicitudCredito_IdSolicitud(Integer idSolicitud);
    
    boolean existsBySolicitudCredito_IdSolicitudAndNumeroCuota(Integer idSolicitud, Integer numeroCuota);
    
    boolean existsByRutaArchivo(String rutaArchivo);
    
    List<Pagare> findBySolicitudCredito_IdSolicitudOrderByNumeroCuotaAsc(Integer idSolicitud);
} 