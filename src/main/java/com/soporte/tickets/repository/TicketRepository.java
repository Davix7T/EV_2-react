package com.soporte.tickets.repository;

import com.soporte.tickets.entity.Ticket;
import com.soporte.tickets.entity.User;
import com.soporte.tickets.enums.Priority;
import com.soporte.tickets.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCreadoPor(User user);

    List<Ticket> findByAsignadoA(User tecnico);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByPrioridad(Priority prioridad);

    List<Ticket> findByCategoriaId(Long categoriaId);

    @Query("SELECT t FROM Ticket t WHERE t.asignadoA IS NULL AND t.status = 'ABIERTO'")
    List<Ticket> findTicketsSinAsignar();

    @Query("SELECT t FROM Ticket t WHERE t.asignadoA.id = :tecnicoId AND t.status NOT IN ('RESUELTO', 'CERRADO')")
    List<Ticket> findTicketsActivosPorTecnico(@Param("tecnicoId") Long tecnicoId);
}
