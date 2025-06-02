package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.Pagare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagareRepository extends JpaRepository<Pagare, Long> {
    List<Pagare> findByIdSolicitudOrderByNumeroCuotaAsc(Long idSolicitud);
    boolean existsByIdSolicitudAndNumeroCuota(Long idSolicitud, Integer numeroCuota);
} 