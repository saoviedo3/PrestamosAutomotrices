package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.ClienteProspecto;
import com.banquito.sistema.originacion.service.ClienteProspectoService;
import com.banquito.sistema.originacion.service.SolicitudCreditoService;
import com.banquito.sistema.originacion.exception.ClienteProspectoNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes-prospectos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteProspectoController {

    private final ClienteProspectoService clienteProspectoService;
    private final SolicitudCreditoService solicitudCreditoService;

    @PostMapping
    public ResponseEntity<ClienteProspecto> crear(@RequestBody ClienteProspecto clienteProspecto) {
        try {
            ClienteProspecto clienteCreado = clienteProspectoService.crear(clienteProspecto);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteProspecto> actualizar(@PathVariable Integer id,
            @RequestBody ClienteProspecto clienteProspecto) {
        try {
            ClienteProspecto clienteActualizado = clienteProspectoService.actualizar(id, clienteProspecto);
            return ResponseEntity.ok(clienteActualizado);
        } catch (ClienteProspectoNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteProspecto> buscarPorId(@PathVariable Integer id) {
        try {
            ClienteProspecto cliente = clienteProspectoService.buscarPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (ClienteProspectoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ClienteProspecto>> listarTodos(@RequestParam(required = false) String estado) {
        try {
            List<ClienteProspecto> clientes;
            if (estado != null && !estado.isEmpty()) {
                clientes = clienteProspectoService.buscarPorEstado(estado);
            } else {
                clientes = clienteProspectoService.listarTodos();
            }
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<ClienteProspecto> buscarPorIdentificacion(@PathVariable String identificacion) {
        try {
            ClienteProspecto cliente = clienteProspectoService.buscarPorIdentificacion(identificacion);
            return ResponseEntity.ok(cliente);
        } catch (ClienteProspectoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteProspecto> buscarPorEmail(@PathVariable String email) {
        try {
            ClienteProspecto cliente = clienteProspectoService.buscarPorEmail(email);
            return ResponseEntity.ok(cliente);
        } catch (ClienteProspectoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<ClienteProspecto> activar(@PathVariable Integer id) {
        try {
            ClienteProspecto cliente = clienteProspectoService.activar(id);
            return ResponseEntity.ok(cliente);
        } catch (ClienteProspectoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<ClienteProspecto> desactivar(@PathVariable Integer id) {
        try {
            ClienteProspecto cliente = clienteProspectoService.desactivar(id);
            return ResponseEntity.ok(cliente);
        } catch (ClienteProspectoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            clienteProspectoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (ClienteProspectoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(ClienteProspectoNotFoundException.class)
    public ResponseEntity<Void> handleClienteProspectoNotFoundException(ClienteProspectoNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteProspecto>> buscarPorNombreOApellido(@RequestParam String termino) {
        List<ClienteProspecto> clientes = clienteProspectoService.buscarPorNombreOApellido(termino);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}/puede-solicitar-credito")
    public ResponseEntity<Boolean> puedeSolicitarCredito(@PathVariable Integer id) {
        boolean puede = clienteProspectoService.puedesolicitarCredito(id);
        return ResponseEntity.ok(puede);
    }

    @GetMapping("/{id}/capacidad-pago")
    public ResponseEntity<BigDecimal> calcularCapacidadPago(@PathVariable Integer id) {
        BigDecimal capacidad = clienteProspectoService.calcularCapacidadPago(id);
        return ResponseEntity.ok(capacidad);
    }

    @PostMapping("/{id}/simular-credito")
    public ResponseEntity<Map<String, Object>> simularCredito(
            @PathVariable Integer id,
            @RequestParam BigDecimal monto,
            @RequestParam BigDecimal tasa,
            @RequestParam Integer plazo) {

        BigDecimal capacidadPago = clienteProspectoService.calcularCapacidadPago(id);
        BigDecimal cuotaMensual = solicitudCreditoService.calcularCuotaMensual(monto, tasa, plazo);

        boolean esViable = cuotaMensual.compareTo(capacidadPago) <= 0;
        BigDecimal porcentajeCompromiso = cuotaMensual
                .divide(capacidadPago, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        Map<String, Object> simulacion = new HashMap<>();
        simulacion.put("montoSolicitado", monto);
        simulacion.put("plazoMeses", plazo);
        simulacion.put("tasaInteres", tasa);
        simulacion.put("cuotaMensual", cuotaMensual);
        simulacion.put("capacidadPago", capacidadPago);
        simulacion.put("esViable", esViable);
        simulacion.put("porcentajeCompromiso", porcentajeCompromiso);

        return ResponseEntity.ok(simulacion);
    }
}
