package net.n2oapp.framework.sandbox.templates;

import lombok.extern.slf4j.Slf4j;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import net.n2oapp.framework.sandbox.loader.ProjectFileLoader;
import net.n2oapp.framework.sandbox.scanner.ProjectRequestScanner;
import net.n2oapp.properties.OverrideProperties;
import net.n2oapp.properties.reader.PropertiesReader;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
public class FileCheckerController {

    @CrossOrigin(origins = "*")
    @PostMapping({"/check-files", "/check-files/"})
    public ResponseEntity<FileCheckerResponse> checkFiles(@RequestBody List<FileModel> files) {
        N2oApplicationBuilder builder = getBuilder();
        builder.scanners(new ProjectRequestScanner(builder.getEnvironment().getSourceTypeRegister(), files));
        builder.scan();

        FileCheckerResponse response = new FileCheckerResponse();
        response.setErrors(new ArrayList<>());
        for (SourceInfo sourceInfo : builder.getEnvironment().getMetadataRegister().find(i -> true)) {
            try {
                builder.read().validate().get(sourceInfo.getId(), sourceInfo.getBaseSourceClass());
            } catch (N2oMetadataValidationException e) {
                response.getErrors().add(e.getMessage());
            }
        }

        if (response.getErrors().isEmpty()) {
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
        }

        return ResponseEntity.ok(response);
    }

    private N2oApplicationBuilder getBuilder() {
        N2oEnvironment environment = new N2oEnvironment();
        environment.setNamespacePersisterFactory(new PersisterFactoryByMap());
        environment.setNamespaceReaderFactory(new ReaderFactoryByMap(environment));
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("n2o_api_messages", "n2o_api_messages", "n2o_config_messages", "test_messages", "messages");
        messageSource.setDefaultEncoding("UTF-8");
        Locale locale = new Locale("ru");
        LocaleContextHolder.setLocale(locale);
        environment.setMessageSource(new MessageSourceAccessor(messageSource));

        OverrideProperties n2oProperties = PropertiesReader.getPropertiesFromClasspath("META-INF/n2o.properties");
        OverrideProperties appProperties = PropertiesReader.getPropertiesFromClasspath("application.properties");
        appProperties.setBaseProperties(n2oProperties);
        environment.setSystemProperties(new SimplePropertyResolver(appProperties));

        N2oApplicationBuilder builder = new N2oApplicationBuilder(environment);
        builder.packs(new N2oSourceTypesPack(), new N2oOperationsPack(), new N2oAllPack(), new N2oApplicationPack(),
                new AccessSchemaPack(), new N2oAllValidatorsPack());
        builder.loaders(new ProjectFileLoader(builder.getEnvironment().getNamespaceReaderFactory()));
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
        return builder;
    }
}
