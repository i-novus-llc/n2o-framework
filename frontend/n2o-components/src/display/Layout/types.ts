import { CSSProperties, ReactNode, HTMLAttributes } from 'react'

export enum Align {
    /** Вертикальное выравнивание по верхнему краю (по умолчанию) */
    TOP = 'top',
    /** Вертикальное выравнивание по центру */
    MIDDLE = 'middle',
    /** Вертикальное выравнивание по нижнему краю */
    BOTTOM = 'bottom',
    /** Растягивание содержимого на всю высоту (если допустимо) */
    STRETCH = 'stretch',
}

export enum Justify {
    /** Горизонтальное выравнивание по началу (по умолчанию) */
    START = 'start',
    /** Горизонтальное выравнивание по концу */
    END = 'end',
    /** Горизонтальное выравнивание по центру */
    CENTER = 'center',
    /** Равномерное распределение с отступами вокруг элементов */
    SPACE_AROUND = 'space-around',
    /** Равномерное распределение с отступами между элементами */
    SPACE_BETWEEN = 'space-between',
    /** Равномерное распределение с одинаковыми отступами вокруг и между элементами */
    SPACE_EVENLY = 'space-evenly',
}

/** Style param в DOM для активации колоночной системы */
export const COLUMNS = '--columns'

export interface RowProps {
    columns?: number | null
    id?: string
    children?: ReactNode
    wrap?: boolean
    align?: Align
    justify?: Justify
    className?: string
    style?: CSSProperties
}

// Маппинг значений выравнивания на классы
export const ALIGN_CLASS_MAP = {
    [Align.TOP]: 'layout-row--align-top',
    [Align.MIDDLE]: 'layout-row--align-middle',
    [Align.BOTTOM]: 'layout-row--align-bottom',
    [Align.STRETCH]: 'layout-row--align-stretch',
}

// Маппинг значений распределения на классы
export const JUSTIFY_CLASS_MAP = {
    [Justify.START]: 'layout-row--justify-start',
    [Justify.END]: 'layout-row--justify-end',
    [Justify.CENTER]: 'layout-row--justify-center',
    [Justify.SPACE_AROUND]: 'layout-row--justify-around',
    [Justify.SPACE_BETWEEN]: 'layout-row--justify-between',
    [Justify.SPACE_EVENLY]: 'layout-row--justify-evenly',
}

/** Style param в DOM для управления колонками */
export const COL_SPAN = '--col-span'
export const COL_OFFSET = '--col-offset'

export interface ColProps extends HTMLAttributes<HTMLDivElement> {
    children: ReactNode
    id?: string
    className?: string
    style?: CSSProperties
    size?: number
    offset?: number
}
