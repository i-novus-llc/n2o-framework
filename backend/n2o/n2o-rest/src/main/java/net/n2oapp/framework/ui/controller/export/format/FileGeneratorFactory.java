package net.n2oapp.framework.ui.controller.export.format;

import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.exception.N2oException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Фабрика генераторов файлов по формату
 */
@Component
@RequiredArgsConstructor
public class FileGeneratorFactory {

    private final List<FileGenerator> generators;

    public FileGenerator getGenerator(String format) {
        String lowerFormat = format.toLowerCase();
        Optional<FileGenerator> generator = generators.stream().filter(g -> g.getFormat().equals(lowerFormat)).findFirst();

        return generator.orElseThrow(() -> new N2oException(String.format("Не найден генератор файла для '%s' формата", lowerFormat)));
    }
}
