{
"description": "Страница с группами полей и регионом с отслеживанием прокрутки по группам полей"
"layout": "Страница с заголовком 'Scrollspy. Группировка элементов', группами полей, меню с заголовками групп полей слева для осуществления быстрого перехода к ним при клике на название группы в списке меню"
"elements": "
Регион с отслеживанием прокрутки (scrollspy, разделительная строка между двумя блоками групп (headlines='true'), размещение слева страницы (placement='left'), максимальная высота региона 500 (max-height='500')), 
Регион содержит две группы данных: 'Основная информация' (список групп, group), 'Медицинская информация' (список групп, group).

Список групп 'Основная информация' состоит из двух списков элементов меню: 'Личные данные' (список элементов меню, menu-item), 'Документы' (список элементов меню, menu-item).
Поля списка элементов меню 'Личные данные': 
поля 'Имя' (компонент ввода однострочного текста или чисел, input-text) и 'Фамилия' (компонент ввода однострочного текста или чисел, input-text) вывести в первой строке (row),
поля 'Пол' (компонент выбора из выпадающего списка, select, варианты выбора (options): 'Мужской', 'Женский') и 'Дата рождения' (компонент ввода даты и времени, date-time) вывести во второй строке (row),
поле 'Адрес' на следующей строке (компонент ввода многострочного текста, text-area).

Поля списка элементов меню 'Документы':
поле 'Документ удостоверяющий личность' (компонент ввода текста с выбором из выпадающего списка, input-select, варианты выбора (options): 'Паспорт гражданина РФ', 'Свидетельстов о рождении', 'Паспорт иностранного гражданина', 'Не определен'),
поля 'Серия' (компонент ввода однострочного текста или чисел, input-text) и 'Номер' (компонент ввода однострочного текста или чисел, input-text) вывести в одной строке (row),
поля 'Дата выдачи' (компонент ввода однострочного текста или чисел, input-text) и 'Код подразделения' (компонент ввода однострочного текста или чисел, input-text) вывести в одной строке (row).

Список групп 'Медицинская информация' состоит из трех элементов меню: 'Медицинское страхование' (кнопка с выпадающим меню, sub-menu), 'Медицинская организация прикрепления' (список элементов меню, menu-item), Диагнозы (список элементов меню, menu-item).
Кнопка с выпадающим меню 'Медицинское страхование' содержит два списка элементов меню: 'Обязательное' (список элементов меню, menu-item), 'Добровольное' (список элементов меню, menu-item).
Поля списка элементов меню 'Обязательное':
поле 'Серия' (компонент ввода однострочного текста или чисел, input-text) и 'Номер' (компонент ввода однострочного текста или чисел, input-text) вывести в первой строке (row),
поле 'Страховая организация' (компонент ввода однострочного текста или чисел, input-text).

Поля списка элементов меню 'Добровольное': 
поле 'Страховая организация' (компонент ввода однострочного текста или чисел, input-text), 
на следующей строке поле 'Номер договора страхования  жизни' (компонент ввода однострочного текста или чисел, input-text).

Поля списка элементов меню 'Медицинская организация прикрепления':
поле 'Медицинская организация' (компонент ввода однострочного текста или чисел, input-text),
на следующей строке поле 'Адрес медицинской организации' (компонент ввода многострочного текста, text-area).

Поля списка элементов меню 'Диагнозы':
кнопка 'Добавить диагноз' (button),
таблица с тремя колонками: 'Основной диагноз' (column), 'Дата постановки'(column), 'Медицинская орагнизация' (column).
}