package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.TipoDocumento;
import com.banquito.sistema.originacion.repository.TipoDocumentoRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.InvalidDataException;
import com.banquito.sistema.exception.UpdateEntityException;

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
                .orElseThrow(() -> new InvalidDataException("TipoDocumento", "No existe un tipo de documento con el id: " + id));
    }

    @Transactional(readOnly = true)
    public TipoDocumento getByNombre(String nombre) {
        return tipoDocumentoRepository.findByNombre(nombre)
                .orElseThrow(() -> new InvalidDataException("TipoDocumento", "No existe un tipo de documento con el nombre: " + nombre));
    }

    @Transactional(readOnly = true)
    public List<TipoDocumento> getByEstado(String estado) {
        return tipoDocumentoRepository.findByEstado(estado);
    }

    @Transactional
    public TipoDocumento create(TipoDocumento tipoDocumento) {
        // Validar que el nombre no esté vacío
        if (tipoDocumento.getNombre() == null || tipoDocumento.getNombre().isBlank()) {
            throw new InvalidDataException(
                "TipoDocumento",
                "El nombre no puede estar vacío"
            );
        }

        // Validar que la descripción no esté vacía
        if (tipoDocumento.getDescripcion() == null || tipoDocumento.getDescripcion().isBlank()) {
            throw new InvalidDataException(
                "TipoDocumento",
                "La descripción no puede estar vacía"
            );
        }

        // Validar que no exista un tipo de documento con el mismo nombre
        if (tipoDocumentoRepository.existsByNombre(tipoDocumento.getNombre())) {
            throw new AlreadyExistsException(
                "TipoDocumento",
                "Ya existe un tipo de documento con el nombre: " + tipoDocumento.getNombre()
            );
        }

        // Establecer valores por defecto
        if (tipoDocumento.getEstado() == null || tipoDocumento.getEstado().isBlank()) {
            tipoDocumento.setEstado("Activo");
        }

        // Validar que el estado sea válido
        String estado = tipoDocumento.getEstado();
        if (!estado.equals("Activo") && !estado.equals("Inactivo")) {
            throw new InvalidDataException(
                "TipoDocumento",
                "El estado debe ser uno de: Activo, Inactivo"
            );
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

        // Validar que el nombre no esté vacío
        if (tipoDocumentoActualizado.getNombre() == null || tipoDocumentoActualizado.getNombre().isBlank()) {
            throw new InvalidDataException(
                "TipoDocumento",
                "El nombre no puede estar vacío"
            );
        }

        // Validar que la descripción no esté vacía
        if (tipoDocumentoActualizado.getDescripcion() == null || tipoDocumentoActualizado.getDescripcion().isBlank()) {
            throw new InvalidDataException(
                "TipoDocumento",
                "La descripción no puede estar vacía"
            );
        }

        // Validar que no exista otro tipo de documento con el mismo nombre
        if (!tipoDocumentoExistente.getNombre().equals(tipoDocumentoActualizado.getNombre()) &&
            tipoDocumentoRepository.existsByNombre(tipoDocumentoActualizado.getNombre())) {
            throw new AlreadyExistsException(
                "TipoDocumento",
                "Ya existe un tipo de documento con el nombre: " + tipoDocumentoActualizado.getNombre()
            );
        }

        // Validar que el estado sea válido
        if (tipoDocumentoActualizado.getEstado() != null && !tipoDocumentoActualizado.getEstado().isBlank()) {
            String estado = tipoDocumentoActualizado.getEstado();
            if (!estado.equals("Activo") && !estado.equals("Inactivo")) {
                throw new InvalidDataException(
                    "TipoDocumento",
                    "El estado debe ser uno de: Activo, Inactivo"
                );
            }
        }

        // Actualizar campos
        tipoDocumentoExistente.setNombre(tipoDocumentoActualizado.getNombre());
        tipoDocumentoExistente.setDescripcion(tipoDocumentoActualizado.getDescripcion());
        tipoDocumentoExistente.setEstado(tipoDocumentoActualizado.getEstado());

        try {
            return tipoDocumentoRepository.save(tipoDocumentoExistente);
        } catch (Exception e) {
            throw new UpdateEntityException(
                "TipoDocumento",
                "Error al actualizar TipoDocumento: " + e.getMessage()
            );
        }
    }
} 