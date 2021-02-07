package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@Controller
public class IngredientController {
    private final  RecipeService recipeService ;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }


    @GetMapping
    @RequestMapping( "/recipe/{id}/ingredients")
    public String ingredients(@PathVariable Long id, Model model) {
        log.debug("Getting ingridients for recipe :{}", id);
        model.addAttribute("recipe", recipeService.getRecipeById(id));
        return "/recipe/ingredient/list";
    }
    @GetMapping
    @RequestMapping( "/recipe/{idRecipe}/ingredient/{idIngredient}/show")
    public String showIngredient(@PathVariable Long idRecipe,@PathVariable Long idIngredient, Model model) {
        log.debug("Getting ingredient  id={} for recipe id = {}", idIngredient,idRecipe);
        model.addAttribute("ingredient",ingredientService.findRecipeByIdAndIngredientid(idRecipe, idIngredient));
        return "/recipe/ingredient/show";
    }
    @GetMapping
    @RequestMapping( "/recipe/{idRecipe}/ingredient/{idIngredient}/update")
    public String updateRecipeIngredient(@PathVariable Long idRecipe,@PathVariable Long idIngredient, Model model){
        IngredientCommand ingredientCommand = ingredientService.findRecipeByIdAndIngredientid(idRecipe, idIngredient);
        model.addAttribute("ingredient", ingredientCommand);

        Set<UnitOfMeasure> allUoM = unitOfMeasureService.findAllUoM();
        model.addAttribute("uomList",allUoM );
        return "/recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping( "/recipe/{idRecipe}/ingredient/new")
    public String newIngredient(@PathVariable Long idRecipe, Model model){

        //make sure we have a good id value
        RecipeCommand recipeCommand = recipeService.findRecipeCommandById(idRecipe);
        //todo raise exception if null
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(idRecipe);
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
        model.addAttribute("ingredient", ingredientCommand);
        Set<UnitOfMeasure> allUoM = unitOfMeasureService.findAllUoM();
        model.addAttribute("uomList",allUoM );
        return "/recipe/ingredient/ingredientform";
    }



    @PostMapping
    @RequestMapping( "/recipe/{idRecipe}/ingredient")
    public String saveUpdateIngredient(@ModelAttribute IngredientCommand command){
        IngredientCommand ingredientCommand = ingredientService.savedIngredientCommand(command);
        return "redirect:/recipe/"+ingredientCommand.getRecipeId()+"/ingredient/"+ingredientCommand.getId()+"/show";

    }

    @GetMapping
    @RequestMapping(value = "/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId){
        ingredientService.deleteIngredientById(recipeId, ingredientId);
        log.debug("deleting ingredient id:" + ingredientId);
        return "redirect:/recipe/"+ recipeId +"/ingredients";
    }
}
