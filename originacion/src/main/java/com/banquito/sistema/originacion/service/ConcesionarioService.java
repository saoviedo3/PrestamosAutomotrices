package com.banquito.sistema.originacion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.sistema.originacion.exception.DuplicateException;
import com.banquito.sistema.originacion.exception.InvalidStateException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.model.Concesionario;
import com.banquito.sistema.originacion.repository.ConcesionarioRepository;

@Service
@Transactional
public class ConcesionarioService {

    private final ConcesionarioRepository repository;
    private final HistorialEstadoService historialEstadoService;

    public ConcesionarioService(ConcesionarioRepository repository, HistorialEstadoService historialEstadoService) {
        this.repository = repository;
        this.historialEstadoService = historialEstadoService;
    }

    @Transactional(readOnly = true)
    public List<Concesionario> findAll() {
        return this.repository.findAll();
    }

    @Transactional(readOnly = true)
    public Concesionario findById(Long id) {
        Optional<Concesionario> concesionario = this.repository.findById(id);
        if (concesionario.isEmpty()) {
            throw new NotFoundException(id.toString(), "Concesionario");
        }
        return concesionario.get();
    }

    @Transactional(readOnly = true)
    public Concesionario findByCodigo(String codigo) {
        Optional<Concesionario> concesionario = this.repository.findByCodigo(codigo);
        if (concesionario.isEmpty()) {
            throw new NotFoundException(codigo, "Concesionario");
        }
        return concesionario.get();
    }

    @Transactional(readOnly = true)
    public List<Concesionario> findByEstado(String estado) {
        return this.repository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Concesionario> findByCiudad(String ciudad) {
        return this.repository.findByCiudad(ciudad);
    }

    @Transactional(readOnly = true)
    public List<Concesionario> findByProvincia(String provincia) {
        return this.repository.findByProvincia(provincia);
    }

    @Transactional(readOnly = true)
    public List<Concesionario> findByNombre(String nombre) {
        return this.repository.findByNombreContainingIgnoreCase(nombre);
    }

    public Concesionario create(Concesionario concesionario) {
        this.validateForCreate(concesionario);
        concesionario.setEstado("ACTIVO");
        Concesionario savedConcesionario = this.repository.save(concesionario);
        
        // Registrar en historial
        this.historialEstadoService.registrarCambioEstado(
            "CONCESIONARIO", 
            savedConcesionario.getId(), 
            null, 
            "ACTIVO", 
            "Creación de concesionario", 
            "SISTEMA"
        );
        
        return savedConcesionario;
    }

    public Concesionario update(Long id, Concesionario concesionario) {
        Concesionario existingConcesionario = this.findById(id);
        this.validateForUpdate(concesionario, existingConcesionario);
        
        concesionario.setId(id);
        concesionario.setFechaCreacion(existingConcesionario.getFechaCreacion());
        
        return this.repository.save(concesionario);
    }

    public Concesionario changeState(Long id, String newState, String motivo, String usuario) {
        Concesionario concesionario = this.findById(id);
        String oldState = concesionario.getEstado();
        
        this.validateStateChange(oldState, newState);
        
        concesionario.setEstado(newState);
        Concesionario updatedConcesionario = this.repository.save(concesionario);
        
        // Registrar en historial
        this.historialEstadoService.registrarCambioEstado(
            "CONCESIONARIO", 
            id, 
            oldState, 
            newState, 
            motivo, 
            usuario
        );
        
        return updatedConcesionario;
    }

    private void validateForCreate(Concesionario concesionario) {
        if (this.repository.existsByCodigo(concesionario.getCodigo())) {
            throw new DuplicateException(concesionario.getCodigo(), "Concesionario con código");
        }
        
        if (concesionario.getEmail() != null && this.repository.existsByEmail(concesionario.getEmail())) {
            throw new DuplicateException(concesionario.getEmail(), "Concesionario con email");
        }
    }

    private void validateForUpdate(Concesionario concesionario, Concesionario existing) {
        if (!existing.getCodigo().equals(concesionario.getCodigo()) && 
            this.repository.existsByCodigo(concesionario.getCodigo())) {
            throw new DuplicateException(concesionario.getCodigo(), "Concesionario con código");
        }
        
        if (concesionario.getEmail() != null && 
            !concesionario.getEmail().equals(existing.getEmail()) && 
            this.repository.existsByEmail(concesionario.getEmail())) {
            throw new DuplicateException(concesionario.getEmail(), "Concesionario con email");
        }
    }

    private void validateStateChange(String currentState, String newState) {
        if (currentState.equals(newState)) {
            return;
        }
        
        // Validaciones de transiciones de estado
        switch (currentState) {
            case "ACTIVO":
                if (!newState.equals("INACTIVO") && !newState.equals("SUSPENDIDO")) {
                    throw new InvalidStateException(currentState, newState, "Concesionario");
                }
                break;
            case "INACTIVO":
                if (!newState.equals("ACTIVO")) {
                    throw new InvalidStateException(currentState, newState, "Concesionario");
                }
                break;
            case "SUSPENDIDO":
                if (!newState.equals("ACTIVO") && !newState.equals("INACTIVO")) {
                    throw new InvalidStateException(currentState, newState, "Concesionario");
                }
                break;
            default:
                throw new InvalidStateException(currentState, newState, "Concesionario");
        }
    }
} 