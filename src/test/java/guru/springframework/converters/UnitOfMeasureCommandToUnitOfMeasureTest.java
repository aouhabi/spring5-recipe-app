package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureCommandToUnitOfMeasureTest {
    UnitOfMeasureCommandToUnitOfMeasure converter;
    public static final String DESCRIPTION = "description";
    public static final Long LONG_VALUE = 1L;

    @BeforeEach
    void setUp() {
        converter = new UnitOfMeasureCommandToUnitOfMeasure();
    }

    @Test
    public void testNullObjectConvert() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObj() throws Exception {
        assertNotNull(converter.convert(new UnitOfMeasureCommand()));
    }
    @Test
    void convert() {
        //given
        UnitOfMeasureCommand source = new UnitOfMeasureCommand();
        source.setId(LONG_VALUE);
        source.setDescription(DESCRIPTION);

        //when
        UnitOfMeasure unitOfMeasure = converter.convert(source);
        //then
        Assertions.assertEquals(source.getId(),unitOfMeasure.getId());
        Assertions.assertEquals(source.getDescription(), unitOfMeasure.getDescription());
    }
}
