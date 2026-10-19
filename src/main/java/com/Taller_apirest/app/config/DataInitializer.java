package com.Taller_apirest.app.config;

import com.Taller_apirest.app.modules.admin.model.RepuestoCatalogo;
import com.Taller_apirest.app.modules.admin.model.Rol;
import com.Taller_apirest.app.modules.admin.model.ServicioCatalogo;
import com.Taller_apirest.app.modules.admin.model.Usuario;
import com.Taller_apirest.app.modules.admin.repository.RepuestoCatalogoRepository;
import com.Taller_apirest.app.modules.admin.repository.ServicioCatalogoRepository;
import com.Taller_apirest.app.modules.admin.repository.UsuarioRepository;
import com.Taller_apirest.app.modules.certificaciones.model.CertificacionAuditoria;
import com.Taller_apirest.app.modules.certificaciones.model.ItemChecklist;
import com.Taller_apirest.app.modules.certificaciones.model.ResultadoAuditoria;
import com.Taller_apirest.app.modules.certificaciones.repository.CertificacionAuditoriaRepository;
import com.Taller_apirest.app.modules.ordenes.model.*;
import com.Taller_apirest.app.modules.ordenes.repository.CotizacionRepository;
import com.Taller_apirest.app.modules.ordenes.repository.OrdenTrabajoRepository;
import com.Taller_apirest.app.modules.proveedores.model.EstadoPedidoReabastecimiento;
import com.Taller_apirest.app.modules.proveedores.model.PedidoReabastecimiento;
import com.Taller_apirest.app.modules.proveedores.model.Proveedor;
import com.Taller_apirest.app.modules.proveedores.model.SuministroProveedor;
import com.Taller_apirest.app.modules.proveedores.repository.PedidoReabastecimientoRepository;
import com.Taller_apirest.app.modules.proveedores.repository.ProveedorRepository;
import com.Taller_apirest.app.modules.vehiculos.model.Cliente;
import com.Taller_apirest.app.modules.vehiculos.model.Vehiculo;
import com.Taller_apirest.app.modules.vehiculos.repository.ClienteRepository;
import com.Taller_apirest.app.modules.vehiculos.repository.VehiculoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final ServicioCatalogoRepository servicioRepository;
    private final RepuestoCatalogoRepository repuestoRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;
    private final CotizacionRepository cotizacionRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final ProveedorRepository proveedorRepository;
    private final PedidoReabastecimientoRepository pedidoRepository;
    private final CertificacionAuditoriaRepository certificacionRepository;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           ServicioCatalogoRepository servicioRepository,
                           RepuestoCatalogoRepository repuestoRepository,
                           ClienteRepository clienteRepository,
                           VehiculoRepository vehiculoRepository,
                           CotizacionRepository cotizacionRepository,
                           OrdenTrabajoRepository ordenTrabajoRepository,
                           ProveedorRepository proveedorRepository,
                           PedidoReabastecimientoRepository pedidoRepository,
                           CertificacionAuditoriaRepository certificacionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.servicioRepository = servicioRepository;
        this.repuestoRepository = repuestoRepository;
        this.clienteRepository = clienteRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.cotizacionRepository = cotizacionRepository;
        this.ordenTrabajoRepository = ordenTrabajoRepository;
        this.proveedorRepository = proveedorRepository;
        this.pedidoRepository = pedidoRepository;
        this.certificacionRepository = certificacionRepository;
    }

    @Override
    public void run(String... args) {
        try {
            log.info("Verificando e inicializando datos semilla en las bases de datos de microservicios...");

            // 1. Inicializar Usuarios de los 5 roles
            if (usuarioRepository.count() == 0) {
                log.info("Poblando usuarios iniciales (taller_admin_db)...");
                usuarioRepository.save(new Usuario(null, "Roberto Gómez (Administrador)", "admin@taller.com", "admin123", Rol.ADMINISTRADOR, "3001234567", true));
                usuarioRepository.save(new Usuario(null, "Carlos Mecánico (Técnico Master)", "carlos.mecanico@taller.com", "mecanico123", Rol.MECANICO, "3109876543", true));
                usuarioRepository.save(new Usuario(null, "Juan Pérez (Cliente Propietario)", "juan.perez@email.com", "cliente123", Rol.CLIENTE, "3204567890", true));
                usuarioRepository.save(new Usuario(null, "Distribuidora AutoPartes (Proveedor)", "ventas@autopartes-bogota.com", "proveedor123", Rol.PROVEEDOR, "3156789012", true));
                usuarioRepository.save(new Usuario(null, "Ing. Laura Auditora (Certificadora)", "laura.auditora@certificaciones.com", "auditor123", Rol.AUDITOR, "3182345678", true));
            }

            // 2. Inicializar Catálogo de Servicios
            if (servicioRepository.count() == 0) {
                log.info("Poblando catálogo de servicios (taller_admin_db)...");
                servicioRepository.save(new ServicioCatalogo(null, "SRV-001", "Mantenimiento Preventivo 10K km", "Mantenimiento", 130000.0, 120, "Revisión general de 25 puntos, rotación de ruedas, ajuste de frenos y niveles.", true));
                servicioRepository.save(new ServicioCatalogo(null, "SRV-002", "Cambio de Aceite Sintético y Filtros", "Lubricación", 85000.0, 45, "Cambio de aceite 5W-30 o 5W-40, filtro de aceite y filtro de aire de motor.", true));
                servicioRepository.save(new ServicioCatalogo(null, "SRV-003", "Sincronización y Limpieza de Inyectores", "Motor", 185000.0, 150, "Limpieza de inyectores por ultrasonido, cambio de bujías y cuerpo de aceleración.", true));
                servicioRepository.save(new ServicioCatalogo(null, "SRV-004", "Cambio de Pastillas de Freno y Discos", "Frenos", 140000.0, 90, "Reemplazo de pastillas, rectificación de discos y purga de líquido DOT-4.", true));
                servicioRepository.save(new ServicioCatalogo(null, "SRV-005", "Alineación y Balanceo Láser 3D", "Suspensión", 65000.0, 60, "Alineación computarizada 3D del tren delantero y trasero, balanceo de 4 ruedas.", true));
            }

            // 3. Inicializar Catálogo de Repuestos
            if (repuestoRepository.count() == 0) {
                log.info("Poblando catálogo de repuestos (taller_admin_db)...");
                repuestoRepository.save(new RepuestoCatalogo(null, "REP-101", "Filtro de Aceite Blindado PH-4967", "Filtros", "Mann Filter", 32000.0, 5, 18, "Distribuidora AutoPartes"));
                repuestoRepository.save(new RepuestoCatalogo(null, "REP-102", "Pastillas de Freno Cerámicas Delanteras", "Frenos", "Brembo", 125000.0, 4, 12, "Distribuidora AutoPartes"));
                repuestoRepository.save(new RepuestoCatalogo(null, "REP-103", "Juego de 4 Bujías Láser Iridium", "Encendido", "NGK", 145000.0, 3, 10, "Distribuidora AutoPartes"));
                repuestoRepository.save(new RepuestoCatalogo(null, "REP-104", "Galón Aceite Sintético 5W-30 Dexos", "Lubricantes", "Mobil 1", 160000.0, 6, 24, "Distribuidora AutoPartes"));
                repuestoRepository.save(new RepuestoCatalogo(null, "REP-105", "Kit Correa de Tiempo y Tensor", "Motor", "Gates", 210000.0, 2, 2, "Distribuidora AutoPartes")); // Bajo stock
            }

            // 4. Inicializar Clientes y Vehículos
            if (clienteRepository.count() == 0) {
                log.info("Poblando clientes y vehículos (taller_clientes_db)...");
                Cliente cliente1 = clienteRepository.save(new Cliente(null, "1020304050", "Juan Pérez", "juan.perez@email.com", "3204567890", "Cra 15 # 104-32, Bogotá"));
                Cliente cliente2 = clienteRepository.save(new Cliente(null, "1098765432", "María Fernanda Torres", "maria.torres@email.com", "3118901234", "Calle 72 # 11-45, Medellín"));

                vehiculoRepository.save(new Vehiculo(null, "ABC-123", "Mazda", "3 Grand Touring", 2022, 42500, "9FBRD1234NZ567890", "Rojo Diamante", cliente1.getId(), cliente1.getNombreCompleto()));
                vehiculoRepository.save(new Vehiculo(null, "XYZ-789", "Renault", "Duster 4x4", 2020, 68000, "8A1BB9876KL123456", "Gris Estrella", cliente1.getId(), cliente1.getNombreCompleto()));
                vehiculoRepository.save(new Vehiculo(null, "KTM-456", "Toyota", "Corolla Cross Híbrido", 2023, 29000, "JTMBD4567PO789012", "Blanco Perlado", cliente2.getId(), cliente2.getNombreCompleto()));
            }

            // 5. Inicializar Proveedor y Pedidos de Reabastecimiento
            if (proveedorRepository.count() == 0) {
                log.info("Poblando proveedor y suministros (taller_proveedores_db)...");
                Proveedor prov = new Proveedor(null, "900.543.210-1", "Distribuidora AutoPartes Andina S.A.S.", "Alejandro Morales", "3156789012", "ventas@autopartes-bogota.com", "Bogotá D.C.");
                prov.getSuministros().add(new SuministroProveedor("REP-101", "Filtro de Aceite Blindado PH-4967", "Filtros", 18000.0, 120));
                prov.getSuministros().add(new SuministroProveedor("REP-102", "Pastillas de Freno Cerámicas Delanteras", "Frenos", 75000.0, 45));
                prov.getSuministros().add(new SuministroProveedor("REP-103", "Juego de 4 Bujías Láser Iridium", "Encendido", 90000.0, 80));
                prov.getSuministros().add(new SuministroProveedor("REP-104", "Galón Aceite Sintético 5W-30 Dexos", "Lubricantes", 105000.0, 200));
                prov.getSuministros().add(new SuministroProveedor("REP-105", "Kit Correa de Tiempo y Tensor", "Motor", 140000.0, 30));
                Proveedor provGuardado = proveedorRepository.save(prov);

                // Pedido de reabastecimiento en tránsito
                PedidoReabastecimiento ped1 = new PedidoReabastecimiento(null, "PED-2026-001", provGuardado.getId(), provGuardado.getRazonSocial(), "REP-105", "Kit Correa de Tiempo y Tensor", 5, 140000.0);
                ped1.setEstado(EstadoPedidoReabastecimiento.EN_CAMINO);
                ped1.setTransportadora("Servientrega Express");
                ped1.setNumeroGuia("SER-98451203");
                ped1.setFechaDespacho(LocalDateTime.now().minusDays(1));
                pedidoRepository.save(ped1);

                // Pedido solicitado recién
                PedidoReabastecimiento ped2 = new PedidoReabastecimiento(null, "PED-2026-002", provGuardado.getId(), provGuardado.getRazonSocial(), "REP-102", "Pastillas de Freno Cerámicas Delanteras", 10, 75000.0);
                pedidoRepository.save(ped2);
            }

            // 6. Inicializar Cotizaciones y Órdenes de Trabajo (Trazabilidad)
            if (cotizacionRepository.count() == 0) {
                log.info("Poblando cotizaciones y órdenes de trabajo (taller_ordenes_db)...");
                // Cotización 1: Aprobada con Orden de Trabajo activa
                Cotizacion cot1 = new Cotizacion(null, "COT-2026-001", "cli-01", "Juan Pérez", "veh-01", "ABC-123",
                        List.of("Mantenimiento Preventivo 10K km", "Cambio de Pastillas de Freno y Discos"), 270000.0,
                        "Mantenimiento programado y chillido agudo al frenar.");
                cot1.setEstado(EstadoCotizacion.APROBADA);
                cot1.setRespuestaCliente("Aprobado por el cliente Juan Pérez vía WhatsApp.");
                cot1.setFechaRespuesta(LocalDateTime.now().minusHours(5));
                cotizacionRepository.save(cot1);

                // Cotización 2: Pendiente para que el cliente la apruebe o rechace en la UI
                Cotizacion cot2 = new Cotizacion(null, "COT-2026-002", "cli-01", "Juan Pérez", "veh-02", "XYZ-789",
                        List.of("Sincronización y Limpieza de Inyectores", "Cambio de Aceite Sintético y Filtros"), 270000.0,
                        "Tironeo en subidas y olor a combustible no quemado.");
                cotizacionRepository.save(cot2);

                // Orden de Trabajo activa con registro de diagnóstico, repuestos y tiempo del mecánico
                OrdenTrabajo orden1 = new OrdenTrabajo(null, "OT-2026-001", cot1.getId(), cot1.getClienteId(), cot1.getClienteNombre(),
                        cot1.getVehiculoId(), "ABC-123", "Mazda 3 Grand Touring 2022", 42500,
                        "mec-01", "Carlos Mecánico (Técnico Master)", "Mantenimiento 10K y revisión de frenos delanteros");
                orden1.setEstado(EstadoOrden.EN_REPARACION);
                orden1.setDiagnosticoTecnico("Pastillas de freno con 15% de vida útil remanente. Discos con surco leve que requiere rectificación. Filtro de cabina saturado.");
                orden1.setReparacionesEfectuadas("Desmontaje de mordazas, rectificación micrométrica de discos, instalación de pastillas Brembo cerámicas y purga con líquido DOT-4.");

                orden1.getRepuestosUtilizados().add(new RepuestoUtilizado("REP-102", "REP-102", "Pastillas de Freno Cerámicas Delanteras", 1, 125000.0));
                orden1.getRepuestosUtilizados().add(new RepuestoUtilizado("REP-101", "REP-101", "Filtro de Aceite Blindado PH-4967", 1, 32000.0));

                orden1.getRegistrosTiempo().add(new RegistroTiempo("Carlos Mecánico", 1, 30, "Diagnóstico e inspección de frenos"));
                orden1.getRegistrosTiempo().add(new RegistroTiempo("Carlos Mecánico", 2, 0, "Rectificación de discos e instalación de pastillas"));

                orden1.recalcularCostos(45000.0);
                OrdenTrabajo ordenGuardada = ordenTrabajoRepository.save(orden1);

                // Orden 2: Completada y lista para certificar o ya certificada
                OrdenTrabajo orden2 = new OrdenTrabajo(null, "OT-2026-002", null, "cli-02", "María Fernanda Torres",
                        "veh-03", "KTM-456", "Toyota Corolla Cross Híbrido 2023", 29000,
                        "mec-01", "Carlos Mecánico (Técnico Master)", "Revisión sistema híbrido y cambio de lubricante sintético");
                orden2.setEstado(EstadoOrden.CERTIFICADA);
                orden2.setDiagnosticoTecnico("Baterías híbridas al 98% de salud, software actualizado a versión 4.2. Aceite con degradación normal.");
                orden2.setReparacionesEfectuadas("Cambio de aceite Mobil 1 0W-20, filtro de aceite y filtro de aire de batería híbrida.");
                orden2.getRepuestosUtilizados().add(new RepuestoUtilizado("REP-104", "REP-104", "Galón Aceite Sintético 5W-30 Dexos", 1, 160000.0));
                orden2.getRegistrosTiempo().add(new RegistroTiempo("Carlos Mecánico", 1, 15, "Mantenimiento preventivo integral"));
                orden2.setFechaFinalizacion(LocalDateTime.now().minusHours(2));
                orden2.recalcularCostos(45000.0);
                OrdenTrabajo orden2Guardada = ordenTrabajoRepository.save(orden2);

                // 7. Certificación de Auditoría para la Orden 2
                if (certificacionRepository.count() == 0) {
                    log.info("Poblando certificación de calidad con hash inmutable (taller_certificaciones_db)...");
                    List<ItemChecklist> checklist = new ArrayList<>();
                    checklist.add(new ItemChecklist("ISO-9001: Calidad de Procedimiento", "Verificación del cumplimiento de la orden de servicio según manual de fabricante", true, "Protocolo verificado conforme"));
                    checklist.add(new ItemChecklist("Seguridad Vial: Frenos y Dirección", "Inspección de efectividad de frenado, discos, pastillas y alineación", true, "Presión y desgaste en tolerancias"));
                    checklist.add(new ItemChecklist("Control de Fluidos y Estanqueidad", "Validación de nivel y ausencia de fugas de aceite, refrigerante y líquido de frenos", true, "Niveles nominales sin fugas"));
                    checklist.add(new ItemChecklist("Torque y Ajustes Estructurales", "Comprobación de torque especificado en pernos de rueda y suspensión con torquímetro calibrado", true, "Torqueado a 110 Nm"));
                    checklist.add(new ItemChecklist("Diagnóstico Electrónico OBD-II", "Escaneo computarizado final sin códigos de avería (DTCs activos)", true, "0 códigos de falla"));
                    checklist.add(new ItemChecklist("Prueba de Ruta y Seguridad Activa", "Verificación en prueba dinámica de estabilidad, ruidos anómalos y respuesta del motor", true, "Comportamiento dinámico óptimo"));

                    CertificacionAuditoria cert = new CertificacionAuditoria(
                            null,
                            "CERT-ISO9001-8842",
                            orden2Guardada.getId(),
                            orden2Guardada.getNumeroOrden(),
                            orden2Guardada.getVehiculoPlaca(),
                            orden2Guardada.getMecanicoNombre(),
                            "aud-01",
                            "Ing. Laura Auditora (Certificadora ISO)",
                            ResultadoAuditoria.APROBADO,
                            100,
                            "Servicio ejecutado con excelencia técnica conforme a las normas ISO 9001 y estándares de seguridad vial vigentes.",
                            "8f4c2e6b1a9d0f3e7c5a2b1d4e6f8a0b3c5d7e9f1a2b4c6d8e0f2a4b6c8d0e2f"
                    );
                    cert.setChecklist(checklist);
                    certificacionRepository.save(cert);
                }
            }

            log.info("¡Datos semilla inicializados exitosamente en las 5 bases de datos de microservicios!");

        } catch (Exception e) {
            log.warn("AVISO: No se pudo conectar a MongoDB o inicializar datos semilla ({}). " +
                    "Si aún no has iniciado MongoDB local o configurado Atlas en application.properties / MONGODB_URI, " +
                    "la aplicación iniciará normalmente y podrás conectar la base de datos en cualquier momento.", e.getMessage());
        }
    }
}
