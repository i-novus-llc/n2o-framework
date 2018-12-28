package net.n2oapp.framework.config.factory;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.CompiledClassAware;
import net.n2oapp.framework.api.metadata.aware.ContextClassAware;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

/**
 * Условия проверки для фабрик метаданных
 */
public abstract class FactoryPredicates {


    /**
     * Проверяет, что класс контекста сборки метаданных равен классу, о котором знает фабрика через  интерфейс {@link ContextClassAware}.
     *
     * @param engine  Движок сборки метаданных
     * @param context Контекст сборки
     * @param <G>     Тип движка сборки метаданных
     * @param <C>     тип контекста
     * @return Если true, то контекст сборки метаданных шире или равен контексту фабрики, или контекст не используется
     */
    public static <G, C extends CompileContext<?, ?>> boolean isContextEquals(G engine, C context) {
        return ((ContextClassAware) engine).getContextClass().equals(context.getClass());
    }

    /**
     * Проверяет, что класс контекста сборки метаданных шире или равен классу, о котором знает фабрика через  интерфейс {@link ContextClassAware}.
     * Если контекст равен null или фабрика не знает о контексте, то результат проверки будет положительный.
     *
     * @param engine  Движок сборки метаданных
     * @param context Контекст сборки
     * @param <G>     Тип движка сборки метаданных
     * @param <C>     тип контекста
     * @return Если true, то контекст сборки метаданных шире или равен контексту фабрики, или контекст не используется
     */
    public static <G, C extends CompileContext<?, ?>> boolean isOptionalContextAssignableFrom(G engine, C context) {
        return !(engine instanceof ContextClassAware)
                || context == null
                || ((ContextClassAware) engine).getContextClass().isAssignableFrom(context.getClass());
    }

    /**
     * Проверяет, что класс собранной метаданной шире или равен классу, о котором знает фабрика через  интерфейс {@link CompiledClassAware}.
     *
     * @param engine   Движок сборки метаданных
     * @param compiled Собранная метаданная
     * @param <G>      Тип движка сборки метаданных
     * @param <D>      Тип собранных метаданных
     * @return Если true, то класс собранной метаданной шире или равен классу, о котором знает фабрика
     */
    public static <G, D> boolean isCompiledAssignableFrom(G engine, D compiled) {
        return ((CompiledClassAware) engine).getCompiledClass().isAssignableFrom(compiled.getClass());
    }

    /**
     * Проверяет, что класс исходной метаданной шире или равен классу, о котором знает фабрика через  интерфейс {@link SourceClassAware}.
     *
     * @param engine Движок сборки метаданных
     * @param source Исходная метаданная
     * @param <G>    Тип движка сборки метаданных
     * @param <S>    Тип исходной метаданных
     * @return Если true, то класс исходной метаданной шире или равен классу, о котором знает фабрика
     */
    public static <G, S> boolean isSourceAssignableFrom(G engine, S source) {
        return ((SourceClassAware) engine).getSourceClass().isAssignableFrom(source.getClass());
    }

    /**
     * Проверяет, что класс исходной метаданной равен классу, о котором знает фабрика через  интерфейс {@link SourceClassAware}.
     *
     * @param engine Движок сборки метаданных
     * @param source Исходная метаданная
     * @param <G>    Тип движка сборки метаданных
     * @param <S>    Тип исходной метаданных
     * @return Если true, то класс исходной метаданной равен классу, о котором знает фабрика
     */
    public static <G, S> boolean isSourceEquals(G engine, S source) {
        return ((SourceClassAware) engine).getSourceClass().equals(source.getClass());
    }

    /**
     * Проверяет, что класс собранной метаданной равен классу, о котором знает фабрика через  интерфейс {@link CompiledClassAware}.
     *
     * @param engine   Движок сборки метаданных
     * @param compiled Собранная метаданная
     * @param <G>      Тип движка сборки метаданных
     * @param <D>      Тип собранных метаданных
     * @return Если true, то класс собранной метаданной равен классу, о котором знает фабрика
     */
    public static <G, D extends Compiled> boolean isCompiledEquals(G engine, D compiled) {
        return ((CompiledClassAware) engine).getCompiledClass().equals(compiled.getClass());
    }
}
