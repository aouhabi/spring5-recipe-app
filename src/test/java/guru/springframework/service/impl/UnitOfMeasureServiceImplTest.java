package guru.springframework.service.impl;

import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceImplTest {

    @Mock
    UnitOfMeasureRepository repository ;
    @InjectMocks
    UnitOfMeasureServiceImpl service ;
    Set<UnitOfMeasure> unitOfMeasures ;

    @BeforeEach
    void setUp() {
        unitOfMeasures = new HashSet<>();
        UnitOfMeasure unit1 = new UnitOfMeasure();
        unit1.setId(1L);

        UnitOfMeasure unit2 = new UnitOfMeasure();
        unit2.setId(2L);

        UnitOfMeasure unit3 = new UnitOfMeasure();
        unit3.setId(3L);

        unitOfMeasures.add(unit1);
        unitOfMeasures.add(unit2);
        unitOfMeasures.add(unit3);
    }

    @Test
    void findAllUoM() {
        // given
        when(repository.findAll()).thenReturn(unitOfMeasures);
        // when
        Set<UnitOfMeasure> allUoM = service.findAllUoM();
        // then
        assertEquals(unitOfMeasures.size(), allUoM.size());
        assertEquals(3, allUoM.size());
        verify(repository, times(1)).findAll() ;
    }
}
