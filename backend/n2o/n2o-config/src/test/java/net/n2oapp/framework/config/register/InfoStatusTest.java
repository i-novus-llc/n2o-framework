package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oSourceTypesPack;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.util.FileSystemUtil;
import net.n2oapp.properties.test.TestStaticProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Тестирование статуса метаданной
 */
class InfoStatusTest {

    private SourceTypeRegister metaModelRegister;

    @BeforeEach
    void setUp() {
        metaModelRegister = new N2oApplicationBuilder().packs(new N2oSourceTypesPack()).getEnvironment().getSourceTypeRegister();
        Properties properties = new Properties();
        String conthPath = "/config/path/";
        properties.setProperty("n2o.config.path", conthPath);
        properties.setProperty("n2o.config.class.packages", "net.n2oapp.framework");
        TestStaticProperties testStaticProperties = new TestStaticProperties();
        testStaticProperties.setProperties(properties);
    }

    @Test
    void testSystemServer() {
        //system path
        InfoConstructor info = new InfoConstructor(new ConfigId("page", metaModelRegister.get(N2oPage.class)));
        info.setLocalPath("page.page.xml");
        assertSame(InfoStatus.calculateStatusByFile(info), InfoStatus.StatusEnum.SYSTEM);

        //system path, test dir
        info = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info.setLocalPath("test/page.page.xml");
        assertSame(InfoStatus.calculateStatusByFile(info), InfoStatus.StatusEnum.SYSTEM);

        //server path
        info = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info.setLocalPath("page.page.xml");
        info.setOverride(true);
        assertSame(InfoStatus.calculateStatusByFile(info), InfoStatus.StatusEnum.SERVER);

        //server path, test dir
        info = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info.setLocalPath("test/page.page.xml");
        info.setOverride(true);
        assertSame(InfoStatus.calculateStatusByFile(info), InfoStatus.StatusEnum.SERVER);
    }

    @Test
    void testEqAncestor(@TempDir Path tempFolder) throws Exception {
        File modified = new File(tempFolder.toAbsolutePath() + "testObj.object.xml");
        createFile("classpath:/net/n2oapp/framework/config/ancestor/testObj2.object.xml", modified);

        //server -> system
        InfoConstructor info = new InfoConstructor("page", metaModelRegister.get(N2oObject.class));
        info.setLocalPath("config/ancestor/testObj.object.xml");
        info.setUri("classpath:/net/n2oapp/framework/config/ancestor/testObj.object.xml");
        InfoConstructor info2 = new InfoConstructor("page", metaModelRegister.get(N2oObject.class));
        info2.setLocalPath("config/ancestor/testObj.object.xml");
        info2.setUri(PathUtil.convertRootPathToUrl(modified.getAbsolutePath()));
        info2.setAncestor(info);
        info2.setOverride(true);

        assertSame(InfoStatus.calculateStatusByFile(info2), InfoStatus.StatusEnum.MODIFY);

        File unmodified = new File(tempFolder.toAbsolutePath() + "testObj.object.xml");
        createFile("classpath:/net/n2oapp/framework/config/ancestor/testObj.object.xml", unmodified);
        //system -> server
        info = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info.setLocalPath("config/ancestor/testObj.object.xml");
        info.setUri("classpath:/net/n2oapp/framework/config/ancestor/testObj.object.xml");

        info2 = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info2.setLocalPath("config/ancestor/testObj.object.xml");
        info2.setUri(PathUtil.convertRootPathToUrl(modified.getAbsolutePath()));
        info2.setOverride(true);
        info2.setAncestor(info);
        assertSame(InfoStatus.calculateStatusByFile(info2), InfoStatus.StatusEnum.SYSTEM);
    }

    @Test
    void testNotEqAncestor() {
        //server2 -> system1
        InfoConstructor info = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info.setUri("test1/page.page.xml");
        info.setLocalPath("test1/page.page.xml");
        InfoConstructor info2 = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info2.setLocalPath("test2/page.page.xml");
        info2.setUri("jar:/system/path/test2/page.page.xml");
        info2.setOverride(true);
        info2.setAncestor(info);
        assertSame(InfoStatus.calculateStatusByFile(info2), InfoStatus.StatusEnum.DUPLICATE);

        //system1 -> server2
        info = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info.setLocalPath("test1/page.page.xml");
        info.setUri("test1/page.page.xml");
        info2 = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info2.setLocalPath("test2/page.page.xml");
        info2.setUri("jar:/system/path/test2/page.page.xml");
        info2.setOverride(true);
        info.setAncestor(info2);
        assertSame(InfoStatus.calculateStatusByFile(info), InfoStatus.StatusEnum.DUPLICATE);

        //system1 -> system2
        info = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info.setLocalPath("test1/page.page.xml");
        info.setUri("test1/page.page.xml");
        info2 = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info2.setLocalPath("test2/page.page.xml");
        info2.setUri("jar:/system/path/test2/page.page.xml");
        info.setAncestor(info2);
        assertSame(InfoStatus.calculateStatusByFile(info), InfoStatus.StatusEnum.DUPLICATE);

        //server1 -> system2 -> system1
        info = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info.setLocalPath("test1/page.page.xml");
        info.setUri("test1/page.page.xml");
        info2 = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info2.setLocalPath("test2/page.page.xml");
        info2.setUri("jar:/system/path/test2/page.page.xml");
        InfoConstructor info3 = new InfoConstructor("page", metaModelRegister.get(N2oPage.class));
        info3.setLocalPath("test1/page.page.xml");
        info3.setUri("file:/config/path/test1/page.page.xml");
        info3.setOverride(true);
        info3.setAncestor(info2);
        info2.setAncestor(info);
        assertSame(InfoStatus.calculateStatusByFile(info3), InfoStatus.StatusEnum.DUPLICATE);
    }

    private static File createFile(String uri, File file) throws IOException {
        try (InputStream inputStream = FileSystemUtil.getContentAsStream(uri)) {
            String content = IOUtils.toString(inputStream, "UTF-8");
            FileUtils.writeStringToFile(file, content, "UTF-8");
            return file;
        }
    }
}
