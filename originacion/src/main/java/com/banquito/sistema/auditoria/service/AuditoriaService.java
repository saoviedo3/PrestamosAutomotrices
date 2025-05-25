package com.banquito.sistema.auditoria.service;

import com.banquito.sistema.auditoria.model.Auditoria;
import com.banquito.sistema.auditoria.repository.AuditoriaRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.InvalidDataException;
import com.banquito.sistema.auditoria.exception.AuditoriaNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditoriaService {
    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Auditoria> getAll() {
        return auditoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Auditoria getById(Integer id) {
        return auditoriaRepository.findById(id)
                .orElseThrow(() -> new AuditoriaNotFoundException(id));
    }

    @Transactional
    public Auditoria createAuditoria(Auditoria auditoria) {
        // 1) Validación de datos obligatorios
        if (auditoria.getTabla() == null || auditoria.getTabla().isBlank()
                || auditoria.getAccion() == null || auditoria.getAccion().isBlank()
                || auditoria.getFechaHora() == null) {
            throw new InvalidDataException(
                    "Auditoria",
                    "Faltan campos obligatorios: tabla, accion y/o fechaHora");
        }

        // 2) Duplicados exactos
        java.sql.Timestamp ts = auditoria.getFechaHora();
        if (auditoriaRepository.existsByTablaAndAccionAndFechaHora(
                auditoria.getTabla(), auditoria.getAccion(), ts)) {
            throw new AlreadyExistsException(
                    "Auditoria",
                    String.format("Ya existe Auditoria para tabla='%s', accion='%s' y fechaHora='%s'",
                            auditoria.getTabla(), auditoria.getAccion(), ts));
        }

        // 3) Inserción y manejo de errores inesperados
        try {
            return auditoriaRepository.save(auditoria);
        } catch (Exception e) {
            throw new CreateEntityException(
                    "Auditoria",
                    "Error al persistir Auditoria: " + e.getMessage());
        }
    }
}