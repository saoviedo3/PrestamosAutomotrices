package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.Concesionario;
import com.banquito.sistema.originacion.model.IdentificadorVehiculo;
import com.banquito.sistema.originacion.model.Vehiculo;
import com.banquito.sistema.originacion.repository.ConcesionarioRepository;
import com.banquito.sistema.originacion.repository.IdentificadorVehiculoRepository;
import com.banquito.sistema.originacion.repository.VehiculoRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.InvalidDataException;
import com.banquito.sistema.originacion.exception.ConcesionarioNotFoundException;
import com.banquito.sistema.originacion.exception.IdentificadorVehiculoNotFoundException;
import com.banquito.sistema.originacion.exception.VehiculoNotFoundException;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;
    private final IdentificadorVehiculoRepository identificadorRepo;
    private final ConcesionarioRepository concesionarioRepo;

    public VehiculoService(VehiculoRepository vehiculoRepository,
            IdentificadorVehiculoRepository identificadorRepo,
            ConcesionarioRepository concesionarioRepo) {
        this.vehiculoRepository = vehiculoRepository;
        this.identificadorRepo = identificadorRepo;
        this.concesionarioRepo = concesionarioRepo;
    }

    public List<Vehiculo> getAllVehiculos() {
        return vehiculoRepository.findAll();
    }

    public Vehiculo getVehiculoById(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new VehiculoNotFoundException(id));
    }

    @Transactional
    public Vehiculo createVehiculo(Vehiculo vehiculo) {

        // 1. Validaciones básicas de datos
        if (vehiculo.getMarca() == null || vehiculo.getMarca().isBlank()) {
            throw new InvalidDataException("Vehiculo", "La marca es obligatoria");
        }
        if (vehiculo.getModelo() == null || vehiculo.getModelo().isBlank()) {
            throw new InvalidDataException("Vehiculo", "El modelo es obligatorio");
        }
        if (vehiculo.getAnio() == null || vehiculo.getAnio() < 1886) {
            throw new InvalidDataException("Vehiculo", "El año no es válido");
        }
        if (vehiculo.getValor() == null || vehiculo.getValor() <= 0) {
            throw new InvalidDataException("Vehiculo", "El valor debe ser positivo");
        }
        if (vehiculo.getColor() == null || vehiculo.getColor().isBlank()) {
            throw new InvalidDataException("Vehiculo", "El color es obligatorio");
        }
        if (vehiculo.getExtras() == null) {
            vehiculo.setExtras(""); // Permitimos cadena vacía, pero no nulo
        }

        // 2. Validar FK IdentificadorVehiculo
        Long identId = vehiculo.getIdIdentificadorVehiculo();
        IdentificadorVehiculo ident = identificadorRepo.findById(identId)
                .orElseThrow(() -> new IdentificadorVehiculoNotFoundException(identId));

        // 3. Validar FK Concesionario
        Long consId = vehiculo.getIdConcesionario();
        Concesionario cons = concesionarioRepo.findById(consId)
                .orElseThrow(() -> new ConcesionarioNotFoundException(consId));

        // 4. Evitar duplicados por identificador
        if (vehiculoRepository.existsByIdentificadorVehiculoId(identId)) {
            throw new AlreadyExistsException(
                    "Vehiculo",
                    "Ya existe un Vehiculo con IdentificadorVehiculo id " + identId);
        }

        // 5. Asociar las referencias antes de guardar
        vehiculo.setIdentificadorVehiculo(ident);
        vehiculo.setConcesionario(cons);
        if (vehiculo.getEstado() == null || vehiculo.getEstado().isBlank()) {
            vehiculo.setEstado("Disponible");
        }

        // 6. Persistencia y manejo de errores en BD
        try {
            return vehiculoRepository.save(vehiculo);
        } catch (DataAccessException ex) {
            throw new CreateEntityException(
                    "Vehiculo",
                    "Error al crear Vehiculo en la base de datos: " + ex.getMostSpecificCause().getMessage());
        }
    }

    @Transactional
    public Vehiculo updateVehiculo(Long id, Vehiculo datos) {
        try {
            Vehiculo existente = getVehiculoById(id);

            // 7. Validaciones básicas
            if (datos.getMarca() == null || datos.getMarca().isBlank()) {
                throw new InvalidDataException("Vehiculo", "La marca es obligatoria");
            }
            if (datos.getModelo() == null || datos.getModelo().isBlank()) {
                throw new InvalidDataException("Vehiculo", "El modelo es obligatorio");
            }
            if (datos.getAnio() == null || datos.getAnio() < 1886) {
                throw new InvalidDataException("Vehiculo", "El año no es válido");
            }
            if (datos.getValor() == null || datos.getValor() <= 0) {
                throw new InvalidDataException("Vehiculo", "El valor debe ser positivo");
            }
            if (datos.getColor() == null || datos.getColor().isBlank()) {
                throw new InvalidDataException("Vehiculo", "El color es obligatorio");
            }
            if (datos.getExtras() == null) {
                datos.setExtras(""); 
            }
            

            existente.setMarca(datos.getMarca());
            existente.setModelo(datos.getModelo());
            existente.setAnio(datos.getAnio());
            existente.setValor(datos.getValor());
            existente.setColor(datos.getColor());
            existente.setExtras(datos.getExtras());
            existente.setEstado(datos.getEstado());

            return vehiculoRepository.save(existente);

        } catch (VehiculoNotFoundException e) {
            throw e;
        } catch (InvalidDataException e) {
            throw e;
        } catch (DataAccessException ex) {
            throw new CreateEntityException(
                    "Vehiculo",
                    "Error al actualizar Vehiculo en BD: " + ex.getMostSpecificCause().getMessage());
        }
    }

    

    // 8. Método para marcar un Vehiculo como "Vendido"
    @Transactional
    public Vehiculo marcarComoVendido(Long id) {
        try {
            Vehiculo existente = getVehiculoById(id);
            existente.setEstado("Vendido");
            return vehiculoRepository.save(existente);
        } catch (VehiculoNotFoundException e) {
            throw e;
        } catch (DataAccessException ex) {
            throw new CreateEntityException(
                    "Vehiculo",
                    "Error al cambiar estado a Vendido en BD: " + ex.getMostSpecificCause().getMessage());
        }
    }

}
