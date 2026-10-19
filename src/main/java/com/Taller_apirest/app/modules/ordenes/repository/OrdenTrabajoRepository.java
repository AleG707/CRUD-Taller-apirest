package com.Taller_apirest.app.modules.ordenes.repository;

import com.Taller_apirest.app.modules.ordenes.model.EstadoOrden;
import com.Taller_apirest.app.modules.ordenes.model.OrdenTrabajo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenTrabajoRepository extends MongoRepository<OrdenTrabajo, String> {
    Optional<OrdenTrabajo> findByNumeroOrden(String numeroOrden);
    List<OrdenTrabajo> findByVehiculoPlaca(String vehiculoPlaca);
    List<OrdenTrabajo> findByClienteId(String clienteId);
    List<OrdenTrabajo> findByMecanicoId(String mecanicoId);
    List<OrdenTrabajo> findByEstado(EstadoOrden estado);
    List<OrdenTrabajo> findByEstadoIn(List<EstadoOrden> estados);
}
