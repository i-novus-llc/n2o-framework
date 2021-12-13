package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.PipelineOperationTypeAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.util.function.Supplier;

public class PersistOperation<S extends SourceMetadata> implements PipelineOperation<InputStream, S>, PipelineOperationTypeAware, MetadataEnvironmentAware {

    private static final String INDENT_DEFAULT = "    ";

    private NamespacePersisterFactory<S, ?> persisterFactory;
    private Format format;

    public PersistOperation() {
        this.format = Format.getRawFormat();
        format.setIndent(INDENT_DEFAULT);
    }

    public PersistOperation(NamespacePersisterFactory<S, ?> persisterFactory) {
        this();
        this.persisterFactory = persisterFactory;
    }

    @Override
    public PipelineOperationType getPipelineOperationType() {
        return PipelineOperationType.PERSIST;
    }

    @Override
    public InputStream execute(CompileContext<?, ?> context,
                     DataSet data,
                     Supplier<S> supplier,
                     CompileProcessor compileProcessor,
                     BindProcessor bindProcessor,
                     SourceProcessor sourceProcessor) {
        S source = supplier.get();
        ByteArrayOutputStream output = writeDocument(source);
        return new ByteArrayInputStream(output.toByteArray());
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.persisterFactory = environment.getNamespacePersisterFactory();
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    private ByteArrayOutputStream writeDocument(S source) {
        Element element = persisterFactory.produce(source).persist(source, source.getNamespace());
        Document doc = new Document();
        doc.addContent(element);
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(format);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            xmlOutput.output(doc, outputStream);
        } catch (IOException e) {
            throw new N2oException("Error during reading metadata " + source.getId(), e);
        }
        return outputStream;
    }
}
