package net.n2oapp.framework.config.audit.git;

import net.n2oapp.framework.api.metadata.reader.ConfigMetadataLocker;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.api.user.StaticUserContext;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.ConfigStarter;
import net.n2oapp.framework.config.audit.N2oConfigAudit;
import net.n2oapp.framework.config.audit.N2oConfigAuditException;
import net.n2oapp.framework.config.audit.git.util.N2oGitFileUtil;
import net.n2oapp.framework.config.audit.git.util.N2oGitUtil;
import net.n2oapp.framework.config.persister.MetadataPersister;
import net.n2oapp.framework.config.register.InfoConstructor;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.audit.model.N2oConfigCommit;
import net.n2oapp.framework.config.register.audit.model.N2oConfigConflict;
import net.n2oapp.framework.config.register.audit.model.N2oConfigHistory;
import net.n2oapp.framework.config.register.audit.model.N2oConfigMessage;
import net.n2oapp.framework.config.register.audit.util.N2oConfigConflictParser;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.util.FileSystemUtil;
import net.n2oapp.properties.StaticProperties;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.context.StaticSpringContext.getBean;

/**
 * Реализация configAudit с использованием git
 */
public class N2oConfigAuditGit implements N2oConfigAudit {
    private static final Logger logger = LoggerFactory.getLogger(N2oGitCore.class);
    private static final String SYSTEM_AUTHOR_NAME = "system";

    private boolean isUpdatable;
    private ConfigMetadataLocker configMetadataLocker;
    private boolean isEnabled;
    private String systemVersion;
    private N2oGitCore gitRepo;
    private XmlInfoScanner xmlScanner;
    private List<String> excludeInUpdate;
    private ConfigStarter configStarter;
    private MetadataRegister configRegister;
    private SourceTypeRegister metaModelRegister;

    public N2oConfigAuditGit(N2oGitCore gitRepo) {
        this.gitRepo = gitRepo;
        this.configStarter = getBean(ConfigStarter.class);
        this.configRegister = getBean(MetadataRegister.class);
        this.metaModelRegister = getBean(SourceTypeRegister.class);
        isEnabled = StaticProperties.getBoolean("n2o.config.audit.enabled");
        xmlScanner = getBean(XmlInfoScanner.class);
        systemVersion = StaticProperties.get("n2o.version");
        configMetadataLocker = getBean(ConfigMetadataLocker.class);
        excludeInUpdate = new ArrayList<>();
        excludeInUpdate.addAll(StaticProperties.getList("n2o.config.audit.update.excludes").stream()
                .map(f -> PathUtil.concatAbsoluteAndLocalPath(configStarter.getConfigPath(), f)).collect(Collectors.toList()));
        isUpdatable = true;
    }

    public N2oConfigAuditGit(boolean isUpdatable, ConfigMetadataLocker configMetadataLocker, boolean isEnabled,
                             String systemVersion, N2oGitCore gitRepo, XmlInfoScanner xmlScanner, List<String> excludeInUpdate,
                             ConfigStarter configStarter, MetadataRegister configRegister, SourceTypeRegister metaModelRegister) {
        this.isUpdatable = isUpdatable;
        this.configMetadataLocker = configMetadataLocker;
        this.isEnabled = isEnabled;
        this.systemVersion = systemVersion;
        this.gitRepo = gitRepo;
        this.xmlScanner = xmlScanner;
        this.excludeInUpdate = excludeInUpdate;
        this.configStarter = configStarter;
        this.configRegister = configRegister;
        this.metaModelRegister = metaModelRegister;
    }

