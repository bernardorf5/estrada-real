import { createContext, useContext, useState } from 'react';
import { api } from './api.js';

const AuthContext = createContext(null);

export const inicio = (papel) => (papel === 'CLIENTE' ? '/pedidos' : '/painel');

export function AuthProvider({ children }) {
  const [sessao, setSessao] = useState(() => {
    try {
      return JSON.parse(sessionStorage.getItem('sessao'));
    } catch {
      return null;
    }
  });

  async function entrar(login, senha) {
    const auth = { login, senha };
    const usuario = await api('/api/auth/me', { auth });
    const nova = { auth, usuario };
    sessionStorage.setItem('sessao', JSON.stringify(nova));
    setSessao(nova);
    return usuario;
  }

  function sair() {
    sessionStorage.removeItem('sessao');
    setSessao(null);
  }

  const chamar = (path, opcoes = {}) => api(path, { ...opcoes, auth: sessao?.auth });

  return (
    <AuthContext.Provider value={{ sessao, entrar, sair, chamar }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
