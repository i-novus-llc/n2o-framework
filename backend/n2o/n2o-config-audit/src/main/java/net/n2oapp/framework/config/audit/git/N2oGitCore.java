package net.n2oapp.framework.config.audit.git;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.config.audit.N2oConfigAuditException;
import net.n2oapp.framework.config.audit.git.util.N2oGitFileUtil;
import net.n2oapp.framework.config.audit.git.util.N2oGitUtil;
import net.n2oapp.framework.config.register.audit.model.N2oConfigHistory;
import net.n2oapp.framework.config.register.audit.model.N2oConfigMessage;
import net.n2oapp.properties.StaticProperties;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.RevWalkUtils;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static net.n2oapp.context.StaticSpringContext.getBean;
import static net.n2oapp.framework.config.audit.git.util.N2oGitUtil.*;

/**
 * Класс для работы c git репозиторием, используется для работы с конфигурациями в git
 */
public class N2oGitCore {
    private static final Logger logger = LoggerFactory.getLogger(N2oGitCore.class);
    private static final String AUTHOR_EMAIL = "i-novus.ru";
    private static final String INTERRUPTED_EXCEPTION_MESSAGE = "InterruptedException for ReentrantLock in GitCore!";
    private static final String TIMEOUT_EXCEPTION_MESSAGE = "TimeoutException for ReentrantLock in GitCore!";
    private static final String DEFAULT_LINE_END = "\n";
    private static final long TIMEOUT = 10;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final String START_LOCAL_BRANCH_NAME = "refs/heads/";
    private static final String START_REMOTE_BRANCH_NAME = "refs/remotes/";
    private static final int LOG_MAX_COUNT = 100000;
    public static String SERVER_BRANCH_NAME;
    public static String SYSTEM_BRANCH_NAME;
    public static String REMOTE_REPO_NAME;
    private static String REMOTE_REPO_URI;
    private static String REMOTE_REPO_USERNAME;
    private static String REMOTE_REPO_PASSWORD;
    private static String REMOTE_REPO_SSH_KEY;
    private static Boolean REMOTE_REPO_AUTH_NEED;
    private final Lock lock = new ReentrantLock();
    private CredentialsProvider credentialsProvider;
    private Git git;

    public N2oGitCore() {
        SERVER_BRANCH_NAME = StaticProperties.get("n2o.config.audit.branch");
        SYSTEM_BRANCH_NAME = "system/" + StaticProperties.get("n2o.config.audit.branch");
        REMOTE_REPO_NAME = StaticProperties.get("n2o.config.audit.remote.name");
        REMOTE_REPO_URI = StaticProperties.get("n2o.config.audit.remote.uri");
        REMOTE_REPO_AUTH_NEED = StaticProperties.getBoolean("n2o.config.audit.remote.auth.enabled");
        REMOTE_REPO_USERNAME = StaticProperties.get("n2o.config.audit.remote.username");
        REMOTE_REPO_PASSWORD = StaticProperties.get("n2o.config.audit.remote.password");
        REMOTE_REPO_SSH_KEY = StaticProperties.get("n2o.config.audit.remote.sshkey");
        if (REMOTE_REPO_AUTH_NEED) {
            if (REMOTE_REPO_URI.contains("ssh")) {
                credentialsProvider = new CredentialsProvider() {
                    @Override
                    public boolean supports(CredentialItem... items) {
                        return true;
                    }

                    @Override
                    public boolean isInteractive() {
                        return true;
                    }

                    @Override
                    public boolean get(URIish uri, CredentialItem... items)
                            throws UnsupportedCredentialItem {
                        for (CredentialItem item : items) {
                            if (item instanceof CredentialItem.StringType) {
                                ((CredentialItem.StringType) item).
                                        setValue(new String(REMOTE_REPO_PASSWORD));
                                continue;
                            }
                        }
                        return true;
                    }
                };
            } else {
                credentialsProvider = new UsernamePasswordCredentialsProvider(REMOTE_REPO_USERNAME, REMOTE_REPO_PASSWORD);
            }
        }
    }

