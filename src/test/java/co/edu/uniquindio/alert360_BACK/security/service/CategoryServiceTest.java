package co.edu.uniquindio.alert360_BACK.security.service;

import co.edu.uniquindio.alert360_BACK.security.dto.CategoryDto;
import co.edu.uniquindio.alert360_BACK.security.entity.Category;
import co.edu.uniquindio.alert360_BACK.security.repository.CategoryRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRespository categoryRespository;

    @BeforeEach
    public void setUp() {
        // Limpia el repositorio antes de cada test para evitar datos preexistentes
        categoryRespository.deleteAll();
    }

    @Test
    public void createCategoryTest() {
        // Configura los datos de prueba
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("New Category");
        categoryDto.setDescription("Category Description");

        // Llama al método que deseas probar
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);

        // Verifica los resultados
        assertNotNull(createdCategory);
        assertEquals("New Category", createdCategory.getName());
        assertEquals("Category Description", createdCategory.getDescription());
    }

    @Test
    public void updateCategoryTest() {
        // Guarda una categoría inicial
        Category category = new Category("Old Category", "Old Description");
        categoryRespository.save(category);

        // Configura los datos actualizados
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Updated Category");
        categoryDto.setDescription("Updated Description");

        // Llama al método que deseas probar
        CategoryDto updatedCategory = categoryService.updateCategory(category.getId(), categoryDto);

        // Verifica los resultados
        assertNotNull(updatedCategory);
        assertEquals("Updated Category", updatedCategory.getName());
        assertEquals("Updated Description", updatedCategory.getDescription());
    }

    @Test
    public void deleteCategoryTest() {
        // Guarda una categoría para eliminar
        Category category = new Category("Category to Delete", "Description");
        categoryRespository.save(category);

        // Llama al método que deseas probar
        assertDoesNotThrow(() -> categoryService.deleteCategory(category.getId()));

        // Verifica que la categoría haya sido eliminada
        Optional<Category> deletedCategory = categoryRespository.findById(category.getId());
        assertTrue(deletedCategory.isEmpty());
    }

    @Test
    public void getCategoryByIdTest() {
        // Guarda una categoría para buscar
        Category category = new Category("Category to Find", "Description");
        categoryRespository.save(category);

        // Llama al método que deseas probar
        CategoryDto foundCategory = categoryService.getCategoryById(category.getId());

        // Verifica los resultados
        assertNotNull(foundCategory);
        assertEquals(category.getName(), foundCategory.getName());
        assertEquals(category.getDescription(), foundCategory.getDescription());
    }

    @Test
    public void getAllCategoriesTest() {
        // Agrega los datos de prueba
        Category category1 = new Category("Category 1", "Description 1");
        Category category2 = new Category("Category 2", "Description 2");
        categoryRespository.saveAll(List.of(category1, category2));

        // Llama al método que deseas probar
        List<CategoryDto> categories = categoryService.getAllCategories();

        // Verifica los resultados
        assertNotNull(categories);
        assertEquals(2, categories.size());
    }
}