package com.estradareal.estrada_real_api.dto;

import com.estradareal.estrada_real_api.model.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class Dtos {

    private Dtos() {
    }

    public record RendimentoDto(
            @NotBlank String entidadeEmpregadora,
            @NotNull @PositiveOrZero BigDecimal valor) {

        public static RendimentoDto de(Rendimento r) {
            return new RendimentoDto(r.getEntidadeEmpregadora(), r.getValor());
        }

        public Rendimento paraEntidade() {
            Rendimento r = new Rendimento();
            r.setEntidadeEmpregadora(entidadeEmpregadora);
            r.setValor(valor);
            return r;
        }
    }

    public record ClienteRegistroRequest(
            @NotBlank String nome,
            @NotBlank @Size(min = 3, max = 60) String login,
            @NotBlank @Size(min = 6, max = 72) String senha,
            @NotBlank String rg,
            @NotBlank String cpf,
            @NotBlank String endereco,
            @NotBlank String profissao,
            @Valid @Size(max = 3) List<RendimentoDto> rendimentos) {
    }

    public record AgenteRegistroRequest(
            @NotBlank String nome,
            @NotBlank @Size(min = 3, max = 60) String login,
            @NotBlank @Size(min = 6, max = 72) String senha,
            @NotBlank String cnpj,
            @NotBlank String razaoSocial,
            @NotNull Papel papel) {
    }

    public record ClienteUpdateRequest(
            @NotBlank String nome,
            @NotBlank String rg,
            @NotBlank String endereco,
            @NotBlank String profissao,
            @Valid @Size(max = 3) List<RendimentoDto> rendimentos) {
    }

    public record UsuarioResponse(Long id, String nome, String login, Papel papel) {
        public static UsuarioResponse de(Usuario u) {
            return new UsuarioResponse(u.getId(), u.getNome(), u.getLogin(), u.getPapel());
        }
    }

    public record ClienteResponse(Long id, String nome, String login, String rg, String cpf,
            String endereco, String profissao, List<RendimentoDto> rendimentos) {
        public static ClienteResponse de(Cliente c) {
            return new ClienteResponse(c.getId(), c.getNome(), c.getLogin(), c.getRg(), c.getCpf(),
                    c.getEndereco(), c.getProfissao(),
                    c.getRendimentos().stream().map(RendimentoDto::de).toList());
        }
    }

    public record AgenteResponse(Long id, String nome, String razaoSocial, Papel papel) {
        public static AgenteResponse de(Agente a) {
            return new AgenteResponse(a.getId(), a.getNome(), a.getRazaoSocial(), a.getPapel());
        }
    }

    public record AutomovelDto(
            @NotBlank String matricula,
            @NotNull @Min(1900) @Max(2100) Integer ano,
            @NotBlank String marca,
            @NotBlank String modelo,
            @NotBlank String placa) {
        public static AutomovelDto de(Automovel a) {
            return new AutomovelDto(a.getMatricula(), a.getAno(), a.getMarca(), a.getModelo(), a.getPlaca());
        }
    }

    public record PedidoRequest(
            @NotNull @Valid AutomovelDto automovel,
            Long proprietarioId,
            Long bancoId,
            BigDecimal valorCredito) {
    }

    public record StatusRequest(@NotNull StatusPedido status) {
    }

    public record PedidoResponse(
            Long id,
            StatusPedido status,
            LocalDateTime criadoEm,
            AutomovelDto automovel,
            Long proprietarioId,
            String proprietario,
            Long bancoId,
            String banco,
            BigDecimal valorCredito,
            String cliente,
            List<RendimentoDto> rendimentosCliente) {

        public static PedidoResponse de(Pedido p) {
            ContratoCredito cc = p.getContratoCredito();
            Usuario dono = p.getAutomovel().getProprietario();
            return new PedidoResponse(
                    p.getId(), p.getStatus(), p.getCriadoEm(),
                    AutomovelDto.de(p.getAutomovel()),
                    dono.getId(), dono.getNome(),
                    cc == null ? null : cc.getBanco().getId(),
                    cc == null ? null : cc.getBanco().getNome(),
                    cc == null ? null : cc.getValor(),
                    p.getCliente().getNome(),
                    p.getCliente().getRendimentos().stream().map(RendimentoDto::de).toList());
        }
    }
}
