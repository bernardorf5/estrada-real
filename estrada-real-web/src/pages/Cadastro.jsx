import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../api.js';
import RendimentosEditor from '../components/RendimentosEditor.jsx';

const vazio = {
  nome: '', login: '', senha: '',
  rg: '', cpf: '', endereco: '', profissao: '',
  cnpj: '', razaoSocial: '',
};

export default function Cadastro() {
  const navigate = useNavigate();
  const [tipo, setTipo] = useState('CLIENTE');
  const [f, setF] = useState(vazio);
  const [rendimentos, setRendimentos] = useState([]);
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(false);

  const campo = (nome) => ({
    value: f[nome],
    onChange: (e) => setF({ ...f, [nome]: e.target.value }),
    required: true,
  });

  async function enviar(e) {
    e.preventDefault();
    setErro('');
    setCarregando(true);
    try {
      if (tipo === 'CLIENTE') {
        await api('/api/auth/register/cliente', {
          method: 'POST',
          body: {
            nome: f.nome, login: f.login, senha: f.senha,
            rg: f.rg, cpf: f.cpf, endereco: f.endereco, profissao: f.profissao,
            rendimentos: rendimentos.map((r) => ({
              entidadeEmpregadora: r.entidadeEmpregadora,
              valor: Number(r.valor),
            })),
          },
        });
      } else {
        await api('/api/auth/register/agente', {
          method: 'POST',
          body: {
            nome: f.nome, login: f.login, senha: f.senha,
            cnpj: f.cnpj, razaoSocial: f.razaoSocial, papel: tipo,
          },
        });
      }
      navigate('/login', { state: { cadastrado: true } });
    } catch (err) {
      setErro(err.message);
    } finally {
      setCarregando(false);
    }
  }

  return (
    <form className="card auth larga" onSubmit={enviar}>
      <h1>Criar conta</h1>
      <p className="sub">Escolha o tipo de conta e preencha os dados.</p>

      <div className="seg">
        {[['CLIENTE', 'Cliente'], ['EMPRESA', 'Empresa'], ['BANCO', 'Banco']].map(([valor, rotulo]) => (
          <button
            type="button"
            key={valor}
            className={tipo === valor ? 'on' : ''}
            onClick={() => setTipo(valor)}
          >
            {rotulo}
          </button>
        ))}
      </div>

      <div className="row">
        <div><label>Nome</label><input {...campo('nome')} /></div>
        <div><label>Usuário</label><input {...campo('login')} minLength={3} /></div>
      </div>
      <label>Senha (mínimo 6 caracteres)</label>
      <input type="password" {...campo('senha')} minLength={6} />

      {tipo === 'CLIENTE' ? (
        <>
          <div className="row">
            <div><label>RG</label><input {...campo('rg')} /></div>
            <div><label>CPF</label><input {...campo('cpf')} placeholder="000.000.000-00" /></div>
          </div>
          <label>Endereço</label>
          <input {...campo('endereco')} />
          <label>Profissão</label>
          <input {...campo('profissao')} />
          <h2 style={{ marginTop: 22 }}>Rendimentos (até 3)</h2>
          <RendimentosEditor itens={rendimentos} onChange={setRendimentos} />
        </>
      ) : (
        <div className="row">
          <div><label>CNPJ</label><input {...campo('cnpj')} /></div>
          <div><label>Razão social</label><input {...campo('razaoSocial')} /></div>
        </div>
      )}

      <div className="err">{erro}</div>
      <button className="btn full" disabled={carregando}>
        {carregando ? 'Criando...' : 'Criar conta'}
      </button>
      <p className="hint">
        Já tem conta? <Link to="/login">Entrar</Link>
      </p>
    </form>
  );
}
