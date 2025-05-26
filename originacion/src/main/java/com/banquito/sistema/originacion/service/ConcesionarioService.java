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

    public ConcesionarioService(ConcesionarioRepository repository) {
        this.repository = repository;
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
    public List<Concesionario> findByEstado(String estado) {
        return this.repository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Concesionario> findByRazonSocial(String razonSocial) {
        return this.repository.findByRazonSocialContainingIgnoreCase(razonSocial);
    }

    public Concesionario create(Concesionario concesionario) {
        this.validateForCreate(concesionario);
        if (concesionario.getEstado() == null || concesionario.getEstado().trim().isEmpty()) {
            concesionario.setEstado("ACTIVO");
        }
        Concesionario savedConcesionario = this.repository.save(concesionario);

        
        return savedConcesionario;
    }

    public Concesionario update(Long id, Concesionario concesionario) {
        Concesionario existingConcesionario = this.findById(id);
        this.validateForUpdate(concesionario, existingConcesionario);

        concesionario.setIdConcesionario(id);
        concesionario.setVersion(existingConcesionario.getVersion());
        
        return this.repository.save(concesionario);
    }

    public Concesionario changeState(Long id, String newState, String motivo, String usuario) {
        Concesionario concesionario = this.findById(id);
        String oldState = concesionario.getEstado();
        
        this.validateStateChange(oldState, newState);
        
        concesionario.setEstado(newState);
        Concesionario updatedConcesionario = this.repository.save(concesionario);
        
        return updatedConcesionario;
    }

    private void validateForCreate(Concesionario concesionario) {
        if (concesionario.getEmailContacto() != null && this.repository.existsByEmailContacto(concesionario.getEmailContacto())) {
            throw new DuplicateException(concesionario.getEmailContacto(), "Concesionario con email");
        }
    }

    private void validateForUpdate(Concesionario concesionario, Concesionario existing) {
        if (concesionario.getEmailContacto() != null && 
            !concesionario.getEmailContacto().equals(existing.getEmailContacto()) && 
            this.repository.existsByEmailContacto(concesionario.getEmailContacto())) {
            throw new DuplicateException(concesionario.getEmailContacto(), "Concesionario con email");
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