    /**
     * <p>Создает репозиторий</p>
     * <p>Если репозиторий создан ранее, инициализирует его.</p>
     * <p>аналог команды ({@code git init})</p>
     */
    public void initRepo(String repositoryPath) throws GitAPIException, IOException {
        if (!isPathValid(repositoryPath))
            throw new N2oConfigAuditException("n2o.audit.wrongRepoPath");
        if (SERVER_BRANCH_NAME == null || SERVER_BRANCH_NAME.trim().isEmpty())
            throw new N2oConfigAuditException("n2o.audit.propertyIsNotConfigured").addData("n2o.config.audit.branch");
        if (REMOTE_REPO_URI == null || REMOTE_REPO_URI.trim().isEmpty())
            throw new N2oConfigAuditException("n2o.audit.propertyIsNotConfigured").addData("n2o.config.audit.remote.uri");
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                configureSsh();
                if (isRepoExists(repositoryPath)) {
                    git = Git.open(new File(repositoryPath));
                    if (!isCurrentBranch(SERVER_BRANCH_NAME))
                        throw new N2oConfigAuditException("n2o.audit.auditInitError").addData(SERVER_BRANCH_NAME);
                    pull();
                } else {
                    File repoDir = new File(repositoryPath);
                    if (repoDir.isDirectory() && repoDir.listFiles() != null && repoDir.listFiles().length > 0) {
                        throw new N2oConfigAuditException("n2o.audit.auditInitError").addData(repositoryPath);
                    }
                    CloneCommand cloneCommand = Git.cloneRepository().setDirectory(repoDir).setRemote(REMOTE_REPO_NAME)
                            .setURI(REMOTE_REPO_URI);
                    if (REMOTE_REPO_AUTH_NEED) {
                        cloneCommand.setCredentialsProvider(credentialsProvider);
                    }
                    git = cloneCommand.call();
                    fetch();
                    if (git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call().isEmpty()) {
                        File file = new File(repositoryPath + "/.gitignore");
                        URL resource = N2oGitCore.class.getClassLoader().getResource("net/n2oapp/framework/config/audit/template/gitignore-template");
                        if (resource != null) {
                            N2oGitFileUtil.createFile(resource.toString(), file);
                            add(".gitignore");
                        }
                        commit(N2oConfigMessage.INIT_COMMIT_PREFIX + " init ", "system");
                        push();
                    }
                    if (!SERVER_BRANCH_NAME.equals("master")) {
                        createOrCheckout(SERVER_BRANCH_NAME);
                    }
                    createOrCheckout(SYSTEM_BRANCH_NAME);
                    git.checkout().setName(SERVER_BRANCH_NAME).call();
                    git.pull().setRemoteBranchName(SERVER_BRANCH_NAME).setRemote(REMOTE_REPO_NAME).call();
                }
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }

