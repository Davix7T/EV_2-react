import http from "./http";
import type { ApiResponse } from "./auth";

export type CategoryResponse = {
  id: number;
  nombre: string;
  descripcion: string | null;
  createdAt: string | null;
};

export const categoryApi = {
  async findAll(): Promise<CategoryResponse[]> {
    const res = await http<ApiResponse<CategoryResponse[]>>("/api/categorias");
    if (res === null) {
      throw new Error("No se pudieron cargar las categorías");
    }
    return res.data;
  },
};

export default categoryApi;
