package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryTest {
    CategoryCommandToCategory converter ;
    public static final String DESCRIPTION = "description";
    public static final Long LONG_VALUE = 1L;

    @BeforeEach
    void setUp() {
        converter = new CategoryCommandToCategory() ;
    }

    @Test
    public void testNullObjectConvert() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObj() throws Exception {
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    void convert() {
        // given
        CategoryCommand source = new CategoryCommand();
        source.setDescription(DESCRIPTION);
        source.setId(LONG_VALUE);

        //when
        Category category = converter.convert(source);

        //then
        Assertions.assertEquals(source.getId(),category.getId());
        Assertions.assertEquals(source.getDescription(), category.getDescription());

    }
}
