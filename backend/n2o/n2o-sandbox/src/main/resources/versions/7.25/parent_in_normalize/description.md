{
"description": "Страница с таблицей, возможностью создания/удаления записи"
"layout": "Страница с заголовком 'Ключевое слово #parent в нормализации полей', группой кнопок 'Создать', 'Удалить' для создания/удаления записи в таблице.
При создании новой записи в таблице к значению во второй и третьей колонке добавлять значение из поля 'Поле 1' (добавить выражение '_{значение поля}')"
"elements": "
Группа кнопок создания и удаления записи (toolbar, generate='create, delete'): 'Создать' (с иконкой в виде плюса перед названием кнопки, открытие модального окна создания новой записи), 'Удалить' (с иконкой в виде корзины перед названием кнопки, выполнить удаление записи, которая выделена в таблице).
Таблица с тремя колонками: 'Поле 1' (column, text-field-id='name', width='200px'), 'Вложенное поле, к которому на нормализации добавляется Поле 1' (column, text-field-id='org.name'), 'Список, в поле каждого элемента на нормализации добавляется Поле 1' (column, text-field-id='departments').
Модальное окно 'test - Создание' (заголовок - 'test - Создание', 
'Поле 1' (текстовое поле, обязательное поле, вывести иконку красной звездочки рядом с названием поля. Если поле пустое, вывести текст ошибки 'Поле обязательно для заполнения' шрифтом красного цвета и изменить цвет контура поля на красный), 
'Поле 2 для проверки #parent в in параметрах' (поле ввода текста с выбором из выпадающего списка),
'Поле 3 для проверки #parent в out параметрах'(поле ввода текста с выбором из выпадающего списка). Кнопками 'Сохранить' (по кнопке создавать новую запись в таблице) и 'Закрыть')
"
}