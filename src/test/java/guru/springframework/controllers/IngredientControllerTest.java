package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {
    MockMvc mockMvc ;
    @Mock
    RecipeService recipeService;
    @Mock
    IngredientService ingredientService;
    @Mock
    UnitOfMeasureService unitOfMeasureService;
    @Mock
    Model model;
    @InjectMocks
    IngredientController controller ;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build() ;
    }

    @Test
    void ingredients() throws Exception {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        when(recipeService.getRecipeById(any())).thenReturn(recipe) ;
        //when
        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/ingredient/list"));
        //then
        verify(recipeService, times(1)).getRecipeById(any());
    }

    @Test
    void showIngrdient() throws Exception {
        // given
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(ingredientService.findRecipeByIdAndIngredientid(any(),any())).thenReturn(ingredientCommand);
        // when
        mockMvc.perform(get("/recipe/1/ingredient/1/show"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/ingredient/show"));
        // then
        verify(ingredientService, times(1)).findRecipeByIdAndIngredientid(any(),any());
    }

    @Test
    void updateRecipeIngredientTest() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();

        //when
        when(ingredientService.findRecipeByIdAndIngredientid(anyLong(), anyLong())).thenReturn(ingredientCommand);
        when(unitOfMeasureService.findAllUoM()).thenReturn(new HashSet<>());

        //then
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
    }

    @Test
    void saveUpdateIngredient() throws Exception {
        // given
        IngredientCommand command = new IngredientCommand();
        command.setRecipeId(1L);
        command.setId(2L);
        when(ingredientService.savedIngredientCommand(any(IngredientCommand.class))).thenReturn(command);

        // when
        mockMvc.perform(post("/recipe/1/ingredient").
                        contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredient/2/show"));

        //then
        verify(ingredientService, times(1)).savedIngredientCommand(any(IngredientCommand.class));

    }

    @Test
    void newIngredient() throws Exception {
        // given
        ArgumentCaptor<IngredientCommand> argumentCaptor = ArgumentCaptor.forClass(IngredientCommand.class) ;

        // when
        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"))
                .andExpect(view().name("/recipe/ingredient/ingredientform"));
//        controller.newIngredient(1L,model);
        //then
        verify(unitOfMeasureService,times(1)).findAllUoM();
//        verify(model).addAttribute(eq("ingredient"),argumentCaptor.capture());
//        IngredientCommand argumentCaptorValue = argumentCaptor.getValue();
//        assertEquals(1L,argumentCaptorValue.getRecipeId());
    }

    @Test
    void deleteIngredient() throws Exception {

        mockMvc.perform(get("/recipe/1/ingredient/2/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients")) ;
        verify(ingredientService,times(1)).deleteIngredientById(1L,2L);
    }
}
