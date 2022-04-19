package net.n2oapp.framework.config.metadata.compile.builder;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.building.*;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.BuildProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.config.metadata.compile.CompileException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * Конструктор сборки метаданных
 * Пример: {@code
 * b = BuildProcessorImpl.of(MyCompiled.class, MySource.class);
 * b.get(MySource::getName).defaults(MySource::getId).set(MyCompiled::setName);
 * b.cast(IdAware.class).get(IdAware::getId).set(IdAware::setId);
 * b.get(MySource::getSomething).add().map(Mappers::singletonList).set(MyCompiled::setSomethings);
 * }
 *
 * @param <O> Тип собранной метаданной
 * @param <I> Тип исходной метаданной*
 */
public class N2oBuildProcessor<O, I> implements BuildProcessor<O, I> {
    private final Class<O> compiledClass;
    private final Class<I> sourceClass;
    private List<ClassOperation<O, I>> operations;
    private boolean fail;


    public static <D extends Compiled, S> N2oBuildProcessor<D, S> of(
            Class<D> compiledClass,
            Class<S> sourceClass) {
        return new N2oBuildProcessor<>(compiledClass, sourceClass);
    }

    public N2oBuildProcessor(Class<O> compiledClass,
                             Class<I> sourceClass) {
        this.compiledClass = compiledClass;
        this.sourceClass = sourceClass;
    }

    private N2oBuildProcessor(N2oBuildProcessor<?, ?> b, boolean fail) {
        this.compiledClass = (Class<O>) b.compiledClass;
        this.sourceClass = (Class<I>) b.sourceClass;
        this.fail = fail;
    }

    public N2oBuildProcessor<O, I> compile(CompileBuilder<O, I> builder) {
        builder.build(this);
        return this;
    }

    public N2oBuildProcessor<O, I> map(CompileMapper<O, I> mapper) {
        return addClassOp((d, s, c, p) -> mapper.map(s, d));
    }

    public <T> N2oBuildProcessor<T, T> optionalCast(Class<T> clazz) {
        if (!clazz.isAssignableFrom(sourceClass) || !clazz.isAssignableFrom(compiledClass)) {
            return new N2oBuildProcessor<>(this, true);
        }
        return (N2oBuildProcessor<T, T>) this;
    }

    public <T> N2oBuildProcessor<T, T> cast(Class<T> clazz) {
        if (!clazz.isAssignableFrom(sourceClass) || !clazz.isAssignableFrom(compiledClass)) {
            throw new CompileException("Can't cast " + sourceClass + " or " + compiledClass + " to " + clazz);
        }
        return (N2oBuildProcessor<T, T>) this;
    }

    public <T> FieldBuildProcessor<O, I, T> get(Function<I, ? extends T> getter) {
        return addFieldOp(new FieldCompileBuilderImpl<>(getter));
    }

    public <T> PluralFieldBuildProcessor<O, I, T, ?> getArray(Function<I, ? extends T[]> getter) {
        return addArrayFieldOp(new CompileArrayFieldBuilder<>(getter));
    }

    @Override
    public <T> PluralFieldBuildProcessor<O, I, T, ?> getList(Function<I, List<? extends T>> listGetter) {
        return null;//todo
    }

    public N2oBuildProcessor route() {
        //todo
        return this;
    }

    public O build(O compiled, I source, CompileContext<?, ?> context, CompileProcessor processor) {
        if (fail)
            return compiled;
        if (operations != null)
            operations.forEach(f -> f.handle(compiled, source, context, processor));
        return compiled;
    }

    private <T> FieldBuildProcessor<O, I, T> addFieldOp(FieldCompileBuilderImpl<O, I, T> fieldOperations) {
        addClassOp(fieldOperations);
        return fieldOperations;
    }

    private <T, R> CompileArrayFieldBuilder<O, I, T, R> addArrayFieldOp(CompileArrayFieldBuilder<O, I, T, R> fieldOperations) {
        addClassOp(fieldOperations);
        return fieldOperations;
    }

