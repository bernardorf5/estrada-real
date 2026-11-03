-- 1) Usuario: tabela base
create table usuario (
    id     bigint generated always as identity primary key,
    nome   varchar(120) not null,
    login  varchar(60)  not null unique,
    senha  varchar(100) not null,  -- sempre guardar o hash (BCrypt), nunca a senha pura
    papel  varchar(10)  not null check (papel in ('CLIENTE', 'EMPRESA', 'BANCO'))
);

-- 2) Cliente: herda de Usuario (mesmo id)
create table cliente (
    id         bigint primary key references usuario (id) on delete cascade,
    rg         varchar(20)  not null,
    cpf        varchar(14)  not null unique,
    endereco   varchar(200) not null,
    profissao  varchar(80)  not null
);

-- 3) Agente: herda de Usuario (mesmo id)
create table agente (
    id            bigint primary key references usuario (id) on delete cascade,
    cnpj          varchar(18)  not null unique,
    razao_social  varchar(120) not null
);

-- 4) Rendimento: pertence a um cliente
create table rendimento (
    id                    bigint generated always as identity primary key,
    cliente_id            bigint        not null references cliente (id) on delete cascade,
    entidade_empregadora  varchar(120)  not null,
    valor                 numeric(12,2) not null check (valor >= 0)
);

-- 5) Automovel: o dono pode ser cliente, empresa ou banco (todos sao usuarios)
create table automovel (
    id              bigint generated always as identity primary key,
    proprietario_id bigint       not null references usuario (id),
    matricula       varchar(30)  not null,
    ano             int          not null check (ano between 1900 and 2100),
    marca           varchar(50)  not null,
    modelo          varchar(60)  not null,
    placa           varchar(10)  not null unique
);

-- 6) Contrato de credito: concedido por um agente (banco)
create table contrato_credito (
    id        bigint generated always as identity primary key,
    banco_id  bigint        not null references agente (id),
    valor     numeric(12,2) not null check (valor > 0)
);

-- 7) Pedido: liga cliente, automovel, avaliador e contrato de credito
create table pedido (
    id                  bigint generated always as identity primary key,
    cliente_id          bigint      not null references cliente (id),
    automovel_id        bigint      not null references automovel (id),
    agente_avaliador_id bigint      references agente (id),
    contrato_credito_id bigint      references contrato_credito (id),
    status              varchar(20) not null default 'EM_ANALISE'
        check (status in ('EM_ANALISE', 'APROVADO', 'REJEITADO', 'CONTRATO_EXECUTADO', 'CANCELADO')),
    criado_em           timestamp   not null default now()
);

alter table usuario          enable row level security;
alter table cliente          enable row level security;
alter table agente           enable row level security;
alter table rendimento       enable row level security;
alter table automovel        enable row level security;
alter table contrato_credito enable row level security;
alter table pedido           enable row level security;
