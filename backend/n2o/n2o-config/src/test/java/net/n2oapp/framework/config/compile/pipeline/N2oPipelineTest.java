package net.n2oapp.framework.config.compile.pipeline;

import net.n2oapp.cache.template.CacheCallback;
import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.compile.*;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.io.IOProcessorAware;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.pipeline.*;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.SourceValidatorFactory;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.config.compile.pipeline.operation.*;
import net.n2oapp.framework.config.factory.MockMetadataFactory;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.reader.N2oSourceLoaderFactory;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import org.apache.commons.io.IOUtils;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;


/**
 * Тест конвеера сборки метаданных ({@link N2oPipelineSupport})
 */
public class N2oPipelineTest {

    private N2oEnvironment env;
    private MetadataRegister metadataRegister;
    private N2oSourceLoaderFactory readerFactory;
    private CacheTemplate sourceCacheTemplate;
    private CacheTemplate compileCacheTemplate;


    @Before
    public void setUp() throws Exception {
        sourceCacheTemplate = new MockSourceCacheTemplate();
        compileCacheTemplate = new MockCompiledCacheTemplate();

        env = mock(N2oEnvironment.class);
        metadataRegister = mock(MetadataRegister.class);
        when(env.getMetadataRegister()).thenReturn(metadataRegister);
        readerFactory = mock(N2oSourceLoaderFactory.class);
        when(env.getSourceLoaderFactory()).thenReturn(readerFactory);
        SourceMergerFactory sourceMergerFactory = new MockMergeFactory();
        when(env.getSourceMergerFactory()).thenReturn(sourceMergerFactory);
        SourceCompilerFactory compilerFactory = new MockSourceCompilerFactory();
        when(env.getSourceCompilerFactory()).thenReturn(compilerFactory);
        SourceTransformerFactory sourceTransformerFactory = new MockSourceTransformer();
        when(env.getSourceTransformerFactory()).thenReturn(sourceTransformerFactory);
        CompileTransformerFactory compileTransformerFactory = new MockCompileTransformer();
        when(env.getCompileTransformerFactory()).thenReturn(compileTransformerFactory);
        SourceValidatorFactory validatorFactory = new MockSourceValidatorFactory();
        when(env.getSourceValidatorFactory()).thenReturn(validatorFactory);
        MetadataBinderFactory metadataBinderFactory = new MockBinderFactory();
        when(env.getMetadataBinderFactory()).thenReturn(metadataBinderFactory);
        NamespacePersisterFactory persisterFactory = new MockPersisterFactory();
        when(env.getNamespacePersisterFactory()).thenReturn(persisterFactory);
        PipelineOperationFactory pof = new MockPipelineOperationFactory();
        when(env.getPipelineOperationFactory()).thenReturn(pof);

        when(env.getReadPipelineFunction()).thenReturn(p -> p.read().transform().validate().cache());
        when(env.getReadCompilePipelineFunction()).thenReturn(p -> p.read().transform().validate().cache().copy().compile().transform().cache().copy());
        when(env.getCompilePipelineFunction()).thenReturn(p -> p.compile().transform().cache());
        when(env.getBindPipelineFunction()).thenReturn(p -> p.bind());

        when(env.getSystemProperties()).thenReturn(new SimplePropertyResolver(new Properties()));
    }

