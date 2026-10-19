package com.Taller_apirest.app.modules.proveedores.service;

import com.Taller_apirest.app.modules.admin.service.RepuestoCatalogoService;
import com.Taller_apirest.app.modules.proveedores.model.EstadoPedidoReabastecimiento;
import com.Taller_apirest.app.modules.proveedores.model.PedidoReabastecimiento;
import com.Taller_apirest.app.modules.proveedores.repository.PedidoReabastecimientoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PedidoReabastecimientoService {

    private final PedidoReabastecimientoRepository pedidoRepository;
    private final RepuestoCatalogoService repuestoCatalogoService;

    public PedidoReabastecimientoService(PedidoReabastecimientoRepository pedidoRepository,
                                         RepuestoCatalogoService repuestoCatalogoService) {
        this.pedidoRepository = pedidoRepository;
        this.repuestoCatalogoService = repuestoCatalogoService;
    }

    public List<PedidoReabastecimiento> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<PedidoReabastecimiento> buscarPorId(String id) {
        return pedidoRepository.findById(id);
    }

    public List<PedidoReabastecimiento> listarPorProveedor(String proveedorId) {
        return pedidoRepository.findByProveedorId(proveedorId);
    }

    public List<PedidoReabastecimiento> listarPorEstado(EstadoPedidoReabastecimiento estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public PedidoReabastecimiento crear(PedidoReabastecimiento pedido) {
        if (pedido.getCodigoPedido() == null || pedido.getCodigoPedido().isBlank()) {
            pedido.setCodigoPedido("PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        pedido.setEstado(EstadoPedidoReabastecimiento.SOLICITADO);
        pedido.setFechaSolicitud(LocalDateTime.now());
        if (pedido.getCantidadSolicitada() != null && pedido.getPrecioUnitario() != null) {
            pedido.setCostoTotal(pedido.getCantidadSolicitada() * pedido.getPrecioUnitario());
        }
        return pedidoRepository.save(pedido);
    }

    public PedidoReabastecimiento despachar(String id, String transportadora, String numeroGuia) {
        return pedidoRepository.findById(id).map(p -> {
            p.setEstado(EstadoPedidoReabastecimiento.EN_CAMINO);
            p.setTransportadora(transportadora);
            p.setNumeroGuia(numeroGuia);
            p.setFechaDespacho(LocalDateTime.now());
            return pedidoRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    public PedidoReabastecimiento registrarEntrega(String id, String observaciones) {
        return pedidoRepository.findById(id).map(p -> {
            p.setEstado(EstadoPedidoReabastecimiento.ENTREGADO);
            p.setObservacionesEntrega(observaciones);
            p.setFechaEntrega(LocalDateTime.now());
            PedidoReabastecimiento guardado = pedidoRepository.save(p);

            // Reabastecer stock en el catálogo del taller automáticamente
            if (guardado.getRepuestoCodigo() != null && guardado.getCantidadSolicitada() != null) {
                repuestoCatalogoService.actualizarStock(guardado.getRepuestoCodigo(), guardado.getCantidadSolicitada());
            }

            return guardado;
        }).orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    public void eliminar(String id) {
        pedidoRepository.deleteById(id);
    }
}
