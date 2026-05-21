import React, { createContext, useContext, useState } from "react";
import type { JwtResponse } from "../api/auth";

type AuthContextType = {
  user: JwtResponse | null;
  isAuthenticated: boolean;
  login: (data: JwtResponse) => void;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<JwtResponse | null>(() => {
    const raw: string | null = localStorage.getItem("user");
    return raw ? (JSON.parse(raw) as JwtResponse) : null;
  });

  const login = (data: JwtResponse): void => {
    localStorage.setItem("token", data.token);
    localStorage.setItem("user", JSON.stringify(data));
    setUser(data);
  };

  const logout = (): void => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setUser(null);
  };

  const value: AuthContextType = {
    user,
    isAuthenticated: user !== null,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export function useAuth(): AuthContextType {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return ctx;
}

export default AuthContext;
