package com.estradareal.estrada_real_api.controller;

import static com.estradareal.estrada_real_api.dto.Dtos.*;

import com.estradareal.estrada_real_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register/cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse registrarCliente(@Valid @RequestBody ClienteRegistroRequest req) {
        return service.registrarCliente(req);
    }

    @PostMapping("/register/agente")
    @ResponseStatus(HttpStatus.CREATED)
    public AgenteResponse registrarAgente(@Valid @RequestBody AgenteRegistroRequest req) {
        return service.registrarAgente(req);
    }

    @GetMapping("/me")
    public UsuarioResponse me(Authentication auth) {
        return service.me(auth.getName());
    }
}
