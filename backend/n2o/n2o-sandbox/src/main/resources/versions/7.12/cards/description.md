{
"description": "Страница с карточками, которые содержат колонки из нескольких блоков, и с модальным окном, которое открывается при клике на кнопку в последнем блоке последней колонки.",
"layout": "Страница с заголовком 'Виджет Карточки' содержит две карточки из трех колонок. Первая колонка содержит три блока, вторая колонка содержит один блок, третья колонка содержит четыре блока. Есть счетчик записей внизу слева под карточками. В последнем блоке последней колонки располагается кнопка, при клике на которую открывается модальное окно.",
"elements": "(карточка, cards, высота карточки 250px, содержит три колонки),
(колонка карточки, col, размер 4, содержит блоки 'image', 'name' и 'rating')
(блок с картинкой, image, источником изображения является 'image' из json-файла 'test.json')
(блок с текстом, name, есть значок-бейдж с цветом 'info', источником данных является 'name' из json-файла 'test.json')
(блок с рейтингом, rating, в качестве значений можно использовать половинки целых чисел, источником данных является 'rating' из json-файла 'test.json'),
(колонка карточки, col, размер 5, содержит блок 'text')
(блок с текстом, text, источником данных является 'text' из json-файла 'test.json'),
(колонка карточки, col, размер 3, содержит блоки 'icon', 'progress', 'checkbox' и 'action')
(блок с иконкой и текстом с тултипом, icon, источником данных является 'icon' из json-файла 'test.json', если значение равно 'ship', отображается иконка с кораблем 'fa fa-ship' с текстом ship справа от иконки и тултипом ship, если значение равно 'bicycle', отображается иконка с кораблем 'fa fa-bicycle' с текстом bicycle справа от иконки и тултипом bicycle)
(блок с индикатором прогресса, progress, источником данных является 'progress' из json-файла 'test.json')
(блок с чекбоксом, checkbox, источником данных является 'checkbox' из json-файла 'test.json')
(блок с тулбаром с кнопкой, Info, кнопка имеет цвет 'success', при клике на кнопку открывается модальное окно),
(модальное окно, modal, состав элементов окна: разделительная линия, лейбл 'ID' и неактивное текстовое поле для ввода со значением из поля 'id' выбранной карточки, лейбл 'Name' и неактивное текстовое поле для ввода со значением из поля 'name' выбранной карточки)."
}