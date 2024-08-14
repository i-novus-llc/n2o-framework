import { TBaseProps } from '../../types'

export enum ButtonType {
    first = 'first',
    last = 'last',
    next = 'next',
    prev = 'prev',
}

export type GetControlButtonArgs = {
    activePage: PaginationProps['activePage'],
    buttonType: ButtonType,
    icon: string,
    label: string | null,
    lastPage: number
    onSelect: PaginationProps['onSelect'],
}

export enum Direction {
    left = 'left',
    right = 'right',
}

export type GetLabelArgs = {
    additionalClass: string,
    direction: Direction,
    icon: GetControlButtonArgs['icon'],
    label: GetControlButtonArgs['label']
}

export type RenderBodyPagingArgs = {
    activePage: number; // Номер активной страницы
    maxPages: number; // Максимальное кол-во отображаемых кнопок перехода между страницами
    onSelect(): void; // Callback нажатия по кнопке страницы
    totalPages: number; // Общее количество страниц
}

export enum Layout {
    bordered = 'bordered',
    'bordered-rounded' = 'bordered-rounded',
    flat = 'flat',
    'flat-rounded' = 'flat-rounded',
    separated = 'separated',
    'separated-rounded' = 'separated-rounded',
}

export type PaginationProps = TBaseProps & {
    activePage?: number, // Номер активной страницы
    count?: number, // Общее кол-во записей
    first?: boolean, // оказать/скрыть кнопку быстрого перехода на первую страницу
    firstIcon?: string, // Вид иконки быстрого перехода на первую страницу
    firstLabel?: string, // Текст кнопки 'First'
    last?: boolean, // Показать/скрыть кнопку быстрого перехода на последнюю страницу
    lastIcon?: string, // Вид иконки быстрого перехода на последнюю страницу
    lastLabel?: string, // Текст кнопки 'Last'
    layout?: Layout, // Стиль
    maxPages?: number, // Максимальное кол-во кнопок перехода между страницами
    next?: boolean, // Показать/скрыть кнопку быстрого перехода на следующую страницу
    nextIcon?: string, // Вид иконки быстрого перехода на следующую страницу
    nextLabel?: string, // Текст кнопки 'Вперед'
    onSelect?(): void, // Сallback нажатия по кнопке страницы
    prev?: boolean, // Показать/скрыть кнопку быстрого перехода на предыдущую страницу
    prevIcon?: string, // Вид иконки быстрого перехода на предудущую страницу
    prevLabel?: string, // Текст кнопки 'Назад'
    showCount?: boolean, // Показать индикатор общего кол-ва записей
    showSinglePage?: boolean, // Скрывать компонент, если страница единственная
    size?: number, // Кол-во записей на одной странице
    t?(str: string, obj?: object): string,
}
