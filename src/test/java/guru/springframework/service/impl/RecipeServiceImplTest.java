package guru.springframework.service.impl;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceImplTest {

    @Mock
    RecipeRepository recipeRepository ;
    @Mock
    RecipeCommandToRecipe commandToRecipe ;
    @Mock
    RecipeToRecipeCommand recipeToCommand ;
    @InjectMocks
    RecipeServiceImpl recipeService ;
    Recipe recipeFounded ;
    final Long recipeId = 1L ;


    @BeforeEach
    public void setUp() throws Exception {
        recipeFounded = new Recipe() ;
        recipeFounded.setId(recipeId); ;
    }

    @Test
    public void getRecipes() {
        Recipe recipe = new Recipe() ;
        Set<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);
        when(recipeRepository.findAll()).thenReturn(recipesData) ;
        Set<Recipe> recipes =  recipeService.getRecipes();
        assertEquals(recipes.size(), recipesData.size());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void getRecipeById() {
        //given
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(this.recipeFounded));
        //when
        Recipe recipe = recipeService.getRecipeById(recipeId);
        //then
        assertNotNull(recipe);
        assertEquals(this.recipeId , recipe.getId());
        verify(recipeRepository, times(1)).findById(anyLong());
    }
    @Test
    void getRecipeByIdNull() {
        //given
        when(recipeRepository.findById(anyLong())).thenThrow(RuntimeException.class);
        //when
        Assertions.assertThrows(RuntimeException.class, () -> {
            recipeService.getRecipeById(3L);
        });
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    void saveRecipeCommandTest() {
        //given
        RecipeCommand commandPassed = new RecipeCommand();
        RecipeCommand commandReturned = new RecipeCommand();
        commandReturned.setId(1L);
        Recipe recipe = new Recipe();
        when(commandToRecipe.convert(commandPassed)).thenReturn(recipe);
        when(recipeRepository.save(recipe)).thenReturn(recipeFounded);
        when(recipeToCommand.convert(recipeFounded)).thenReturn(commandReturned);
        //when
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(commandPassed);
        //then
        Assertions.assertNotNull(savedRecipeCommand);
        assertEquals(commandReturned.getId(), savedRecipeCommand.getId());
    }

    @Test
    void findRecipeCommandById() {
        //given
        RecipeCommand returnedRecipeCommand = new RecipeCommand();
        returnedRecipeCommand.setId(1L);
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(recipeFounded));
        when(recipeToCommand.convert(any(Recipe.class))).thenReturn(returnedRecipeCommand) ;
        //when
        RecipeCommand recipeCommandById = recipeService.findRecipeCommandById(1L);
        //then
        assertNotNull(recipeCommandById);
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteRecipeByIdTest() {
        //when
        recipeService.deleteRecipeById(recipeId);
        //then
        verify(recipeRepository, times(1)).deleteById(any());
    }
}
