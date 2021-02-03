package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.domain.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryCommandTest {
    CategoryToCategoryCommand converter ;
    public static final String DESCRIPTION = "description";
    public static final Long LONG_VALUE = 1L;
    @BeforeEach
    void setUp() {
        converter = new CategoryToCategoryCommand();
    }

    @Test
    public void testNullObjectConvert() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObj() throws Exception {
        assertNotNull(converter.convert(new Category()));
    }

    @Test
    void convert() {
        // given
        Category source = new Category();
        source.setDescription(DESCRIPTION);
        source.setId(LONG_VALUE);
        //when
        CategoryCommand categoryCommand = converter.convert(source);
        // then
        Assertions.assertEquals(source.getId(),categoryCommand.getId());
        Assertions.assertEquals(source.getDescription(), categoryCommand.getDescription());

    }
}
