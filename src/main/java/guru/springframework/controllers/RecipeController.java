package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by jt on 6/1/17.
 */
@Slf4j
@Controller
public class RecipeController {
    private final RecipeService recipeService ;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipe/{id}/show")
    public String getRecipeById(@PathVariable String id, Model model){
        Recipe recipe = recipeService.getRecipeById(Long.valueOf(id));
        model.addAttribute("recipe",recipe );
               return "/recipe/show";
    }

    @RequestMapping(value = "/recipe/new", method = RequestMethod.GET)
    public String newRecipePage(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "/recipe/recipeform" ;
    }
    @RequestMapping(value = "/recipe", method = RequestMethod.POST)
    public String savedOrUpdatedRecipe(@ModelAttribute RecipeCommand recipeCommand) {
        RecipeCommand recipe = recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipe/" + recipe.getId()  + "/show";
    }

    @RequestMapping(value = "/recipe/{id}/update", method = RequestMethod.GET)
    public String updateRecipe(@PathVariable Long id,  Model model) {
        RecipeCommand command = recipeService.findRecipeCommandById(id);
        model.addAttribute("recipe", command);
        return "/recipe/recipeform";
    }

    @RequestMapping(value = "/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable Long id){
        recipeService.deleteRecipeById(id);
        return "redirect:/";
    }

}
