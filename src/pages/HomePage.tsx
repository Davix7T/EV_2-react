import React from "react";
import { useAuth } from "../context/AuthContext";

const HomePage: React.FC = () => {
  const { user, logout } = useAuth();

  if (!user) return null;

  return (
    <div className="center-container" style={{ background: '#f3f4f6', minHeight: '100vh' }}>
      <div className="card-light">
        <h2 className="title">Bienvenido, {user.nombre}</h2>
        <p className="meta"><strong>Username:</strong> {user.username}</p>
        <p className="meta"><strong>Email:</strong> {user.email}</p>
        <p className="meta"><strong>Rol:</strong> {user.role}</p>
        <button onClick={() => logout()} className="btn btn-danger" style={{ marginTop: '1rem' }}>
          Cerrar sesión
        </button>
      </div>
    </div>
  );
};

export default HomePage;