    private void configureSsh() {
        SshSessionFactory.setInstance(new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host hc, Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(REMOTE_REPO_PASSWORD);
            }

            @Override
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                JSch defaultJSch = super.createDefaultJSch(fs);
                defaultJSch.addIdentity(REMOTE_REPO_SSH_KEY);
                return defaultJSch;
            }
        });
    }

    private void createOrCheckout(String branchName) throws IOException, GitAPIException {
        if (git.getRepository().findRef(REMOTE_REPO_NAME + "/" + branchName) == null) {
            git.checkout().setCreateBranch(true).setName(branchName).call();
            push();
        } else {
            git.checkout().setCreateBranch(true).setName(branchName)
                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                    .setStartPoint(REMOTE_REPO_NAME + "/" + branchName).call();
            git.pull().setRemoteBranchName(branchName).setRemote(REMOTE_REPO_NAME).call();
        }
    }

    private static boolean isPathValid(String repositoryPath) {
        return !repositoryPath.contains("src/main/resources");
    }

    /**
     * Определяет текущую ветку.
     *
     * @param branchName название ветки
     * @return true - branchName текущая ветка, false - branchName не текущая ветка
     * throws IOException не найден git репозиторий
     */
    public boolean isCurrentBranch(String branchName) {
        try {
            return branchName.equals(git.getRepository().getBranch());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Добавляет файл в git индекс по локальному пути {@code git add --path @param path}
     *
     * @param path локальный путь к файлу
     */
    public void add(String path) throws GitAPIException {
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                git.add().addFilepattern(path).call();
                git.add().setUpdate(true).addFilepattern(path).call();
                logger.debug("add " + path);
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Добавляет все файлы в git индекс
     */
    public void addAll() throws GitAPIException {
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                git.add().addFilepattern(".").call();//stage добавленных файлов
                git.add().setUpdate(true).addFilepattern(".").call();//stage удаленных файлов
                logger.debug("add all");
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }


    /**
     * Делает коммит вне зависимости от изменений.
     *
     * @param message сообщение коммита
     * @param author  автор коммита
     */
    public List<DiffEntry> commit(String message, String author) throws GitAPIException, IOException {
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                message = buildMessage(message, git);
                PersonIdent personIdent = new PersonIdent(author == null ? "undefined" : author, AUTHOR_EMAIL);
                RevCommit commit = git.commit().setMessage(message).setAuthor(personIdent).setCommitter(personIdent).call();
                logger.info("commit " + message);
                return getCommitFiles(commit);
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Определяет наличие изменений в репозитории.
     *
     * @return true - в репозитории нет новых, измененных, удаленных, непроиндексированных файлов, false - обратное
     */
    public Boolean isClean() {
        try {
            if (git == null)
                return false;
            Status statusCall = git.status().call();
            return statusCall.getAdded().isEmpty()
                    && statusCall.getChanged().isEmpty()
                    && statusCall.getModified().isEmpty()
                    && statusCall.getRemoved().isEmpty()
                    && statusCall.getMissing().isEmpty()
                    && statusCall.getConflicting().isEmpty()
                    && statusCall.getUntracked().isEmpty();
        } catch (GitAPIException e) {
            throw new N2oConfigAuditException(e);
        }
    }

    /**
     * Возвращает строку с описанием состояния репозитория
     *
     * @return строка с описанием состояния
     */
    public String status() throws IOException {
        try {
            if (git == null)
                return null;
            Status statusCall = git.status().call();
            long count = 0;
            count = Files.walk(git.getRepository().getWorkTree().toPath()).filter(p -> {
                String path = p.toAbsolutePath().toString();
                return !path.contains(".git") && !path.contains(".n2o_git_stash") && (path.endsWith(".xml") || path.endsWith(".groovy"));
            }).count();
            String countString = "Files: " + count;
            return countString + " " +
                    "Added: " + statusCall.getAdded().size() + " " +
                    "Changed: " + statusCall.getChanged().size() + " " +
                    "Modified: " + statusCall.getModified().size() + " " +
                    "Removed: " + statusCall.getRemoved().size() + " " +
                    "Untracked: " + statusCall.getUntracked().size() + " " +
                    "Conflicts: " + statusCall.getConflicting().size() + " "
                    + "isClean: " + isClean() + " ";
        } catch (GitAPIException e) {
            throw new N2oConfigAuditException(e);
        }
    }

    /**
     * Получение истории изменений в ветке
     */
    public Iterable<RevCommit> getLog(String branchName, boolean isRemote) throws GitAPIException, IOException {
        LogCommand command = git.log().setMaxCount(LOG_MAX_COUNT);
        String fullBranchName;
        if (isRemote) {
            fullBranchName = branchName.startsWith(START_REMOTE_BRANCH_NAME) ? branchName : START_REMOTE_BRANCH_NAME + branchName;
        } else {
            fullBranchName = branchName.startsWith(START_LOCAL_BRANCH_NAME) ? branchName : START_LOCAL_BRANCH_NAME + branchName;
        }
        if (!isBranchExist(fullBranchName, isRemote)) {
            throw new N2oException("n2o.audit.branchIsNotExists").addData(fullBranchName);
        }
        command.add(git.getRepository().resolve(fullBranchName));
        return command.call();
    }

    /**
     * Получение всей истории изменений
     */
    public Iterable<RevCommit> getAllLog() throws GitAPIException, IOException {
        return git.log().setMaxCount(LOG_MAX_COUNT).all().call();
    }

    /**
     * Получение истории изменений по определенному пути
     *
     * @param path путь
     */
    public Iterable<RevCommit> getLogByPath(String path) throws GitAPIException {
        return git.log().setRevFilter(RevFilter.ALL).setMaxCount(LOG_MAX_COUNT).addPath(path).call();
    }

    /**
     * Получение всех ссылок репозитория
     */
    public Map<String, Ref> getAllRefs() {
        return git.getRepository().getAllRefs();
    }

    public String drawGraph() {
        return N2oGitUtil.drawGraph(git)
                .stream()
                .reduce("", (s1, s2) -> s1 + DEFAULT_LINE_END + s2);
    }

    /**
     * закрывает git репозиторий и все ресурсы связанные с его использованием
     */
    public void closeRepo() {
        if (git == null)
            return;
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                git.close();
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Сливает удаленную системную ветку в локальную серверную
     */
    public List<DiffEntry> merge(String author, String branchName, boolean isOrigin) throws IOException, GitAPIException {
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                if (!isCurrentBranch(SERVER_BRANCH_NAME)) {
                    throw new N2oConfigAuditException("n2o.audit.switchOnServerBranch");
                }
                fetch();
                String brName = isOrigin ? REMOTE_REPO_NAME + "/" + branchName : branchName;
                // merge делаем без коммита, потому что нет возможности иначе указать автора
                MergeCommand mergeCommand = git.merge().include(git.getRepository().getRef(brName))
                        .setFastForward(MergeCommand.FastForwardMode.NO_FF).setCommit(false);
                String strategy = StaticProperties.get("n2o.config.audit.merge.conflict_mode");
                if (!strategy.equalsIgnoreCase("manual")) {
                    mergeCommand.setStrategy(MergeStrategy.get(strategy.toLowerCase()));
                }
                MergeResult mergeResult = mergeCommand.call();
                List<DiffEntry> result;
                if (mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.ALREADY_UP_TO_DATE)) {
                    result = new ArrayList<>();
                } else {
                    git.add().addFilepattern(".").call();
                    String message;
                    if (mergeResult.getConflicts() == null || mergeResult.getConflicts().isEmpty()) {
                        message = N2oConfigMessage.MERGE_PREFIX.value + branchName;
                    } else {
                        message = N2oConfigMessage.CONFLICT_MERGE_PREFIX.value + branchName;
                    }
                    result = commit(message, author);
                }
                logger.info("merge " + branchName);
                return result;
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Переключение на ветку
     *
     * @param branchName имя ветки
     * @throws GitAPIException
     */
    public void checkout(String branchName) throws GitAPIException, IOException {
        if (branchName == null || branchName.isEmpty())
            return;
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                if (git.getRepository().getRef(branchName) == null) {
                    git.checkout().setCreateBranch(true).setName(branchName).call();
                } else {
                    git.checkout().setName(branchName).call();
                }
                logger.info("checkout " + branchName);
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Удаление всех незакоммиченных файлов из рабочей директории
     */
    public void cleanRepo() {
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                if (!isClean()) {
                    git.reset().setRef("HEAD").setMode(ResetCommand.ResetType.HARD).call();
                    git.clean().setCleanDirectories(true).call();
                    logger.info("clear repo");
                }
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (GitAPIException e) {
            throw new N2oConfigAuditException(e);
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Удаление незакоммиченных файлов по пути localPaths
     */
    public void resetFiles(Set<String> localPaths) {
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                if (!isClean()) {
                    git.clean().setCleanDirectories(true).setPaths(localPaths).call();
                    StringBuilder str = new StringBuilder();
                    localPaths.stream().forEach(s -> str.append(s).append("; "));
                    logger.info("clear files from repo: " + str.toString());
                }
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (GitAPIException e) {
            throw new N2oConfigAuditException(e);
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }

    public void pull() throws GitAPIException {
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                PullCommand pc = git.pull();
                if (REMOTE_REPO_AUTH_NEED) {
                    pc.setCredentialsProvider(credentialsProvider);
                }
                pc.call();
                logger.info("pull");
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }

    public void push() throws GitAPIException {
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                if (!isClean()) {
                    throw new N2oConfigAuditException("You try push, but your repository isn't clean");
                }
                PushCommand pc = git.push();
                if (REMOTE_REPO_AUTH_NEED) {
                    pc.setCredentialsProvider(credentialsProvider);
                }
                pc.call();
                logger.info("push");
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }

    public int calculateAheadCount(Ref local, Ref tracking) throws IOException {
        RevWalk walk = new RevWalk(git.getRepository());
        try {
            RevCommit localCommit = walk.parseCommit(local.getObjectId());
            RevCommit trackingCommit = walk.parseCommit(tracking.getObjectId());
            walk.setRevFilter(RevFilter.MERGE_BASE);
            walk.markStart(localCommit);
            walk.markStart(trackingCommit);
            RevCommit mergeBase = walk.next();
            walk.reset();
            walk.setRevFilter(RevFilter.ALL);
            return RevWalkUtils.count(walk, localCommit, mergeBase);
        } finally {
            walk.dispose();
        }
    }

    public Set<String> getNotCommitedFiles() throws IOException {
        try {
            Status statusCall = git.status().call();
            Set<String> result = new HashSet<>();
            statusCall.getAdded().stream().forEach(f -> result.add(f));
            statusCall.getChanged().stream().forEach(f -> result.add(f));
            statusCall.getRemoved().stream().forEach(f -> result.add(f));
            statusCall.getMissing().stream().forEach(f -> result.add(f));
            statusCall.getModified().stream().forEach(f -> result.add(f));
            statusCall.getUntracked().stream().forEach(f -> result.add(f));
            statusCall.getConflicting().stream().forEach(f -> result.add(f));
            return result;
        } catch (GitAPIException e) {
            throw new N2oConfigAuditException(e);
        }
    }

    public List<String> getAllBranchNames(boolean isRemote) {
        try {
            ListBranchCommand command = git.branchList();
            if (isRemote) {
                command.setListMode(ListBranchCommand.ListMode.REMOTE);
            }
            List<Ref> branches = command.call();
            if (branches == null)
                return new ArrayList<>();
            if (isRemote) {
                int c = (START_REMOTE_BRANCH_NAME + REMOTE_REPO_NAME + "/").length();
                return branches.stream().map(b -> b.getName().substring(c)).collect(Collectors.toList());
            } else {
                return branches.stream().map(b -> b.getName().substring(START_LOCAL_BRANCH_NAME.length())).collect(Collectors.toList());
            }
        } catch (GitAPIException e) {
            throw new N2oConfigAuditException(e);
        }
    }

    public List<N2oConfigHistory> getHistoryByPath(String localPath) {
        List<N2oConfigHistory> histories = new ArrayList<>();
        try {
            Iterable<RevCommit> revCommits = getLogByPath(localPath);
            List<RevCommit> commits = new ArrayList<>();
            revCommits.forEach(commits::add);
            if (!commits.isEmpty()) {
                Repository repo = git.getRepository();
                for (int i = 0; i < commits.size() - 1; i++) {
                    RevCommit newCommit = commits.get(i);
                    List<RevCommit> oldCommits;
                    if (i == commits.size() - 2) {
                        oldCommits = Collections.singletonList(commits.get(i + 1));
                    } else {
                        oldCommits = Arrays.asList(newCommit.getParents());
                    }
                    AbstractTreeIterator oldTreeParser = oldCommits.isEmpty() ? new EmptyTreeIterator() : prepareTreeParser(repo, oldCommits.get(0).getName());
                    AbstractTreeIterator newTreeParser = prepareTreeParser(repo, newCommit.getName());
                    createHistoryRow(oldTreeParser, newTreeParser, oldCommits, newCommit, repo, histories, localPath);
                }
                RevCommit newCommit = commits.get(((ArrayList) commits).size() - 1);
                AbstractTreeIterator oldTreeParser = new EmptyTreeIterator();
                AbstractTreeIterator newTreeParser = prepareTreeParser(repo, newCommit.getName());
                createHistoryRow(oldTreeParser, newTreeParser, null, newCommit, repo, histories, localPath);
            }
        } catch (GitAPIException e) {
            throw new N2oConfigAuditException("Config audit history error.", e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return histories;
    }

    private void fetch() throws GitAPIException {
        try {
            if (lock.tryLock(TIMEOUT, TIME_UNIT)) {
                FetchCommand fetchCommand = git.fetch();
                if (REMOTE_REPO_AUTH_NEED) {
                    fetchCommand.setCredentialsProvider(credentialsProvider);
                }
                fetchCommand.call();
            } else {
                throw new N2oConfigAuditException(TIMEOUT_EXCEPTION_MESSAGE);
            }
        } catch (InterruptedException e) {
            throw new N2oConfigAuditException(INTERRUPTED_EXCEPTION_MESSAGE);
        } finally {
            lock.unlock();
        }
    }

    private List<DiffEntry> getCommitFiles(RevCommit commit) throws IOException {
        RevWalk rw = new RevWalk(git.getRepository());
        RevCommit realParent = commit.getParentCount() > 0 ? commit.getParent(0) : commit;
        RevCommit parent = rw.parseCommit(realParent.getId());
        DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
        df.setRepository(git.getRepository());
        df.setDiffComparator(RawTextComparator.DEFAULT);
        df.setDetectRenames(true);
        return df.scan(parent.getTree(), commit.getTree());
    }

    private void createHistoryRow(AbstractTreeIterator oldTreeParser, AbstractTreeIterator newTreeParser,
                                  List<RevCommit> oldCommits, RevCommit newCommit, Repository repo, List<N2oConfigHistory> histories,
                                  String localPath) throws GitAPIException, IOException {
        List<DiffEntry> diff = git.diff().setOldTree(oldTreeParser).setNewTree(newTreeParser).setPathFilter(PathFilter.create(localPath)).call();
        for (DiffEntry entry : diff) {
            fillHistory(histories, entry, localPath, newCommit, oldCommits, repo);
        }
    }

    private void fillHistory(List<N2oConfigHistory> histories, DiffEntry entry, String localPath, RevCommit newCommit,
                             List<RevCommit> oldCommits, Repository repo) throws IOException {
        N2oConfigHistory history = new N2oConfigHistory();
        mapHistory(localPath, newCommit, oldCommits, entry, repo, git, history);
        histories.add(history);
    }

    private AbstractTreeIterator prepareTreeParser(Repository repo, String ref) throws IOException {
        RevWalk walk = new RevWalk(repo);
        ObjectId objectId = repo.resolve(ref);
        RevCommit commit = walk.parseCommit(objectId);
        RevTree tree = walk.parseTree(commit.getTree().getId());
        CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
        try (ObjectReader oldReader = repo.newObjectReader()) {
            oldTreeParser.reset(oldReader, tree.getId());
        }
        walk.dispose();
        return oldTreeParser;
    }

    private boolean isBranchExist(String fullBranchName, boolean isRemote) throws GitAPIException {
        ListBranchCommand command = git.branchList();
        if (isRemote) {
            command.setListMode(ListBranchCommand.ListMode.REMOTE);
        }
        return command.call().stream().filter(br -> br.getName().equals(fullBranchName)).findFirst().isPresent();
    }
}
