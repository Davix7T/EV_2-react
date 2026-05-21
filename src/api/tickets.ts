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

export type TicketCreateDto = {
  titulo: string;
  descripcion: string;
  prioridad: string;
  categoriaId: number | null;
};

export const ticketApi = {
  async findAll(): Promise<TicketResponse[]> {
    const res = await http<ApiResponse<TicketResponse[]>>("/api/tickets");
    if (res === null) {
      throw new Error("No se pudo cargar la lista de tickets");
    }
    return res.data;
  },

  async create(dto: TicketCreateDto): Promise<TicketResponse> {
    const res = await http<ApiResponse<TicketResponse>>("/api/tickets", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dto),
    });
    if (res === null) {
      throw new Error("No se pudo crear el ticket");
    }
    return res.data;
  },
};

export default ticketApi;
