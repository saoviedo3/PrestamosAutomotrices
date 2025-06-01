package com.banquito.sistema.analisis.repository;

import com.banquito.sistema.analisis.model.ObservacionAnalista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservacionAnalistaRepository extends JpaRepository<ObservacionAnalista, Long> {
    
    List<ObservacionAnalista> findByIdSolicitud(Long idSolicitud);
    
    List<ObservacionAnalista> findByUsuario(String usuario);
} 