{
"description": "Страница с использованием значка в филдсетах",
"layout": "Страница с заголовком 'Badge в филдсетах', двумя строками с полем 'name' и вариантами цвета 'color' и вариантами применения значка (баджа)",
"elements": "Поле 'name' (input-text, id='name', default-value='Бадж', название поля выводится в одну строку с названием следующего компонента), 
Группа радио-кнопок 'color' (radio-group, id='color', inline='true', варианты ответа в чек-боксе (options): primary, secondary, info, danger, warning, success. По умолчанию выбрано значение 'info'. Цвет значка (баджа) в компонентах далее зависит от значения выбранного в компоненте 'color': primary - черная заливка и белый цвет шрифта, secondary - серая заливка и черный цвет шрифта, info - сине-зеленая заливка и белый цвет шрифта, danger - красная заливка и белый цвет шрифта, warning - желто-оранжевая заливка и черный цвет шрифта, success - зеленая заливка и белый цвет шрифта),
Филдсет со сворачиваемым делителем (line, label='line', badge='{name}', badge-color='{color.name}', badge-shape='rounded', badge-position='left'), 
Филдсет с заголовком (set, label='set', badge='{name}', badge-color='{color.name}', badge-shape='rounded', badge-position='left'),
Мультисет (multi-set, id='multi', label='multi', badge='{name}', badge-color='{color.name}', badge-shape='rounded', badge-position='left'), кнопка 'Добавить' (добавление новой строки мультисета с кнопкой удаления этой строки справа в виде корзины))"
}