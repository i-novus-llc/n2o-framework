package net.n2oapp.framework.config.compile.builder;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.config.metadata.compile.CompileException;
import net.n2oapp.framework.config.metadata.compile.builder.N2oBuildProcessor;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompileBuilderTest {

    @Test
    void compile() {
        MySource source = new MySource();
        source.id = "id";
        source.name = "name";

        N2oBuildProcessor<MyCompiled, MySource> b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);
        b.map(this::compileSome);
        MyCompiled compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.id, is("id1"));
        assertThat(compiled.name, is("name1"));
    }

    @Test
    void set() {
        MySource source = new MySource();
        source.id = "id";
        source.name = "name";

        N2oBuildProcessor<MyCompiled, MySource> b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);

        b.get(MySource::getId).set(MyCompiled::setId);
        b.get(MySource::getName).set(MyCompiled::setName);
        MyCompiled compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.id, is("id"));
        assertThat(compiled.name, is("name"));
    }

    @Test
    void put() {
        MySource source = new MySource();
        source.name = "test";

        N2oBuildProcessor<MyCompiled, MySource> b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);

        b.get(MySource::getName).put(MyCompiled::getProperties, "name");
        MyCompiled compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.properties.get("name"), is("test"));
    }

    @Test
    void cast() {
        MySource source = new MySource();
        source.id = "id";
        source.name = "name";

        //Удачный cast
        N2oBuildProcessor<MyCompiled, MySource> b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);

        b.cast(IdAware.class).get(IdAware::getId).set(IdAware::setId);
        MyCompiled compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.id, is("id"));

        //Неудачный опциональный cast
        b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);
        b.optionalCast(SrcAware.class).get(SrcAware::getSrc).set(SrcAware::setSrc);
        compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.id, nullValue());

        //Неудачный cast
        try {
            b.cast(SrcAware.class).get(SrcAware::getSrc).set(SrcAware::setSrc);
            fail();
        } catch (CompileException ignored) {
        }

        //Двойной cast
        b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);
        b.cast(IdAware.class).get(IdAware::getId).set(IdAware::setId);
        b.cast(NameAware.class).get(NameAware::getName).set(NameAware::setName);
        compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.id, is("id"));
        assertThat(compiled.name, is("name"));
    }

    @Test
    void defaults() {
        MySource source = new MySource();
        source.id = "id";
        source.name = null;

        //Значения по умолчанию через функцию
        N2oBuildProcessor<MyCompiled, MySource> b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);

        b.get(MySource::getName).defaults(MySource::getId).set(MyCompiled::setName);
        MyCompiled compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.name, is("id"));

        //Значения по умолчанию через константу
        b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);
        b.get(MySource::getName).defaults("test").set(MyCompiled::setName);
        compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.name, is("test"));

        //Значения по умолчанию в цепочке
        b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);
        b.get(MySource::getName).defaults(v -> null).defaults("test1").set(MyCompiled::setName);
        compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.name, is("test1"));

        //Значение по умолчанию не понадобилось
        b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);
        b.get(MySource::getId).defaults("test").set(MyCompiled::setId);
        compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.id, is("id"));
    }

    @Test
    void resolve() {
        CompileProcessor mockProcessor = mock(CompileProcessor.class);
        when(mockProcessor.resolve("one")).thenReturn("two");
        when(mockProcessor.resolve("1", Integer.class)).thenReturn(2);
        MySource source = new MySource();
        source.name = "one";
        source.id = "1";

        N2oBuildProcessor<MyCompiled, MySource> b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);

        b.get(MySource::getName).resolve().set(MyCompiled::setName);
        b.get(MySource::getId).resolve(Integer.class).set(MyCompiled::setType);
        MyCompiled compiled = b.build(new MyCompiled(), source, null, mockProcessor);

        assertThat(compiled.name, is("two"));
        assertThat(compiled.type, is(2));
    }

    @Test
    void map() {
        MySource source = new MySource();
        source.type = MySource.Type.ONE;

        //Конвертация: Type -> Integer
        N2oBuildProcessor<MyCompiled, MySource> b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);

        b.get(MySource::getType).map(Enum::ordinal).set(MyCompiled::setType);
        MyCompiled compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.type, is(0));

        //Двойная конвертация: Type -> Integer -> Set<Integer>
        b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);
        b.get(MySource::getType).map(Enum::ordinal).map(Collections::singleton).set(MyCompiled::setTypes);
        compiled = b.build(new MyCompiled(), source, null, null);

        assertThat(compiled.types, hasItem(0));
    }

    @Test
    void combination() {
        MySource source = new MySource();
        source.inner = new MySource.InnerSource();
        CompileProcessor mockProcessor = mock(CompileProcessor.class);
        when(mockProcessor.compile(source.inner, null)).thenReturn(new MyCompiled.InnerCompiled());

        N2oBuildProcessor<MyCompiled, MySource> b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);

        b.get(MySource::getInner).compile(MyCompiled.InnerCompiled.class).set(MyCompiled::setInner);
        MyCompiled compiled = b.build(new MyCompiled(), source, null, mockProcessor);

        assertThat(compiled.inner, isA(MyCompiled.InnerCompiled.class));
    }

    @Test
    void collect() {
        MySource source = new MySource();
        source.inners = new MySource.InnerSource[] {new MySource.InnerSource("1"), new MySource.InnerSource("2")};

        N2oBuildProcessor<MyCompiled, MySource> b = N2oBuildProcessor.of(MyCompiled.class, MySource.class);
//        b.getArray(MySource::getInners).map().collect(Collectors.toSet()).set(MyCompiled::setInners);
    }

    private void compileSome(MySource source, MyCompiled compiled) {
        compiled.setId(source.getId() + "1");
        compiled.setName(source.getName() + "1");
    }

    static class MySource implements Source, IdAware, NameAware {
        String id;
        String name;
        Type type;
        String fooId;
        InnerSource inner;
        InnerSource[] inners;

        enum Type {
            ONE, TWO
        }

        static class InnerSource implements Source {
            String id;

            public InnerSource() {
            }

            public InnerSource(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getFooId() {
            return fooId;
        }

        public void setFooId(String fooId) {
            this.fooId = fooId;
        }

        public InnerSource getInner() {
            return inner;
        }

        public void setInner(InnerSource inner) {
            this.inner = inner;
        }

        public InnerSource[] getInners() {
            return inners;
        }

        public void setInners(InnerSource[] inners) {
            this.inners = inners;
        }
    }

    static class MyCompiled implements Compiled, IdAware, NameAware {
        String id;
        String name;
        int type;
        Set<Integer> types;
        Foo foo;
        InnerCompiled inner;
        Set<InnerCompiled> inners;
        Map<String, Object> properties = new HashMap<>();

        static class InnerCompiled implements Compiled {
            String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Set<Integer> getTypes() {
            return types;
        }

        public void setTypes(Set<Integer> types) {
            this.types = types;
        }

        public Foo getFoo() {
            return foo;
        }

        public void setFoo(Foo foo) {
            this.foo = foo;
        }

        public InnerCompiled getInner() {
            return inner;
        }

        public void setInner(InnerCompiled inner) {
            this.inner = inner;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public Set<InnerCompiled> getInners() {
            return inners;
        }

        public void setInners(Set<InnerCompiled> inners) {
            this.inners = inners;
        }
    }

    static class Foo implements IdAware {
        String id;

        public Foo(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }
    }

}
