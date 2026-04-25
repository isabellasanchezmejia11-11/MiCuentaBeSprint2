package com.fabricaescuela.micuenta.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fabricaescuela.micuenta.application.dto.request.CreateBudgetRequest;
import com.fabricaescuela.micuenta.application.dto.response.BudgetResponse;
import com.fabricaescuela.micuenta.application.exception.BudgetAlreadyExistsException;
import com.fabricaescuela.micuenta.application.exception.ResourceNotFoundException;
import com.fabricaescuela.micuenta.domain.model.Budget;
import com.fabricaescuela.micuenta.domain.model.Category;
import com.fabricaescuela.micuenta.domain.model.MovementType;
import com.fabricaescuela.micuenta.domain.model.User;
import com.fabricaescuela.micuenta.domain.repository.BudgetRepository;
import com.fabricaescuela.micuenta.domain.repository.CategoryRepository;
import com.fabricaescuela.micuenta.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateBudgetUseCase Tests")
class CreateBudgetUseCaseTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateBudgetUseCase createBudgetUseCase;

    private User testUser;
    private Category testCategory;
    private CreateBudgetRequest validRequest;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "John", "Doe", "john@example.com", "password_hash");
        testCategory = new Category(1L, "Alimentación", MovementType.EXPENSE, 1L);
        validRequest = new CreateBudgetRequest(
                new BigDecimal("500.00"),
                1,
                2024,
                1L
        );
    }

    @Test
    @DisplayName("Escenario: Creación exitosa de presupuesto mensual")
    void testSuccessfulBudgetCreation() {
        // Arrange
        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(testCategory));
        when(budgetRepository.existsByUserIdAndCategoryIdAndMonthAndYear(1L, 1L, 1, 2024))
                .thenReturn(false);

        Budget savedBudget = new Budget(1L, new BigDecimal("500.00"), 1, 2024, 1L, 1L);
        when(budgetRepository.save(any(Budget.class)))
                .thenReturn(savedBudget);

        // Act
        BudgetResponse response = createBudgetUseCase.execute("john@example.com", validRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(new BigDecimal("500.00"), response.amountLimit());
        assertEquals(1, response.month());
        assertEquals(2024, response.year());
        assertEquals("Alimentación", response.categoryName());
        assertEquals(1L, response.userId());

        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    @Test
    @DisplayName("Escenario: Intento de crear presupuesto duplicado")
    void testDuplicateBudgetCreation() {
        // Arrange
        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(testCategory));
        when(budgetRepository.existsByUserIdAndCategoryIdAndMonthAndYear(1L, 1L, 1, 2024))
                .thenReturn(true);

        // Act & Assert
        BudgetAlreadyExistsException exception = assertThrows(
                BudgetAlreadyExistsException.class,
                () -> createBudgetUseCase.execute("john@example.com", validRequest)
        );

        assertEquals("Ya existe un presupuesto activo para esta categoría en el mes seleccionado",
                exception.getMessage());

        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    @DisplayName("Escenario: Intento con monto igual a cero")
    void testBudgetCreationWithZeroAmount() {
        // Arrange
        CreateBudgetRequest zeroAmountRequest = new CreateBudgetRequest(
                BigDecimal.ZERO,
                1,
                2024,
                1L
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createBudgetUseCase.execute("john@example.com", zeroAmountRequest)
        );

        assertEquals("El monto debe ser mayor a cero", exception.getMessage());

        verify(categoryRepository, never()).findById(anyLong());
        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    @DisplayName("Escenario: Intento con monto negativo")
    void testBudgetCreationWithNegativeAmount() {
        // Arrange
        CreateBudgetRequest negativeAmountRequest = new CreateBudgetRequest(
                new BigDecimal("-100.00"),
                1,
                2024,
                1L
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createBudgetUseCase.execute("john@example.com", negativeAmountRequest)
        );

        assertEquals("El monto debe ser mayor a cero", exception.getMessage());

        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    @DisplayName("Escenario: Categoría no encontrada")
    void testBudgetCreationWithNonExistentCategory() {
        // Arrange
        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        CreateBudgetRequest requestWithInvalidCategory = new CreateBudgetRequest(
                new BigDecimal("500.00"),
                1,
                2024,
                999L
        );

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> createBudgetUseCase.execute("john@example.com", requestWithInvalidCategory)
        );

        assertEquals("Category not found", exception.getMessage());

        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    @DisplayName("Escenario: Usuario no encontrado")
    void testBudgetCreationWithNonExistentUser() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> createBudgetUseCase.execute("nonexistent@example.com", validRequest)
        );

        assertEquals("Authenticated user not found", exception.getMessage());

        verify(categoryRepository, never()).findById(anyLong());
        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    @DisplayName("Escenario: Mes inválido (menor a 1)")
    void testBudgetCreationWithInvalidMonthBelowOne() {
        // Arrange
        CreateBudgetRequest invalidMonthRequest = new CreateBudgetRequest(
                new BigDecimal("500.00"),
                0,
                2024,
                1L
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createBudgetUseCase.execute("john@example.com", invalidMonthRequest)
        );

        assertEquals("El mes debe estar entre 1 y 12", exception.getMessage());

        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    @DisplayName("Escenario: Mes inválido (mayor a 12)")
    void testBudgetCreationWithInvalidMonthAboveTwelve() {
        // Arrange
        CreateBudgetRequest invalidMonthRequest = new CreateBudgetRequest(
                new BigDecimal("500.00"),
                13,
                2024,
                1L
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createBudgetUseCase.execute("john@example.com", invalidMonthRequest)
        );

        assertEquals("El mes debe estar entre 1 y 12", exception.getMessage());

        verify(budgetRepository, never()).save(any(Budget.class));
    }
}
