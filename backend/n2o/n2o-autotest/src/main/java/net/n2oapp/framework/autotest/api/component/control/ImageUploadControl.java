package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.autotest.api.component.page.Page;

import java.io.File;

/**
 * Загрузка изображения для автотестирования
 */
public interface ImageUploadControl extends Control {

    /**
     * Загрузка изображений в виде файлов
     * @param image список загружаемых изображений
     * @return первое загруженное изображение
     */
    File uploadImage(File... image);

    /**
     * Загрузка изображений через classpath
     * @param imageName список имен загружаемых изображений
     * @return первый загруженный файл
     */
    File uploadFromClasspath(String... imageName);

    /**
     * Проверка наличия кнопки очистки изображения
     * @param index номер проверяемого изображения
     */
    void shouldHaveDeleteButton(int index);

    /**
     * Проверка отсутствия кнопки очистки файла
     * @param index номер проверяемого изображения
     */
    void shouldNotHaveDeleteButton(int index);

    /**
     * Удаление изображения из поля по номеру
     * @param index номер изображения
     */
    void deleteImage(int index);

    /**
     * Проверка наличия возмножности предварительного просмотра изображения
     * @param index номер загруженного изображения
     */
    void shouldHavePreview(int index);

    /**
     * Проверка отсутствия возмножности предварительного просмотра изображения
     * @param index номер загруженного изображения
     */
    void shouldNotHavePreview(int index);

    /**
     * Открытие предварительного просмотра
     * @param page тип открываемой страницы
     * @param index номер загруженного изображения
     * @return диалог предварительного просмотра для автотестирования
     */
    PreviewDialog openPreviewDialog(Page page, int index);

    /**
     * Проверка количества загруженных изображений
     * @param size ожидаемое количество загруженных изображений
     */
    void shouldHaveSize(int size);

    /**
     * Проверка наличия имени у загруженного изображения
     * @param index номер проверяемого изображения
     */
    void shouldHaveNameInfo(int index);

    /**
     * Проверка отсутствия имени у загруженного изображения
     * @param index номер проверяемого изображения
     */
    void shouldNotHaveNameInfo(int index);

    /**
     * Проверка соответствия имени у загруженного изображения
     * @param index номер проверяемого изображения
     * @param fileName ожидаемое имя изображения
     */
    void shouldHaveName(int index, String fileName);

    /**
     * Проверка наличия информации о размере у загруженного изображения
     * @param index номер проверяемого изображения
     */
    void shouldHaveVisibleSizeInfo(int index);

    /**
     * Проверка отсутствия информации о размере у загруженного изображения
     * @param index номер проверяемого изображения
     */
    void shouldNotHaveVisibleSizeInfo(int index);

    /**
     * Проверка размера загруженного изображения
     * @param index номер проверяемого изображения
     * @param fileSize ожидаемый размер изображения
     */
    void shouldHaveSize(int index, String fileSize);

    /**
     * Проверка формы поля для загрузки изображения
     * @param shape ожидаемая форма
     */
    void uploadAreaShouldHaveShape(ShapeType shape);

    /**
     * Проверка иконки поля для загрузки изображения
     * @param icon ожидаемая иконка
     */
    void uploadAreaShouldHaveIcon(String icon);

    /**
     * Проверка размера поля для загрузки изображения
     * @param regex регулярное выражение соответствующее ожидаемому размеру шрифта в пикселях
     */
    void uploadAreaShouldHaveIconSizeMatches(String regex);

    /**
     * Проверка ширины поля для загрузки изображения
     * @param regex регулярное выражение соответствующее ожидаемому размеру ширины в пикселях
     */
    void uploadAreaShouldHaveWidthMatches(String regex);

    /**
     * Проверка высоты поля для загрузки изображения
     * @param regex регулярное выражение соответствующее ожидаемому размеру высоты в шрифта пикселях
     */
    void uploadAreaShouldHaveHeightMatches(String regex);

    /**
     * Компонент диалог предварительного просмотра для автотестирования
     */
    interface PreviewDialog {
        /**
         * Проверка существования диалога
         */
        void shouldExists();

        /**
         * Закрытие диалога
         */
        void close();

        /**
         * Проверка ссылки диалога
         * @param link ожидаемая ссылка
         */
        void shouldHaveLink(String link);

    }

}
