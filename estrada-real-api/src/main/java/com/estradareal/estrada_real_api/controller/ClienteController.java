package com.estradareal.estrada_real_api.controller;

import static com.estradareal.estrada_real_api.dto.Dtos.*;

import com.estradareal.estrada_real_api.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes/me")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public ClienteResponse consultar(Authentication auth) {
        return service.me(auth.getName());
    }

    @PutMapping
    public ClienteResponse atualizar(Authentication auth, @Valid @RequestBody ClienteUpdateRequest req) {
        return service.atualizar(auth.getName(), req);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(Authentication auth) {
        service.excluir(auth.getName());
    }
}
