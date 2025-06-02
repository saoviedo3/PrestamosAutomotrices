package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.ClienteProspecto;
import com.banquito.sistema.originacion.repository.ClienteProspectoRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.InvalidDataException;
import com.banquito.sistema.exception.UpdateEntityException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClienteProspectoService {
    private final ClienteProspectoRepository clienteProspectoRepository;

    public ClienteProspectoService(ClienteProspectoRepository clienteProspectoRepository) {
        this.clienteProspectoRepository = clienteProspectoRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteProspecto> getAll() {
        return clienteProspectoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ClienteProspecto getById(Long id) {
        return clienteProspectoRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("ClienteProspecto", "No existe un cliente prospecto con el id: " + id));
    }

    @Transactional(readOnly = true)
    public ClienteProspecto getByCedula(String cedula) {
        return clienteProspectoRepository.findByCedula(cedula)
                .orElseThrow(() -> new InvalidDataException("ClienteProspecto", "No existe un cliente prospecto con la cédula: " + cedula));
    }

    @Transactional(readOnly = true)
    public List<ClienteProspecto> getByEstado(String estado) {
        return clienteProspectoRepository.findByEstado(estado);
    }

    @Transactional
    public ClienteProspecto create(ClienteProspecto cliente) {
        // Validar que no exista un cliente con la misma cédula
        if (clienteProspectoRepository.existsByCedula(cliente.getCedula())) {
            throw new AlreadyExistsException(
                "ClienteProspecto",
                "Ya existe un cliente prospecto con la cédula: " + cliente.getCedula()
            );
        }

        // Validar que la cédula tenga el formato correcto (10 dígitos)
        if (cliente.getCedula() == null || !cliente.getCedula().matches("\\d{10}")) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "La cédula debe tener 10 dígitos numéricos"
            );
        }

        // Validar que el email tenga formato válido
        if (cliente.getEmail() != null && !cliente.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "El email no tiene un formato válido"
            );
        }

        // Validar que el teléfono tenga formato válido (10 dígitos)
        if (cliente.getTelefono() != null && !cliente.getTelefono().matches("\\d{10}")) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "El teléfono debe tener 10 dígitos numéricos"
            );
        }

        // Validar que los ingresos y egresos sean positivos
        if (cliente.getIngresos() != null && cliente.getIngresos().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "Los ingresos deben ser mayores a cero"
            );
        }

        if (cliente.getEgresos() != null && cliente.getEgresos().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "Los egresos deben ser mayores a cero"
            );
        }

        // Establecer valores por defecto
        if (cliente.getEstado() == null || cliente.getEstado().isBlank()) {
            cliente.setEstado("Activo");
        }

        try {
            return clienteProspectoRepository.save(cliente);
        } catch (Exception e) {
            throw new CreateEntityException(
                "ClienteProspecto",
                "Error al crear ClienteProspecto: " + e.getMessage()
            );
        }
    }

    @Transactional
    public ClienteProspecto update(Long id, ClienteProspecto clienteActualizado) {
        ClienteProspecto clienteExistente = getById(id);

        // Validar que la nueva cédula no exista en otro cliente
        if (!clienteExistente.getCedula().equals(clienteActualizado.getCedula()) &&
            clienteProspectoRepository.existsByCedula(clienteActualizado.getCedula())) {
            throw new AlreadyExistsException(
                "ClienteProspecto",
                "Ya existe un cliente prospecto con la cédula: " + clienteActualizado.getCedula()
            );
        }

        // Validar que la cédula tenga el formato correcto
        if (clienteActualizado.getCedula() == null || !clienteActualizado.getCedula().matches("\\d{10}")) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "La cédula debe tener 10 dígitos numéricos"
            );
        }

        // Validar que el email tenga formato válido
        if (clienteActualizado.getEmail() != null && !clienteActualizado.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "El email no tiene un formato válido"
            );
        }

        // Validar que el teléfono tenga formato válido
        if (clienteActualizado.getTelefono() != null && !clienteActualizado.getTelefono().matches("\\d{10}")) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "El teléfono debe tener 10 dígitos numéricos"
            );
        }

        // Validar que los ingresos y egresos sean positivos
        if (clienteActualizado.getIngresos() != null && clienteActualizado.getIngresos().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "Los ingresos deben ser mayores a cero"
            );
        }

        if (clienteActualizado.getEgresos() != null && clienteActualizado.getEgresos().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "Los egresos deben ser mayores a cero"
            );
        }

        // Actualizar campos
        clienteExistente.setCedula(clienteActualizado.getCedula());
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        clienteExistente.setIngresos(clienteActualizado.getIngresos());
        clienteExistente.setEgresos(clienteActualizado.getEgresos());
        clienteExistente.setActividadEconomica(clienteActualizado.getActividadEconomica());
        clienteExistente.setEstado(clienteActualizado.getEstado());

        try {
            return clienteProspectoRepository.save(clienteExistente);
        } catch (Exception e) {
            throw new UpdateEntityException(
                "ClienteProspecto",
                "Error al actualizar ClienteProspecto: " + e.getMessage()
            );
        }
    }

    @Transactional(readOnly = true)
    public List<ClienteProspecto> findByEstado(String estado) {
        return clienteProspectoRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<ClienteProspecto> searchByNombreOrApellido(String termino) {
        return clienteProspectoRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(termino, termino);
    }
} 