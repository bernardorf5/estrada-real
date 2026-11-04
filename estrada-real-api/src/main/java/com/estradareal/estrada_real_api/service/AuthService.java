package com.estradareal.estrada_real_api.service;

import static com.estradareal.estrada_real_api.dto.Dtos.*;

import com.estradareal.estrada_real_api.model.*;
import com.estradareal.estrada_real_api.repository.*;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UsuarioRepository usuarios;
    private final ClienteRepository clientes;
    private final AgenteRepository agentes;
    private final PasswordEncoder encoder;

    public AuthService(UsuarioRepository usuarios, ClienteRepository clientes,
            AgenteRepository agentes, PasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.clientes = clientes;
        this.agentes = agentes;
        this.encoder = encoder;
    }

    public ClienteResponse registrarCliente(ClienteRegistroRequest r) {
        if (usuarios.existsByLogin(r.login()))
            throw Regras.invalido("Login já em uso");
        if (clientes.existsByCpf(r.cpf()))
            throw Regras.invalido("CPF já cadastrado");
        Regras.maximoTresRendimentos(r.rendimentos());

        Cliente c = new Cliente();
        c.setNome(r.nome());
        c.setLogin(r.login());
        c.setSenha(encoder.encode(r.senha()));
        c.setPapel(Papel.CLIENTE);
        c.setRg(r.rg());
        c.setCpf(r.cpf());
        c.setEndereco(r.endereco());
        c.setProfissao(r.profissao());
        List<RendimentoDto> rends = r.rendimentos() == null ? List.of() : r.rendimentos();
        c.substituirRendimentos(rends.stream().map(RendimentoDto::paraEntidade).toList());
        return ClienteResponse.de(clientes.save(c));
    }

    public AgenteResponse registrarAgente(AgenteRegistroRequest r) {
        if (r.papel() == Papel.CLIENTE)
            throw Regras.invalido("Agente deve ser EMPRESA ou BANCO");
        if (usuarios.existsByLogin(r.login()))
            throw Regras.invalido("Login já em uso");
        if (agentes.existsByCnpj(r.cnpj()))
            throw Regras.invalido("CNPJ já cadastrado");

        Agente a = new Agente();
        a.setNome(r.nome());
        a.setLogin(r.login());
        a.setSenha(encoder.encode(r.senha()));
        a.setPapel(r.papel());
        a.setCnpj(r.cnpj());
        a.setRazaoSocial(r.razaoSocial());
        return AgenteResponse.de(agentes.save(a));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse me(String login) {
        return usuarios.findByLogin(login)
                .map(UsuarioResponse::de)
                .orElseThrow(() -> Regras.naoEncontrado("Usuário não encontrado"));
    }
}
