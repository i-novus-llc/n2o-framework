import { KeyboardEvent } from 'react'

import { BadgeType } from '../InputSelect/PopupList'
import { Filter, TOption, PopUpProps } from '../InputSelect/types'

export interface State {
    input?: string
    isExpanded: boolean
    options: TOption[]
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    selected: any[]
    value: string
    activeValueId: string | null
}

export interface Props {
    popUpItemRef: PopUpProps['popUpItemRef']
    popUpStyle: PopUpProps['popUpStyle']
    /**
     * Данные для badge
     */
    badge?: BadgeType
    className?: string
    cleanable: boolean
    closePopupOnSelect: boolean
    descriptionFieldId: string
    /**
     * Флаг активности
     */
    disabled: boolean
    /**
     * Ключ enabled в данных
     */
    enabledFieldId: string
    fetchData(arg: object): () => void
    /**
     * Фильтрация
     */
    filter?: Filter
    /**
     * Формат
     */
    format?: string
    groupFieldId: string
    /**
     * Флаг наличия поиска
     */
    hasSearch: boolean
    /**
     * Ключ icon в данных
     */
    iconFieldId: string
    /**
     * Ключ image в данных
     */
    imageFieldId: string
    initial?: string | number
    /**
     * Ключ label в данных
     */
    labelFieldId: string
    /**
     * Флаг загрузки
     */
    loading: boolean,
    onBlur(arg: Props['value']): void
    /**
     * Callback на изменение
     */
    onChange(arg: TOption | TOption[] | null | string): void
    /**
     * Callback на закрытие попапа
     */
    onClose(): void
    /**
     * Callback при вводе в инпут
     */
    onInput(input: string): void
    onKeyDown?(evt: KeyboardEvent<HTMLButtonElement>): void
    /**
     * Callback на поиск
     */
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    onSearch(input: State['input'], delay?: boolean, callback?: any): void
    /**
     * Данные
     */
    options: TOption[]
    page?: number
    /**
     * Placeholder контрола
     */
    placeholder?: string
    /**
     * Сброс значения при потере фокуса
     */
    resetOnBlur: boolean
    /**
     * Поиск по нажатию кнопки
     */
    searchByTap: boolean
    selectFormat?: string
    selectFormatFew?: string
    selectFormatMany?: string
    selectFormatOne?: string
    /**
     * Ключ image в данных
     */
    statusFieldId: string
    style?: object
    type?: string
    /**
     * Значение
     */
    value: unknown
    /**
     * Ключ id в данных
     */
    valueFieldId: string
    size?: number
    count?: number
    onOpen?(): void
}
