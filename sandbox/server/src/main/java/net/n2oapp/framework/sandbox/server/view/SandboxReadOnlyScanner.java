package net.n2oapp.framework.sandbox.server.view;

import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.register.scanner.OverrideInfoScanner;
import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.framework.config.util.FileSystemUtil;
import net.n2oapp.framework.sandbox.server.editor.model.TemplateModel;

import java.util.List;

import static net.n2oapp.framework.config.register.RegisterUtil.collectInfo;
import static net.n2oapp.framework.config.register.storage.PathUtil.convertPathToClasspathUri;

public class SandboxReadOnlyScanner implements OverrideInfoScanner<XmlInfo> {
    private SourceTypeRegister sourceTypeRegister;
    private TemplateModel templateModel;

    public SandboxReadOnlyScanner(SourceTypeRegister sourceTypeRegister, TemplateModel templateModel) {
        this.sourceTypeRegister = sourceTypeRegister;
        this.templateModel = templateModel;
    }

    @Override
    public List<XmlInfo> scan() {
        List<Node> projectNodes = FileSystemUtil.getNodesByLocationPattern(convertPathToClasspathUri(templateModel.getTemplateId()) + "/**/*.*.xml");
        return collectInfo(
                projectNodes,
                node -> RegisterUtil.createXmlInfo(SandboxReadOnlyScanner.class, node.getLocalPath(), node.getURI(), sourceTypeRegister));
    }
}
