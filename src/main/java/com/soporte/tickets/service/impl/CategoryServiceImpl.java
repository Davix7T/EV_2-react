package com.soporte.tickets.service.impl;

import com.soporte.tickets.dto.request.CategoryRequest;
import com.soporte.tickets.dto.response.CategoryResponse;
import com.soporte.tickets.entity.Category;
import com.soporte.tickets.exception.BadRequestException;
import com.soporte.tickets.exception.ResourceNotFoundException;
import com.soporte.tickets.repository.CategoryRepository;
import com.soporte.tickets.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CategoryResponse findById(Long id) {
        return mapToResponse(getCategory(id));
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNombre(request.getNombre())) {
            throw new BadRequestException("Ya existe una categoría con el nombre: " + request.getNombre());
        }
        Category category = Category.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .build();
        return mapToResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getCategory(id);

        if (!category.getNombre().equals(request.getNombre())
                && categoryRepository.existsByNombre(request.getNombre())) {
            throw new BadRequestException("Ya existe una categoría con el nombre: " + request.getNombre());
        }
        category.setNombre(request.getNombre());
        category.setDescripcion(request.getDescripcion());
        return mapToResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría", id);
        }
        categoryRepository.deleteById(id);
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", id));
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .nombre(category.getNombre())
                .descripcion(category.getDescripcion())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
