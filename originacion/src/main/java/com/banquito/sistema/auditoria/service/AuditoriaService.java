package com.banquito.sistema.auditoria.service;

import com.banquito.sistema.auditoria.model.Auditoria;
import com.banquito.sistema.auditoria.repository.AuditoriaRepository;
import com.banquito.sistema.originacion.exception.CreateEntityException;
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
    public Auditoria getById(Long id) {
        return auditoriaRepository.findById(id)
            .orElseThrow(() -> new AuditoriaNotFoundException(id));
    }

    @Transactional
    public Auditoria createAuditoria(Auditoria auditoria) {
        try {
            return auditoriaRepository.save(auditoria);
        } catch (Exception e) {
            // Reusa la CreateEntityException que ya existe
            throw new CreateEntityException(
                "Auditoria",
                "Error al crear Auditoria: " + e.getMessage()
            );
        }
    }
}