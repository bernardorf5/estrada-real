import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth.jsx';
import RendimentosEditor from '../components/RendimentosEditor.jsx';

export default function Perfil() {
  const { chamar, sair } = useAuth();
  const navigate = useNavigate();
  const [c, setC] = useState(null);
  const [rendimentos, setRendimentos] = useState([]);
  const [erro, setErro] = useState('');
  const [ok, setOk] = useState('');

  useEffect(() => {
    chamar('/api/clientes/me')
      .then((dados) => {
        setC(dados);
        setRendimentos(dados.rendimentos.map((r) => ({ ...r, valor: String(r.valor) })));
      })
      .catch((e) => setErro(e.message));
  }, []);

  if (!c) return <div className="err">{erro}</div>;

  const campo = (nome) => ({
    value: c[nome],
    onChange: (e) => setC({ ...c, [nome]: e.target.value }),
    required: true,
  });

  async function salvar(e) {
    e.preventDefault();
    setErro('');
    setOk('');
    try {
      await chamar('/api/clientes/me', {
        method: 'PUT',
        body: {
          nome: c.nome, rg: c.rg, endereco: c.endereco, profissao: c.profissao,
          rendimentos: rendimentos.map((r) => ({
            entidadeEmpregadora: r.entidadeEmpregadora,
            valor: Number(r.valor),
          })),
        },
      });
      setOk('Alterações salvas.');
    } catch (err) {
      setErro(err.message);
    }
  }

  async function excluir() {
    if (!confirm('Excluir sua conta e todos os seus pedidos? Isso não pode ser desfeito.')) return;
    try {
      await chamar('/api/clientes/me', { method: 'DELETE' });
      sair();
      navigate('/login');
    } catch (err) {
      setErro(err.message);
    }
  }

  return (
    <form className="card auth larga" onSubmit={salvar}>
      <h1>Meu perfil</h1>
      <p className="sub">Consulte, edite ou exclua seus dados.</p>
      <div className="row">
        <div><label>Nome</label><input {...campo('nome')} /></div>
        <div><label>CPF</label><input value={c.cpf} disabled /></div>
      </div>
      <div className="row">
        <div><label>RG</label><input {...campo('rg')} /></div>
        <div><label>Profissão</label><input {...campo('profissao')} /></div>
      </div>
      <label>Endereço</label>
      <input {...campo('endereco')} />
      <h2 style={{ marginTop: 22 }}>Rendimentos (até 3)</h2>
      <RendimentosEditor itens={rendimentos} onChange={setRendimentos} />
      <div className="err">{erro}</div>
      {ok && <p className="ok-msg">{ok}</p>}
      <div className="acoes">
        <button className="btn">Salvar alterações</button>
        <button type="button" className="btn ghost" onClick={excluir}>Excluir conta</button>
      </div>
    </form>
  );
}
