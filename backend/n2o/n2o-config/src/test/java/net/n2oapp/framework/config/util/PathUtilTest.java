package net.n2oapp.framework.config.util;

import net.n2oapp.framework.config.register.storage.PathUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author dfirstov
 * @since 26.03.2015
 */
class PathUtilTest {

    @Test
    void testConvertRootPathToUrl() {
        assertEquals("classpath*:META-INF/conf/", PathUtil.convertRootPathToUrl("classpath*:META-INF/conf/**/*.xml"));
        String url = "file:/C:/test-n2o/META-INF/conf/";
        assertEquals(url, PathUtil.convertRootPathToUrl("C:/test-n2o/META-INF/conf"));
        assertEquals(url, PathUtil.convertRootPathToUrl("C:/test-n2o/META-INF/conf/"));
        assertEquals(url, PathUtil.convertRootPathToUrl("C:\\test-n2o\\META-INF\\conf\\"));
        assertEquals(url, PathUtil.convertRootPathToUrl("file:/C:/test-n2o/META-INF/conf/"));
        url = "file:/test-n2o/META-INF/conf/";
        assertEquals(url, PathUtil.convertRootPathToUrl("test-n2o/META-INF/conf/"));
        assertEquals(url, PathUtil.convertRootPathToUrl("/test-n2o/META-INF/conf/"));
    }

    @Test
    void testConvertUrlToAbsolutePath() {
        assertNull(PathUtil.convertUrlToAbsolutePath("classpath*:META-INF/conf/"));
        assertNull(PathUtil.convertUrlToAbsolutePath("jar:file:/C:/n2o-bundle/conf/default.application.xml"));
        assertEquals("C:/app/conf/test.xml", PathUtil.convertUrlToAbsolutePath("file:C:/app/conf/test.xml"));
    }

    @Test
    void testConvertUrlToPattern() {
        String patternPath = "classpath*:META-INF/conf/**/*.xml";
        assertEquals(patternPath, PathUtil.convertUrlToPattern("classpath*:META-INF/conf", "xml"));
        assertEquals(patternPath, PathUtil.convertUrlToPattern("classpath*:META-INF/conf/", "xml"));
        assertEquals("classpath*:META-INF/conf/", PathUtil.convertUrlToPattern("classpath*:META-INF/conf/", null));
    }

    @Test
    void testConvertRootPathToFileUrl() {
        String localPath = "test/default.application.xml";
        String fileUrl1 = "classpath*:META-INF/conf/test/default.application.xml";
        assertEquals(fileUrl1, PathUtil.convertRootPathToFilePathPattern("classpath*:META-INF/conf/**/*.xml", localPath));
        String fileUrl2 = "file:/C:/app/conf/test/default.application.xml";
        assertEquals(fileUrl2, PathUtil.convertRootPathToFilePathPattern("C:/app/conf", localPath));
        assertEquals(fileUrl2, PathUtil.convertRootPathToFilePathPattern("C:/app/conf/", localPath));
        assertEquals(fileUrl2, PathUtil.convertRootPathToFilePathPattern("C:/app/conf/", "/" + localPath));
    }
}
