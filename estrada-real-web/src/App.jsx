import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider, inicio, useAuth } from './auth.jsx';
import Layout from './components/Layout.jsx';
import Login from './pages/Login.jsx';
import Cadastro from './pages/Cadastro.jsx';
import Pedidos from './pages/Pedidos.jsx';
import PedidoForm from './pages/PedidoForm.jsx';
import Perfil from './pages/Perfil.jsx';
import Painel from './pages/Painel.jsx';

function Protegida({ papeis, children }) {
  const { sessao } = useAuth();
  if (!sessao) return <Navigate to="/login" replace />;
  if (!papeis.includes(sessao.usuario.papel)) {
    return <Navigate to={inicio(sessao.usuario.papel)} replace />;
  }
  return children;
}

const CLIENTE = ['CLIENTE'];
const AGENTE = ['EMPRESA', 'BANCO'];

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route element={<Layout />}>
            <Route path="/login" element={<Login />} />
            <Route path="/cadastro" element={<Cadastro />} />
            <Route path="/pedidos" element={<Protegida papeis={CLIENTE}><Pedidos /></Protegida>} />
            <Route path="/pedidos/novo" element={<Protegida papeis={CLIENTE}><PedidoForm /></Protegida>} />
            <Route path="/pedidos/:id/editar" element={<Protegida papeis={CLIENTE}><PedidoForm /></Protegida>} />
            <Route path="/perfil" element={<Protegida papeis={CLIENTE}><Perfil /></Protegida>} />
            <Route path="/painel" element={<Protegida papeis={AGENTE}><Painel /></Protegida>} />
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
