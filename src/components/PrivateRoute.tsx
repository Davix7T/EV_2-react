import React from "react";
import { useAuth } from "../context/AuthContext";

type Props = {
  children: React.ReactNode;
  fallback: React.ReactNode;
};

const PrivateRoute: React.FC<Props> = ({ children, fallback }) => {
  const { isAuthenticated } = useAuth();
  return <>{isAuthenticated ? children : fallback}</>;
};

export default PrivateRoute;
