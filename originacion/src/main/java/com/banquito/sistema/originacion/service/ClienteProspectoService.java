package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.ClienteProspecto;
import com.banquito.sistema.originacion.repository.ClienteProspectoRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.originacion.exception.ClienteProspectoNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new ClienteProspectoNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public ClienteProspecto getByCedula(String cedula) {
        return clienteProspectoRepository.findByCedula(cedula)
                .orElseThrow(() -> new ClienteProspectoNotFoundException("Cédula: " + cedula));
    }

    @Transactional
    public ClienteProspecto create(ClienteProspecto cliente) {
        if (clienteProspectoRepository.existsByCedula(cliente.getCedula())) {
            throw new AlreadyExistsException(
                "ClienteProspecto",
                "Ya existe un cliente con la cédula: " + cliente.getCedula()
            );
        }

        if (clienteProspectoRepository.existsByEmail(cliente.getEmail())) {
            throw new AlreadyExistsException(
                "ClienteProspecto",
                "Ya existe un cliente con el email: " + cliente.getEmail()
            );
        }

        if (cliente.getEstado() == null || cliente.getEstado().isBlank()) {
            cliente.setEstado("Nuevo");
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

        if (!clienteExistente.getCedula().equals(clienteActualizado.getCedula()) &&
            clienteProspectoRepository.existsByCedula(clienteActualizado.getCedula())) {
            throw new AlreadyExistsException(
                "ClienteProspecto",
                "Ya existe un cliente con la cédula: " + clienteActualizado.getCedula()
            );
        }

        if (!clienteExistente.getEmail().equals(clienteActualizado.getEmail()) &&
            clienteProspectoRepository.existsByEmail(clienteActualizado.getEmail())) {
            throw new AlreadyExistsException(
                "ClienteProspecto",
                "Ya existe un cliente con el email: " + clienteActualizado.getEmail()
            );
        }

        clienteExistente.setCedula(clienteActualizado.getCedula());
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        clienteExistente.setIngresos(clienteActualizado.getIngresos());
        clienteExistente.setEgresos(clienteActualizado.getEgresos());
        clienteExistente.setActividadEconomica(clienteActualizado.getActividadEconomica());
        clienteExistente.setEstado(clienteActualizado.getEstado());

        try {
            return clienteProspectoRepository.save(clienteExistente);
        } catch (Exception e) {
            throw new CreateEntityException(
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