{
"description": "Страница c таблицей и базовыми функциями",
"layout": "Страница с заголовком 'Список контактов', таблицей с тестовыми данными, фильтрами,двумя тулбарами и счетчиком записей. 
Строки в таблице выделяются чекбоксом. В таблице на одной странице отображается 5 записей. Фильтр таблицы расположен наверху.
Первый тулбар находится сверху слева, второй тулбар находится сверху справа. Модальное окно добавления записи."  
"elements": "Колонки таблицы: Фамилия(текст, surname), Имя (текст,name), Отчество(текст,patronymic), День рождения(текст,формат 'date DD.MM.YYYY', birthday),
Пол(текст,gender.name, значок, цвет значка зависит от gender.id), VIP(чекбокс,vip, при выборе вызывается метод 'update'), колонка без заголовка(тулбар).
Тулбар колонки: Меню (Кнопка с выпадающим меню с элементами: Просмотр, Изменить). 
Фильтр таблицы: первая колонка первой строки фильтра:Фамилия(поле ввода, surname),
Вторая колонка первой строки фильтра: Имя(поле ввода, name)
Третья колонка первой строки фильтра: Отчество(поле ввода, patronymic)
Вторая строка фильтра: День рождения(интервал дат, birthday, формат 'DD.MM.YYYY'),Пол(группа радиокнопок,gender), Чекбокс без заголовка(чекбокс,vip).
Первый тулбар: Добавить(кнопка с иконкой, при клике открывается модальное окно 'Карточка нового клиента'),
Удалить(кнопка с иконкой,множественное удаление, вызов функции, доступна,если выбрана хотя бы одна запись в таблице)
Второй тулбар: Кнопка-иконка(filters), кнопка-иконка(columns), кнопка-иконка(refresh),кнопка-иконка(resize)
Модальное окно: первая колонка первой строки: Фамилия(поле ввода,surname), Имя(поле ввода, name), Отчество(поле ввода, patronymic)
вторая колонка первой строки:День рождения(поле выбора даты), Пол(группа радиокнопок, gender, расположены в одну линию),VIP(Чекбокс,vip)"
}