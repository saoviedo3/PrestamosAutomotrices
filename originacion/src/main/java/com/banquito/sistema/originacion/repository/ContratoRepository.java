package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Integer> {

    List<Contrato> findBySolicitudCredito_IdSolicitud(Integer idSolicitud);
    
    List<Contrato> findByEstado(String estado);
    
    Optional<Contrato> findBySolicitudCredito_IdSolicitudAndEstado(Integer idSolicitud, String estado);
    
    List<Contrato> findByFechaGeneradoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Contrato> findByFechaFirmaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Contrato> findByRutaArchivoContainingIgnoreCase(String rutaArchivo);
    
    List<Contrato> findByCondicionEspecialContainingIgnoreCase(String condicionEspecial);
    
    long countByEstado(String estado);
    
    boolean existsBySolicitudCredito_IdSolicitud(Integer idSolicitud);
    
    boolean existsBySolicitudCredito_IdSolicitudAndEstado(Integer idSolicitud, String estado);
    
    boolean existsByRutaArchivo(String rutaArchivo);
} 