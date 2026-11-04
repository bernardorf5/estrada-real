import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth.jsx';

export default function Layout() {
  const { sessao, sair } = useAuth();
  const navigate = useNavigate();
  const papel = sessao?.usuario.papel;

  return (
    <>
      <header className="topo">
        <span className="logo">Estrada Real</span>
        <nav>
          {papel === 'CLIENTE' && (
            <>
              <NavLink to="/pedidos" end>Meus pedidos</NavLink>
              <NavLink to="/pedidos/novo">Novo pedido</NavLink>
              <NavLink to="/perfil">Meu perfil</NavLink>
            </>
          )}
          {papel && papel !== 'CLIENTE' && <NavLink to="/painel">Painel</NavLink>}
        </nav>
        <span className="sp" />
        {sessao && (
          <>
            <span className="quem">{sessao.usuario.nome}</span>
            <button
              className="btn ghost sm"
              onClick={() => {
                sair();
                navigate('/login');
              }}
            >
              Sair
            </button>
          </>
        )}
      </header>
      <main>
        <Outlet />
      </main>
    </>
  );
}
