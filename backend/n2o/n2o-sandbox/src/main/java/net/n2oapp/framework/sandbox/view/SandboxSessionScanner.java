package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.register.scanner.OverrideInfoScanner;
import net.n2oapp.framework.sandbox.server.editor.model.FileModel;
import net.n2oapp.framework.sandbox.server.editor.model.ProjectFileInfo;
import net.n2oapp.framework.sandbox.server.editor.model.ProjectModel;
import net.n2oapp.framework.sandbox.server.utils.FileNameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Сканер информации о метаданных из файлов проекта
 */
public class SandboxSessionScanner implements OverrideInfoScanner<ProjectFileInfo> {

    private ProjectModel project;
    private N2oApplicationBuilder builder;

    public SandboxSessionScanner(ProjectModel project, N2oApplicationBuilder builder) {
        this.project = project;
        this.builder = builder;
    }

    @Override
    public List<ProjectFileInfo> scan() {
        List<ProjectFileInfo> result = new ArrayList<>();
        for (FileModel fm : project.getFiles()) {
            if (FileNameUtil.isSourceFile(fm.getFile())) {
                Class<? extends SourceMetadata> baseSourceClass =
                        builder.getEnvironment().getSourceTypeRegister().get(FileNameUtil.getTypeFromFile(fm.getFile())).getBaseSourceClass();
                String name = FileNameUtil.getNameFromFile(fm.getFile());
                ProjectFileInfo fileInfo = new ProjectFileInfo(name, baseSourceClass);
                fileInfo.setSource(fm.getSource());
                result.add(fileInfo);
            }
        }

        return result;
    }
}
