package com.estradareal.estrada_real_api.repository;

import com.estradareal.estrada_real_api.model.Pedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteIdOrderByCriadoEmDesc(Long clienteId);
    List<Pedido> findAllByOrderByCriadoEmDesc();
    void deleteByClienteId(Long clienteId);
}
