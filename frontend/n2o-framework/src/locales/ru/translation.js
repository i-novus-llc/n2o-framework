import numeral from 'numeral'

numeral.register('locale', 'ru', {
    delimiters: {
        thousands: ' ',
        decimal: ',',
    },
    abbreviations: {
        thousand: 'тыс.',
        million: 'млн.',
        billion: 'млрд.',
        trillion: 'трлн.',
    },
    ordinal: () => '.',
    currency: {
        symbol: '₽',
    },
})

export default {
    defaultPromptMessage:
        'Все несохраненные данные будут утеряны, вы уверены, что хотите уйти?',
    login: 'Логин',
    paginationInterval:
        '(0){записей};(1){запись};(2-4){записи};(5-inf){записей};',
    paginationCount_0: 'запись',
    paginationCount_1: 'записи',
    paginationCount_2: 'записей',
    paginationTotal: 'Всего',
    noData: 'Нет данных для отображения',
    chooseTime: 'Выберите время',
    dialogTitle: 'Подтвердите действие',
    dialogText: 'Вы уверены?',
    confirm: 'Да',
    deny: 'Нет',
    search: 'Найти',
    reset: 'Сбросить',
    downloadAll: 'Загрузить все',
    currentPage: 'Текущая страница',
    uploadFile: 'Загрузить файл',
    save: 'Сохранить',
    rub: 'руб',
    nothingFound: 'Ничего не найдено',
    selected: 'Выбрано',
    hide: 'Скрыть',
    details: 'Подробнее',
    pageNotFound: 'Страница не найдена',
    accessDenied: 'Доступ запрещён',
    innerAppError: 'Внутренняя ошибка приложения',
    badGateway: 'Неверный ответ от восходящего сервера',
    routerError:
        'Нужно добавить {{page}} в компонент {{n2o}} или включить {{embeddedRouting}}.',
    pagesNotConfigured: 'Страницы не настроены',
    loading: 'Загрузка',
    panelFullScreenHelp: 'ESC - выход из полноэкранного режима',
    calendarMonth: 'Месяц',
    calendarDay: 'День',
    calendarToday: 'Сегодня',
    calendarWeek: 'Неделя',
    calendarAgenda: 'Повестка дня',
    calendarNext: 'Вперед',
    calendarPrevious: 'Назад',
    calendarNoEventsInRange: 'На данном отрезке времени события отсутствуют.',
    calendarTomorrow: 'Завтра',
    calendarWorkweek: 'Рабочая неделя',
    calendarYesterday: 'Вчера',
    calendarEvent: 'Событие',
    calendarAllDay: 'Весь день',
    calendarDate: 'Дата',
    calendarTime: 'Время',
    imageUploadAvailableImageTypes: 'Вы можете загрузить только файл',
}
