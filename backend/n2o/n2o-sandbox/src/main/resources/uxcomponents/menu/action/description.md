{
"description": "Страница с верхним колонтитулом, содержащим выпадающее меню.",
"layout": "Страница с заголовком 'Действия в пунктах меню' содержит верхний колонтитул с выпадающим меню с заголовком 'Действия' и со значениями 'Алерт', 'Открыть модальное окно', 'Открыть drawer окно'.
Пункт 'Алерт' открывает информационное окно с текстом 'Сообщение'.
Пункт 'Открыть модальное окно' открывает модальное окно с заголовком 'Модальное окно'.
Пункт 'Открыть drawer окно' открывает боковое меню с заголовком 'Модальное окно'.",
"elements": "(верхний колонтитул, header, содержит основное меню),
(основное меню, nav, содержит выпадающее меню 'Действия'),
Действия (выпадающее меню, dropdown-menu, наименование 'Действия', содержит значения 'Алерт', 'Открыть модальное окно', 'Открыть drawer окно'),
Алерт (компонент меню, menu-item, наименование 'Алерт', открывает информационное окно),
(информационное окно, alert, содержит текст 'Сообщение', имеет цвет 'info', длительность показа уведомления 5000),
Открыть модальное окно (компонент меню, menu-item, наименование 'Открыть модальное окно', открывает модальное окно 'Модальное окно'),
Открыть drawer окно (компонент меню, menu-item, наименование 'Открыть drawer окно', открывает боковое меню 'Модальное окно'),
Модальное окно (модальное окно, page, заголовок 'Модальное окно')"
}