package guru.springframework.service.impl;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {
    @Mock
    IngredientRepository ingredientRepository;
    @Mock
    RecipeRepository recipeRepository ;
    @Mock
    IngredientToIngredientCommand ingredientToIngredientCommand;
    @Mock
    IngredientCommandToIngredient ingredientCommandToIngredient;
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;
    @InjectMocks
    IngredientServiceImpl ingredientService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findRecipeByIdAndIngredientid() {
        // given
        Long ingredientId = 3L;
        Long recipeId = 1L;
        Recipe recipe = new Recipe() ;
        recipe.setId(recipeId);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(1L);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(ingredientId);
        ingredientCommand.setRecipeId(recipeId);

        //when
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(ingredientToIngredientCommand.convert(ingredient3)).thenReturn(ingredientCommand);

        IngredientCommand recipeByIdAndIngredientid = ingredientService.findRecipeByIdAndIngredientid(recipeId, ingredientId);
        //then
        verify(recipeRepository, times(1)).findById(any());
        verify(ingredientToIngredientCommand).convert(any(Ingredient.class));
        assertEquals(ingredientId, recipeByIdAndIngredientid.getId());
        assertEquals(recipeId, recipeByIdAndIngredientid.getRecipeId());
    }

    @Test
    void savedIngredientCommand() {
        // given
        UnitOfMeasure unit = new UnitOfMeasure();
        UnitOfMeasureCommand unitCommand = new UnitOfMeasureCommand();
        unitCommand.setId(1l);
        unit.setId(1L);
        IngredientCommand command = new IngredientCommand();
        command.setDescription("updated description");
        command.setUnitOfMeasure(unitCommand);
        command.setId(2L);
        command.setRecipeId(1L);
        Recipe recipe = new Recipe();
        recipe.setId(1l);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setUnitOfMeasure(unit);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setUnitOfMeasure(unit);
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        when(unitOfMeasureRepository.findById(1L)).thenReturn(Optional.of(unit));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);
        when(ingredientToIngredientCommand.convert(any(Ingredient.class))).thenReturn(command);


        // when
        IngredientCommand savedIngredientCommand = ingredientService.savedIngredientCommand(command);

        // then
        assertEquals(command.getId(), savedIngredientCommand.getId());
        assertEquals(command.getDescription(), savedIngredientCommand.getDescription());

    }

    @Test
    void deleteIngredientById() {
        //given
        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(3L);
        recipe.addIngredient(ingredient);
        ingredient.setRecipe(recipe);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        //when
        ingredientService.deleteIngredientById(1L, 3L);

        //then
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}
