package com.banquito.sistema.originacion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.UpdateEntityException;
import com.banquito.sistema.originacion.exception.DuplicateException;
import com.banquito.sistema.originacion.exception.InvalidStateException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.model.Concesionario;
import com.banquito.sistema.originacion.model.Vendedor;
import com.banquito.sistema.originacion.repository.VendedorRepository;

@Service
public class VendedorService {

    private final VendedorRepository repository;
    private final ConcesionarioService concesionarioService;

    public VendedorService(VendedorRepository repository,
            ConcesionarioService concesionarioService) {
        this.repository = repository;
        this.concesionarioService = concesionarioService;
    }

    public List<Vendedor> findAll() {
        return this.repository.findAll();
    }

    public Vendedor findById(Long id) {
        Optional<Vendedor> vendedor = this.repository.findById(id);
        if (vendedor.isEmpty()) {
            throw new NotFoundException(id.toString(), "Vendedor");
        }
        return vendedor.get();
    }

    @Transactional
    public Vendedor create(Vendedor vendedor) {
        try {
            // Normalizamos email antes de validar
            if (vendedor.getEmail() != null) {
                String emailNormalized = vendedor.getEmail().trim().toLowerCase();
                vendedor.setEmail(emailNormalized);
            }

            validateForCreate(vendedor);

            Vendedor saved = this.repository.save(vendedor);

            // Cargar concesionario en la respuesta
            Concesionario c = this.concesionarioService.findById(saved.getIdConcesionario());
            saved.setConcesionario(c);

            return saved;
        } catch (Exception e) {
            throw new CreateEntityException("Vendedor", e.getMessage());
        }
    }

    @Transactional
    public Vendedor update(Long id, Vendedor vendedor) {
        try {
            Vendedor existingVendedor = this.findById(id);
            validateForUpdate(vendedor, existingVendedor);

            // Si cambió de concesionario, validamos que exista y esté ACTIVO
            if (!vendedor.getIdConcesionario().equals(existingVendedor.getIdConcesionario())) {
                Concesionario concesionarioNuevo = this.concesionarioService.findById(vendedor.getIdConcesionario());
                if (!"ACTIVO".equals(concesionarioNuevo.getEstado())) {
                    throw new InvalidStateException(concesionarioNuevo.getEstado(), "ACTIVO", "Concesionario");
                }
            }

            // Normalizar email
            if (vendedor.getEmail() != null) {
                vendedor.setEmail(vendedor.getEmail().trim().toLowerCase());
            }

            // Aseguramos que sea una actualización, no inserción
            vendedor.setId(id);
            vendedor.setVersion(existingVendedor.getVersion());

            // Guardamos el vendedor “en crudo”
            Vendedor guardado = this.repository.save(vendedor);

            // Volvemos a cargar el Concesionario para que no venga null
            Concesionario c = this.concesionarioService.findById(guardado.getIdConcesionario());
            guardado.setConcesionario(c);

            return guardado;
        } catch (Exception e) {
            throw new UpdateEntityException("Vendedor", e.getMessage());
        }
    }

    @Transactional
    public Vendedor changeState(Long id, String newState, String motivo, String usuario) {
        Vendedor vendedor = this.findById(id);
        String oldState = vendedor.getEstado();

        validateStateChange(oldState, newState);
        vendedor.setEstado(newState);

        try {
            return this.repository.save(vendedor);
        } catch (Exception e) {
            throw new UpdateEntityException("Vendedor", e.getMessage());
        }
    }

    private void validateForCreate(Vendedor vendedor) {
        if (vendedor.getEmail() != null 
                && this.repository.existsByEmail(vendedor.getEmail().trim().toLowerCase())) {
            throw new DuplicateException(vendedor.getEmail(), "Vendedor con email");
        }
    }

    private void validateForUpdate(Vendedor vendedor, Vendedor existing) {
        if (vendedor.getEmail() != null && !vendedor.getEmail().isBlank()) {
            String nuevoEmail = vendedor.getEmail().trim().toLowerCase();
            String emailExistente = existing.getEmail() != null
                    ? existing.getEmail().trim().toLowerCase()
                    : "";

            // Si realmente se cambió el email (ignorando mayúsculas/minúsculas y espacios)
            if (!nuevoEmail.equals(emailExistente)) {
                // Y ese nuevo email ya está en BD (pertenece a otro registro) → excepción
                if (this.repository.existsByEmail(nuevoEmail)) {
                    throw new DuplicateException(vendedor.getEmail(), "Vendedor con email");
                }
            }
            // Asignamos la versión normalizada para que se guarde en minúsculas
            vendedor.setEmail(nuevoEmail);
        }
    }

    private void validateStateChange(String currentState, String newState) {
        if (currentState.equals(newState)) {
            return;
        }

        switch (currentState) {
            case "ACTIVO":
                if (!newState.equals("INACTIVO") && !newState.equals("SUSPENDIDO")) {
                    throw new InvalidStateException(currentState, newState, "Vendedor");
                }
                break;
            case "INACTIVO":
                if (!newState.equals("ACTIVO")) {
                    throw new InvalidStateException(currentState, newState, "Vendedor");
                }
                break;
            case "SUSPENDIDO":
                if (!newState.equals("ACTIVO") && !newState.equals("INACTIVO")) {
                    throw new InvalidStateException(currentState, newState, "Vendedor");
                }
                break;
            default:
                throw new InvalidStateException(currentState, newState, "Vendedor");
        }
    }
}
