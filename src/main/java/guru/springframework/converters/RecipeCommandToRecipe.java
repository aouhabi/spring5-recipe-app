package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeCommandToRecipe  implements Converter<RecipeCommand, Recipe> {
    private final  NotesCommandToNotes notesConverster ;
    private final IngredientCommandToIngredient ingredientCoverter;
    private final CategoryCommandToCategory categoryConverter;

    public RecipeCommandToRecipe(NotesCommandToNotes notesConverster, IngredientCommandToIngredient ingredientCoverter, CategoryCommandToCategory categoryConverter) {
        this.notesConverster = notesConverster;
        this.ingredientCoverter = ingredientCoverter;
        this.categoryConverter = categoryConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeCommand source) {
        if( source == null ){
            return null;
        }
        final Recipe recipe = new Recipe() ;
        recipe.setId(source.getId());
        recipe.setCookTime(source.getCookTime());
        recipe.setDifficulty(source.getDifficulty());
        recipe.setDescription(source.getDescription());
        recipe.setDirections(source.getDirections());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setUrl(source.getUrl());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setNotes(notesConverster.convert(source.getNotes()));
        if (source.getCategories() != null && source.getCategories().size() > 0){
            source.getCategories()
                    .forEach( category -> recipe.getCategories().add(categoryConverter.convert(category)));
        }
        if (source.getIngredients() != null && source.getIngredients().size() > 0){
            source.getIngredients()
                    .forEach(ingredient -> recipe.getIngredients().add(ingredientCoverter.convert(ingredient)));
        }
        if(source.getImage() != null){
            recipe.setImage(source.getImage());
        }
        return recipe;
    }
}
