package com.banquito.sistema.originacion.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.HistorialEstado;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {

    List<HistorialEstado> findByEntidadTipoAndEntidadId(String entidadTipo, Long entidadId);
    
    List<HistorialEstado> findByEntidadTipo(String entidadTipo);
    
    List<HistorialEstado> findByUsuarioCambio(String usuarioCambio);
    
    List<HistorialEstado> findByFechaCambioBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<HistorialEstado> findByEntidadTipoAndEntidadIdOrderByFechaCambioDesc(String entidadTipo, Long entidadId);
    
    List<HistorialEstado> findByEstadoNuevo(String estadoNuevo);
} 