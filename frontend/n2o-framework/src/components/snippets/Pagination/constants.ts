export const TOTAL = 'Всего записей:'
export const TOTAL_TITLE = 'Узнать количество записей'

export const PAGE_LINK_CLASS = 'page-item page-link'

export const SIMPLE_PAGINATION_COUNT = 7
export const MAX_PAGES = 5
export const SEPARATION_LIMIT = 3

/* Переключатели на 1 и последнюю страницы */
export const FULL = {
    extraFirst: true,
    extraEnd: true,
}

export const FIRST = {
    extraFirst: true,
    extraEnd: false,
}

export const END = {
    extraFirst: false,
    extraEnd: true,
}

export const COUNT_NEVER = 'never'
export const COUNT_BY_REQUEST = 'by-request'

export type showCountType = 'always' | 'by-request' | 'never' | boolean
type onSelectType = (page: number, withCount?: boolean) => void

export interface IPagination {
    className: string,
    style: object,
    showLast: boolean,
    showCount: showCountType,
    count?: number,
    hasNext?: boolean,
    maxPages: number,
    size: number,
    onSelect: onSelectType,
    activePage: number,
    prevIcon?: string,
    nextIcon?: string,
    prevLabel?: string | null,
    nextLabel?: string | null,
    prev?: boolean,
    next?: boolean,
    loading?: boolean,
}

export interface ITotal {
    total?: string | number | null,
    title?: string,
    className?: string,
    onClick(): void,
    visible: boolean,
}

export interface ISelect {
    title?: string | number | null,
    onClick?(): void,
    style?: object,
    className?: string,
    active?: boolean,
    disabled?: boolean,
    icon?: string,
    visible?: boolean,
}

export interface IPages {
    pages: number[],
    activePage: number,
    onSelect: onSelectType,
    extraFirstVisible: boolean,
    extraLastVisible: boolean,
    lastPage: number | undefined,
    showLast: boolean,
    hasNext: boolean,
    loading: boolean,
}

export interface IExtraPage {
    visible: boolean,
    page: number | undefined,
    onSelect: onSelectType,
    ellipsis?: 'left' | 'right',
    canSelect?: boolean
    showLast: boolean
    hasNext: boolean
    disabled: boolean
}
