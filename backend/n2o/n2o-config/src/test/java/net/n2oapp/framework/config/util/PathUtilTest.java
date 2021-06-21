package net.n2oapp.framework.config.util;

import org.junit.Test;
import net.n2oapp.framework.config.register.storage.PathUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author dfirstov
 * @since 26.03.2015
 */
public class PathUtilTest {

    @Test
    public void testConvertRootPathToUrl() {
        assert "classpath*:META-INF/conf/".equals(PathUtil.convertRootPathToUrl("classpath*:META-INF/conf/**/*.xml"));
        String url = "file:/C:/test-n2o/META-INF/conf/";
        assert url.equals(PathUtil.convertRootPathToUrl("C:/test-n2o/META-INF/conf"));
        assert url.equals(PathUtil.convertRootPathToUrl("C:/test-n2o/META-INF/conf/"));
        assert url.equals(PathUtil.convertRootPathToUrl("C:\\test-n2o\\META-INF\\conf\\"));
        assert url.equals(PathUtil.convertRootPathToUrl("file:/C:/test-n2o/META-INF/conf/"));
        url = "file:/test-n2o/META-INF/conf/";
        assertEquals(url, PathUtil.convertRootPathToUrl("test-n2o/META-INF/conf/"));
        assertEquals(url, PathUtil.convertRootPathToUrl("/test-n2o/META-INF/conf/"));
    }

    @Test
    public void testConvertUrlToAbsolutePath() {
        assert PathUtil.convertUrlToAbsolutePath("classpath*:META-INF/conf/") == null;
        assert PathUtil.convertUrlToAbsolutePath("jar:file:/C:/n2o-bundle/conf/default.application.xml") == null;
        assert "C:/app/conf/test.xml".equals(PathUtil.convertUrlToAbsolutePath("file:C:/app/conf/test.xml"));
    }

    @Test
    public void testConvertUrlToPattern() {
        String patternPath = "classpath*:META-INF/conf/**/*.xml";
        assert patternPath.equals(PathUtil.convertUrlToPattern("classpath*:META-INF/conf", "xml"));
        assert patternPath.equals(PathUtil.convertUrlToPattern("classpath*:META-INF/conf/", "xml"));
        assert "classpath*:META-INF/conf/".equals(PathUtil.convertUrlToPattern("classpath*:META-INF/conf/", null));
    }

    @Test
    public void testConvertRootPathToFileUrl() {
        String localPath = "test/default.application.xml";
        String fileUrl1 = "classpath*:META-INF/conf/test/default.application.xml";
        assert fileUrl1.equals(PathUtil.convertRootPathToFilePathPattern("classpath*:META-INF/conf/**/*.xml", localPath));
        String fileUrl2 = "file:/C:/app/conf/test/default.application.xml";
        assert fileUrl2.equals(PathUtil.convertRootPathToFilePathPattern("C:/app/conf", localPath));
        assert fileUrl2.equals(PathUtil.convertRootPathToFilePathPattern("C:/app/conf/", localPath));
        assert fileUrl2.equals(PathUtil.convertRootPathToFilePathPattern("C:/app/conf/", "/" + localPath));
    }

}
