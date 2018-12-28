package net.n2oapp.framework.config.audit.git.util;

import net.n2oapp.framework.config.audit.N2oConfigAuditException;
import net.n2oapp.framework.config.audit.git.N2oGitCore;
import net.n2oapp.framework.config.register.audit.model.N2oConfigCommit;
import net.n2oapp.framework.config.register.audit.model.N2oConfigHistory;
import net.n2oapp.framework.config.register.audit.model.N2oConfigMessage;
import net.n2oapp.framework.config.register.storage.PathUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static net.n2oapp.framework.config.register.audit.model.N2oConfigMessage.*;

/**
 * @author dfirstov
 * @since 16.09.2015
 */
public class N2oGitUtil {
    public static final String DEFAULT_FILE_ENCODING = "UTF-8";
    private static final String BRANCHES_PROP = "n2o.config.audit.merge.branches";
    private static final String COMMIT = "commit ";
    private static final String AUTHOR = "Author: ";
    private static final String DATE = "Date: ";
    private static final String MESSAGE = "Message: ";
    private static final String MERGE = "Merge: ";

    public static boolean isRepoExists(String repositoryPath) {
        String repoRoot = repositoryPath.endsWith("/") ? repositoryPath : repositoryPath + "/";
        File repoDir = new File(repoRoot + ".git");
        return repoDir.isDirectory() && repoDir.exists();
    }

    public static String buildMessage(String message, Git git) throws GitAPIException {
        if (hasPrefix(message))
            return message;
        Status status = git.status().call();
        if (status.getAdded().size() == 1)
            return CREATED_PREFIX + message;
        else if (status.getChanged().size() == 1)
            return UPDATED_PREFIX + message;
        else if (status.getRemoved().size() == 1)
            return DELETED_PREFIX + message;
        return message;
    }

    public static void mapCommit(RevCommit gitCommit, N2oConfigCommit n2oCommit) {
        n2oCommit.setId(gitCommit.getName());
        n2oCommit.setAuthor(gitCommit.getAuthorIdent().getName());
        n2oCommit.setDate(gitCommit.getAuthorIdent().getWhen());
        Optional<N2oConfigMessage> prefix = N2oConfigMessage.getPrefix(gitCommit.getFullMessage());
        n2oCommit.setMessagePrefix(prefix.isPresent() ? prefix.get() : null);
        n2oCommit.setMessage(N2oConfigMessage.getMessage(gitCommit.getFullMessage()));
        n2oCommit.setType(prefix.isPresent() ? N2oConfigHistory.Type.byPrefix(prefix.get()): N2oConfigHistory.Type.COMMIT);
        n2oCommit.setConflict(N2oConfigMessage.CONFLICT_MERGE_PREFIX.equals(n2oCommit.getMessagePrefix()));
    }

