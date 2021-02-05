package guru.springframework.service.impl;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.service.RecipeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

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
        return recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

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

}
