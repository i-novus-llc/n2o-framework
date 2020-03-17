package net.n2oapp.framework.autotest.controls;

import lombok.Builder;
import lombok.Getter;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.FileUploadControl;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestApplication;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.dataprovider.TestDataProviderIOv1;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Автотесты поля загрузки файлов
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(classes = AutoTestApplication.class,
        properties = "server.port=7775",
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(FileUploadAT.FileUploadController.class)
public class FileUploadAT extends AutoTestBase {

    private SimplePage simplePage;
    private static List<FileModel> fileStore = new ArrayList<>();

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/controls/fileupload/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/default.header.xml"));

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oControlsV2IOPack(), new N2oQueriesPack());
        builder.ios(new TestDataProviderIOv1());
    }

    @Test
    public void wrongRestTest() {
        FileUploadControl fileUpload = getFields().field("FileUpload1").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();
        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/controls/test1.json");
        fileUpload.uploadFilesShouldBe(1);
        fileUpload.uploadFileNameShouldBe(0, "test1.json");
        fileUpload.uploadFileSizeShouldBe(0, "91");
        fileUpload.deleteFile(0);
        fileUpload.uploadFilesShouldBe(0);
    }

    @Test
    @Ignore //Нет информации о загруженом файле
    public void oneFileUploadTest() {
        FileUploadControl fileUpload = getFields().field("FileUpload2").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();
        assertThat(fileStore.size(), is(0));

        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/controls/test1.json");
        fileUpload.uploadFilesShouldBe(1);

        fileUpload.uploadFileShouldHaveLink(0, "http://localhost:37775/files/test1.json");
        fileUpload.uploadFileNameShouldBe(0, "test1.json");
        fileUpload.uploadFileSizeShouldBe(0, "91");

        assertThat(fileStore.size(), is(1));
        fileUpload.deleteFile(0);
        fileUpload.uploadFilesShouldBe(0);
        assertThat(fileStore.size(), is(0));
    }

    @Test
    @Ignore //первый файл загружается два раза
    public void serialTwoFileUploadTest() {
        FileUploadControl fileUpload = getFields().field("FileUpload3").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();
        assertThat(fileStore.size(), is(0));

        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/controls/test1.json");
        fileUpload.uploadFilesShouldBe(1);
        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/controls/test2.json");
        fileUpload.uploadFilesShouldBe(2);

        fileUpload.uploadFileShouldHaveLink(0, "http://localhost:37775/files/test1.json");
        fileUpload.uploadFileNameShouldBe(0, "test1.json");
        fileUpload.uploadFileSizeShouldBe(0, "91");
        fileUpload.uploadFileShouldHaveLink(1, "http://localhost:37775/files/test2.json");
        fileUpload.uploadFileNameShouldBe(1, "test2.json");
        fileUpload.uploadFileSizeShouldBe(1, "91");

        assertThat(fileStore.size(), is(2));
        fileUpload.deleteFile(1);
        fileUpload.deleteFile(0);
        fileUpload.uploadFilesShouldBe(0);
        assertThat(fileStore.size(), is(0));
    }

    @RestController
    public static class FileUploadController {
        @RequestMapping(method = RequestMethod.POST, value = "files")
        public ResponseEntity<FileModel> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
            return new ResponseEntity<>(storeFile(file), HttpStatus.OK);
        }

        @RequestMapping(method = RequestMethod.POST, value = "multi")
        public ResponseEntity<List<FileModel>> uploadMultiFile(@RequestParam("file") MultipartFile[] file) throws IOException {
            List<FileModel> result = new ArrayList<>();
            for (MultipartFile mf : file) {
                result.add(storeFile(mf));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        @RequestMapping(method = RequestMethod.DELETE, value = "files/{id}")
        public void deleteFile(@PathVariable String id) {
            for (FileModel fs : fileStore) {
                if (id.equals(fs.id)) {
                    fileStore.remove(fs);
                    break;
                }
            }
        }

        private FileModel storeFile(MultipartFile file) throws IOException {
            FileModel fm = FileModel.builder().id(UUID.randomUUID().toString()).fileName(file.getOriginalFilename())
                    .url("/files/" + file.getOriginalFilename()).size(file.getSize()).build();

            byte[] cont = file.getBytes();
            assertThat(file.getSize() - cont.length, is(0L));

            fileStore.add(fm);
            return fm;
        }
    }

    @Getter
    @Builder
    private static class FileModel {
        String id;
        String fileName;
        String url;
        long size;
    }

    private Fields getFields() {
        return simplePage.single().widget(FormWidget.class).fields();
    }

}
