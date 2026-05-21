import React, { useEffect, useMemo, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { ticketApi, type TicketResponse } from "../api/tickets";

const statusOptions: string[] = ["", "ABIERTO", "EN_PROCESO", "PENDIENTE", "RESUELTO", "CERRADO"];
const priorityOptions: string[] = ["", "BAJA", "MEDIA", "ALTA", "CRITICA"];

const HomePage: React.FC = () => {
  const { user, logout } = useAuth();
  const [tickets, setTickets] = useState<TicketResponse[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [statusFilter, setStatusFilter] = useState<string>("");
  const [priorityFilter, setPriorityFilter] = useState<string>("");
  const [categoryFilter, setCategoryFilter] = useState<string>("");

  useEffect(() => {
    const loadTickets = async (): Promise<void> => {
      setLoading(true);
      setError(null);
      try {
        const data = await ticketApi.findAll();
        setTickets(data);
      } catch (err: unknown) {
        setError((err as Error).message || "No se pudieron cargar los tickets");
      } finally {
        setLoading(false);
      }
    };

    loadTickets();
  }, []);

  const filteredTickets = useMemo(() => {
    return tickets.filter((ticket: TicketResponse) => {
      const matchesStatus = statusFilter ? ticket.status === statusFilter : true;
      const matchesPriority = priorityFilter ? ticket.prioridad === priorityFilter : true;
      const matchesCategory = categoryFilter
        ? ticket.categoriaNombre?.toLowerCase().includes(categoryFilter.toLowerCase())
        : true;
      return matchesStatus && matchesPriority && matchesCategory;
    });
  }, [tickets, statusFilter, priorityFilter, categoryFilter]);

  if (!user) return null;

  return (
    <div className="center-container" style={{ background: '#f3f4f6', minHeight: '100vh' }}>
      <div className="card-light" style={{ maxWidth: '1000px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: '1rem' }}>
          <div>
            <h2 className="title">Bienvenido, {user.nombre}</h2>
            <p className="meta"><strong>Username:</strong> {user.username}</p>
            <p className="meta"><strong>Email:</strong> {user.email}</p>
            <p className="meta"><strong>Rol:</strong> {user.role}</p>
          </div>
          <button onClick={() => logout()} className="btn btn-danger" style={{ height: '40px' }}>
            Cerrar sesión
          </button>
        </div>

        <section style={{ marginTop: '1.5rem' }}>
          <h3 className="title" style={{ marginBottom: '0.75rem', fontSize: '1.1rem' }}>Listado de tickets</h3>
          <div className="filters-box">
            <label className="label">Estado</label>
            <select className="input" value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
              {statusOptions.map((statusOption) => (
                <option key={statusOption} value={statusOption}>
                  {statusOption || "Todos"}
                </option>
              ))}
            </select>

            <label className="label">Prioridad</label>
            <select className="input" value={priorityFilter} onChange={(e) => setPriorityFilter(e.target.value)}>
              {priorityOptions.map((priorityOption) => (
                <option key={priorityOption} value={priorityOption}>
                  {priorityOption || "Todas"}
                </option>
              ))}
            </select>

            <label className="label">Categoría</label>
            <input
              className="input"
              value={categoryFilter}
              onChange={(e) => setCategoryFilter(e.target.value)}
              placeholder="Filtrar por categoría"
            />
          </div>
        </section>

        {loading && <p>Cargando tickets...</p>}
        {error && <div className="error">{error}</div>}

        {!loading && !error && (
          <div className="ticket-list">
            {filteredTickets.length === 0 ? (
              <p>No hay tickets que coincidan con los filtros.</p>
            ) : (
              filteredTickets.map((ticket: TicketResponse) => (
                <div key={ticket.id} className="ticket-card">
                  <div className="ticket-header">
                    <strong>{ticket.titulo}</strong>
                    <span>{ticket.status}</span>
                  </div>
                  <p className="meta"><strong>Prioridad:</strong> {ticket.prioridad}</p>
                  <p className="meta"><strong>Categoría:</strong> {ticket.categoriaNombre || "Sin categoría"}</p>
                  <p className="meta"><strong>Asignado a:</strong> {ticket.asignadoANombreCompleto || ticket.asignadoAUsername || "Sin asignar"}</p>
                  <p className="meta"><strong>Creado:</strong> {ticket.createdAt ? new Date(ticket.createdAt).toLocaleString() : "-"}</p>
                </div>
              ))
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default HomePage;
