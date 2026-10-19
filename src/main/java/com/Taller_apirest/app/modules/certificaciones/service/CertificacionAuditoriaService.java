package com.Taller_apirest.app.modules.certificaciones.service;

import com.Taller_apirest.app.modules.certificaciones.model.CertificacionAuditoria;
import com.Taller_apirest.app.modules.certificaciones.model.ItemChecklist;
import com.Taller_apirest.app.modules.certificaciones.model.ResultadoAuditoria;
import com.Taller_apirest.app.modules.certificaciones.repository.CertificacionAuditoriaRepository;
import com.Taller_apirest.app.modules.ordenes.model.EstadoOrden;
import com.Taller_apirest.app.modules.ordenes.model.OrdenTrabajo;
import com.Taller_apirest.app.modules.ordenes.service.OrdenTrabajoService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificacionAuditoriaService {

    private final CertificacionAuditoriaRepository certificacionRepository;
    private final OrdenTrabajoService ordenTrabajoService;

    public CertificacionAuditoriaService(CertificacionAuditoriaRepository certificacionRepository,
                                         OrdenTrabajoService ordenTrabajoService) {
        this.certificacionRepository = certificacionRepository;
        this.ordenTrabajoService = ordenTrabajoService;
    }

    public List<CertificacionAuditoria> listarTodas() {
        return certificacionRepository.findAll();
    }

    public Optional<CertificacionAuditoria> buscarPorId(String id) {
        return certificacionRepository.findById(id);
    }

    public Optional<CertificacionAuditoria> buscarPorCodigo(String codigoCertificado) {
        return certificacionRepository.findByCodigoCertificado(codigoCertificado);
    }

    public Optional<CertificacionAuditoria> buscarPorOrdenId(String ordenId) {
        return certificacionRepository.findByOrdenTrabajoId(ordenId);
    }

    public List<CertificacionAuditoria> listarPorPlaca(String placa) {
        return certificacionRepository.findByVehiculoPlaca(placa.toUpperCase().trim());
    }

    public CertificacionAuditoria auditarYCertificar(CertificacionAuditoria certificacion) {
        if (certificacion.getCodigoCertificado() == null || certificacion.getCodigoCertificado().isBlank()) {
            certificacion.setCodigoCertificado("CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        certificacion.setFechaCertificacion(LocalDateTime.now());

        // Calcular puntaje de calidad según checklist
        if (certificacion.getChecklist() != null && !certificacion.getChecklist().isEmpty()) {
            long cumplidos = certificacion.getChecklist().stream().filter(ItemChecklist::isCumplido).count();
            int puntaje = (int) Math.round(((double) cumplidos / certificacion.getChecklist().size()) * 100);
            certificacion.setPuntajeCalidad(puntaje);

            if (puntaje == 100) {
                certificacion.setResultado(ResultadoAuditoria.APROBADO);
            } else if (puntaje >= 80) {
                certificacion.setResultado(ResultadoAuditoria.APROBADO_CON_OBSERVACIONES);
            } else {
                certificacion.setResultado(ResultadoAuditoria.NO_CONFORME);
            }
        }

        // Generar hash de trazabilidad inmutable
        String hash = generarHashTrazabilidad(
                certificacion.getOrdenTrabajoId(),
                certificacion.getVehiculoPlaca(),
                certificacion.getMecanicoNombre(),
                certificacion.getAuditorNombre(),
                certificacion.getFechaCertificacion().toString()
        );
        certificacion.setHashTrazabilidad(hash);

        // Guardar certificado
        CertificacionAuditoria guardado = certificacionRepository.save(certificacion);

        // Si fue aprobada la auditoría, actualizar la orden de trabajo a CERTIFICADA
        if (certificacion.getOrdenTrabajoId() != null && certificacion.getResultado() != ResultadoAuditoria.NO_CONFORME) {
            ordenTrabajoService.cambiarEstado(certificacion.getOrdenTrabajoId(), EstadoOrden.CERTIFICADA);
        }

        return guardado;
    }

    public List<ItemChecklist> generarChecklistEstandar() {
        List<ItemChecklist> lista = new ArrayList<>();
        lista.add(new ItemChecklist("ISO-9001: Calidad de Procedimiento", "Verificación del cumplimiento de la orden de servicio según manual de fabricante", true, "Protocolo verificado conforme"));
        lista.add(new ItemChecklist("Seguridad Vial: Frenos y Dirección", "Inspección de efectividad de frenado, discos, pastillas y alineación", true, "Presión y desgaste en tolerancias"));
        lista.add(new ItemChecklist("Control de Fluidos y Estanqueidad", "Validación de nivel y ausencia de fugas de aceite, refrigerante y líquido de frenos", true, "Niveles nominales sin fugas"));
        lista.add(new ItemChecklist("Torque y Ajustes Estructurales", "Comprobación de torque especificado en pernos de rueda y suspensión con torquímetro calibrado", true, "Torqueado a 110 Nm"));
        lista.add(new ItemChecklist("Diagnóstico Electrónico OBD-II", "Escaneo computarizado final sin códigos de avería (DTCs activos)", true, "0 códigos de falla"));
        lista.add(new ItemChecklist("Prueba de Ruta y Seguridad Activa", "Verificación en prueba dinámica de estabilidad, ruidos anómalos y respuesta del motor", true, "Comportamiento dinámico óptimo"));
        return lista;
    }

    private String generarHashTrazabilidad(String ordenId, String placa, String mecanico, String auditor, String fecha) {
        try {
            String raw = ordenId + "|" + placa + "|" + mecanico + "|" + auditor + "|" + fecha + "|" + UUID.randomUUID();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

    public void eliminar(String id) {
        certificacionRepository.deleteById(id);
    }
}