    @Test
    public void readPipeline() throws IOException {
        XmlInfo pageInfo = new XmlInfo("pageId", N2oPage.class, "", "");

        when(metadataRegister.get("pageId", N2oSimplePage.class)).thenReturn(pageInfo);
        when(metadataRegister.get("pageId", N2oPage.class)).thenReturn(pageInfo);

        PageContext context = mock(PageContext.class);
        when(context.getSourceId(any())).thenReturn("pageId");
        when(context.getCompiledId(any())).thenReturn("pageId");
        when(context.getSourceClass()).thenReturn(N2oPage.class);
        when(context.getCompiledClass()).thenReturn(Page.class);

        //read
        when(readerFactory.read(pageInfo, null)).then(m -> createSource());
        N2oSimplePage source = N2oPipelineSupport.readPipeline(env).read().get("pageId", N2oSimplePage.class);
        assertThat(source.getName(), is("test"));

        // read + transform
        N2oSimplePage resultSourceTransformPage = N2oPipelineSupport.readPipeline(env).read().transform().get("pageId", N2oSimplePage.class);
        assertThat(resultSourceTransformPage.getName(), is("transformed test"));

        // read + validate
        try{
            N2oPipelineSupport.readPipeline(env).read().transform().validate().get("pageId", N2oSimplePage.class);
            Assert.fail();
        } catch (N2oMetadataValidationException e) {
        }

        // read + cache
        ReadTerminalPipeline<ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline>> readPipeline = N2oPipelineSupport.readPipeline(env).read().cache();
        source = readPipeline.get("pageId", N2oSimplePage.class);
        assertThat(source.getName(), is("test"));//cache miss

        source = readPipeline.get("pageId", N2oSimplePage.class);
        assertThat(source.getName(), is("cached test"));//cache hit

        // read + compile
        Page compiled = new N2oReadPipeline(env).read().compile().get(context);
        assertThat(compiled.getPageProperty().getTitle(), is("compiled test"));

        // read + cache + compile
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> compilePipeline = N2oPipelineSupport.readPipeline(env).read().cache().copy().compile();
        compiled = compilePipeline.get(context);
        assertThat(compiled.getPageProperty().getTitle(), is("compiled cached test"));//second cache hit

        // read + compile + transform
        compiled = N2oPipelineSupport.readPipeline(env).read().compile().transform().get(context);
        assertThat(compiled.getPageProperty().getTitle(), is("transformed compiled test"));

        // read + compile + cache + transform
        compiled = N2oPipelineSupport.readPipeline(env).read().compile().cache().transform().get(context);
        assertThat(compiled.getPageProperty().getTitle(), is("transformed compiled test"));//compile cache miss

        compiled = N2oPipelineSupport.readPipeline(env).read().compile().cache().transform().get(context);
        assertThat(compiled.getPageProperty().getTitle(), is("transformed cached compiled test"));//compile cache hit

        // read + compile + bind
        DataSet data = new DataSet();
        compiled = N2oPipelineSupport.readPipeline(env).read().compile().bind().get(context, data);
        assertThat(compiled.getPageProperty().getTitle(), is("binding compiled test"));

        //read + persist
        InputStream inputStream = N2oPipelineSupport.readPipeline(env).read().persist().get("pageId", N2oSimplePage.class);
        String xml = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        assertThat(xml, containsString("<test xmlns=\"test-namespace\" />"));
    }

    @Test
    public void compilePipeline() {
        XmlInfo pageInfo = new XmlInfo("pageId", N2oPage.class, "", "");
        when(metadataRegister.get("pageId", N2oSimplePage.class)).thenReturn(pageInfo);
        when(metadataRegister.get("pageId", N2oPage.class)).thenReturn(pageInfo);
        XmlInfo pageInfo2 = new XmlInfo("page2", N2oPage.class, "", "");
        when(metadataRegister.get("page2", N2oSimplePage.class)).thenReturn(pageInfo2);
        N2oSimplePage page2 = new N2oSimplePage();
        page2.setId("page2");
        page2.setName("name2");
        when(readerFactory.read(pageInfo2, null)).thenReturn(page2);
        PageContext context = mock(PageContext.class);
        when(context.getSourceId(any())).thenReturn("pageId");
        when(context.getCompiledId(any())).thenReturn("pageId");
        when(context.getSourceClass()).thenReturn(N2oPage.class);
        when(context.getCompiledClass()).thenReturn(Page.class);

        //compile
        Page compiledPage = N2oPipelineSupport.compilePipeline(env).compile().get(createSource(), context);
        assertThat(compiledPage.getPageProperty().getTitle(), is("compiled test"));

        //merge - compile
        N2oSimplePage source = createSource();
        source.setRefId("page2");
        compiledPage = N2oPipelineSupport.compilePipeline(env).merge().compile().get(source, context);
        assertThat(compiledPage.getPageProperty().getTitle(), is("compiled merged test"));

        //transform + compile
        compiledPage = N2oPipelineSupport.compilePipeline(env).transform().compile().get(createSource(), context);
        assertThat(compiledPage.getPageProperty().getTitle(), is("compiled transformed test"));

        //compile + transform
        compiledPage = N2oPipelineSupport.compilePipeline(env).compile().transform().get(createSource(), context);
        assertThat(compiledPage.getPageProperty().getTitle(), is("transformed compiled test"));

        //compile + cache
        CompileTerminalPipeline<CompileBindTerminalPipeline> pipeline = N2oPipelineSupport.compilePipeline(env).compile().cache();
        compiledPage = pipeline.get(createSource(), context);
        assertThat(compiledPage.getPageProperty().getTitle(), is("compiled test"));//cache miss

        compiledPage = pipeline.get(createSource(), context);
        assertThat(compiledPage.getPageProperty().getTitle(), is("cached compiled test"));//cache hit

        //compile + bind
        DataSet data = new DataSet();
        compiledPage = N2oPipelineSupport.compilePipeline(env).compile().bind().get(createSource(), context, data);
        assertThat(compiledPage.getPageProperty().getTitle(), is("binding compiled test"));

        //compile + bind + bind
        compiledPage = N2oPipelineSupport.compilePipeline(env).compile().bind().bind().get(createSource(), context, data);
        assertThat(compiledPage.getPageProperty().getTitle(), is("binding binding compiled test"));
    }

