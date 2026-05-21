package com.soporte.tickets.repository;

import com.soporte.tickets.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
