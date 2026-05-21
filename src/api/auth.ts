import http from "./http";
import type { Role } from "../types/role";

export type LoginDto = {
  username: string;
  password: string;
};

export type JwtResponse = {
  token: string;
  id: number;
  username: string;
  email: string;
  nombre: string;
  apellido: string;
  role: Role;
};

export type ApiResponse<T> = {
  success: boolean;
  message: string;
  data: T;
};

export const authApi = {
  async login(dto: LoginDto): Promise<ApiResponse<JwtResponse>> {
    const res = await http<ApiResponse<JwtResponse>>("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dto),
    });

    if (res === null) {
      throw new Error("Empty response from server");
    }
    return res;
  },
};

export default authApi;