    @Test
    public void bindingPipeline() {
        PageContext context = mock(PageContext.class);
        when(context.getSourceId(any())).thenReturn("pageId");
        when(context.getCompiledId(any())).thenReturn("pageId");
        when(context.getSourceClass()).thenReturn(N2oPage.class);
        when(context.getCompiledClass()).thenReturn(Page.class);
        DataSet data = new DataSet();

        //bind
        Page compiledPage = N2oPipelineSupport.bindPipeline(env).bind().get(createCompiled(), context, data);
        assertThat(compiledPage.getPageProperty().getTitle(), is("binding test"));

        //transform + bind
        compiledPage = N2oPipelineSupport.bindPipeline(env).transform().bind().get(createCompiled(), context, data);
        assertThat(compiledPage.getPageProperty().getTitle(), is("binding transformed test"));

        //bind + bind
        compiledPage = N2oPipelineSupport.bindPipeline(env).bind().bind().get(createCompiled(), context, data);
        assertThat(compiledPage.getPageProperty().getTitle(), is("binding binding test"));
    }

    @Test
    public void persistPipeline() throws IOException {
        XmlInfo pageInfo = new XmlInfo("pageId", N2oPage.class, "", "");

        when(metadataRegister.get("pageId", N2oSimplePage.class)).thenReturn(pageInfo);
        when(metadataRegister.get("pageId", N2oPage.class)).thenReturn(pageInfo);

        PageContext context = mock(PageContext.class);
        when(context.getSourceId(any())).thenReturn("pageId");
        when(context.getCompiledId(any())).thenReturn("pageId");
        when(context.getSourceClass()).thenReturn(N2oPage.class);
        when(context.getCompiledClass()).thenReturn(Page.class);

        //read
        when(readerFactory.read(pageInfo, null)).then(m -> createSource());

        InputStream inputStream = N2oPipelineSupport.persistPipeline(env).persist().get(createSource());
        String xml = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        assertThat(xml, containsString("<test xmlns=\"test-namespace\" />"));
    }

    private N2oSimplePage createSource() {
        N2oSimplePage sourcePage;
        sourcePage = new N2oSimplePage();
        sourcePage.setId("test");
        sourcePage.setName("test");
        sourcePage.setSrc("test");
        return sourcePage;
    }

    private Page createCompiled() {
        Page page;
        page = new Page();
        page.getPageProperty().setTitle("test");
        return page;
    }


    static class MockSourceTransformer extends MockMetadataFactory<SourceTransformer<?>> implements SourceTransformerFactory {

        @Override
        public <S> S transform(S source, ValidateProcessor p) {
            ((N2oSimplePage)source).setName("transformed " + ((N2oSimplePage)source).getName());
            return source;
        }
    }

    static class MockCompileTransformer extends MockMetadataFactory<CompileTransformer<?, ?>> implements CompileTransformerFactory {

        @Override
        public <D extends Compiled> D transform(D compiled, CompileContext<?, ?> context, CompileProcessor processor) {
            ((Page) compiled).getPageProperty().setTitle("transformed " + ((Page) compiled).getPageProperty().getTitle());
            return compiled;
        }
    }


    static class MockSourceValidatorFactory extends MockMetadataFactory<SourceValidator> implements SourceValidatorFactory {

