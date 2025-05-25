package com.banquito.sistema.analisis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.analisis.model.ObservacionAnalista;

import java.util.List;

@Repository
public interface ObservacionAnalistaRepository extends JpaRepository<ObservacionAnalista, Integer> {
    
    List<ObservacionAnalista> findByIdSolicitud(Integer idSolicitud);
    
    List<ObservacionAnalista> findByUsuario(String usuario);
} 