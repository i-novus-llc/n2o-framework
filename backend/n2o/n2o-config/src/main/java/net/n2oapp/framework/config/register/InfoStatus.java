package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.config.util.FileSystemUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Статусы метаданных
 */
@Deprecated
public class InfoStatus {

    public enum StatusEnum {
        SYSTEM("{n2o.system}"),
        SERVER("{n2o.server}"),
        MODIFY("{n2o.modify}"),
        DUPLICATE("{n2o.duplicated}");

        public String value;

        StatusEnum(String value) {
            this.value = value;
        }

    }

    public static boolean isServerFile(XmlInfo info) {
        return info.isOverride();
    }

    public static boolean isSystemFile(XmlInfo info) {
        return !isServerFile(info);
    }

    public static StatusEnum calculateStatusByFile(XmlInfo info) {
        return calculateStatusByFile(info, true);
    }

    public static StatusEnum calculateStatusByFile(XmlInfo info, boolean checkDiff) {
        return calculateStatusByFile(info, checkDiff, true);
    }

    public static StatusEnum calculateStatusByFile(XmlInfo info, boolean checkDiff, boolean checkDuplicate) {
        boolean isServerFile = isServerFile(info);
        boolean hasAncestor = info.getAncestor() != null;
        if (hasAncestor) {
            if (checkDuplicate) {
                boolean hasDuplicateAncestor = findNotEqualsLocalPath(info.getAncestor(), info.getLocalPath()) != null;
                if (hasDuplicateAncestor)
                    return StatusEnum.DUPLICATE;
            }
            try {
                if (info.getLocalPath().equals(info.getAncestor().getLocalPath())
                        && info.getUri().equals(info.getAncestor().getUri())) {
                    return StatusEnum.SERVER;
                }
                boolean isIdenticalContent = isIdenticalContentSimple(info.getAncestor().getURI(), info.getURI(), checkDiff);
                if (isIdenticalContent) {
                    return StatusEnum.SYSTEM;
                } else {
                    return StatusEnum.MODIFY;
                }
            } catch (IOException e) {
                throw new N2oException(e);
            }
        } else {
            if (isServerFile) {
                return StatusEnum.SERVER;
            } else {
                return StatusEnum.SYSTEM;
            }
        }
    }

    private static XmlInfo findNotEqualsLocalPath(XmlInfo info, String localPath) {
        if (!info.getLocalPath().equals(localPath))
            return info;
        if (info.getAncestor() != null)
            return findNotEqualsLocalPath(info.getAncestor(), localPath);
        return null;
    }

    public static boolean checkIsDuplicate(XmlInfo info) {
        List<XmlInfo> existsInfos = RegisterUtil.retrieveInfoTree(info, new ArrayList<>());
        return existsInfos.stream().anyMatch(i -> i.getId().equals(info.getId()) && !i.getLocalPath().equals(info.getLocalPath()));
    }

    private static boolean isIdenticalContentSimple(String uri1, String uri2, boolean checkDiff) throws IOException {
        if (checkDiff) {
            return isIdenticalContent(uri1, uri2);
        } else {
            long fileSize1 = FileSystemUtil.getFileSizeByUri(uri1);
            long fileSize2 = FileSystemUtil.getFileSizeByUri(uri2);
            return fileSize1 != fileSize2;
        }
    }


    private static boolean isIdenticalContent(String uri1, String uri2) throws IOException {
        String file1 = FileSystemUtil.getContentByUri(uri1);
        String file2 = FileSystemUtil.getContentByUri(uri2);
        String resFile1 = file1.replaceAll("\r", "");
        String resFile2 = file2.replaceAll("\r", "");
        return resFile1.equals(resFile2);
    }

}
