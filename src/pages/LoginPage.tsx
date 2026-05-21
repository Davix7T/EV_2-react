import React, { useState } from "react";
import { authApi, type LoginDto } from "../api/auth";
import { useAuth } from "../context/AuthContext";

const LoginPage: React.FC = () => {
  const { login } = useAuth();

  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const validate = (): string | null => {
    if (!username.trim()) return "El usuario es requerido";
    if (password.length < 6) return "La contraseña debe tener al menos 6 caracteres";
    return null;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    const clientErr: string | null = validate();
    if (clientErr) {
      setError(clientErr);
      return;
    }

    setLoading(true);
    try {
      const dto: LoginDto = { username, password };
      const res = await authApi.login(dto);
      if (!res.success) {
        setError(res.message || "Credenciales inválidas");
      } else {
        login(res.data);
      }
    } catch (err: unknown) {
      setError((err as Error).message || "Error en el servidor");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="center-container">
      <div className="card">
        <h1 className="title">Iniciar sesión</h1>
        {error && <div className="error">{error}</div>}
        <form onSubmit={handleSubmit}>
          <label className="label">Usuario</label>
          <input
            className="input-dark"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            disabled={loading}
          />

          <label className="label">Contraseña</label>
          <input
            type="password"
            className="input-dark"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            disabled={loading}
          />

          <button
            type="submit"
            className={`btn btn-primary ${loading ? 'btn-disabled' : ''}`}
            disabled={loading}
          >
            {loading ? "Ingresando…" : "Ingresar"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
