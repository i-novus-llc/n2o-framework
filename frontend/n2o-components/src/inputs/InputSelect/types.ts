import React, { CSSProperties, KeyboardEvent, RefObject } from 'react'

import { BadgeType } from './PopupList'

export interface PopUpProps { popUpStyle?: CSSProperties, popUpItemRef?: Ref | null }

export enum Filter {
    endsWith = 'endsWith',
    includes = 'includes',
    server = 'server',
    startsWith = 'startsWith',
}

export interface TOption {
    className?: string
    disabled?: boolean
    formattedTitle?: string
    id: string | number
    parentId: string | number
    label: string
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    value: any
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export type Ref = RefObject<any>

export type getSearchMinLengthHintType = (
    customHint?: string,
    component?: React.ComponentType
) => null | string | JSX.Element

export interface State {
    activeValueId?: string | number | null
    caching?: boolean
    input?: string
    inputFocus?: boolean
    isExpanded?: boolean
    isInputSelected?: boolean
    isPopupFocused?: boolean
    options: Props['options']
    prevModel?: Record<string, unknown>
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    value?: any[]
}

/* FIXME сделать общиим с Select props */
export interface Props {
    popUpItemRef?: PopUpProps['popUpItemRef']
    popUpStyle?: PopUpProps['popUpStyle']
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    alerts?: any[]
    /**
     * Авто фокусировка на селекте
     */
    autoFocus: boolean
    /**
     * Данные для badge
     */
    badge?: BadgeType
    /**
     * Флаг кэширования запросов,
     * если false лист при открытии селекта всегда запрашиватся заного т.е. с page = 1
     */
    caching?: boolean
    className?: string
    /**
     * Флаг закрытия попапа при выборе
     */
    closePopupOnSelect: boolean
    count?: number
    datasource?: string
    descriptionFieldId: string
    /**
     * Флаг активности
     */
    disabled: boolean
    /**
     * Неактивные данные
     */
    disabledValues: []
    enabledFieldId: string
    expandPopUp: boolean
    getSearchMinLengthHint: getSearchMinLengthHintType
    fetchData(extraParams: Record<string, unknown>, concat: boolean, cacheReset: boolean): void
    /**
     * Варианты фильтрации
     */
    filter: Filter | boolean
    /**
     * Формат
     */
    format?: string
    /**
     * Поле для группировки
     */
    groupFieldId: string
    /**
     * Флаг наличия чекбоксов в селекте
     */
    hasCheckboxes: boolean
    /**
     * Ключ icon в данных
     */
    iconFieldId: string
    /**
     * Ключ image в данных
     */
    imageFieldId: string
    isExpanded?: boolean
    /**
     * Ключ label в данных
     */
    labelFieldId: string
    /**
     * Флаг загрузки
     */
    loading: boolean
    /**
     * Максимальная длина текста в тэге, до усечения
     */
    maxTagTextLength?: number
    maxTagCount?: number
    model: Record<string, unknown>
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    models?: any
    /**
     * Мульти выбор значений
     */
    multiSelect: boolean,
    onBlur(arg: TOption | TOption[] | null): void
    /**
     * Callback на изменение
     */
    onChange(arg: TOption | TOption[] | null): void
    /**
     * Callback на закрытие
     */
    onClose(): void
    onDismiss(arg: string): void
    onInput(input: State['input']): void
    onKeyDown?(evt: KeyboardEvent<HTMLTextAreaElement | HTMLInputElement>): void
    /**
     * Callback на поиск
     */
    onSearch(input: State['input'], delay: Props['throttleDelay']): void
    /**
     * Callback на выбор
     */
    onSelect(item: TOption): void
    /**
     * Callback на переключение
     */
    onToggle(arg: boolean): void
    openOnFocus?: boolean
    /**
     * Массив данных
     */
    options: TOption[]
    page?: number
    /**
     * Placeholder контрола
     */
    placeholder?: string
    /**
     * Флаг авто размера попапа
     */
    popupAutoSize: boolean
    /**
     * Фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
     */
    resetOnBlur: boolean
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    setFilter(arg: Pick<any, number | symbol>): void
    size?: number
    /**
     * Ключ сортировки в данных
     */
    sortFieldId: string
    statusFieldId: string
    style: object
    throttleDelay?: number
    /**
     * Значение
     */
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    value: any
    /**
     * Ключ id в данных
     */
    valueFieldId: string
    quickSearchParam: string
    searchMinLength?: number
}
