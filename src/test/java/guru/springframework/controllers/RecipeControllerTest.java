package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.service.RecipeService;
import guru.springframework.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;


import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {
    @InjectMocks
    RecipeController controller;
    @Mock
    RecipeService service ;
    @Mock
    Model model;
    MockMvc mockMvc;
    Recipe recipe;


    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build() ;
        recipe = new Recipe();
        recipe.setId(1L);

    }

    @Test
    public void getshowPage() throws Exception {
        mockMvc.perform(get("/recipe/show/1")).
                andExpect(status().isOk()).
                andExpect(view().name("/recipe/show"));
    }

    @Test
    public void getRecipeById() throws Exception {
        //given
        when(service.getRecipeById(anyLong())).thenReturn(recipe) ;
        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        //when
        String view = controller.getRecipeById("1",model);
        //then
        assertEquals("/recipe/show", view);
        verify(service, times(1)).getRecipeById(anyLong());
        verify(model).addAttribute(eq("recipe"),argumentCaptor.capture());
        Recipe argumentCaptorValue = argumentCaptor.getValue();
        assertEquals(recipe.getId(),argumentCaptorValue.getId());

    }
}
