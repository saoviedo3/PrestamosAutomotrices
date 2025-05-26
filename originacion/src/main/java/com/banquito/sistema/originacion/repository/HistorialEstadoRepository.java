package com.banquito.sistema.originacion.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.HistorialEstado;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {

    List<HistorialEstado> findByIdSolicitud(Long idSolicitud);
    
    List<HistorialEstado> findByUsuario(String usuario);
    
    List<HistorialEstado> findByFechaHoraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<HistorialEstado> findByIdSolicitudOrderByFechaHoraDesc(Long idSolicitud);
    
    List<HistorialEstado> findByEstado(String estado);
} 