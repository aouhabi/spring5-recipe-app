package guru.springframework.service.impl;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private  final RecipeRepository recipeRepository ;
    private  final RecipeCommandToRecipe commandToRecipe ;
    private  final RecipeToRecipeCommand recipeToCommand ;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe converter, RecipeToRecipeCommand recipeToCommand) {
        this.recipeRepository = recipeRepository;
        this.commandToRecipe = converter;
        this.recipeToCommand = recipeToCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().forEach(recipes::add);
        return recipes;
    }

    @Override
    public Recipe getRecipeById(Long id) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if (!recipeOptional.isPresent()) {
            throw new NotFoundException("Recipe Not Found. For ID value: " + id.toString() );
        }
        return recipeOptional.get();    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = commandToRecipe.convert(command);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        RecipeCommand recipeCommand = recipeToCommand.convert(savedRecipe);
        return recipeCommand;
    }

    @Override
    @Transactional
    public RecipeCommand findRecipeCommandById(Long id) {
        Recipe recipe = this.getRecipeById(id);
        RecipeCommand recipeCommand = recipeToCommand.convert(recipe);
        return recipeCommand;
    }

    @Override
    public void deleteRecipeById(Long id) {
            recipeRepository.deleteById(id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception){

        log.error("Handling not found exception");
        log.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }

}
