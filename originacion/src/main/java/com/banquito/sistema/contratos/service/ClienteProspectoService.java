package com.banquito.sistema.contratos.service;

import com.banquito.sistema.contratos.model.ClienteProspecto;
import com.banquito.sistema.contratos.repository.ClienteProspectoRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.InvalidDataException;
import com.banquito.sistema.contratos.exception.ClienteProspectoNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ClienteProspectoService {
    private final ClienteProspectoRepository clienteProspectoRepository;

    // Constantes del negocio de préstamos automotrices
    private static final BigDecimal INGRESOS_MINIMOS = new BigDecimal("800.00"); // USD mínimos para calificar
    private static final BigDecimal RATIO_MAXIMO_DEUDA_INGRESO = new BigDecimal("0.40"); // 40% máximo de endeudamiento
    private static final BigDecimal RATIO_MINIMO_DISPONIBLE = new BigDecimal("0.30"); // 30% mínimo disponible para cuota
    private static final Pattern CEDULA_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

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
        // Validaciones de negocio críticas
        validarDatosBasicos(cliente);
        validarCapacidadFinanciera(cliente);
        validarIdentidadYContacto(cliente);
        
        // Validar duplicados
        validarDuplicados(cliente);

        // Calcular perfil de riesgo
        String perfilRiesgo = calcularPerfilRiesgo(cliente);
        
        // Determinar estado inicial basado en análisis preliminar
        String estadoInicial = determinarEstadoInicial(cliente, perfilRiesgo);
        cliente.setEstado(estadoInicial);

        try {
            ClienteProspecto clienteGuardado = clienteProspectoRepository.save(cliente);
            
            // Aquí se podría integrar con servicios externos del banco
            // Como consulta a centrales de riesgo, validación de identidad, etc.
            consultarCentralRiesgo(clienteGuardado);
            
            return clienteGuardado;
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

        // Solo permitir actualización si está en estados permitidos
        if (!estadoPermiteActualizacion(clienteExistente.getEstado())) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "No se puede actualizar un cliente en estado: " + clienteExistente.getEstado()
            );
        }

        // Validaciones de negocio
        validarDatosBasicos(clienteActualizado);
        validarCapacidadFinanciera(clienteActualizado);
        validarIdentidadYContacto(clienteActualizado);

        // Validar duplicados si se están cambiando datos únicos
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

        // Recalcular perfil si cambiaron datos financieros
        boolean cambioFinanciero = !clienteExistente.getIngresos().equals(clienteActualizado.getIngresos()) ||
                                  !clienteExistente.getEgresos().equals(clienteActualizado.getEgresos());
        
        // Actualizar campos
        clienteExistente.setCedula(clienteActualizado.getCedula());
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        clienteExistente.setIngresos(clienteActualizado.getIngresos());
        clienteExistente.setEgresos(clienteActualizado.getEgresos());
        clienteExistente.setActividadEconomica(clienteActualizado.getActividadEconomica());

        // Si cambió información financiera, recalcular estado
        if (cambioFinanciero) {
            String nuevoPerfilRiesgo = calcularPerfilRiesgo(clienteExistente);
            String nuevoEstado = determinarEstadoInicial(clienteExistente, nuevoPerfilRiesgo);
            clienteExistente.setEstado(nuevoEstado);
        }

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

    /**
     * Analiza la capacidad de pago del cliente para un monto específico
     */
    @Transactional(readOnly = true)
    public AnalisisCapacidadPago analizarCapacidadPago(Long clienteId, BigDecimal montoSolicitado, Integer plazoMeses) {
        ClienteProspecto cliente = getById(clienteId);
        
        BigDecimal ingresoDisponible = cliente.getIngresos().subtract(cliente.getEgresos());
        BigDecimal ratioEndeudamiento = cliente.getEgresos().divide(cliente.getIngresos(), 4, RoundingMode.HALF_UP);
        
        // Calcular cuota estimada (simulación simple)
        BigDecimal tasaEstimada = new BigDecimal("0.15"); // 15% anual estimada
        BigDecimal cuotaEstimada = calcularCuotaEstimada(montoSolicitado, tasaEstimada, plazoMeses);
        
        BigDecimal capacidadMaxima = ingresoDisponible.multiply(RATIO_MINIMO_DISPONIBLE);
        boolean puedeCalificar = cuotaEstimada.compareTo(capacidadMaxima) <= 0;
        
        return new AnalisisCapacidadPago(
            puedeCalificar,
            capacidadMaxima,
            cuotaEstimada,
            ratioEndeudamiento,
            ingresoDisponible,
            calcularPerfilRiesgo(cliente)
        );
    }

    /**
     * Obtiene clientes pre-aprobados para préstamos automotrices
     */
    @Transactional(readOnly = true)
    public List<ClienteProspecto> getClientesPreAprobados() {
        List<ClienteProspecto> candidatos = clienteProspectoRepository.findByEstado("Evaluado");
        return candidatos.stream()
                .filter(this::cumplePrerequisitosPreAprobacion)
                .toList();
    }

    // Métodos privados de validación y cálculo

    private void validarDatosBasicos(ClienteProspecto cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().trim().length() < 2) {
            throw new InvalidDataException("ClienteProspecto", "El nombre debe tener al menos 2 caracteres");
        }
        
        if (cliente.getApellido() == null || cliente.getApellido().trim().length() < 2) {
            throw new InvalidDataException("ClienteProspecto", "El apellido debe tener al menos 2 caracteres");
        }

        if (cliente.getActividadEconomica() == null || cliente.getActividadEconomica().trim().isEmpty()) {
            throw new InvalidDataException("ClienteProspecto", "La actividad económica es obligatoria");
        }
    }

    private void validarCapacidadFinanciera(ClienteProspecto cliente) {
        if (cliente.getIngresos() == null || cliente.getIngresos().compareTo(INGRESOS_MINIMOS) < 0) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "Los ingresos mínimos requeridos son: $" + INGRESOS_MINIMOS
            );
        }

        if (cliente.getEgresos() == null || cliente.getEgresos().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDataException("ClienteProspecto", "Los egresos no pueden ser negativos");
        }

        // Validar ratio de endeudamiento
        BigDecimal ratioEndeudamiento = cliente.getEgresos().divide(cliente.getIngresos(), 4, RoundingMode.HALF_UP);
        if (ratioEndeudamiento.compareTo(RATIO_MAXIMO_DEUDA_INGRESO) > 0) {
            throw new InvalidDataException(
                "ClienteProspecto",
                "El ratio de endeudamiento (" + ratioEndeudamiento.multiply(new BigDecimal("100")).setScale(1) + 
                "%) excede el máximo permitido (40%)"
            );
        }
    }

    private void validarIdentidadYContacto(ClienteProspecto cliente) {
        // Validar cédula ecuatoriana
        if (cliente.getCedula() == null || !CEDULA_PATTERN.matcher(cliente.getCedula()).matches()) {
            throw new InvalidDataException("ClienteProspecto", "La cédula debe contener exactamente 10 dígitos");
        }

        // Validar cédula ecuatoriana con algoritmo del dígito verificador
        if (!validarCedulaEcuatoriana(cliente.getCedula())) {
            throw new InvalidDataException("ClienteProspecto", "La cédula no es válida según algoritmo ecuatoriano");
        }

        // Validar email
        if (cliente.getEmail() == null || !EMAIL_PATTERN.matcher(cliente.getEmail()).matches()) {
            throw new InvalidDataException("ClienteProspecto", "El formato del email no es válido");
        }

        // Validar teléfono
        if (cliente.getTelefono() == null || cliente.getTelefono().length() < 7) {
            throw new InvalidDataException("ClienteProspecto", "El teléfono debe tener al menos 7 dígitos");
        }

        if (cliente.getDireccion() == null || cliente.getDireccion().trim().length() < 10) {
            throw new InvalidDataException("ClienteProspecto", "La dirección debe tener al menos 10 caracteres");
        }
    }

    private void validarDuplicados(ClienteProspecto cliente) {
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
    }

    private String calcularPerfilRiesgo(ClienteProspecto cliente) {
        BigDecimal ratioEndeudamiento = cliente.getEgresos().divide(cliente.getIngresos(), 4, RoundingMode.HALF_UP);
        BigDecimal ingresoDisponible = cliente.getIngresos().subtract(cliente.getEgresos());
        
        // Clasificación por ingresos y endeudamiento
        if (cliente.getIngresos().compareTo(new BigDecimal("3000")) >= 0 && 
            ratioEndeudamiento.compareTo(new BigDecimal("0.20")) <= 0) {
            return "BAJO"; // Cliente premium
        } else if (cliente.getIngresos().compareTo(new BigDecimal("1500")) >= 0 && 
                   ratioEndeudamiento.compareTo(new BigDecimal("0.35")) <= 0) {
            return "MEDIO"; // Cliente estándar
        } else {
            return "ALTO"; // Requiere análisis adicional
        }
    }

    private String determinarEstadoInicial(ClienteProspecto cliente, String perfilRiesgo) {
        BigDecimal ingresoDisponible = cliente.getIngresos().subtract(cliente.getEgresos());
        
        // Estados del negocio de préstamos automotrices
        if (ingresoDisponible.compareTo(new BigDecimal("300")) < 0) {
            return "Rechazado"; // Ingresos insuficientes
        } else if ("ALTO".equals(perfilRiesgo)) {
            return "Requiere_Evaluacion"; // Necesita análisis manual
        } else if ("BAJO".equals(perfilRiesgo)) {
            return "Pre_Aprobado"; // Cliente premium
        } else {
            return "Evaluado"; // Cliente estándar evaluado
        }
    }

    private boolean estadoPermiteActualizacion(String estado) {
        return "Nuevo".equals(estado) || 
               "Evaluado".equals(estado) || 
               "Requiere_Evaluacion".equals(estado);
    }

    private boolean validarCedulaEcuatoriana(String cedula) {
        // Algoritmo de validación de cédula ecuatoriana
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;
        
        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            int resultado = digito * coeficientes[i];
            if (resultado > 9) {
                resultado -= 9;
            }
            suma += resultado;
        }
        
        int digitoVerificador = (10 - (suma % 10)) % 10;
        return digitoVerificador == Character.getNumericValue(cedula.charAt(9));
    }

    private BigDecimal calcularCuotaEstimada(BigDecimal monto, BigDecimal tasaAnual, Integer plazoMeses) {
        BigDecimal tasaMensual = tasaAnual.divide(new BigDecimal("12"), 6, RoundingMode.HALF_UP);
        BigDecimal factor = BigDecimal.ONE.add(tasaMensual).pow(plazoMeses);
        return monto.multiply(tasaMensual.multiply(factor))
                .divide(factor.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
    }

    private boolean cumplePrerequisitosPreAprobacion(ClienteProspecto cliente) {
        BigDecimal ingresoDisponible = cliente.getIngresos().subtract(cliente.getEgresos());
        return ingresoDisponible.compareTo(new BigDecimal("500")) >= 0 &&
               "BAJO".equals(calcularPerfilRiesgo(cliente)) ||
               "MEDIO".equals(calcularPerfilRiesgo(cliente));
    }

    private void consultarCentralRiesgo(ClienteProspecto cliente) {
        // Simulación de consulta a central de riesgo
        // En un entorno real, aquí se integraría con servicios del banco
        // para consultar Buró de Crédito, DataCrédito, etc.
        
        // Por ahora, solo logging para simular la integración
        System.out.println("Consultando central de riesgo para cliente: " + cliente.getCedula());
        // Aquí se podría actualizar el score crediticio externo
    }

    // Clase interna para análisis de capacidad de pago
    public static class AnalisisCapacidadPago {
        private final boolean puedeCalificar;
        private final BigDecimal capacidadMaximaCuota;
        private final BigDecimal cuotaEstimada;
        private final BigDecimal ratioEndeudamiento;
        private final BigDecimal ingresoDisponible;
        private final String perfilRiesgo;

        public AnalisisCapacidadPago(boolean puedeCalificar, BigDecimal capacidadMaximaCuota,
                                   BigDecimal cuotaEstimada, BigDecimal ratioEndeudamiento,
                                   BigDecimal ingresoDisponible, String perfilRiesgo) {
            this.puedeCalificar = puedeCalificar;
            this.capacidadMaximaCuota = capacidadMaximaCuota;
            this.cuotaEstimada = cuotaEstimada;
            this.ratioEndeudamiento = ratioEndeudamiento;
            this.ingresoDisponible = ingresoDisponible;
            this.perfilRiesgo = perfilRiesgo;
        }

        // Getters
        public boolean isPuedeCalificar() { return puedeCalificar; }
        public BigDecimal getCapacidadMaximaCuota() { return capacidadMaximaCuota; }
        public BigDecimal getCuotaEstimada() { return cuotaEstimada; }
        public BigDecimal getRatioEndeudamiento() { return ratioEndeudamiento; }
        public BigDecimal getIngresoDisponible() { return ingresoDisponible; }
        public String getPerfilRiesgo() { return perfilRiesgo; }
    }
} 