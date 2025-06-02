package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    Optional<Contrato> findByIdSolicitud(Long idSolicitud);
    List<Contrato> findByEstado(String estado);
    boolean existsByIdSolicitud(Long idSolicitud);
} 