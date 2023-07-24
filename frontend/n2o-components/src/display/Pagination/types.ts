import { TBaseProps } from '../../types'

export enum ButtonType {
    first = 'first',
    prev = 'prev',
    next = 'next',
    last = 'last'
}

export type GetControlButtonArgs = {
    buttonType: ButtonType,
    icon: string,
    label: string | null,
    activePage: PaginationProps['activePage'],
    lastPage: number
    onSelect: PaginationProps['onSelect'],
}

export enum Direction {
    left = 'left',
    right = 'right'
}

export type GetLabelArgs = {
    direction: Direction,
    icon: GetControlButtonArgs['icon'],
    label: GetControlButtonArgs['label'],
    additionalClass: string
}

export type RenderBodyPagingArgs = {
    activePage: number; // Номер активной страницы
    maxPages: number; // Максимальное кол-во отображаемых кнопок перехода между страницами
    totalPages: number; // Общее количество страниц
    onSelect(): void; // Callback нажатия по кнопке страницы
}

export enum Layout {
    bordered = 'bordered',
    flat = 'flat',
    separated = 'separated',
    'flat-rounded' = 'flat-rounded',
    'bordered-rounded' = 'bordered-rounded',
    'separated-rounded' = 'separated-rounded',
}

export type PaginationProps = TBaseProps & {
    layout?: Layout, // Стиль
    prev?: boolean, // Показать/скрыть кнопку быстрого перехода на предыдущую страницу
    prevIcon?: string, // Вид иконки быстрого перехода на предудущую страницу
    prevLabel?: string, // Текст кнопки 'Назад'
    next?: boolean, // Показать/скрыть кнопку быстрого перехода на следующую страницу
    nextIcon?: string, // Вид иконки быстрого перехода на следующую страницу
    nextLabel?: string, // Текст кнопки 'Вперед'
    first?: boolean, // оказать/скрыть кнопку быстрого перехода на первую страницу
    firstIcon?: string, // Вид иконки быстрого перехода на первую страницу
    firstLabel?: string, // Текст кнопки 'First'
    last?: boolean, // Показать/скрыть кнопку быстрого перехода на последнюю страницу
    lastIcon?: string, // Вид иконки быстрого перехода на последнюю страницу
    lastLabel?: string, // Текст кнопки 'Last'
    showCount?: boolean, // Показать индикатор общего кол-ва записей
    showSinglePage?: boolean, // Скрывать компонент, если страница единственная
    maxPages?: number, // Максимальное кол-во кнопок перехода между страницами
    count?: number, // Общее кол-во записей
    size?: number, // Кол-во записей на одной странице
    activePage?: number, // Номер активной страницы
    onSelect?(): void, // Сallback нажатия по кнопке страницы
    t?(str: string, obj?: object): string,
}
