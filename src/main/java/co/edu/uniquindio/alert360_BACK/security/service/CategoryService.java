package co.edu.uniquindio.alert360_BACK.security.service;

import co.edu.uniquindio.alert360_BACK.security.dto.CategoryDto;
import co.edu.uniquindio.alert360_BACK.security.entity.Category;
import co.edu.uniquindio.alert360_BACK.security.exception.DuplicateResourceException;
import co.edu.uniquindio.alert360_BACK.security.exception.ResourceNotFoundException;
import co.edu.uniquindio.alert360_BACK.security.repository.CategoryRespository;
import co.edu.uniquindio.alert360_BACK.security.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRespository categoryRespository;

    @Autowired
    private DtoMapper dtoMapper;


    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRespository.findByName(categoryDto.getName()).isPresent()) {
            throw new DuplicateResourceException("La categoría ya existe");
        }

        Category category = new Category(
                categoryDto.getName(),
                categoryDto.getDescription()
        );
        categoryRespository.save(category);
        categoryDto.setId(category.getId());
        return categoryDto;
    }

    public CategoryDto updateCategory(String id, CategoryDto categoryDto) {
        Category category = categoryRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        if (!category.getName().equals(categoryDto.getName()) &&
                categoryRespository.findByName(categoryDto.getName()).isPresent()) {
            throw new DuplicateResourceException("No se puede actualizar la categoría, ya que existe una con el mismo nombre");
        }

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        categoryRespository.save(category);

        return dtoMapper.mapToDTO(category, CategoryDto.class);
    }

    public void deleteCategory(String id) {
        if (!categoryRespository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada");
        }
        categoryRespository.deleteById(id);
    }

    public CategoryDto getCategoryById(String id) {
        Category category = categoryRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        return dtoMapper.mapToDTO(category, CategoryDto.class);
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRespository.findAll();
        return categories.stream()
                .map(category -> dtoMapper.mapToDTO(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

}