    @Override
    public void init() {
        if (!isEnabled())
            return;
        if (StaticProperties.getBoolean("n2o.config.monitoring.enabled")) {
            throw new N2oConfigAuditException("n2o.audit.auditInitError.setMonitoringDisabled");
        }
        if (!MetadataPersister.RestoreMode.REPLACE.equals(StaticProperties.getEnum("n2o.config.restore.mode", MetadataPersister.RestoreMode.class))) {
            throw new N2oConfigAuditException("n2o.audit.auditInitError.setRestoreMode");
        }
        String configPath = configStarter.getConfigPath();
        if (configPath == null) {
            throw new N2oConfigAuditException("n2o.audit.auditInitError.setPath");
        }
        try {
            gitRepo.initRepo(configPath);
        } catch (GitAPIException e) {
            throw new N2oConfigAuditException("n2o.audit.repoInitFailed", e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void commit(String localPath, String message) {
        if (isEnabled()) {
            if (gitRepo.isClean())
                return;
            if (localPath == null || localPath.isEmpty())
                throw new N2oConfigAuditException("n2o.audit.commitDirectoryIsNotSet");
            if (message == null || message.isEmpty())
                throw new N2oConfigAuditException("n2o.audit.commitMessageCouldNotBeEmpty");
            try {
                gitRepo.add(localPath);
                gitRepo.commit(message, getAuthor());
                if (StaticProperties.getBoolean("n2o.config.audit.remote.push.auto.enabled")) {
                    gitRepo.push();
                }
            } catch (Exception e) {
                gitRepo.resetFiles(Collections.singleton(localPath));
                throw new N2oConfigAuditException("n2o.audit.changesUpdateFailed", e);
            }
        }
    }

    @Override
    public void commitAll(String message) {
        if (isEnabled()) {
            if (gitRepo.isClean())
                return;
            if (message == null || message.isEmpty())
                throw new N2oConfigAuditException("n2o.audit.commitMessageCouldNotBeEmpty");
            try {
                gitRepo.addAll();
                gitRepo.commit(message, getAuthor());
                if (StaticProperties.getBoolean("n2o.config.audit.remote.push.auto.enabled")) {
                    try {
                        gitRepo.push();
                    } catch (GitAPIException e) {
                        logger.error("Push failed", e);
                        throw new N2oConfigAuditException("n2o.audit.pushFailed", e);
                    }
                }
            } catch (Exception e) {
                gitRepo.cleanRepo();
                throw new N2oConfigAuditException("n2o.audit.repoHasNonCommittedChanges", e);
            }
        }
    }

    @Override
    public List<N2oConfigHistory> getHistory(String localPath) {
        if (localPath == null || !isEnabled())
            return Collections.emptyList();
        return gitRepo.getHistoryByPath(localPath);
    }

    @Override
    public N2oConfigConflict retrieveConflict(String localPath) {
        if (!isEnabled())
            return null;
        try {
            return retrieveConflictFromDirectory(localPath);
        } catch (Exception e) {
            logger.error("IO exception when retrive conflict", e);
            throw new N2oConfigAuditException("n2o.audit.conflictInfoReceivingException", e);
        }
    }

    @Override
    public String retrieveGraph() {
        if (!isEnabled())
            return null;
        return gitRepo.drawGraph();
    }

    @Override
    public List<N2oConfigCommit> getCommits() {
        if (!isEnabled())
            return Collections.emptyList();
        List<N2oConfigCommit> histories = new ArrayList<>();
        try {
            if (!gitRepo.isClean()) {
                Set<String> files = gitRepo.getNotCommitedFiles();
                int i = 0;
                for (String f : files) {
                    i++;
                    N2oConfigHistory history = new N2oConfigHistory();
                    history.setId("" + i);
                    history.setState(N2oConfigHistory.State.CHANGED);
                    history.setMessage(f);
                    history.setLocalPath(f);
                    histories.add(history);
                }
            }
            Iterable<RevCommit> revCommits;
            revCommits = gitRepo.getLog(N2oGitCore.SERVER_BRANCH_NAME, false);
            List<RevCommit> commits = new ArrayList<>();
            int aheadCount = gitRepo.calculateAheadCount(gitRepo.getAllRefs().get("refs/heads/" + N2oGitCore.SERVER_BRANCH_NAME),
                    gitRepo.getAllRefs().get("refs/remotes/" + N2oGitCore.REMOTE_REPO_NAME + "/" + N2oGitCore.SERVER_BRANCH_NAME));
            int ind = 0;
            revCommits.forEach(commits::add);
            for (RevCommit newCommit : commits) {
                N2oConfigCommit commit = new N2oConfigCommit();
                N2oGitUtil.mapCommit(newCommit, commit);
                if (ind < aheadCount) {
                    commit.setState(N2oConfigHistory.State.COMMITED);
                } else {
                    commit.setState(N2oConfigHistory.State.PUSHED);
                }
                ind++;
                histories.add(commit);
            }
        } catch (GitAPIException e) {
            throw new N2oConfigAuditException("Config audit history error.", e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return histories;
    }

    @Override
    public void reestablish() {
        if (!isEnabled())
            return;
        try {
            if (!gitRepo.isClean()) {
                gitRepo.cleanRepo();
            }
            if (!gitRepo.isCurrentBranch(N2oGitCore.SERVER_BRANCH_NAME)) {
                gitRepo.checkout(N2oGitCore.SERVER_BRANCH_NAME);
            }
        } catch (Exception e) {
            logger.error("Reestablish audit git Error", e);
            throw new N2oConfigAuditException("n2o.audit.repoRecoveryFailed", e);
        }
    }

    @Override
    public void updateSystem() {
        if (isEnabled()) {
            if (!gitRepo.isCurrentBranch(N2oGitCore.SERVER_BRANCH_NAME)) {
                throw new N2oConfigAuditException("n2o.audit.switchOnServerBranch");
            }
            if (!gitRepo.isClean()) {
                throw new N2oConfigAuditException("n2o.audit.cleanupRepo");
            }
            configMetadataLocker.lock();
            try {
                if (isSystemUpdated()) {
                    logger.info("System configurations not found");
                    return;
                }
                gitRepo.checkout(N2oGitCore.SYSTEM_BRANCH_NAME);
                gitRepo.pull();
                removeAllFiles();
                updateFilesFromWar();
                commitAll(N2oConfigMessage.SYSTEM_UPDATE_PREFIX + systemVersion);
                gitRepo.push();
                gitRepo.checkout(N2oGitCore.SERVER_BRANCH_NAME);
            } catch (Exception e) {
                logger.error("Не удалось обновить системные конфигурации.", e);
                reestablish();
                throw new N2oConfigAuditException("n2o.audit.systemConfigUpdateFailed", e);
            } finally {
                configMetadataLocker.unlock();
            }
        }
    }

    @Override
    public void pull() {
        if (!isEnabled())
            return;
        configMetadataLocker.lock();
        try {
            gitRepo.pull();
        } catch (Exception e) {
            gitRepo.cleanRepo();
            logger.error("Pull is failed!", e);
            throw new N2oConfigAuditException("n2o.audit.pullFailed", e);
        } finally {
            configMetadataLocker.unlock();
        }
    }

    @Override
    public void push() {
        if (!isEnabled())
            return;
        try {
            gitRepo.push();
        } catch (GitAPIException e) {
            logger.error("Push is failed!", e);
            throw new N2oConfigAuditException("n2o.audit.pushFailed", e);
        }
    }

    @Override
    public boolean isUpdatable() {
        return isUpdatable;
    }

    @Override
    public void setUpdatable(boolean updatable) {
        isUpdatable = updatable;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }


    @Override
    public String checkStatus() {
        try {
            return gitRepo.status();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void merge(String branchName, boolean isOrigin) {
        if (isEnabled()) {
            if (!gitRepo.isClean()) {
                throw new N2oConfigAuditException("Repo is not clean");
            }
            configMetadataLocker.lock();
            try {
                gitRepo.pull();
                List<DiffEntry> files = gitRepo.merge(getAuthor(), branchName, isOrigin);
                if (StaticProperties.getBoolean("n2o.config.audit.remote.push.auto.enabled")) {
                    gitRepo.push();
                }
                files.stream().forEach(f -> updateMetadata(f));
            } catch (Exception e) {
                reestablish();
                logger.error("Branch [" + branchName + "] merge failed", e);
                throw new N2oConfigAuditException("n2o.audit.mergeError", e).addData(branchName);
            } finally {
                configMetadataLocker.unlock();
            }
        }
    }

    private boolean isSystemUpdated() {
        try {
            Iterator<RevCommit> revCommits = gitRepo.getLog(N2oGitCore.SYSTEM_BRANCH_NAME, false).iterator();
            String version = "";
            while (revCommits.hasNext()) {
                RevCommit commit = revCommits.next();
                if (commit.getFullMessage().contains(N2oConfigMessage.SYSTEM_UPDATE_PREFIX.value)) {
                    version = commit.getFullMessage().substring(N2oConfigMessage.SYSTEM_UPDATE_PREFIX.value.length()).trim();
                    break;
                }
            }
            return systemVersion.equals(version);
        } catch (GitAPIException e) {
            throw new N2oConfigAuditException("Checking system updated failed!", e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String getAuthor() {
        UserContext userContext = StaticUserContext.getUserContext();//todo лучше юзера передавать в api, чем добывать здесь
        return userContext.getUsername() != null ? userContext.getUsername() : SYSTEM_AUTHOR_NAME;
    }

    private N2oConfigConflict retrieveConflictFromDirectory(String localPath) throws IOException {
        N2oConfigConflict configConflict = new N2oConfigConflict();
        N2oConfigHistory history = gitRepo.getHistoryByPath(localPath).stream()
                .filter(c -> c.getMessagePrefix().equals(N2oConfigMessage.CONFLICT_MERGE_PREFIX)).findFirst().get();
        configConflict.setContent(history.getPreviousContent().get(0));
        configConflict.setMergeContent(history.getPreviousContent().get(1));
        configConflict.setConflictContent(history.getContent());
        return N2oConfigConflictParser.restoreContentsByConflict(configConflict);
    }

    public void closeRepo() {
        if (!isEnabled() || gitRepo == null)
            return;
        gitRepo.closeRepo();
    }

    private void updateMetadata(DiffEntry diffEntry) {
        if (diffEntry.getChangeType().equals(DiffEntry.ChangeType.ADD)) {
            InfoConstructor folderInfo = RegisterUtil.createFolderInfo(
                    Node.byDirectory(new File(PathUtil.concatAbsoluteAndLocalPath(configStarter.getConfigPath(),
                            diffEntry.getNewPath())), configStarter.getConfigPath()), metaModelRegister);
            if (hasPostfix(diffEntry.getNewPath())) {
                configRegister.add(
                        folderInfo);
            }
        } else if (diffEntry.getChangeType().equals(DiffEntry.ChangeType.MODIFY)) {
            InfoConstructor folderInfo = RegisterUtil.createFolderInfo(
                    Node.byDirectory(new File(PathUtil.concatAbsoluteAndLocalPath(configStarter.getConfigPath(),
                            diffEntry.getNewPath())), configStarter.getConfigPath()), metaModelRegister);
            String filePath = diffEntry.getNewPath();
            if (hasPostfix(filePath)) {
                configRegister.update(folderInfo);
            }
        } else if (diffEntry.getChangeType().equals(DiffEntry.ChangeType.RENAME)) {
            InfoConstructor folderInfo = RegisterUtil.createFolderInfo(
                    Node.byDirectory(new File(PathUtil.concatAbsoluteAndLocalPath(configStarter.getConfigPath(),
                            diffEntry.getNewPath())), configStarter.getConfigPath()), metaModelRegister);
            configRegister.add(folderInfo);
            String filePath = diffEntry.getOldPath();
            if (hasPostfix(filePath)) {
                String metadataId = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.indexOf("."));
                String postfix = filePath.substring(filePath.indexOf(".") + 1, filePath.lastIndexOf("."));
                configRegister.remove(metadataId, metaModelRegister.get(postfix).getBaseSourceClass());
            }
        } else if (diffEntry.getChangeType().equals(DiffEntry.ChangeType.DELETE)) {
            String filePath = diffEntry.getOldPath();
            if (hasPostfix(filePath)) {
                String metadataId = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.indexOf("."));
                String postfix = filePath.substring(filePath.indexOf(".") + 1, filePath.lastIndexOf("."));
                configRegister.remove(metadataId, metaModelRegister.get(postfix).getBaseSourceClass());
            }
        }
    }

    private boolean hasPostfix(String filePath) {
        return filePath.indexOf(".") != filePath.lastIndexOf(".");
    }

    private void removeAllFiles() throws IOException {
        String path = configStarter.getConfigPath();
        FileSystemUtil.removeAllFromDirectory(path, excludeInUpdate);
    }

    private void updateFilesFromWar() throws IOException {
        List<InfoConstructor> infoConstructors = xmlScanner.scan();
        infoConstructors.stream().forEach(ic -> {
            try {
                N2oGitFileUtil.createFile(ic.getURI(), new File(PathUtil.concatAbsoluteAndLocalPath(configStarter.getConfigPath(), ic.getLocalPath())));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        logger.info("System configurations updated.");
    }


}