    private N2oBuildProcessor<O, I> addClassOp(ClassOperation<O, I> op) {
        if (operations == null)
            operations = new ArrayList<>();
        operations.add(op);
        return this;
    }

    /**
     * Конвейер сборки поля
     *
     * @param <D> Тип собранной метаданной
     * @param <S> Тип исходной метаданной*
     * @param <T> Тип исходного поля
     */
    public static class FieldCompileBuilderImpl<D, S, T> implements FieldBuildProcessor<D, S, T>, ClassOperation<D, S> {
        private FieldCompileBuilderImpl upstream;
        private Function<S, ? extends T> getter;
        private BiConsumer<D, ? super T> setter;
        private List<FieldOperation<D, S, T, T>> operations;
        private FieldOperation<D, S, T, ?> mapper;

        FieldCompileBuilderImpl(Function<S, ? extends T> getter) {
            this.getter = getter;
        }

        FieldCompileBuilderImpl(FieldCompileBuilderImpl<D, S, ?> previous) {
            previous.upstream = this;
        }

        /**
         * Установить значение в собранный объект
         *
         * @param setter Функция установки значения
         */
        @Override
        public void set(BiConsumer<D, ? super T> setter) {
            this.setter = setter;
        }

        /**
         * Вставить запись в карту
         *
         * @param mapGetter Функция получения карты
         * @param key       Ключ карты
         */
        @Override
        public void put(Function<D, ? extends Map<String, Object>> mapGetter, String key) {
            this.setter = (t, v) -> mapGetter.apply(t).put(key, v);
        }

        @Override
        public <R> FieldBuildProcessor<D, S, R> map(Function<T, R> mapper) {
            addMapper((v, d, s, c, p) -> mapper.apply(v));
            return new FieldCompileBuilderImpl<>(this);
        }

        @Override
        public <R extends Compiled> FieldBuildProcessor<D, S, R> compile(Class<? super R> compiledClazz) {
            addMapper((v, d, s, c, p) -> p.compile(v, c));//todo добавить проверку на вхождение классов
            return new FieldCompileBuilderImpl<>(this);
        }

        @Override
        public <R extends Compiled> FieldBuildProcessor<D, S, R> compile(CompileConstructor<R, T> constructor,
                                                                         CompileBuilder<R, T> builder) {
            addMapper((v, d, s, c, p) -> {
                R result = constructor.construct(v);
                //todo builder.build(new BuildProcessorImpl<R, T>(result.getClass(), v.getClass()));
                return result;
            });
            return new FieldCompileBuilderImpl<>(this);
        }

        @Override
        public FieldBuildProcessor<D, S, T> defaults(T defaultValue) {
            addOperation((v, d, s, c, p) -> v != null ? v : defaultValue);
            return this;
        }

        @Override
        public FieldBuildProcessor<D, S, T> defaults(Function<S, T> defaultValue) {
            addOperation((v, d, s, c, p) -> v != null ? v : defaultValue.apply(s));
            return this;
        }

        @Override
        public FieldBuildProcessor<D, S, String> resolve() {
            addMapper((v, d, s, c, p) -> v instanceof String ? p.resolve((String) v) : v);
            return new FieldCompileBuilderImpl<>(this);
        }

        @Override
        public <R> FieldBuildProcessor<D, S, R> resolve(Class<R> resolvedClazz) {
            addMapper((v, d, s, c, p) -> v instanceof String ? p.resolve((String) v, resolvedClazz) : v);
            return new FieldCompileBuilderImpl<>(this);
        }

        void build(T value, D compiled, S source, CompileContext<?, ?> context, CompileProcessor p) {
            if (operations != null)
                for (FieldOperation<D, S, T, T> h : operations) {
                    value = h.handle(value, compiled, source, context, p);
                }
            if (mapper != null) {
                Object result = mapper.handle(value, compiled, source, context, p);
                upstream.build(result, compiled, source, context, p);
            }
            if (setter != null) {
                setter.accept(compiled, value);
            }
        }

