import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { inicio, useAuth } from '../auth.jsx';

export default function Login() {
  const { entrar } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [login, setLogin] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(false);

  async function enviar(e) {
    e.preventDefault();
    setErro('');
    setCarregando(true);
    try {
      const usuario = await entrar(login.trim(), senha);
      navigate(inicio(usuario.papel));
    } catch (err) {
      setErro(err.message);
    } finally {
      setCarregando(false);
    }
  }

  return (
    <form className="card auth" onSubmit={enviar}>
      <h1>Entrar</h1>
      <p className="sub">Acesse com seu usuário e senha.</p>
      {location.state?.cadastrado && <p className="ok-msg">Conta criada. Faça o login.</p>}
      <label>Usuário</label>
      <input value={login} onChange={(e) => setLogin(e.target.value)} required />
      <label>Senha</label>
      <input type="password" value={senha} onChange={(e) => setSenha(e.target.value)} required />
      <div className="err">{erro}</div>
      <button className="btn full" disabled={carregando}>
        {carregando ? 'Entrando...' : 'Entrar'}
      </button>
      <p className="hint">
        Não tem conta? <Link to="/cadastro">Cadastre-se</Link>
      </p>
    </form>
  );
}
