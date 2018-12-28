package net.n2oapp.framework.config.audit.git;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.config.audit.N2oConfigAuditException;
import net.n2oapp.framework.config.audit.git.util.N2oGitFileUtil;
import net.n2oapp.framework.config.audit.git.util.model.N2oGitTestEnv;
import net.n2oapp.framework.config.register.ConfigId;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.register.audit.model.N2oConfigConflict;
import net.n2oapp.framework.config.register.audit.model.N2oConfigHistory;
import net.n2oapp.framework.config.register.audit.model.N2oConfigMessage;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static net.n2oapp.framework.config.audit.git.util.N2oGitTestUtil.addToConfReg;
import static net.n2oapp.framework.config.audit.git.util.N2oGitTestUtil.generateStorageFile;
import static net.n2oapp.framework.config.audit.git.util.N2oGitTestUtil.iniTestEnv;
import static net.n2oapp.framework.config.audit.git.util.N2oGitTestUtil.resolveURI;
import static net.n2oapp.framework.config.register.audit.model.N2oConfigMessage.CREATED_PREFIX;
import static net.n2oapp.framework.config.register.audit.model.N2oConfigMessage.UPDATED_PREFIX;
import static net.n2oapp.framework.config.register.audit.model.N2oConfigMessage.DELETED_PREFIX;

