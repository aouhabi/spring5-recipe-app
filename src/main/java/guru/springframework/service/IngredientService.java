package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findRecipeByIdAndIngredientid(Long recipeId, Long ingrdientId );

    IngredientCommand savedIngredientCommand(IngredientCommand command);

    void deleteIngredientById(Long recipeId, Long ingredientId);
}