        @Override
        public <S> void validate(S source, ValidateProcessor p) throws N2oMetadataValidationException {
            if ("test".equals(((N2oSimplePage)source).getSrc()))
                throw new N2oMetadataValidationException("validated " + ((N2oSimplePage)source).getName());
        }
    }


    static class MockSourceCacheTemplate extends CacheTemplate {
        private N2oSimplePage cache;

        @Override
        public Object execute(String cacheRegion, Object key, CacheCallback callback) {
            if (cache == null) {
                N2oSimplePage source = (N2oSimplePage) callback.doInCacheMiss();
                cache = new N2oSimplePage();
                cache.setName(source.getName());
                return source;
            } else {
                N2oSimplePage copy = new N2oSimplePage();
                copy.setName("cached " + cache.getName());
                return copy;
            }
        }
    }

    static class MockCompiledCacheTemplate extends CacheTemplate {
        private Page cache;

        @Override
        public Object execute(String cacheRegion, Object key, CacheCallback callback) {
            if (cache == null) {
                Page compiled = (Page) callback.doInCacheMiss();
                cache = new Page();
                cache.getPageProperty().setTitle(compiled.getPageProperty().getTitle());
                return compiled;
            } else {
                Page copy = new Page();
                copy.getPageProperty().setTitle("cached " + cache.getPageProperty().getTitle());
                return copy;
            }
        }
    }

    static class MockSourceCompilerFactory extends MockMetadataFactory<SourceCompiler> implements SourceCompilerFactory {

        @Override
        public <D extends Compiled, S, C extends CompileContext<?, ?>> D compile(S source, C context, CompileProcessor p) {
            Page page = new Page();
            page.getPageProperty().setTitle("compiled " + ((N2oSimplePage) source).getName());
            return (D) page;
        }
    }

    static class MockBinderFactory extends MockMetadataFactory<MetadataBinder<?>> implements MetadataBinderFactory {

        @Override
        public <D extends Compiled> D bind(D compiled, BindProcessor processor) {
            ((Page) compiled).getPageProperty().setTitle("binding " + ((Page) compiled).getPageProperty().getTitle());
            return compiled;
        }
    }

    static class MockMergeFactory extends MockMetadataFactory<SourceMerger<?>> implements SourceMergerFactory {

        @Override
        public <S> S merge(S source, S override) {
            String name = ((N2oSimplePage) override).getName();
            ((N2oSimplePage)source).setName("merged " + (name != null ? name : ((N2oSimplePage) source).getName()));
            return source;
        }
    }

    private class MockPipelineOperationFactory extends N2oPipelineOperationFactory {
        MockPipelineOperationFactory() {
            add(new ReadOperation<>(env.getMetadataRegister(), env.getSourceLoaderFactory()),
                    new MergeOperation<>(env.getSourceMergerFactory()),
                    new ValidateOperation<>(env.getSourceValidatorFactory()),
                    new CopyOperation<>(),
                    new SourceTransformOperation<>(env.getSourceTransformerFactory()),
                    new CompileTransformOperation<>(env.getCompileTransformerFactory()),
                    new SourceCacheOperation<>(sourceCacheTemplate, env.getMetadataRegister()),
                    new CompileCacheOperation<>(compileCacheTemplate),
                    new CompileOperation<>(env.getSourceCompilerFactory()),
                    new BindOperation<>(env.getMetadataBinderFactory()),
                    new PersistOperation<>(env.getNamespacePersisterFactory()));
        }
    }

    private static class MockPersisterFactory implements NamespacePersisterFactory<NamespaceUriAware, NamespacePersister<NamespaceUriAware>> {

        @Override
        public NamespacePersister<NamespaceUriAware> produce(Class<NamespaceUriAware> clazz, Namespace... namespaces) {
            return new NamespacePersister<NamespaceUriAware>() {
                @Override
                public Element persist(NamespaceUriAware entity, Namespace namespace) {
                    return new Element("test", "test-namespace");
                }

                @Override
                public String getNamespaceUri() {
                    return "test-namespace";
                }

                @Override
                public String getElementName() {
                    return "test";
                }

                @Override
                public Class<NamespaceUriAware> getElementClass() {
                    return null;
                }
            };
        }

        @Override
        public void add(NamespacePersister<NamespaceUriAware> persister) {

        }
    }
}