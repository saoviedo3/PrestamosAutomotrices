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
    private final HistorialEstadoService historialEstadoService;

    public VendedorService(VendedorRepository repository, 
                          ConcesionarioService concesionarioService,
                          HistorialEstadoService historialEstadoService) {
        this.repository = repository;
        this.concesionarioService = concesionarioService;
        this.historialEstadoService = historialEstadoService;
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
    public Vendedor findByCodigo(String codigo) {
        Optional<Vendedor> vendedor = this.repository.findByCodigo(codigo);
        if (vendedor.isEmpty()) {
            throw new NotFoundException(codigo, "Vendedor");
        }
        return vendedor.get();
    }

    @Transactional(readOnly = true)
    public Vendedor findByCedula(String cedula) {
        Optional<Vendedor> vendedor = this.repository.findByCedula(cedula);
        if (vendedor.isEmpty()) {
            throw new NotFoundException(cedula, "Vendedor");
        }
        return vendedor.get();
    }

    @Transactional(readOnly = true)
    public List<Vendedor> findByEstado(String estado) {
        return this.repository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Vendedor> findByConcesionarioId(Long concesionarioId) {
        return this.repository.findByConcesionarioId(concesionarioId);
    }

    @Transactional(readOnly = true)
    public List<Vendedor> findByNombre(String nombre) {
        return this.repository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(nombre, nombre);
    }

    public Vendedor create(Vendedor vendedor) {
        this.validateForCreate(vendedor);
        
        // Validar que el concesionario existe y está activo
        Concesionario concesionario = this.concesionarioService.findById(vendedor.getConcesionario().getId());
        if (!"ACTIVO".equals(concesionario.getEstado())) {
            throw new InvalidStateException(concesionario.getEstado(), "ACTIVO", "Concesionario");
        }
        
        vendedor.setEstado("ACTIVO");
        Vendedor savedVendedor = this.repository.save(vendedor);
        
        // Registrar en historial
        this.historialEstadoService.registrarCambioEstado(
            "VENDEDOR", 
            savedVendedor.getId(), 
            null, 
            "ACTIVO", 
            "Creación de vendedor", 
            "SISTEMA"
        );
        
        return savedVendedor;
    }

    public Vendedor update(Long id, Vendedor vendedor) {
        Vendedor existingVendedor = this.findById(id);
        this.validateForUpdate(vendedor, existingVendedor);
        
        // Validar que el concesionario existe y está activo
        if (!vendedor.getConcesionario().getId().equals(existingVendedor.getConcesionario().getId())) {
            Concesionario concesionario = this.concesionarioService.findById(vendedor.getConcesionario().getId());
            if (!"ACTIVO".equals(concesionario.getEstado())) {
                throw new InvalidStateException(concesionario.getEstado(), "ACTIVO", "Concesionario");
            }
        }
        
        vendedor.setId(id);
        vendedor.setFechaCreacion(existingVendedor.getFechaCreacion());
        
        return this.repository.save(vendedor);
    }

    public Vendedor changeState(Long id, String newState, String motivo, String usuario) {
        Vendedor vendedor = this.findById(id);
        String oldState = vendedor.getEstado();
        
        this.validateStateChange(oldState, newState);
        
        vendedor.setEstado(newState);
        Vendedor updatedVendedor = this.repository.save(vendedor);
        
        // Registrar en historial
        this.historialEstadoService.registrarCambioEstado(
            "VENDEDOR", 
            id, 
            oldState, 
            newState, 
            motivo, 
            usuario
        );
        
        return updatedVendedor;
    }

    private void validateForCreate(Vendedor vendedor) {
        if (this.repository.existsByCodigo(vendedor.getCodigo())) {
            throw new DuplicateException(vendedor.getCodigo(), "Vendedor con código");
        }
        
        if (this.repository.existsByCedula(vendedor.getCedula())) {
            throw new DuplicateException(vendedor.getCedula(), "Vendedor con cédula");
        }
        
        if (vendedor.getEmail() != null && this.repository.existsByEmail(vendedor.getEmail())) {
            throw new DuplicateException(vendedor.getEmail(), "Vendedor con email");
        }
    }

    private void validateForUpdate(Vendedor vendedor, Vendedor existing) {
        if (!existing.getCodigo().equals(vendedor.getCodigo()) && 
            this.repository.existsByCodigo(vendedor.getCodigo())) {
            throw new DuplicateException(vendedor.getCodigo(), "Vendedor con código");
        }
        
        if (!existing.getCedula().equals(vendedor.getCedula()) && 
            this.repository.existsByCedula(vendedor.getCedula())) {
            throw new DuplicateException(vendedor.getCedula(), "Vendedor con cédula");
        }
        
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