/**
 * Тест работы аудита и гит
 * Для теста необходимо локально создать удаленный репозиторий и путь к нему указать в настройках
 * N2oGitTestProperties настройка  n2o.config.audit.remote.uri
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class N2oGitAuditTest {
    private static N2oGitTestEnv env;

    @Before
    public void init() throws IOException, GitAPIException {
        env = iniTestEnv();
    }

    /**
     * проверка инициализации git репозитория
     */
    @Test
    public void test1Init() throws Exception {
        //случай когда папка conf не пуста и в ней нет .git
        String confDir = env.getTestProperties().getProperty("n2o.config.path");
        String localPath = "mock.object.xml";
        File testFile = new File(confDir + localPath);
        testFile.createNewFile();
        try {
            env.getConfigAuditGit().init();
            Assert.fail();
        } catch (N2oConfigAuditException e) {
        }
        testFile.delete();
        //случай пустой папки conf
        env.getConfigAuditGit().init();
        File gitDir = new File(confDir + "/.git");
        Assert.assertTrue(gitDir.exists());
        Assert.assertTrue(env.getGitCore().isCurrentBranch(env.getServerBranchName()));
        env.getGitCore().closeRepo();
        //случай когда git репозиторий уже инициализирован
        env.getConfigAuditGit().init();
        Assert.assertTrue(env.getGitCore().isCurrentBranch(env.getServerBranchName()));
        env.getGitCore().checkout(env.getSystemBranchName());
        env.getGitCore().closeRepo();
        // проверка что при старте выбрана серверная ветка
        try {
            env.getConfigAuditGit().init();
            Assert.fail();
        } catch (N2oException e) {
            Assert.assertTrue(e instanceof N2oException);
        }
        Git git = Git.open(new File(confDir));
        git.checkout().setName(env.getServerBranchName()).call();
    }

    /**
     * проверка добавления, изменения, удаления xml файлов через админку
     * и проверка истории по данному файлу
     */
    @Test
    public void test2CommitAndHistory() throws Exception {
        env.getConfigAuditGit().init();
        //добавление нового файла
        String localPath = "page/page3.page.xml";
        addToConfReg(localPath, true);
        N2oGitFileUtil.createFile(resolveURI(localPath), generateStorageFile(localPath));
        env.getConfigAuditGit().commit(localPath, CREATED_PREFIX.toString() + localPath);
        Iterator<RevCommit> iterator = env.getGitCore().getLog(env.getServerBranchName(), false).iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next().getShortMessage().startsWith(CREATED_PREFIX.value));
        //изменение файла
        N2oGitFileUtil.createFile(resolveURI("page/page2.page.xml"), generateStorageFile(localPath));
        env.getConfigAuditGit().commit(localPath, UPDATED_PREFIX.toString() + localPath);
        iterator = env.getGitCore().getLog(env.getServerBranchName(), false).iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next().getShortMessage().startsWith(UPDATED_PREFIX.value));
        //удаление файла
        Assert.assertTrue(generateStorageFile(localPath).delete());
        env.getConfigAuditGit().commit(localPath, DELETED_PREFIX.toString() + localPath);
        iterator = env.getGitCore().getLog(env.getServerBranchName(), false).iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next().getShortMessage().startsWith(DELETED_PREFIX.value));
        List<N2oConfigHistory> history = env.getConfigAuditGit().getHistory(localPath);
        Assert.assertTrue(history.size() == 3);
    }

    /**
     * проверка функции мержа системной и серверной ветки
     */
    @Test
    public void test3Merge() throws Exception {
        SelectiveReader reader = new SelectiveStandardReader().addObjectReader();
        String objPath = "object/object1.object.xml";
        String objPath2 = "object/object2.object.xml";
        env.getConfigAuditGit().init();
        updateWorkRepo();
        // случай 1. изменений нет
        RevCommit prevCommit = env.getGitCore().getLog(env.getServerBranchName(), false).iterator().next();
        env.getConfigAuditGit().merge(env.getSystemBranchName(), true);
        RevCommit commit = env.getGitCore().getLog(env.getServerBranchName(), false).iterator().next();
        Assert.assertTrue(commit.getFullMessage().equals(prevCommit.getFullMessage()));

        // случай 2. изменнения в системной ветке есть, в серверной нет
        //создаем файл в ветке system
        saveFileFromTo(objPath, objPath, env.getWorkRepoGit(), env.getSystemBranchName(), "create object1");
        saveFileFromTo(objPath2, objPath, env.getWorkRepoGit(), env.getSystemBranchName(), "create object2");
        env.getWorkRepoGit().push().setPushAll().call();
        // мержим удаленную ветку system в локальную ветку server в репозитории с конфигурациями. проверяем, что новый файл появился в репозитории
        env.getConfigAuditGit().merge(env.getSystemBranchName(), true);
        Iterator<RevCommit> iterator = env.getGitCore().getLog(env.getServerBranchName(), false).iterator();
        Assert.assertTrue(iterator.next().getFullMessage().startsWith(N2oConfigMessage.MERGE_PREFIX.value));

        // случай 3. конфликтыне изменения
        // случай 3.1 стратегия мержа recursive (настройка manual)
        saveFileFromTo(objPath, "object/object2.object.xml", env.getWorkRepoGit(), env.getSystemBranchName(), "update object1");
        env.getWorkRepoGit().push().setPushAll().call();
        saveFileFromTo(objPath, "object/object3.object.xml", "update object1");
        env.getConfigAuditGit().merge(env.getSystemBranchName(), true);
        //todo сделать утилиту проверяющую наличие конфликтынх файлов и использовать ее тут
        //Assert.assertTrue(env.getGitCore().getGit().status().call().getConflicting().size() == 1);
        N2oConfigConflict conflict = env.getConfigAuditGit().retrieveConflict(objPath);
        assertConflict(conflict);

        // случай 3.2 стратегия мержа theirs
        env.getTestProperties().getProperties().setProperty("n2o.config.audit.merge.conflict_mode", "theirs");
        saveFileFromTo(objPath2, "object/object4.object.xml", env.getWorkRepoGit(), env.getSystemBranchName(), "update object2");
        env.getWorkRepoGit().push().setPushAll().call();
        saveFileFromTo(objPath2, "object/object5.object.xml", "update object2");
        env.getConfigAuditGit().merge(env.getSystemBranchName(), true);
        iterator = env.getGitCore().getLog(env.getServerBranchName(), false).iterator();
        Assert.assertTrue(iterator.next().getFullMessage().startsWith(N2oConfigMessage.MERGE_PREFIX.value));
        XmlInfo objectInfo = (XmlInfo) env.getConfReg().get("object2", N2oObject.class);
        N2oObject object1 = reader.readByURI(objectInfo.getURI());
        Assert.assertTrue(object1.getName().equals("object4"));

        // случай 3.3 стратегия мержа ours
        env.getTestProperties().getProperties().setProperty("n2o.config.audit.merge.conflict_mode", "ours");
        saveFileFromTo(objPath2, "object/object1.object.xml", env.getWorkRepoGit(), env.getSystemBranchName(), "update object2");
        env.getWorkRepoGit().push().setPushAll().call();
        saveFileFromTo(objPath2, "object/object2.object.xml", "update object");
        env.getConfigAuditGit().merge(env.getSystemBranchName(), true);
        iterator = env.getGitCore().getLog(env.getServerBranchName(), false).iterator();
        Assert.assertTrue(iterator.next().getFullMessage().startsWith(N2oConfigMessage.MERGE_PREFIX.value));
        env.getConfReg().update(new XmlInfo("object2", N2oObject.class, "object2.object.xml", ""));
        objectInfo = (XmlInfo) env.getConfReg().get("object2", N2oObject.class);
        object1 = reader.readByURI(objectInfo.getURI());
        Assert.assertTrue(object1.getName().equals("object2"));
    }


    /**
     * тесты обновления метаданных в ConfigRegister после мержа
     */
    @Test
    public void test4UpdateMetadataAfterMerge() throws Exception {
        SelectiveReader reader = new SelectiveStandardReader().addObjectReader();
        env.getConfigAuditGit().init();
        updateWorkRepo();
        // случай 1. добавление метаданной
        Assert.assertFalse(env.getConfReg().contains("object11", N2oObject.class));
        saveFileFromTo("object/object11.object.xml", "object/object1.object.xml", env.getWorkRepoGit(), env.getSystemBranchName(), "create object11");
        env.getWorkRepoGit().push().setPushAll().call();
        env.getConfigAuditGit().merge(env.getSystemBranchName(), true);
        Assert.assertTrue(env.getConfReg().contains("object11", N2oObject.class));
        // случай 2. изменение метаданной
        XmlInfo object11Info = (XmlInfo) env.getConfReg().get("object11", N2oObject.class);
        N2oObject object11 = reader.readByURI(object11Info.getURI());
        Assert.assertTrue(object11.getName().equals("object1"));
        saveFileFromTo("object/object11.object.xml", "object/object2.object.xml", env.getWorkRepoGit(), env.getSystemBranchName(), "update object11");
        env.getWorkRepoGit().push().setPushAll().call();
        env.getConfigAuditGit().merge(env.getSystemBranchName(), true);
        XmlInfo object11newInfo = (XmlInfo) env.getConfReg().get("object11", N2oObject.class);
        N2oObject object11new = reader.readByURI(object11newInfo.getURI());
        Assert.assertTrue(object11new.getName().equals("object2"));
        // случай 3. переименование
        Assert.assertTrue(env.getConfReg().contains("object11", N2oObject.class));
        Assert.assertFalse(env.getConfReg().contains("object12", N2oObject.class));
        renameFileFromTo("object/object12.object.xml", "object/object11.object.xml", env.getWorkRepoGit(), env.getSystemBranchName(), "rename object11 to object12");
        env.getWorkRepoGit().push().setPushAll().call();
        env.getConfigAuditGit().merge(env.getSystemBranchName(), true);
        Assert.assertFalse(env.getConfReg().contains("object11", N2oObject.class));
        Assert.assertTrue(env.getConfReg().contains("object12", N2oObject.class));
        // случай 4. удаление метаданной
        removeFile("object/object12.object.xml", env.getWorkRepoGit(), env.getSystemBranchName(), "remove object12");
        env.getWorkRepoGit().push().setPushAll().call();
        env.getConfigAuditGit().merge(env.getSystemBranchName(), true);
        Assert.assertFalse(env.getConfReg().contains("object12", N2oObject.class));
    }

    @Test
    public void test5UpdateSystemBranch() throws Exception {
        env.getConfigAuditGit().init();
        updateWorkRepo();
        // проверка exception не на той ветке
        env.getGitCore().checkout(env.getSystemBranchName());
        try {
            env.getConfigAuditGit().updateSystem();
            Assert.fail();
        } catch (N2oConfigAuditException e) {
        }
        env.getGitCore().checkout(env.getServerBranchName());
        // проверка exception незакоммиченные изменения
        File test = new File(PathUtil.concatAbsoluteAndLocalPath(env.getConfigPath(), "test.object.xml"));
        N2oGitFileUtil.createFile(resolveURI("object/object2.object.xml"), test);
        try {
            env.getConfigAuditGit().updateSystem();
            Assert.fail();
        } catch (N2oException e) {
            Assert.assertTrue(e instanceof N2oException);
        }
        N2oGitFileUtil.deleteFile(test);
        //выполняем обновление и проверяем что файлы из classpath появились
        File workRepo = new File(env.getWorkRepoPath());
        Assert.assertTrue(workRepo.listFiles().length == 2);
        env.getConfigAuditGit().updateSystem();
        env.getWorkRepoGit().checkout().setName(env.getSystemBranchName()).call();
        env.getWorkRepoGit().pull().call();
        Assert.assertTrue(workRepo.listFiles().length == 4);
    }

    @Test
    public void test6PullPush() throws Exception {
        env.getConfigAuditGit().init();
        updateWorkRepo();
        saveFileFromTo("test.object.xml", "object/object2.object.xml", env.getWorkRepoGit(), env.getServerBranchName(), "test commit");
        env.getWorkRepoGit().push().call();
        env.getConfigAuditGit().pull();
        Iterator<RevCommit> iterator = env.getGitCore().getLog(env.getServerBranchName(), false).iterator();
        Assert.assertTrue(iterator.next().getFullMessage().startsWith("test commit"));


        String localPath = "page/page3.page.xml";
        addToConfReg(localPath, true);
        N2oGitFileUtil.createFile(resolveURI(localPath), generateStorageFile(localPath));
        env.getConfigAuditGit().commit(localPath, "test2");
        env.getConfigAuditGit().push();
        env.getWorkRepoGit().checkout().setName(env.getServerBranchName()).call();
        env.getWorkRepoGit().pull().call();
        iterator = env.getWorkRepoGit().log().call().iterator();
        Assert.assertTrue(iterator.next().getFullMessage().contains("test2"));
    }

    private void updateWorkRepo() throws GitAPIException {
        env.getWorkRepoGit().pull().call();
        env.getWorkRepoGit().checkout().setCreateBranch(true).setName(env.getServerBranchName()).call();
        env.getWorkRepoGit().checkout().setCreateBranch(true).setName(env.getSystemBranchName()).call();
        env.getWorkRepoGit().pull().call();
    }

    private void saveFileFromTo(String pathTo, String pathFrom, Git workRepo, String branchName, String message) throws GitAPIException, IOException {
        workRepo.checkout().setName(branchName).call();
        N2oGitFileUtil.createFile(resolveURI(pathFrom),
                new File(PathUtil.concatAbsoluteAndLocalPath(workRepo.getRepository().getWorkTree().getAbsolutePath(), pathTo)));
        workRepo.add().addFilepattern(pathTo).call();
        workRepo.commit().setMessage(message).setAuthor("test", "test@i-novus.ru").call();
    }

    private void saveFileFromTo(String pathTo, String pathFrom, String message) throws GitAPIException, IOException {
        env.getGitCore().checkout(env.getServerBranchName());
        N2oGitFileUtil.createFile(resolveURI(pathFrom), new File(env.getConfigPath() + "/" + pathTo));
        env.getConfigAuditGit().commit(pathTo, message);
    }

    private void renameFileFromTo(String pathTo, String pathFrom, Git workRepo, String branchName, String message) throws GitAPIException {
        workRepo.checkout().setName(branchName).call();
        File file = new File(PathUtil.concatAbsoluteAndLocalPath(workRepo.getRepository().getWorkTree().getAbsolutePath(), pathFrom));
        File newFile = new File(PathUtil.concatAbsoluteAndLocalPath(workRepo.getRepository().getWorkTree().getAbsolutePath(), pathTo));
        file.renameTo(newFile);
        workRepo.add().addFilepattern(pathTo).call();
        workRepo.rm().addFilepattern(pathFrom).call();
        workRepo.commit().setMessage(message).setAuthor("test", "test@i-novus.ru").call();
    }

    private void removeFile(String path, Git workRepo, String branchName, String message) throws GitAPIException {
        workRepo.checkout().setName(branchName).call();
        File file = new File(PathUtil.concatAbsoluteAndLocalPath(workRepo.getRepository().getWorkTree().getAbsolutePath(), path));
        file.delete();
        workRepo.rm().addFilepattern(path).call();
        workRepo.commit().setMessage(message).setAuthor("test", "test@i-novus.ru").call();
    }

    private void assertConflict(N2oConfigConflict conflict){
        assertEqualsContent(conflict.getContent(), "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<object xmlns=\"http://n2oapp.net/framework/config/schema/object-1.0\"\n" +
                "        xmlns:n2o=\"http://n2oapp.net/framework/config/schema/n2o-invocations-2.0\">\n" +
                "    <name>object3</name>\n" +
                "</object>\n");
        assertEqualsContent(conflict.getMergeContent(), ("<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<object xmlns=\"http://n2oapp.net/framework/config/schema/object-1.0\"\n" +
                "        xmlns:n2o=\"http://n2oapp.net/framework/config/schema/n2o-invocations-2.0\">\n" +
                "    <name>object2</name>\n" +
                "    <fields>\n" +
                "        <field id=\"id\"/>\n" +
                "    </fields>\n" +
                "</object>\n"));
        assertEqualsContent(conflict.getParentContent(), ("<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<object xmlns=\"http://n2oapp.net/framework/config/schema/object-1.0\"\n" +
                "        xmlns:n2o=\"http://n2oapp.net/framework/config/schema/n2o-invocations-2.0\">\n" +
                "\n" +
                "</object>"));
    }

    private void assertEqualsContent(String content, String result) {
        Assert.assertEquals(content.replaceAll("\\s+",""), result.replaceAll("\\s+",""));
    }

}
