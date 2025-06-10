package com.banquito.sistema.originacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.contratos.model.Pagare;

import java.util.List;

@Repository
public interface PagareOriginacionRepository extends JpaRepository<Pagare, Long> {
    List<Pagare> findByIdSolicitudOrderByNumeroCuotaAsc(Long idSolicitud);
    boolean existsByIdSolicitudAndNumeroCuota(Long idSolicitud, Integer numeroCuota);
} 