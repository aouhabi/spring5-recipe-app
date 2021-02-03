package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UnitOfMeasureToUnitOfMeasureCommandTest {
    UnitOfMeasureToUnitOfMeasureCommand converter;
    public static final String DESCRIPTION = "description";
    public static final Long LONG_VALUE = 1L;


    @BeforeEach
    void setUp() {
        converter = new UnitOfMeasureToUnitOfMeasureCommand() ;
    }

    @Test
    void testSourceNull() {
        Assertions.assertNull(converter.convert(null));
    }
    @Test
    public void testEmptyObj() throws Exception {
        Assertions.assertNotNull(converter.convert(new UnitOfMeasure()));
    }

    @Test
    void convert() {
        // given
        UnitOfMeasure  unitOfMeasure= new UnitOfMeasure();
        unitOfMeasure.setId(LONG_VALUE);
        unitOfMeasure.setDescription(DESCRIPTION);
        // when
        UnitOfMeasureCommand command = converter.convert(unitOfMeasure);
        //then
        Assertions.assertEquals(unitOfMeasure.getId(), command.getId());
        Assertions.assertEquals(unitOfMeasure.getDescription(), command.getDescription());
    }
}
