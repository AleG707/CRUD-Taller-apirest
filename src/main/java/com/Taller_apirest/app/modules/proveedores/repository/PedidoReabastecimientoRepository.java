package com.Taller_apirest.app.modules.proveedores.repository;

import com.Taller_apirest.app.modules.proveedores.model.EstadoPedidoReabastecimiento;
import com.Taller_apirest.app.modules.proveedores.model.PedidoReabastecimiento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoReabastecimientoRepository extends MongoRepository<PedidoReabastecimiento, String> {
    Optional<PedidoReabastecimiento> findByCodigoPedido(String codigoPedido);
    List<PedidoReabastecimiento> findByProveedorId(String proveedorId);
    List<PedidoReabastecimiento> findByEstado(EstadoPedidoReabastecimiento estado);
}
