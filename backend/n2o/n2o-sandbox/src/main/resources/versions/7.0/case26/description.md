{
"description":"Страница с таблицей и зависимой таблицей",
"layout": "Страница с именем 'Зависимости на кнопках от master виджета', двумя таблицами с тестовыми данными и счетчиком записей. Вторая таблица с фильтром и тулбаром.",
"elements": "Колонки первой таблицы: id(текст), type(текст).\nКолонки второй таблицы: id(текст).\nТулбар: 'Зависит от master resolve'(кнопка, описание 'Блокируется, если в master type!=1', условие доступности:  в первой таблице выбрана строка, где type равно 1),\n'Зависит от detail filter'(кнопка, описание 'Блокируется, если в detail filter type!=1', условие доступности: значение в поле фильтра type равно 1).\nПоля фильтра: type(поле ввода текста, type)."
}