package com.banquito.sistema.originacion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.sistema.originacion.exception.DuplicateException;
import com.banquito.sistema.originacion.exception.InvalidStateException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.UpdateEntityException;
import com.banquito.sistema.originacion.model.Concesionario;
import com.banquito.sistema.originacion.repository.ConcesionarioRepository;

@Service
public class ConcesionarioService {

    private final ConcesionarioRepository repository;

    public ConcesionarioService(ConcesionarioRepository repository) {
        this.repository = repository;
    }

    public List<Concesionario> findAll() {
        return this.repository.findAll();
    }

    public Concesionario findById(Long id) {
        Optional<Concesionario> concesionario = this.repository.findById(id);
        if (concesionario.isEmpty()) {
            throw new NotFoundException(id.toString(), "Concesionario");
        }
        return concesionario.get();
    }

    @Transactional
    public Concesionario create(Concesionario concesionario) {
        try {
            this.validateForCreate(concesionario);
            if (concesionario.getEstado() == null || concesionario.getEstado().trim().isEmpty()) {
                concesionario.setEstado("ACTIVO");
            }
            Concesionario savedConcesionario = this.repository.save(concesionario);
            // Sincronización bidireccional opcional:
            if (savedConcesionario.getVehiculos() != null) {
                savedConcesionario.getVehiculos().forEach(v -> v.setConcesionario(savedConcesionario));
            }
            return savedConcesionario;
        } catch (Exception e) {
            throw new CreateEntityException("Concesionario", e.getMessage());
        }
    }

    @Transactional
    public Concesionario update(Long id, Concesionario concesionario) {
        try {
            Concesionario existingConcesionario = this.findById(id);
            this.validateForUpdate(concesionario, existingConcesionario);

            concesionario.setIdConcesionario(id);
            concesionario.setVersion(existingConcesionario.getVersion());

            if (concesionario.getVehiculos() != null) {
                existingConcesionario.setVehiculos(concesionario.getVehiculos());
                existingConcesionario.getVehiculos().forEach(v -> v.setConcesionario(existingConcesionario));
            }

            existingConcesionario.setRazonSocial(concesionario.getRazonSocial());
            existingConcesionario.setDireccion(concesionario.getDireccion());
            existingConcesionario.setTelefono(concesionario.getTelefono());
            existingConcesionario.setEmailContacto(concesionario.getEmailContacto());
            existingConcesionario.setEstado(concesionario.getEstado());

            return this.repository.save(existingConcesionario);
        } catch (Exception e) {
            throw new UpdateEntityException("Concesionario", e.getMessage());
        }
    }

    public Concesionario changeState(Long id, String newState, String motivo, String usuario) {
        Concesionario concesionario = this.findById(id);  // Lanza NotFoundException si no existe
        String oldState = concesionario.getEstado();

        this.validateStateChange(oldState, newState);     // Lanza InvalidStateException si estado no válido

        concesionario.setEstado(newState);

        try {
            return this.repository.save(concesionario);    // Captura errores solo al persistir
        } catch (Exception e) {
            throw new UpdateEntityException("Concesionario", e.getMessage());
        }
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
