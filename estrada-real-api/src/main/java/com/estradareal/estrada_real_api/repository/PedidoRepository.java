package com.estradareal.estrada_real_api.repository;

import com.estradareal.estrada_real_api.model.Pedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteIdOrderByCriadoEmDesc(Long clienteId);

    List<Pedido> findAllByOrderByCriadoEmDesc();

    List<Pedido> findByContratoCreditoBancoIdOrderByCriadoEmDesc(Long bancoId);

    @Query("select p from Pedido p where p.contratoCredito is null "
            + "or p.automovel.proprietario.id = :agenteId order by p.criadoEm desc")
    List<Pedido> findDaEmpresa(@Param("agenteId") Long agenteId);

    void deleteByClienteId(Long clienteId);
}