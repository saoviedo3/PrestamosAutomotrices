package com.banquito.sistema.originacion.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.HistorialEstado;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {

    List<HistorialEstado> findByIdSolicitud(Long idSolicitud);
    
    List<HistorialEstado> findByUsuario(String usuario);
    
    List<HistorialEstado> findByFechaHoraBetween(Timestamp fechaInicio, Timestamp fechaFin);
    
    List<HistorialEstado> findByIdSolicitudOrderByFechaHoraDesc(Long idSolicitud);
    
    List<HistorialEstado> findByEstado(String estado);
} 