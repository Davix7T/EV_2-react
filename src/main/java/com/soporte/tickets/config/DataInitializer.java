package com.soporte.tickets.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.soporte.tickets.entity.Category;
import com.soporte.tickets.entity.Ticket;
import com.soporte.tickets.entity.User;
import com.soporte.tickets.enums.Priority;
import com.soporte.tickets.enums.Role;
import com.soporte.tickets.enums.TicketStatus;
import com.soporte.tickets.repository.CategoryRepository;
import com.soporte.tickets.repository.TicketRepository;
import com.soporte.tickets.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        seedUsers();
        seedCategories();
        seedTickets();
        log.info("Datos iniciales cargados correctamente");
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;

        userRepository.save(User.builder()
                .username("admin")
                .email("admin@soporte.com")
                .password(passwordEncoder.encode("admin123"))
                .nombre("Administrador")
                .apellido("Sistema")
                .role(Role.ADMIN)
                .build());

        userRepository.save(User.builder()
                .username("tecnico1")
                .email("tecnico1@soporte.com")
                .password(passwordEncoder.encode("tecnico123"))
                .nombre("Carlos")
                .apellido("Pérez")
                .role(Role.TECNICO)
                .build());

        userRepository.save(User.builder()
                .username("tecnico2")
                .email("tecnico2@soporte.com")
                .password(passwordEncoder.encode("tecnico123"))
                .nombre("María")
                .apellido("Gómez")
                .role(Role.TECNICO)
                .build());

        userRepository.save(User.builder()
                .username("usuario1")
                .email("usuario1@empresa.com")
                .password(passwordEncoder.encode("usuario123"))
                .nombre("Juan")
                .apellido("López")
                .role(Role.USUARIO)
                .build());

        userRepository.save(User.builder()
                .username("usuario2")
                .email("usuario2@empresa.com")
                .password(passwordEncoder.encode("usuario123"))
                .nombre("Ana")
                .apellido("Martínez")
                .role(Role.USUARIO)
                .build());

        log.info("Usuarios de prueba creados");
    }

    private void seedCategories() {
        if (categoryRepository.count() > 0) return;

        categoryRepository.save(Category.builder()
                .nombre("Hardware")
                .descripcion("Problemas con equipos físicos: computadores, impresoras, periféricos")
                .build());

        categoryRepository.save(Category.builder()
                .nombre("Software")
                .descripcion("Instalación, configuración y errores de aplicaciones")
                .build());

        categoryRepository.save(Category.builder()
                .nombre("Red y Conectividad")
                .descripcion("Problemas de red, internet, VPN y conectividad")
                .build());

        categoryRepository.save(Category.builder()
                .nombre("Seguridad")
                .descripcion("Incidentes de seguridad, accesos y contraseñas")
                .build());

        categoryRepository.save(Category.builder()
                .nombre("Mantenimiento Preventivo")
                .descripcion("Solicitudes de mantenimiento programado")
                .build());

        categoryRepository.save(Category.builder()
                .nombre("Otros")
                .descripcion("Solicitudes que no encajan en otras categorías")
                .build());

        log.info("Categorías de prueba creadas");
    }

    private void seedTickets() {
        if (ticketRepository.count() > 0) return;

        // Obtener usuarios y categorías
        User tecnico1 = userRepository.findByUsername("tecnico1").orElseThrow();
        User tecnico2 = userRepository.findByUsername("tecnico2").orElseThrow();
        User usuario1 = userRepository.findByUsername("usuario1").orElseThrow();
        User usuario2 = userRepository.findByUsername("usuario2").orElseThrow();

        Category hardware = categoryRepository.findByNombre("Hardware").orElseThrow();
        Category software = categoryRepository.findByNombre("Software").orElseThrow();
        Category red = categoryRepository.findByNombre("Red y Conectividad").orElseThrow();
        Category seguridad = categoryRepository.findByNombre("Seguridad").orElseThrow();

        // Ticket 1: Hardware - Asignado a tecnico1
        ticketRepository.save(Ticket.builder()
                .titulo("Computadora no enciende")
                .descripcion("La computadora del escritorio no enciende, revisado cable de poder pero no funciona")
                .status(TicketStatus.ABIERTO)
                .prioridad(Priority.ALTA)
                .categoria(hardware)
                .creadoPor(usuario1)
                .asignadoA(tecnico1)
                .build());

        // Ticket 2: Software - Asignado a tecnico2
        ticketRepository.save(Ticket.builder()
                .titulo("Error en instalación de Office")
                .descripcion("Error durante la instalación de Microsoft Office: código 0x80004005")
                .status(TicketStatus.EN_PROCESO)
                .prioridad(Priority.MEDIA)
                .categoria(software)
                .creadoPor(usuario2)
                .asignadoA(tecnico2)
                .build());

        // Ticket 3: Red - Asignado a tecnico1
        ticketRepository.save(Ticket.builder()
                .titulo("Conexión WiFi inestable")
                .descripcion("La conexión WiFi se desconecta frecuentemente en el área de recepción")
                .status(TicketStatus.PENDIENTE)
                .prioridad(Priority.MEDIA)
                .categoria(red)
                .creadoPor(usuario1)
                .asignadoA(tecnico1)
                .build());

        // Ticket 4: Seguridad - Asignado a tecnico2
        ticketRepository.save(Ticket.builder()
                .titulo("Cambio de contraseña requerido")
                .descripcion("Requiere asistencia para cambiar contraseña de acceso corporativo")
                .status(TicketStatus.EN_PROCESO)
                .prioridad(Priority.BAJA)
                .categoria(seguridad)
                .creadoPor(usuario2)
                .asignadoA(tecnico2)
                .build());

        // Ticket 5: Software - Sin asignar
        ticketRepository.save(Ticket.builder()
                .titulo("Actualizaciones de Windows pendientes")
                .descripcion("Sistema operativo requiere actualizar a última versión de Windows 11")
                .status(TicketStatus.ABIERTO)
                .prioridad(Priority.CRITICA)
                .categoria(software)
                .creadoPor(usuario1)
                .asignadoA(null)
                .build());

        // Ticket 6: Hardware - Resuelto
        ticketRepository.save(Ticket.builder()
                .titulo("Impresora no imprime en color")
                .descripcion("La impresora está imprimiendo solo en blanco y negro")
                .status(TicketStatus.RESUELTO)
                .prioridad(Priority.MEDIA)
                .categoria(hardware)
                .creadoPor(usuario2)
                .asignadoA(tecnico1)
                .build());

        log.info("Tickets de prueba creados");
    }
}
