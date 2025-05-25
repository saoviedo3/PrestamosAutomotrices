package com.banquito.sistema.auditoria.repository;

import com.banquito.sistema.auditoria.model.Auditoria;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {
    boolean existsByTablaAndAccionAndFechaHora(String tabla, String accion, java.sql.Timestamp ts);
}
