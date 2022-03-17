package net.n2oapp.framework.sandbox.server.view;

import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.register.scanner.OverrideInfoScanner;
import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.framework.config.util.FileSystemUtil;

import java.util.List;

import static net.n2oapp.framework.config.register.RegisterUtil.collectInfo;
import static net.n2oapp.framework.config.register.storage.PathUtil.convertRootPathToUrl;

public class SandboxScanner implements OverrideInfoScanner<XmlInfo> {
    private SourceTypeRegister sourceTypeRegister;
    private String basePath;
    private String projectId;

    public SandboxScanner(SourceTypeRegister sourceTypeRegister, String basePath, String projectId) {
        this.sourceTypeRegister = sourceTypeRegister;
        this.basePath = basePath;
        this.projectId = projectId;
    }

    @Override
    public List<XmlInfo> scan() {
        List<Node> projectNodes = FileSystemUtil.getNodesByLocationPattern(convertRootPathToUrl(basePath) + projectId + "/**/*.*.xml");
        return collectInfo(
                projectNodes,
                node -> RegisterUtil.createXmlInfo(SandboxScanner.class, node.getLocalPath(), node.getURI(), sourceTypeRegister));
    }
}