        @Override
        public void handle(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p) {
            if (getter == null)
                return;
            build(getter.apply(source), compiled, source, context, p);
        }

        private void addOperation(FieldOperation<D, S, T, T> handler) {
            if (operations == null)
                operations = new ArrayList<>();
            operations.add(handler);
        }

        private <R> void addMapper(FieldOperation<D, S, T, R> handler) {
            mapper = handler;
        }
    }

    public static class CompileArrayFieldBuilder<D, S, T, L> implements ClassOperation<D, S>, PluralFieldBuildProcessor<D, S, T, L> {
        private Function<S, ? extends T[]> arrayGetter;
        private BiConsumer<D, ? super L> listSetter;
        private List<FieldOperation<D, S, T, T>> operations;
        private Collector<T, L, L> collector;

        CompileArrayFieldBuilder(Function<S, ? extends T[]> getter) {
            this.arrayGetter = getter;
        }

        public void set(BiConsumer<D, ? super L> listSetter) {
            this.listSetter = listSetter;
        }

        @Override
        public PluralFieldBuildProcessor<D, S, T, L> collect(Collector<T, ?, L> collector) {
            if (this.collector != null)
                throw new IllegalStateException("Can't call collector twice");
            this.collector = (Collector<T, L, L>) collector;
            return this;
        }

        @Override
        public <R> PluralFieldBuildProcessor<D, S, R, L> map(Function<T, R> mapper) {
            return null;
        }

        @Override
        public <R extends Compiled> PluralFieldBuildProcessor<D, S, R, L> compile(Class<? super R> compiledClass) {
            return null;
        }

        @Override
        public <R extends Compiled> PluralFieldBuildProcessor<D, S, R, L> compile(CompileConstructor<R, T> constructor, CompileBuilder<R, T> builder) {
            return null;
        }

        @Override
        public void handle(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p) {
            if (arrayGetter == null)
                return;
            T[] array = arrayGetter.apply(source);
            List<T> list = new ArrayList<>(array.length);
            for (T value : array) {
                if (operations != null)
                    for (FieldOperation<D, S, T, T> h : operations) {
                        value = h.handle(value, compiled, source, context, p);
                    }
                list.add(value);
            }
            L result;
            if (collector != null) {
                L collection = (L) collector.supplier().get();
                for (T value : list) {
                    collector.accumulator().accept(collection, value);
                }
                result = collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)
                        ? collection
                        : collector.finisher().apply(collection);
            } else {
                result = (L) list;
            }
            if (listSetter != null) {
                listSetter.accept(compiled, result);
            }
        }

        private void addOperation(FieldOperation<D, S, T, T> handler) {
            if (operations == null)
                operations = new ArrayList<>();
            operations.add(handler);
        }

    }

    /**
     * Операция над полем метаданной
     *
     * @param <D> Тип собранной метаданной
     * @param <S> Тип исходной метаданной
     * @param <T> Тип исходного поля
     * @param <R> Тип собранного поля
     */
    interface FieldOperation<D, S, T, R> {
        /**
         * Применить обработку к полю
         *
         * @param value    Исходное значение поля
         * @param compiled Собранное значение поля
         * @param source   Исходная метаданная
         * @param context  Контекст сборки
         * @param p        Процессор сборки
         * @return Собранное значение поля
         */
        R handle(T value, D compiled, S source, CompileContext<?, ?> context, CompileProcessor p);
    }

    /**
     * Операция над метаданной
     *
     * @param <O> Тип собранной метаданной
     * @param <I> Тип исходной метаданной
     */
    interface ClassOperation<O, I> {
        /**
         * Применить обработку к собранной метаданной
         *
         * @param compiled Собранная метаданная
         * @param source   Исходная метаданная
         * @param context  Контекст сборки
         * @param p        Процессор сборки
         */
        void handle(O compiled, I source, CompileContext<?, ?> context, CompileProcessor p);
    }

}
