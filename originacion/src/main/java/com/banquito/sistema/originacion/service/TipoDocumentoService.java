package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.TipoDocumento;
import com.banquito.sistema.originacion.repository.TipoDocumentoRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.originacion.exception.TipoDocumentoNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoDocumentoService {
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    @Transactional(readOnly = true)
    public List<TipoDocumento> getAll() {
        return tipoDocumentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TipoDocumento getById(Long id) {
        return tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new TipoDocumentoNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public TipoDocumento getByNombre(String nombre) {
        return tipoDocumentoRepository.findByNombre(nombre)
                .orElseThrow(() -> new TipoDocumentoNotFoundException("Nombre: " + nombre));
    }

    @Transactional(readOnly = true)
    public List<TipoDocumento> getByEstado(String estado) {
        return tipoDocumentoRepository.findByEstado(estado);
    }

    @Transactional
    public TipoDocumento create(TipoDocumento tipoDocumento) {
        if (tipoDocumentoRepository.existsByNombre(tipoDocumento.getNombre())) {
            throw new AlreadyExistsException(
                "TipoDocumento",
                "Ya existe un tipo de documento con el nombre: " + tipoDocumento.getNombre()
            );
        }

        if (tipoDocumento.getEstado() == null || tipoDocumento.getEstado().isBlank()) {
            tipoDocumento.setEstado("Activo");
        }

        try {
            return tipoDocumentoRepository.save(tipoDocumento);
        } catch (Exception e) {
            throw new CreateEntityException(
                "TipoDocumento",
                "Error al crear TipoDocumento: " + e.getMessage()
            );
        }
    }

    @Transactional
    public TipoDocumento update(Long id, TipoDocumento tipoDocumentoActualizado) {
        TipoDocumento tipoDocumentoExistente = getById(id);

        if (!tipoDocumentoExistente.getNombre().equals(tipoDocumentoActualizado.getNombre()) &&
            tipoDocumentoRepository.existsByNombre(tipoDocumentoActualizado.getNombre())) {
            throw new AlreadyExistsException(
                "TipoDocumento",
                "Ya existe un tipo de documento con el nombre: " + tipoDocumentoActualizado.getNombre()
            );
        }

        tipoDocumentoExistente.setNombre(tipoDocumentoActualizado.getNombre());
        tipoDocumentoExistente.setDescripcion(tipoDocumentoActualizado.getDescripcion());
        tipoDocumentoExistente.setEstado(tipoDocumentoActualizado.getEstado());

        try {
            return tipoDocumentoRepository.save(tipoDocumentoExistente);
        } catch (Exception e) {
            throw new CreateEntityException(
                "TipoDocumento",
                "Error al actualizar TipoDocumento: " + e.getMessage()
            );
        }
    }
} 