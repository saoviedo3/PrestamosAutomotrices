package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.dto.ClienteProspectoDTO;
import com.banquito.sistema.originacion.dto.SimulacionCreditoDTO;
import com.banquito.sistema.originacion.mapper.ClienteProspectoMapper;
import com.banquito.sistema.originacion.model.ClienteProspecto;
import com.banquito.sistema.originacion.service.ClienteProspectoService;
import com.banquito.sistema.originacion.service.SolicitudCreditoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes-prospectos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteProspectoController {

    private final ClienteProspectoService clienteProspectoService;
    private final SolicitudCreditoService solicitudCreditoService;
    private final ClienteProspectoMapper clienteProspectoMapper;

    @PostMapping
    public ResponseEntity<ClienteProspectoDTO> crear(@RequestBody ClienteProspectoDTO clienteDTO) {
        ClienteProspecto cliente = clienteProspectoMapper.toEntity(clienteDTO);
        ClienteProspecto clienteCreado = clienteProspectoService.crear(cliente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteProspectoMapper.toDTO(clienteCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteProspectoDTO> actualizar(@PathVariable Integer id, 
                                                        @RequestBody ClienteProspectoDTO clienteDTO) {
        ClienteProspecto cliente = clienteProspectoMapper.toEntity(clienteDTO);
        ClienteProspecto clienteActualizado = clienteProspectoService.actualizar(id, cliente);
        return ResponseEntity.ok(clienteProspectoMapper.toDTO(clienteActualizado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteProspectoDTO> buscarPorId(@PathVariable Integer id) {
        ClienteProspecto cliente = clienteProspectoService.buscarPorId(id);
        return ResponseEntity.ok(clienteProspectoMapper.toDTO(cliente));
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<ClienteProspectoDTO> buscarPorCedula(@PathVariable String cedula) {
        ClienteProspecto cliente = clienteProspectoService.buscarPorCedula(cedula);
        return ResponseEntity.ok(clienteProspectoMapper.toDTO(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteProspectoDTO>> listarTodos(@RequestParam(required = false) String estado) {
        List<ClienteProspecto> clientes;
        
        if (estado != null && !estado.isEmpty()) {
            clientes = clienteProspectoService.listarPorEstado(estado);
        } else {
            clientes = clienteProspectoService.listarActivos();
        }
        
        List<ClienteProspectoDTO> clientesDTO = clientes.stream()
                .map(clienteProspectoMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(clientesDTO);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteProspectoDTO>> buscarPorNombreOApellido(@RequestParam String termino) {
        List<ClienteProspecto> clientes = clienteProspectoService.buscarPorNombreOApellido(termino);
        List<ClienteProspectoDTO> clientesDTO = clientes.stream()
                .map(clienteProspectoMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(clientesDTO);
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<ClienteProspectoDTO> activar(@PathVariable Integer id) {
        ClienteProspecto cliente = clienteProspectoService.activar(id);
        return ResponseEntity.ok(clienteProspectoMapper.toDTO(cliente));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<ClienteProspectoDTO> desactivar(@PathVariable Integer id) {
        ClienteProspecto cliente = clienteProspectoService.desactivar(id);
        return ResponseEntity.ok(clienteProspectoMapper.toDTO(cliente));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<ClienteProspectoDTO> rechazar(@PathVariable Integer id) {
        ClienteProspecto cliente = clienteProspectoService.rechazar(id);
        return ResponseEntity.ok(clienteProspectoMapper.toDTO(cliente));
    }

    @GetMapping("/{id}/puede-solicitar-credito")
    public ResponseEntity<Boolean> puedesolicitarCredito(@PathVariable Integer id) {
        boolean puede = clienteProspectoService.puedesolicitarCredito(id);
        return ResponseEntity.ok(puede);
    }

    @GetMapping("/{id}/capacidad-pago")
    public ResponseEntity<BigDecimal> calcularCapacidadPago(@PathVariable Integer id) {
        BigDecimal capacidad = clienteProspectoService.calcularCapacidadPago(id);
        return ResponseEntity.ok(capacidad);
    }

    @PostMapping("/{id}/simular-credito")
    public ResponseEntity<SimulacionCreditoDTO> simularCredito(@PathVariable Integer id,
                                                             @RequestParam BigDecimal monto,
                                                             @RequestParam BigDecimal tasa,
                                                             @RequestParam Integer plazo) {
        // Calcular capacidad de pago
        BigDecimal capacidadPago = clienteProspectoService.calcularCapacidadPago(id);
        
        // Simular crédito
        BigDecimal cuotaMensual = solicitudCreditoService.calcularCuotaMensual(monto, tasa, plazo);
        
        // Evaluar viabilidad
        boolean esViable = cuotaMensual.compareTo(capacidadPago) <= 0;
        BigDecimal porcentajeCompromiso = cuotaMensual
                .divide(capacidadPago, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"));
        
        SimulacionCreditoDTO simulacion = new SimulacionCreditoDTO();
        simulacion.setMontoSolicitado(monto);
        simulacion.setPlazoMeses(plazo);
        simulacion.setTasaInteres(tasa);
        simulacion.setCuotaMensual(cuotaMensual);
        simulacion.setCapacidadPago(capacidadPago);
        simulacion.setEsViable(esViable);
        simulacion.setPorcentajeCompromiso(porcentajeCompromiso);
        
        return ResponseEntity.ok(simulacion);
    }
} 