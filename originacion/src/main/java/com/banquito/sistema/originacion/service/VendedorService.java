package com.banquito.sistema.originacion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.sistema.originacion.exception.DuplicateException;
import com.banquito.sistema.originacion.exception.InvalidStateException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.model.Concesionario;
import com.banquito.sistema.originacion.model.Vendedor;
import com.banquito.sistema.originacion.repository.VendedorRepository;

@Service
@Transactional
public class VendedorService {

    private final VendedorRepository repository;
    private final ConcesionarioService concesionarioService;

    public VendedorService(VendedorRepository repository,
            ConcesionarioService concesionarioService,
            HistorialEstadoService historialEstadoService) {
        this.repository = repository;
        this.concesionarioService = concesionarioService;

    }

    @Transactional(readOnly = true)
    public List<Vendedor> findAll() {
        return this.repository.findAll();
    }

    @Transactional(readOnly = true)
    public Vendedor findById(Long id) {
        Optional<Vendedor> vendedor = this.repository.findById(id);
        if (vendedor.isEmpty()) {
            throw new NotFoundException(id.toString(), "Vendedor");
        }
        return vendedor.get();
    }

    @Transactional(readOnly = true)
    public List<Vendedor> findByEstado(String estado) {
        return this.repository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Vendedor> findByConcesionarioId(Long concesionarioId) {
        return this.repository.findByIdConcesionario(concesionarioId);
    }

    @Transactional(readOnly = true)
    public List<Vendedor> findBynombre(String nombre) {
        return this.repository.findBynombreContainingIgnoreCase(nombre);
    }

    public Vendedor create(Vendedor vendedor) {
        this.validateForCreate(vendedor);

        //Guarda el vendedor (id, versión, etc. se generan aquí)
        Vendedor saved = this.repository.save(vendedor);

        // Carga el concesionario y así lo tendrás en la respuesta
        Concesionario c = this.concesionarioService.findById(saved.getIdConcesionario());
        saved.setConcesionario(c);

        return saved;
    }

    public Vendedor update(Long id, Vendedor vendedor) {
        Vendedor existingVendedor = this.findById(id);
        this.validateForUpdate(vendedor, existingVendedor);

        // Validar que el concesionario existe y está activo
        if (!vendedor.getIdConcesionario().equals(existingVendedor.getIdConcesionario())) {
            Concesionario concesionario = this.concesionarioService.findById(vendedor.getIdConcesionario());
            if (!"ACTIVO".equals(concesionario.getEstado())) {
                throw new InvalidStateException(concesionario.getEstado(), "ACTIVO", "Concesionario");
            }
        }

        vendedor.setId(id);
        vendedor.setVersion(existingVendedor.getVersion());

        return this.repository.save(vendedor);
    }

    public Vendedor changeState(Long id, String newState, String motivo, String usuario) {
        Vendedor vendedor = this.findById(id);
        String oldState = vendedor.getEstado();

        this.validateStateChange(oldState, newState);

        vendedor.setEstado(newState);
        Vendedor updatedVendedor = this.repository.save(vendedor);

        return updatedVendedor;
    }

    private void validateForCreate(Vendedor vendedor) {
        if (vendedor.getEmail() != null && this.repository.existsByEmail(vendedor.getEmail())) {
            throw new DuplicateException(vendedor.getEmail(), "Vendedor con email");
        }
    }

    private void validateForUpdate(Vendedor vendedor, Vendedor existing) {
        if (vendedor.getEmail() != null &&
                !vendedor.getEmail().equals(existing.getEmail()) &&
                this.repository.existsByEmail(vendedor.getEmail())) {
            throw new DuplicateException(vendedor.getEmail(), "Vendedor con email");
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