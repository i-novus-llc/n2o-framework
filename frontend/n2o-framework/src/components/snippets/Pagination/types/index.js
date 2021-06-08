import { shape, number, string, bool, oneOf, func, object } from 'prop-types'

export const pagingType = shape({
    /**
     * Стиль
     * */
    layout: oneOf([
        'bordered',
        'flat',
        'separated',
        'bordered-rounded',
        'flat-rounded',
        'separated-rounded',
    ]),
    /**
     * Место расположения пагинации
     * */
    place: oneOf([
        'topLeft',
        'topRight',
        'bottomLeft',
        'bottomRight',
        'topCenter',
        'bottomCenter',
    ]),
    /**
     * Показать/скрыть кнопку быстрого перехода на предыдущую страницу
     */
    prev: bool,
    /**
     * Вид иконки быстрого перехода на предудущую страницу
     * */
    prevIcon: string,
    /**
     * Текст кнопки 'Назад'
     */
    prevLabel: string,
    /**
     * Показать/скрыть кнопку быстрого перехода на следующую страницу
     */
    next: bool,
    /**
     * Вид иконки быстрого перехода на следующую страницу
     */
    nextIcon: string,
    /**
     * Текст кнопки 'Вперед'
     */
    nextLabel: string,
    /**
     * Показать/скрыть кнопку быстрого перехода на первую страницу
     */
    first: bool,
    /**
     * Вид иконки быстрого перехода на первую страницу
     * */
    firstIcon: string,
    /**
     * Текст кнопки 'First'
     */
    firstLabel: string,
    /**
     * Показать/скрыть кнопку быстрого перехода на последнюю страницу
     */
    last: bool,
    /**
     * Вид иконки быстрого перехода на последнюю страницу
     * */
    lastIcon: string,
    /**
     * Текст кнопки 'Last'
     */
    lastLabel: string,
    /**
     * Показать индикатор общего кол-ва записей
     */
    showCount: bool,
    /**
     * Скрывать компонент, если страница единственная
     */
    showSinglePage: bool,
    /**
     * Максимальное кол-во кнопок перехода между страницами
     */
    maxPages: number,
    /**
     * Общее кол-во записей
     */
    count: number,
    /**
     * Кол-во записей на одной странице
     */
    size: number,
    /**
     * Номер активной страницы
     */
    activePage: number,
    /**
     * Сallback нажатия по кнопке страницы
     */
    onSelect: func,
    /**
     * Класс для списка внутри nav
     */
    className: string,
    t: func,
    style: object,
})
