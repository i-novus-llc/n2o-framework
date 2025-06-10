package net.n2oapp.framework.sandbox.cases.fileupload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class PersonStorageController {

    private final Map<String, PersonModel> storage = new HashMap<>();
    private final FileStorageController fileStorageController;
    private int id = 0;

    public PersonStorageController(@Autowired FileStorageController fileStorageController) {
        this.fileStorageController = fileStorageController;
        PersonModel personModel = new PersonModel("" + id++, "Ivanov", "Ivan", "Ivanovich", null);
        storage.put(personModel.getId(), personModel);
    }

    public ListResponse getList() {
        return new ListResponse(storage.values());
    }

    public PersonModel getById(@PathVariable String id) {
        return storage.get(id);
    }

    public String createPerson(@RequestBody PersonModel person) {
        person.setId("" + id++);
        storage.put(person.getId(), person);
        return person.getId();
    }

    public void updatePerson(@RequestBody PersonModel person) {
        PersonModel personModel = storage.get(person.getId());
        personModel.setName(person.getName());
        personModel.setSurname(person.getSurname());
        personModel.setPatronymic(person.getPatronymic());
    }

    public void deletePerson(@PathVariable String id) {
        storage.remove(id);
    }

    public void setDoc(String personId, FileModel doc) {
        if (storage.get(personId).getFiles() == null)
            storage.get(personId).setFiles(new HashMap<>());
        storage.get(personId).getFiles().put(doc.getId(), doc);
    }

    public void deleteDoc(String personId, String id) {
        fileStorageController.delete(id);
        storage.get(personId).getFiles().remove(id);
    }

    public FileModel storeFile(MultipartFile file, String url) {
        return fileStorageController.storeFile(file, url);
    }

    public Resource loadFile(String fileName) {
        return fileStorageController.loadFile(fileName);
    }

    @Getter
    @AllArgsConstructor
    static class ListResponse {
        @JsonProperty
        final Collection<PersonModel> persons;
    }
}