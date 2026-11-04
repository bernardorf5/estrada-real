package com.estradareal.estrada_real_api.controller;

import static com.estradareal.estrada_real_api.dto.Dtos.*;

import com.estradareal.estrada_real_api.service.PedidoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping("/meus")
    public List<PedidoResponse> meus(Authentication auth) {
        return service.meus(auth.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criar(Authentication auth, @Valid @RequestBody PedidoRequest req) {
        return service.criar(auth.getName(), req);
    }

    @PutMapping("/{id}")
    public PedidoResponse atualizar(Authentication auth, @PathVariable Long id,
            @Valid @RequestBody PedidoRequest req) {
        return service.atualizar(auth.getName(), id, req);
    }

    @PatchMapping("/{id}/cancelar")
    public PedidoResponse cancelar(Authentication auth, @PathVariable Long id) {
        return service.cancelar(auth.getName(), id);
    }
}
