package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.exception.ValidationException;
import com.banquito.sistema.originacion.model.ClienteProspecto;
import com.banquito.sistema.originacion.repository.ClienteProspectoRepository;
import com.banquito.sistema.originacion.exception.ClienteProspectoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteProspectoService {

    private final ClienteProspectoRepository clienteProspectoRepository;

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_INACTIVO = "INACTIVO";
    private static final String ESTADO_PROSPECTO = "PROSPECTO";
    private static final String ESTADO_RECHAZADO = "RECHAZADO";

    // Patrones de validación
    private static final Pattern CEDULA_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^\\d{9,10}$");

    /**
     * Crear un nuevo cliente prospecto
     */
    public ClienteProspecto crear(ClienteProspecto clienteProspecto) {
        try {
            validarClienteProspecto(clienteProspecto);
            validarCedulaUnica(clienteProspecto.getCedula());
            
            if (clienteProspecto.getEmail() != null && !clienteProspecto.getEmail().isEmpty()) {
                validarEmailUnico(clienteProspecto.getEmail());
            }
            
            clienteProspecto.setEstado(ESTADO_PROSPECTO);
            return clienteProspectoRepository.save(clienteProspecto);
        } catch (Exception e) {
            throw new BusinessException("Error al crear cliente prospecto: " + e.getMessage(), "CREAR_CLIENTE_PROSPECTO");
        }
    }

    /**
     * Actualizar cliente prospecto existente
     */
    public ClienteProspecto actualizar(Integer id, ClienteProspecto clienteProspecto) {
        try {
            ClienteProspecto clienteExistente = buscarPorId(id);
            
            validarClienteProspecto(clienteProspecto);
            
            // Validar cédula única solo si cambió
            if (!clienteExistente.getCedula().equals(clienteProspecto.getCedula())) {
                validarCedulaUnica(clienteProspecto.getCedula());
            }
            
            // Validar email único solo si cambió
            if (clienteProspecto.getEmail() != null && 
                !clienteProspecto.getEmail().equals(clienteExistente.getEmail())) {
                validarEmailUnico(clienteProspecto.getEmail());
            }
            
            clienteProspecto.setIdClienteProspecto(id);
            clienteProspecto.setEstado(clienteExistente.getEstado()); // Mantener estado actual
            
            return clienteProspectoRepository.save(clienteProspecto);
        } catch (Exception e) {
            throw new BusinessException("Error al actualizar cliente prospecto: " + e.getMessage(), "ACTUALIZAR_CLIENTE_PROSPECTO");
        }
    }

    /**
     * Buscar cliente por ID
     */
    @Transactional(readOnly = true)
    public ClienteProspecto buscarPorId(Integer id) {
        return clienteProspectoRepository.findById(id)
                .orElseThrow(() -> new ClienteProspectoNotFoundException(id.toString(), "ID"));
    }

    /**
     * Buscar cliente por cédula
     */
    @Transactional(readOnly = true)
    public ClienteProspecto buscarPorCedula(String cedula) {
        return clienteProspectoRepository.findByCedula(cedula)
                .orElseThrow(() -> new NotFoundException(cedula, "ClienteProspecto"));
    }

    /**
     * Listar todos los clientes activos
     */
    @Transactional(readOnly = true)
    public List<ClienteProspecto> listarActivos() {
        return clienteProspectoRepository.findByEstado(ESTADO_ACTIVO);
    }

    /**
     * Listar clientes por estado
     */
    @Transactional(readOnly = true)
    public List<ClienteProspecto> listarPorEstado(String estado) {
        return clienteProspectoRepository.findByEstado(estado);
    }

    /**
     * Buscar clientes por nombre o apellido
     */
    @Transactional(readOnly = true)
    public List<ClienteProspecto> buscarPorNombreOApellido(String termino) {
        return clienteProspectoRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(termino, termino);
    }

    /**
     * Activar cliente prospecto
     */
    public ClienteProspecto activar(Integer id) {
        ClienteProspecto cliente = buscarPorId(id);
        cliente.setEstado(ESTADO_ACTIVO);
        return clienteProspectoRepository.save(cliente);
    }

    /**
     * Desactivar cliente prospecto
     */
    public ClienteProspecto desactivar(Integer id) {
        ClienteProspecto cliente = buscarPorId(id);
        cliente.setEstado(ESTADO_INACTIVO);
        return clienteProspectoRepository.save(cliente);
    }

    /**
     * Rechazar cliente prospecto
     */
    public ClienteProspecto rechazar(Integer id) {
        ClienteProspecto cliente = buscarPorId(id);
        cliente.setEstado(ESTADO_RECHAZADO);
        return clienteProspectoRepository.save(cliente);
    }

    /**
     * Verificar si el cliente puede solicitar crédito
     */
    @Transactional(readOnly = true)
    public boolean puedesolicitarCredito(Integer id) {
        ClienteProspecto cliente = buscarPorId(id);
        return ESTADO_ACTIVO.equals(cliente.getEstado()) && 
               cliente.getIngresos() != null && 
               cliente.getIngresos().compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Calcular capacidad de pago del cliente
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularCapacidadPago(Integer id) {
        ClienteProspecto cliente = buscarPorId(id);
        
        if (cliente.getIngresos() == null || cliente.getEgresos() == null) {
            throw new BusinessException("Cliente no tiene información financiera completa", "CALCULAR_CAPACIDAD_PAGO");
        }
        
        BigDecimal ingresoNeto = cliente.getIngresos().subtract(cliente.getEgresos());
        // Asumimos que puede comprometer máximo el 30% de sus ingresos netos
        return ingresoNeto.multiply(new BigDecimal("0.30"));
    }

    // Métodos privados de validación
    private void validarClienteProspecto(ClienteProspecto cliente) {
        if (cliente.getCedula() == null || cliente.getCedula().trim().isEmpty()) {
            throw new ValidationException("cedula", "requerida");
        }
        
        if (!CEDULA_PATTERN.matcher(cliente.getCedula()).matches()) {
            throw new ValidationException("cedula", "debe tener 10 dígitos");
        }
        
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new ValidationException("nombre", "requerido");
        }
        
        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
            throw new ValidationException("apellido", "requerido");
        }
        
        if (cliente.getEmail() != null && !cliente.getEmail().isEmpty() && 
            !EMAIL_PATTERN.matcher(cliente.getEmail()).matches()) {
            throw new ValidationException("email", "formato inválido");
        }
        
        if (cliente.getTelefono() != null && !cliente.getTelefono().isEmpty() && 
            !TELEFONO_PATTERN.matcher(cliente.getTelefono()).matches()) {
            throw new ValidationException("telefono", "debe tener 9 o 10 dígitos");
        }
        
        if (cliente.getIngresos() != null && cliente.getIngresos().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("ingresos", "no pueden ser negativos");
        }
        
        if (cliente.getEgresos() != null && cliente.getEgresos().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("egresos", "no pueden ser negativos");
        }
    }

    private void validarCedulaUnica(String cedula) {
        if (clienteProspectoRepository.existsByCedula(cedula)) {
            throw new ValidationException("cedula", "ya existe en el sistema");
        }
    }

    private void validarEmailUnico(String email) {
        if (clienteProspectoRepository.existsByEmail(email)) {
            throw new ValidationException("email", "ya existe en el sistema");
        }
    }

    @Transactional(readOnly = true)
    public List<ClienteProspecto> listarTodos() {
        return clienteProspectoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ClienteProspecto buscarPorIdentificacion(String identificacion) {
        return clienteProspectoRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new ClienteProspectoNotFoundException(identificacion, "identificación"));
    }

    @Transactional(readOnly = true)
    public ClienteProspecto buscarPorEmail(String email) {
        return clienteProspectoRepository.findByEmail(email)
                .orElseThrow(() -> new ClienteProspectoNotFoundException(email, "email"));
    }

    @Transactional(readOnly = true)
    public List<ClienteProspecto> buscarPorEstado(String estado) {
        return clienteProspectoRepository.findByEstado(estado);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!clienteProspectoRepository.existsById(id)) {
            throw new ClienteProspectoNotFoundException(id.toString(), "ID");
        }
        clienteProspectoRepository.deleteById(id);
    }
} 