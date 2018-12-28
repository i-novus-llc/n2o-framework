package net.n2oapp.framework.config.audit.git.util;

import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static net.n2oapp.framework.config.audit.git.util.N2oGitUtil.DEFAULT_FILE_ENCODING;

/**
 * @author dfirstov
 * @since 04.08.2015
 */
public class N2oGitFileUtil {

    public static File createFile(XmlInfo info, String repositoryPath) throws IOException {
        File file = getFile(info, repositoryPath);
        if (file == null)
            return null;
        return createFile(info.getURI(), file);
    }

    private static File getFile(XmlInfo info, String repositoryPath) {
        if (info == null || info.getLocalPath() == null)
            return null;
        return new File(PathUtil.concatAbsoluteAndLocalPath(repositoryPath, info.getLocalPath()));
    }

    public static File createFile(String uri, File file) throws IOException {
        try (InputStream inputStream = FileSystemUtil.getContentAsStream(uri)) {
            String content = IOUtils.toString(inputStream, DEFAULT_FILE_ENCODING);
            FileUtils.writeStringToFile(file, content, DEFAULT_FILE_ENCODING);
            return file;
        }
    }

    public static void deleteFile(File file) throws IOException {
        FileUtils.forceDelete(file);
    }
}
