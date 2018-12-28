package net.n2oapp.framework.config.audit.git.util.model;

import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.ConfigStarter;
import net.n2oapp.framework.config.audit.N2oConfigAudit;
import net.n2oapp.framework.config.audit.git.N2oConfigAuditGit;
import net.n2oapp.framework.config.audit.git.N2oGitCore;
import net.n2oapp.framework.config.audit.git.util.N2oGitTestUtil;
import net.n2oapp.framework.config.audit.git.util.mock.XmlInfoScannerMock;
import net.n2oapp.framework.config.reader.mock.ConfigMetadataLockerMock;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.properties.StaticProperties;
import net.n2oapp.properties.test.TestStaticProperties;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author dfirstov
 * @since 25.09.2015
 */
public class N2oGitTestEnv {
    private TemporaryFolder tempFolder = new TemporaryFolder();

    private static String SERVER_BRANCH_NAME = "server";
    private static String SYSTEM_BRANCH_NAME = "system/server";
    private static String REMOTE_NAME = "origin";
    private N2oGitCore gitCore;
    private N2oConfigAudit configAuditGit;
    private MetadataRegister confReg;
    private TestStaticProperties testProperties;
    private Git remoteGit;
    private Git workRepoGit;
    private String configPath;
    private String workRepoPath;

    public N2oGitTestEnv(MetadataRegister confReg, SourceTypeRegister metaModelRegister) throws IOException, GitAPIException {
        tempFolder.create();
        testProperties = (TestStaticProperties) StaticSpringContext.getBean(StaticProperties.class);
        testProperties.setProperties(initProperties());
        this.gitCore = new N2oGitCore();
        this.configPath = getTestProperties().getProperty("n2o.config.path");
        List<String> excludeInUpdate = new ArrayList<>();
        excludeInUpdate.add(PathUtil.normalize(PathUtil.concatAbsoluteAndLocalPath(configPath, ".git")));
        excludeInUpdate.add(PathUtil.normalize(PathUtil.concatAbsoluteAndLocalPath(configPath, ".gitignore")));
        ConfigMetadataLockerMock configMetadataLockerMock = new ConfigMetadataLockerMock();
        ConfigStarter configStarter = mock(ConfigStarter.class);
        when(configStarter.getConfigPath()).thenReturn(configPath);
        this.configAuditGit = new N2oConfigAuditGit(true, configMetadataLockerMock, true, "test_version", gitCore,
                new XmlInfoScannerMock("classpath:net/n2oapp/framework/config/audit/git/**/*.xml", metaModelRegister), excludeInUpdate, configStarter,
                confReg, metaModelRegister);
        this.confReg = confReg;

    }

    private Properties initProperties() throws IOException, GitAPIException {
        Properties properties = new Properties();
        properties.setProperty("n2o.config.audit.storage.mode", "modify");
        properties.setProperty("n2o.config.audit.merge.conflict_mode", "manual");
        properties.setProperty("n2o.config.audit.enabled", "true");
        properties.setProperty("n2o.config.ignores", ".git,.n2o_git_stash");
        properties.setProperty("n2o.config.audit.branch", SERVER_BRANCH_NAME);
        properties.setProperty("n2o.config.class.packages", "net.n2oapp.framework");
        properties.setProperty("n2o.version", "test_version");
        properties.setProperty("n2o.config.audit.remote.name", REMOTE_NAME);
        properties.setProperty("n2o.config.audit.remote.auth.enabled","false");
        properties.setProperty("n2o.config.path", initRepoDir());
        properties.setProperty("n2o.config.audit.remote.uri", initRemoteRepo());
        properties.setProperty("n2o.config.audit.update.excludes",".git,.gitignore");
        properties.setProperty("n2o.config.restore.mode", "replace");
        properties.setProperty("n2o.context.compile.max.nesting", "7");
        return properties;
    }

    public String getConfigPath() {
        return configPath;
    }

    public String initRepoDir() throws IOException {
        String rootDir = "test_conf";
        tempFolder.newFolder(rootDir);
        return N2oGitTestUtil.replaceSlash(tempFolder.getRoot().getAbsolutePath()) + "/" + rootDir + "/";
    }

    public String initRemoteRepo() throws IOException, GitAPIException {
        String rootDir = "test_remote_repo";
        File repo = tempFolder.newFolder(rootDir);
        remoteGit = Git.init().setDirectory(repo).setBare(true).call();
        String workDir = "test_work_repo";
        File work = tempFolder.newFolder(workDir);
        workRepoPath = work.getPath();
        workRepoGit = Git.cloneRepository().setDirectory(work).setRemote(REMOTE_NAME)
                .setURI(N2oGitTestUtil.replaceSlash(tempFolder.getRoot().getAbsolutePath()) + "/" + rootDir + "/").call();
        return N2oGitTestUtil.replaceSlash(tempFolder.getRoot().getAbsolutePath()) + "/" + rootDir + "/";
    }

    public String getServerBranchName() {
        return SERVER_BRANCH_NAME;
    }

    public N2oGitCore getGitCore() {
        return gitCore;
    }

    public void setGitCore(N2oGitCore gitCore) {
        this.gitCore = gitCore;
    }

    public N2oConfigAudit getConfigAuditGit() {
        return configAuditGit;
    }

    public void setConfigAuditGit(N2oConfigAudit configAuditGit) {
        this.configAuditGit = configAuditGit;
    }

    public MetadataRegister getConfReg() {
        return confReg;
    }

    public void setConfReg(MetadataRegister confReg) {
        this.confReg = confReg;
    }


    public Git getRemoteGit() {
        return remoteGit;
    }

    public Git getWorkRepoGit() {
        return workRepoGit;
    }

    public TemporaryFolder getTempFolder() {
        return tempFolder;
    }

    public TestStaticProperties getTestProperties() {
        return testProperties;
    }

    public String getSystemBranchName() {
        return SYSTEM_BRANCH_NAME;
    }

    public String getWorkRepoPath() {
        return workRepoPath;
    }

    public void deleteRepo() {
        tempFolder.delete();
    }
}
