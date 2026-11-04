package com.estradareal.estrada_real_api.controller;

import static com.estradareal.estrada_real_api.dto.Dtos.*;

import com.estradareal.estrada_real_api.service.AgenteService;
import com.estradareal.estrada_real_api.service.PedidoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agentes")
public class AgenteController {

    private final AgenteService agentes;
    private final PedidoService pedidos;

    public AgenteController(AgenteService agentes, PedidoService pedidos) {
        this.agentes = agentes;
        this.pedidos = pedidos;
    }

    @GetMapping
    public List<AgenteResponse> listar() {
        return agentes.listar();
    }

    @GetMapping("/pedidos")
    public List<PedidoResponse> todosOsPedidos(Authentication auth) {
        return pedidos.doAgente(auth.getName());
    }

    @PatchMapping("/pedidos/{id}/status")
    public PedidoResponse atualizarStatus(Authentication auth, @PathVariable Long id,
            @Valid @RequestBody StatusRequest req) {
        return pedidos.atualizarStatus(auth.getName(), id, req.status());
    }
}