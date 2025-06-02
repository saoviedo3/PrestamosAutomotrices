package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {
    Optional<TipoDocumento> findByNombre(String nombre);
    List<TipoDocumento> findByEstado(String estado);
    boolean existsByNombre(String nombre);
} 