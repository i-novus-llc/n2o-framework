package net.n2oapp.framework.config.audit.git.util;

import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.framework.api.event.N2oEventBus;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.reader.ConfigMetadataLocker;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.api.test.TestContextEngine;
import net.n2oapp.framework.api.test.TestStaticUserContext;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.api.util.ToListConsumer;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.audit.git.util.mock.ConfigRegisterMock;
import net.n2oapp.framework.config.audit.git.util.mock.FolderInfoScannerMock;
import net.n2oapp.framework.config.audit.git.util.mock.N2oEventBusMock;
import net.n2oapp.framework.config.audit.git.util.mock.XmlInfoScannerMock;
import net.n2oapp.framework.config.audit.git.util.model.N2oGitTestEnv;
import net.n2oapp.framework.config.metadata.pack.N2oSourceTypesPack;
import net.n2oapp.framework.config.reader.mock.ConfigMetadataLockerMock;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.register.InfoConstructor;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.scanner.FolderInfoScanner;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.config.register.storage.Node;

import net.n2oapp.properties.StaticProperties;
import net.n2oapp.properties.test.TestStaticProperties;
import net.n2oapp.watchdir.WatchDir;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Утилитный класс для тестирования аудита метданных с использованием git
 */
public class N2oGitTestUtil {
    private static String configPath;
    private static ConfigRegisterMock confReg;
    private static SourceTypeRegister metaModelRegister;

    public static N2oGitTestEnv iniTestEnv() throws IOException, GitAPIException {
        metaModelRegister = new N2oApplicationBuilder().packs(new N2oSourceTypesPack()).getEnvironment().getSourceTypeRegister();
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBean(StaticProperties.class)).thenReturn(new TestStaticProperties());
        when(context.getBean(N2oEventBus.class)).thenReturn(new N2oEventBusMock());
        XmlInfoScannerMock xmlInfoScannerMock = new XmlInfoScannerMock("classpath:net/n2oapp/framework/config/audit/git/service/test_conf/**/*.xml", metaModelRegister);
        when(context.getBean(XmlInfoScanner.class)).thenReturn(xmlInfoScannerMock);
        when(context.getBean("watchDir")).thenReturn(new WatchDir());
        when(context.getBean(ConfigMetadataLocker.class)).thenReturn(new ConfigMetadataLockerMock());
        when(context.getBean(SourceTypeRegister.class)).thenReturn(metaModelRegister);
        new StaticSpringContext().setApplicationContext(context);
        initContext();
        ConfigRegisterMock confReg = initConfigRegister();
        N2oGitTestEnv env = new N2oGitTestEnv(confReg, metaModelRegister);
        configPath = env.getTestProperties().getProperty("n2o.config.path");
        FolderInfoScanner folderInfoScanner = new FolderInfoScannerMock(null);
        when(context.getBean(FolderInfoScanner.class)).thenReturn(folderInfoScanner);
        return env;
    }

    public static void initContext() {
        Map<String, Object> userContext = new HashMap<>();
        userContext.put(UserContext.USERNAME, "admin");
        new TestStaticUserContext(new TestContextEngine(userContext));
    }

    public static ConfigRegisterMock initConfigRegister() {
        List<Class<? extends CompiledMetadata>> classes = new ArrayList<>();
        classes.add(CompiledObject.class);
        confReg = new ConfigRegisterMock(new ToListConsumer<>(), metaModelRegister);

        return confReg;
    }

    public static File generateStorageFile(String localPath) {
        return new File(configPath + localPath);
    }

    public static XmlInfo addToConfReg(String localPath, boolean isFile) throws IOException {
        return addToConfReg(localPath, isFile, true, N2oStandardPage.class);
    }

    public static XmlInfo addToConfReg(String localPath, boolean isFile, boolean createFile, Class<? extends N2oMetadata> infoClass) throws IOException {
        URL url = new URL(resolveURI(localPath));
        InfoConstructor info = RegisterUtil.createXmlInfo(Node.byLocationPattern(new UrlResource(url), "classpath*:net/n2oapp/framework/config/audit/git/service/test_conf/**/*.xml"),
                metaModelRegister);
        if (isFile) {
            if (createFile) {
                N2oGitFileUtil.createFile(info.construct(), configPath);
            }
        }
        confReg.add(info);
        return (XmlInfo) confReg.get(info.getId(), infoClass);
    }

    public static String addUriPrefix(String path) {
        return (path.startsWith("/") ? "file:" : "file:/") + path;
    }

    public static String resolveURI(String localPath) {
        URL resource = N2oGitTestUtil.class.getClassLoader().getResource("net/n2oapp/framework/config/audit/git/service/test_conf/" + localPath);
        assert resource != null;
        return resource.toString();
    }

    public static String replaceSlash(String path) {
        return path.replace("\\", "/");
    }
}
