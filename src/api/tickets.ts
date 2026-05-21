import http from "./http";
import type { ApiResponse } from "./auth";

export type TicketResponse = {
  id: number;
  titulo: string;
  descripcion: string;
  status: string;
  prioridad: string;
  categoriaId: number | null;
  categoriaNombre: string | null;
  creadoPorId: number | null;
  creadoPorUsername: string | null;
  asignadoAId: number | null;
  asignadoAUsername: string | null;
  asignadoANombreCompleto: string | null;
  createdAt: string | null;
  updatedAt: string | null;
  closedAt: string | null;
};

export const ticketApi = {
  async findAll(): Promise<TicketResponse[]> {
    const res = await http<ApiResponse<TicketResponse[]>>("/api/tickets");
    if (res === null) {
      throw new Error("No se pudo cargar la lista de tickets");
    }
    return res.data;
  },
};

export default ticketApi;
