package net.n2oapp.framework.context.test;

import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.context.smart.impl.ChangeDependencyException;
import net.n2oapp.framework.context.smart.impl.ContextException;
import net.n2oapp.framework.context.smart.impl.ContextRecursiveException;
import net.n2oapp.framework.context.test.provider.ChangeableDependenciesProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: operehod
 * Date: 27.01.2015
 * Time: 17:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-mock-context.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ContextTest {

    @Before
    public void setUp() throws Exception {

    }

    @Autowired
    private ContextEngine contextEngine;


    //name всегда равен id (например org.id = 1, org.name = "1")
    @Test
    public void testEngine() throws Exception {

        assert contextEngine.get("org.id").equals(1);
        assert contextEngine.get("org.name").equals("1");
        assert contextEngine.get("dep.id").equals(1);
        assert contextEngine.get("dep.name").equals("1");
        assert contextEngine.get("cab.id").equals(1);
        assert contextEngine.get("cab.name").equals("1");

        //пытаемся получить параметр контекста, для которого отсутствует провайдер
        boolean error = false;
        try {
            contextEngine.get("org.shortName");
        } catch (ContextException e) {
            error = true;
        }
        assert error;

        //переключаем
        Map<String, Object> values = new HashMap<>();
        values.put("org.id", 2);
        values.put("dep.id", 3);
        values.put("cab.id", 4);
        contextEngine.set(values);

        assert contextEngine.get("org.id").equals(2);
        assert contextEngine.get("org.name").equals("2");
        assert contextEngine.get("dep.id").equals(3);
        assert contextEngine.get("dep.name").equals("3");
        assert contextEngine.get("cab.id").equals(4);
        assert contextEngine.get("cab.name").equals("4");

        //сеттим новый кабинет
        values = new HashMap<>();
        values.put("cab.id", 12);
        values.put("cab.name", 19); //просечивание этого параметра никак не должно повлиять
        contextEngine.set(values);

        assert contextEngine.get("org.id").equals(2);
        assert contextEngine.get("org.name").equals("2");
        assert contextEngine.get("dep.id").equals(3);
        assert contextEngine.get("dep.name").equals("3");
        assert contextEngine.get("cab.id").equals(12);
        assert contextEngine.get("cab.name").equals("12");


        //сеттим новый департамент (это ошибка, т.к. провайдер ждет dep.id и org.id вместе)
        try {
            values = new HashMap<>();
            values.put("dep.id", 12);
            contextEngine.set(values);
            assert false;
        } catch (ContextException e) {
            assert true;
        }
    }

    @Test
    public void testRecursive() throws Exception {

        //провайдер запрашивает сам себя (RecursiveErrorProvider)
        boolean error = false;
        try {
            contextEngine.get("recursiveKey1");
        } catch (ContextRecursiveException e) {
            error = true;
        }
        assert error;


        // провайдер1(RecursiveFirstLvlErrorProvider) запрашивает провайдер2(RecursiveSecondLvlErrorProvider), который запрашивает провайдер1
        error = false;
        try {
            contextEngine.get("recursiveKey3");
        } catch (ContextRecursiveException e) {
            error = true;
        }
        assert error;

        try {
            Map<String, Object> values = new HashMap<>();
            values.put("recursiveKey6", 6);
            values.put("recursiveKey7", 7);
            contextEngine.set(values);
        } catch (Exception e) {
            assert false;
        }

        try {
            Map<String, Object> values = new HashMap<>();
            values.put("recursiveKey7", 7);
            values.put("recursiveKey6", 6);
            contextEngine.set(values);
        } catch (Exception e) {
            assert false;
        }

    }


    @Test
    public void testGetWithBaseParams() throws Exception {
        assert contextEngine.get("org.id").equals(1);
        assert contextEngine.get("org.name").equals("1");

        assert contextEngine.get("org.id", Collections.<String, Object>singletonMap("org.id", 13)).equals(13);
        assert contextEngine.get("org.name", Collections.<String, Object>singletonMap("org.id", 13)).equals("13");
    }

    @Test
    @Ignore
    public void testDependencyConflict() throws Exception {
        // проверка изменения зависимостей параметра
        ChangeableDependenciesProvider.hasFirstParent = true;
        assert contextEngine.get("changeableDependenciesKey.id").equals(1);
        ChangeableDependenciesProvider.hasFirstParent = false;
        try {
            contextEngine.get("changeableDependenciesKey.id");
            assert false;
        } catch (ChangeDependencyException e) {
            assert true;//ошибка, т.к. сначало было org.id, а потом dep.id
        }
        try {
            contextEngine.get("changeableDependenciesKey.id");
            assert false;
        } catch (ChangeDependencyException e) {
            assert true;//ошибка та же, зависимости нарушать нельзя даже повторно
        }
        try {
            contextEngine.get("changeableDependenciesKey.id", Collections.singletonMap("dep.id", 1));//вместе с base params
            assert false;
        } catch (ChangeDependencyException e) {
            assert true;//ошибка та же, т.к. base params роли не играют
        }
        //проверяем, что set провайдер достает те же значения, что и get провайдер
        ChangeableDependenciesProvider.hasFirstParent = true;
        Map<String, Object> values = new HashMap<>();
        values.put("changeableDependenciesKey.id", 1);
        contextEngine.set(values);//первый раз все хорошо, т.к. set запросил org.id
        ChangeableDependenciesProvider.hasFirstParent = false;
        try {
            contextEngine.set(values);
            assert false;
        } catch (ContextException e) {
            assert true;//ошибка, т.к. второй раз set запросил dep.id, а не org.id
        }

    }
}

