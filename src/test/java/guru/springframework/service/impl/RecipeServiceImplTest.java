package guru.springframework.service.impl;

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
}