    public static void mapHistory(String localPath, RevCommit newCommit, List<RevCommit> oldCommits, DiffEntry diff, Repository repo, Git git, N2oConfigHistory history) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DiffFormatter formatter = new DiffFormatter(outputStream);
            formatter.setRepository(repo);
            formatter.format(diff);
            mapCommit(newCommit, history);
            history.setLocalPath(localPath);
            history.setDiff(newCommit.toString() + "\n" + new String(outputStream.toByteArray(), N2oGitUtil.DEFAULT_FILE_ENCODING));//todo не используется
            history.setContent(retrieveContent(newCommit, localPath, git));
            if (oldCommits != null && !oldCommits.isEmpty()) {
                List<String> prevContents = new ArrayList<>();
                for (RevCommit commit : oldCommits)
                    prevContents.add(retrieveContent(commit, localPath, git));
                history.setPreviousContent(prevContents);
            }
        }
    }

    public static String retrieveContent(RevCommit commitObject, String localPath, Git git) throws IOException {
        RevCommit commit = new RevWalk(git.getRepository()).parseCommit(commitObject);
        RevTree tree = commit.getTree();
        Repository repo = git.getRepository();
        TreeWalk treeWalk = new TreeWalk(repo);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        treeWalk.setFilter(TreeFilter.ANY_DIFF);
        if(!localPath.isEmpty()) {
            treeWalk.setFilter(PathFilter.create(localPath));
        }
        if (!treeWalk.next()) {
            return "";
        }
        ObjectId objectId = treeWalk.getObjectId(0);
        ObjectLoader loader = repo.open(objectId);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            loader.copyTo(out);
            return new String(out.toByteArray(), DEFAULT_FILE_ENCODING);
        }
    }

    public static List<String> drawGraph(Git git) {
        List<String> graph = new ArrayList<>();
        try {
            Iterator<RevCommit> commits = git.log().all().call().iterator();
            RevCommit developCommit = commits.next();
            boolean isForked = false;
            while (developCommit != null) {
                if (developCommit.getParentCount() > 1) {
                    developCommit = createFork(graph, developCommit, isForked, git);
                    isForked = developCommit != null;
                    while (developCommit != null && developCommit.getParentCount() == 1) {
                        graph.add("* | " + COMMIT + developCommit.getName());
                        graph.add("| | " + AUTHOR + developCommit.getAuthorIdent().getName());
                        addSimpleForkedBlock(graph, developCommit);
                        developCommit = getParentRevCommit(developCommit, 0, git);
                    }
                } else if (!isForked) {
                    graph.add("*   " + COMMIT + developCommit.getName());
                    graph.add("|   " + AUTHOR + developCommit.getAuthorIdent().getName());
                    graph.add("|   " + DATE + developCommit.getAuthorIdent().getWhen());
                    graph.add("|   " + MESSAGE + developCommit.getFullMessage());
                    graph.add("|");
                    developCommit = getParentRevCommit(developCommit, 0, git);
                }
                if (developCommit != null && developCommit.getParentCount() == 0) {
                    graph.add("*   " + COMMIT + developCommit.getName());
                    graph.add("    " + AUTHOR + developCommit.getAuthorIdent().getName());
                    graph.add("    " + DATE + developCommit.getAuthorIdent().getWhen());
                    graph.add("    " + MESSAGE + developCommit.getFullMessage());
                    developCommit = getParentRevCommit(developCommit, 0, git);
                }
            }
        } catch (GitAPIException e) {
            throw new N2oConfigAuditException("n2o.audit.couldNotGetGitTree", e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return graph;
    }

    public static N2oConfigCommit mapMessageBranch(N2oConfigCommit commit) {
        if (N2oConfigHistory.Type.MERGE.equals(commit.getType())) {
            String branchName = commit.getMessage();
            if (branchName.equals(N2oGitCore.SYSTEM_BRANCH_NAME)) {
                commit.setMessage("n2o.audit.updateMerged");
            } else {
                String name = BRANCHES_PROP + "." + branchName;
                commit.setMessage(name == null ? branchName : name);
            }
        }
        return commit;
    }

    private static RevCommit createFork(List<String> graph, RevCommit developCommit, boolean isForked, Git git) throws IOException {
        graph.add((isForked ? "* |  " : "*   ") + COMMIT + developCommit.getName());
        graph.add((isForked ? "|\\ \\ " : "|\\  ") + MERGE + getParentRevCommit(developCommit, 0, git).getName().substring(0, 6) + " " + getParentRevCommit(developCommit, 1, git).getName().substring(0, 6));
        graph.add((isForked ? "| |/" : "| |") + " " + AUTHOR + developCommit.getAuthorIdent().getName());
        addSimpleForkedBlock(graph, developCommit);
        RevCommit masterCommit = getParentRevCommit(developCommit, 1, git);
        RevCommit parentMasterCommit = getParentRevCommit(developCommit, 0, git);
        if (parentMasterCommit.getParentCount() > 0) {
            graph.add("| * " + COMMIT + masterCommit.getName());
            graph.add("| | " + AUTHOR + masterCommit.getAuthorIdent().getName());
            addSimpleForkedBlock(graph, masterCommit);
        } else {
            graph.add("| * " + COMMIT + masterCommit.getName());
            graph.add("|/  " + AUTHOR + masterCommit.getAuthorIdent().getName());
            graph.add("|   " + DATE + masterCommit.getAuthorIdent().getWhen());
            graph.add("|   " + MESSAGE + masterCommit.getFullMessage());
            graph.add("|");
        }
        developCommit = getParentRevCommit(developCommit, 0, git);
        return developCommit;
    }

    private static void addSimpleForkedBlock(List<String> graph, RevCommit masterCommit) {
        graph.add("| | " + DATE + masterCommit.getAuthorIdent().getWhen());
        graph.add("| | " + MESSAGE + masterCommit.getFullMessage());
        graph.add("| |");
    }

    private static RevCommit getParentRevCommit(RevCommit commit, int parentId, Git git) throws IOException {
        if (commit.getParents() == null || commit.getParentCount() == 0)
            return null;
        ObjectId commitId = git.getRepository().resolve(commit.getParent(parentId).getName());
        RevWalk revWalk = new RevWalk(git.getRepository());
        commit = revWalk.parseCommit(commitId);
        return commit;
    }
}
