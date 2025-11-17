package net.n2oapp.framework.api.data;

import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты фабрики конструкторов критериев
 */
@SuppressWarnings("unchecked")
class CriteriaConstructorFactoryTest {

    private CriteriaConstructorFactory factory;
    private CriteriaConstructor testConstructor;
    private CriteriaConstructor parentConstructor;
    private N2oPreparedCriteria criteria;

    @BeforeEach
    void setUp() {
        criteria = new N2oPreparedCriteria();
        testConstructor = mock(CriteriaConstructor.class);
        parentConstructor = mock(CriteriaConstructor.class);
    }

    @Test
    void testConstructorWithEmptyList() {
        factory = new CriteriaConstructorFactory(Collections.emptyList());
        assertNotNull(factory);
    }

    @Test
    void testConstructWithMultipleConstructors() {
        when(testConstructor.getCriteriaClass()).thenReturn(TestCriteria.class);
        when(parentConstructor.getCriteriaClass()).thenReturn(ParentCriteria.class);
        
        TestCriteria expectedResult = new TestCriteria();
        when(testConstructor.construct(eq(criteria), any(TestCriteria.class))).thenReturn(expectedResult);
        
        List<CriteriaConstructor<?>> constructors = List.of(testConstructor, parentConstructor);
        factory = new CriteriaConstructorFactory(constructors);
        
        TestCriteria instance = new TestCriteria();
        TestCriteria result = factory.construct(criteria, instance);
        
        verify(testConstructor, times(1)).construct(criteria, instance);
        verify(parentConstructor, never()).construct(any(), any());
        assertSame(expectedResult, result);
    }

    @Test
    void testConstructWhenConstructorNotFound() {
        when(testConstructor.getCriteriaClass()).thenReturn(TestCriteria.class);
        List<CriteriaConstructor<?>> constructors = List.of(testConstructor);
        factory = new CriteriaConstructorFactory(constructors);
        
        UnrelatedCriteria instance = new UnrelatedCriteria();
        UnrelatedCriteria result = factory.construct(criteria, instance);
        
        verify(testConstructor, never()).construct(any(), any());
        assertSame(instance, result);
    }

    @Test
    void testConstructWithEmptyConstructorList() {
        factory = new CriteriaConstructorFactory(Collections.emptyList());
        
        TestCriteria instance = new TestCriteria();
        TestCriteria result = factory.construct(criteria, instance);
        
        assertSame(instance, result);
    }

    @Test
    void testConstructWithGrandparentClassMatch() {
        CriteriaConstructor<GrandparentCriteria> grandparentConstructor = mock(CriteriaConstructor.class);
        when(grandparentConstructor.getCriteriaClass()).thenReturn(GrandparentCriteria.class);
        
        GrandchildCriteria expectedResult = new GrandchildCriteria();
        when(grandparentConstructor.construct(eq(criteria), any(GrandchildCriteria.class))).thenReturn(expectedResult);
        
        List<CriteriaConstructor<?>> constructors = List.of(grandparentConstructor);
        factory = new CriteriaConstructorFactory(constructors);
        
        GrandchildCriteria instance = new GrandchildCriteria();
        GrandchildCriteria result = factory.construct(criteria, instance);
        
        verify(grandparentConstructor, times(1)).construct(criteria, instance);
        assertSame(expectedResult, result);
    }

    @Test
    void testConstructPreferExactMatchOverParent() {
        CriteriaConstructor<ChildCriteria> childConstructor = mock(CriteriaConstructor.class);
        when(childConstructor.getCriteriaClass()).thenReturn(ChildCriteria.class);
        when(parentConstructor.getCriteriaClass()).thenReturn(ParentCriteria.class);
        
        ChildCriteria expectedResult = new ChildCriteria();
        when(childConstructor.construct(eq(criteria), any(ChildCriteria.class))).thenReturn(expectedResult);
        
        List<CriteriaConstructor<?>> constructors = List.of(childConstructor, parentConstructor);
        factory = new CriteriaConstructorFactory(constructors);
        
        ChildCriteria instance = new ChildCriteria();
        ChildCriteria result = factory.construct(criteria, instance);
        
        verify(childConstructor, times(1)).construct(criteria, instance);
        verify(parentConstructor, never()).construct(any(), any());
        assertSame(expectedResult, result);
    }

    @Test
    void testConstructWithNullInstance() {
        when(testConstructor.getCriteriaClass()).thenReturn(TestCriteria.class);
        List<CriteriaConstructor<?>> constructors = List.of(testConstructor);
        factory = new CriteriaConstructorFactory(constructors);
        
        assertThrows(NullPointerException.class, () -> {
            factory.construct(criteria, null);
        });
    }

    @Test
    void testConstructWithNullCriteria() {
        when(testConstructor.getCriteriaClass()).thenReturn(TestCriteria.class);
        TestCriteria instance = new TestCriteria();
        when(testConstructor.construct(isNull(), any(TestCriteria.class))).thenReturn(instance);
        
        List<CriteriaConstructor<?>> constructors = List.of(testConstructor);
        factory = new CriteriaConstructorFactory(constructors);
        
        TestCriteria result = factory.construct(null, instance);
        
        verify(testConstructor, times(1)).construct(null, instance);
        assertSame(instance, result);
    }

    // Тестовые классы для проверки иерархии
    static class GrandparentCriteria {
    }

    static class ParentCriteria extends GrandparentCriteria {
    }

    static class ChildCriteria extends ParentCriteria {
    }

    static class GrandchildCriteria extends ChildCriteria {
    }

    static class TestCriteria {
    }

    static class UnrelatedCriteria {
    }
}
