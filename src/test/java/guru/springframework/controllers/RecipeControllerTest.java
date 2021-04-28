package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.service.RecipeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        when(service.getRecipeById(any())).thenReturn(recipe);
        mockMvc.perform(get("/recipe/1/show")).
                andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("/recipe/show"));
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
    @Test
    public void testGetRecipeNotFound() throws Exception {

        when(service.getRecipeById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    public void testGetRecipeNumberFormatException() throws Exception {

//        when(service.getRecipeById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/adel/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void testGetNewRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("/recipe/recipeform"));
        //given
        ArgumentCaptor<RecipeCommand> argumentCaptor = ArgumentCaptor.forClass(RecipeCommand.class);
        //when
        String page = controller.newRecipePage(model);
        //then
        verify(model).addAttribute(eq("recipe") , argumentCaptor.capture()) ;
        RecipeCommand value = argumentCaptor.getValue();
        Assertions.assertEquals(RecipeCommand.class,value.getClass());

    }

    @Test
    void testPostNewRecipeForm() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);
        when(service.saveRecipeCommand(any())).thenReturn(command);
        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));
    }

    @Test
    void updateRecipePage() throws Exception {
        //given
        RecipeCommand command = new RecipeCommand() ;
        command.setId(1L);
        //when
        when(service.findRecipeCommandById(any())).thenReturn(command);
        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("/recipe/recipeform")) ;
        verify(service, times(1)).findRecipeCommandById(any());
    }

    @Test
    void deleteRecipeTest() throws Exception {
        //given
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        verify(service, times(1)).deleteRecipeById(any());

    }
}
