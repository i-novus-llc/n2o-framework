{
"description": "Страница с автоматически прокручиваемым сгрупированным меню",
"layout": "Страница с заголовком 'Scrollspy регион с группами' содержит автоматически прокручиваемое сгруппированное меню, которое содержит две группы: группа 'ОСНОВНАЯ ИНФОРМАЦИЯ' включает в себя пункты 'Личные данные','Документы', группа 'МЕДИЦИНСКАЯ ИНФОРМАЦИЯ' включает в себя пункты 'Медицинское страхование', 'Обязательное', 'Добровольное', 'Медицинская организация прикрепления','Диагнозы'. При нажатии на пункт меню происходит перемещение на раздел, который находится на этой же странице.
Пункт меню с заголовком 'Личные данные' перемещает в раздел с заголовком 'Личные данные', который содержит пять полей: 'Имя','Фамилия','Пол','Дата рождения','Адрес'. Все поля выводятся в две колонки, кроме поля 'Адрес', которое выводится во всю ширину формы.
Пункт меню с заголовком 'Документы' перемещает в раздел с заголовком 'Документы', который содержит пять полей: 'Документ удостоверяющий личность','Серия','Номер','Дата выдачи','Код подразделения'. Все поля выводятся в две колонки, кроме поля 'Документ удостоверяющий личность', которое выводится во всю ширину формы.
Пункт меню с заголовком 'Медицинское страхование' представлено в виде выпадающего меню, которое содержит два пункта меню 'Обязательное' и 'Добровольное'.
Пункт меню с заголовком 'Обязательное' перемещает в раздел с заголовком 'Обязательное', который содержит три поля: 'Серия','Номер','Страховая организация'. Все поля выводятся в две колонки, кроме поля 'Страховая организация', которое выводится во всю ширину формы.
Пункт меню с заголовком 'Добровольное' перемещает в раздел с заголовком 'Добровольное', который содержит два поля: 'Страховая организация','Номер договора страхования жизни'. Все поля выводятся во всю ширину формы.
Пункт меню с заголовком 'Медицинская организация прикрепления' перемещает в раздел с заголовком 'Медицинская организация прикрепления', который содержит два поля: 'Медицинская организация','Адрес медицинской организации'. Все поля выводятся во всю ширину формы.
Пункт меню с заголовком 'Диагнозы' перемещает в раздел с заголовком 'Диагнозы', который содержит панель инструментов с одной кнопкой 'Добавить диагноз' и таблицу с трамя колонками: 'Основной диагноз','Дата постановки','Медицинская орагнизация'.",
"elements": "Поля формы: (автоматически прокручиваемое меню, r1, максимальная высота 500, расположение слева, разделение групп линией),
ОСНОВНАЯ ИНФОРМАЦИЯ (группа, group, заголовок 'ОСНОВНАЯ ИНФОРМАЦИЯ', включает в себя пункты 'Личные данные','Документы'),
Личные данные (пункт меню, mi1, значение 'Личные данные', осуществляет переход в раздел, который содержит поля 'Имя','Фамилия','Пол','Дата рождения','Адрес'),
Имя (поле ввода, firstname, заголовок 'Имя'),
Фамилия (поле ввода, lastname, заголовок 'Фамилия'),
Пол (выпадающий список, gender, заголовок 'Пол', выбор из двух значений: Мужской, Женский),
Дата рождения (дата, birthday, заголовок 'Дата рождения'),
Адрес (поле ввода многострочного текста, address, 'Адрес'),
Документы (пункт меню, mi2, значение 'Документы', осуществляет переход в раздел, который содержит поля 'Документ удостоверяющий личность','Серия','Номер','Дата выдачи','Код подразделения'),
Документ удостоверяющий личность (выпадающий список, type, заголовок 'Документ удостоверяющий личность', выбор из четырех значений: Паспорт гражданина РФ, Свидетельстов о рождении, Паспорт иностранного гражданина, Не определен),
Серия (поле ввода, serie, заголовок 'Серия'),
Номер (поле ввода, number, заголовок 'Номер'),
Дата выдачи (дата, date, заголовок 'Дата выдачи'),
Код подразделения (поле ввода, code, заголовок 'Код подразделения'),
МЕДИЦИНСКАЯ ИНФОРМАЦИЯ (группа, group, заголовок 'МЕДИЦИНСКАЯ ИНФОРМАЦИЯ', включает в себя пункты 'Медицинское страхование', 'Обязательное', 'Добровольное', 'Медицинская организация прикрепления','Диагнозы', есть разделительная линия над заголовком),
Медицинское страхование (выпадающее меню, mi3, значение 'Медицинское страхование', содержит пункты меню  'Обязательное' и 'Добровольное'),
Обязательное (пункт меню, mi4, значение 'Обязательное', осуществляет переход в раздел, который содержит поля 'Серия','Номер','Страховая организация'),
Серия (поле ввода, serie, заголовок 'Серия'),
Номер (поле ввода, number, заголовок 'Номер'),
Страховая организация (поле ввода, org, заголовок 'Страховая организация'),
Добровольное (пункт меню, mi5, значение 'Добровольное', осуществляет переход в раздел, который содержит поля 'Страховая организация','Номер договора страхования жизни'),
Страховая организация (поле ввода, org, заголовок 'Страховая организация'),
Номер договора страхования жизни (поле ввода, number, заголовок 'Номер договора страхования жизни'),
Медицинская организация прикрепления (пункт меню, mi6, значение 'Медицинская организация прикрепления', осуществляет переход в раздел, который содержит поля 'Медицинская организация','Адрес медицинской организации'),
Медицинская организация (поле ввода, organization.name, 'Медицинская организация'),
Адрес медицинской организации (поле ввода многострочного текста, organization.address, 'Адрес медицинской организации'),
Диагнозы (пункт меню, mi7, значение 'Диагнозы', осуществляет переход в раздел, который содержит панель инструментов с одной кнопкой 'Добавить диагноз' и таблицу с трамя колонками: 'Основной диагноз','Дата постановки','Медицинская орагнизация'),
Добавить диагноз (кнопка, button, значение 'Добавить диагноз', место расположение слева, доступна для нажатия),
Основной диагноз (колонка таблицы, diagnosisKey, заголовок 'Основной диагноз'),
Дата постановки (колонка таблицы, date, заголовок 'Дата постановки'),
Медицинская орагнизация (колонка таблицы, org, заголовок 'Медицинская орагнизация')"
}