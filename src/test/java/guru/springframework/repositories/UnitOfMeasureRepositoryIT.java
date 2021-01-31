package guru.springframework.repositories;

import guru.springframework.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest

public class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository repository;
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void findUnitOfMeasureByDescription() {
        Optional<UnitOfMeasure> unitOfMeasure = repository.findUnitOfMeasureByDescription("Teaspoon");
        assertEquals("Teaspoon", unitOfMeasure.get().getDescription());
    }
    @Test
    public void findUnitOfMeasureByDescriptionCup() {
        Optional<UnitOfMeasure> unitOfMeasure = repository.findUnitOfMeasureByDescription("Cup");
        assertEquals("Cup", unitOfMeasure.get().getDescription());
    }
}
