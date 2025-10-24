import React from 'react'
import { Filter, type TOption } from '@i-novus/n2o-components/lib/inputs/InputSelect/types'
import { type BadgeType } from '@i-novus/n2o-components/lib/inputs/InputSelect/PopupList'
import { Select } from '@i-novus/n2o-components/lib/inputs/Select/Select'

import { listContainer } from '../listContainer'
import { EMPTY_ARRAY, NOOP_FUNCTION } from '../../../utils/emptyTypes'

const fetchDataNoop = () => () => {}

/**
 * Контейнер для {@link N2OSelect}
 * @param {object} props - Свойства компонента
 * @param {boolean} [props.loading=false] - Флаг анимации загрузки
 * @param {Array} [props.options] - Данные для выбора
 * @param {string} [props.valueFieldId] - Ключ поля значения в данных
 * @param {string} [props.labelFieldId] - Ключ поля метки в данных
 * @param {string} [props.iconFieldId] - Поле для иконки
 * @param {string} [props.imageFieldId] - Поле для картинки
 * @param {object} [props.badge] - Данные для баджа
 * @param {boolean} [props.disabled=false] - Флаг неактивности
 * @param {Array} [props.disabledValues=[]] - Неактивные данные
 * @param hasCheckboxes
 * @param {string} [props.filter] - Варианты фильтрации
 * @param {string|number} [props.value] - Текущее значение
 * @param {function} [props.onInput=() => {}] - Callback при вводе в инпут
 * @param {function} [props.onSelect=() => {}] - Callback при выборе значения
 * @param {string} [props.placeholder] - Подсказка в инпуте
 * @param {boolean} [props.resetOnBlur=false] - Сброс значения при потере фокуса
 * @param {function} [props.onOpen=() => {}] - Callback открытия попапа
 * @param {function} [props.onClose=() => {}] - Callback закрытия попапа
 * @param {string} [props.groupFieldId] - Поле для группировки
 * @param {string} [props.format] - Формат отображения
 * @param {boolean} [props.searchByTap=false] - Поиск по тапу
 * @param {function} [props.fetchData=() => () => {}] - Функция загрузки данных
 * @param {function} [props.onSearch=() => {}] - Callback поиска
 */
function Wrapper({
    loading = false,
    disabled = false,
    disabledValues = EMPTY_ARRAY,
    resetOnBlur = false,
    hasCheckboxes = false,
    searchByTap = false,
    onInput = NOOP_FUNCTION,
    onSelect = NOOP_FUNCTION,
    onOpen = NOOP_FUNCTION,
    onClose = NOOP_FUNCTION,
    fetchData = fetchDataNoop,
    onSearch = NOOP_FUNCTION,
    filter,
    ...props
}: Props) {
    const filterType = filter === 'server' ? false : filter

    return (
        // @ts-ignore FIXME нужно привести типы в порядок, объеденить с i select
        <Select
            {...props}
            filter={filterType as Filter}
            loading={loading}
            disabled={disabled}
            disabledValues={disabledValues as never}
            resetOnBlur={resetOnBlur}
            hasCheckboxes={hasCheckboxes}
            searchByTap={searchByTap}
            onInput={onInput}
            onSelect={onSelect}
            onOpen={onOpen}
            onClose={onClose}
            fetchData={fetchData}
            onSearch={onSearch}
        />
    )
}

export interface Props {
    options?: TOption[]
    loading?: boolean
    valueFieldId?: string
    labelFieldId?: string
    iconFieldId?: string
    imageFieldId?: string
    badge?: BadgeType
    disabled?: boolean
    disabledValues?: unknown[]
    filter?: Filter
    value?: string | number
    onInput?(): void
    onSelect?(): void
    placeholder?: string
    resetOnBlur?: boolean
    onOpen?(): void
    onClose?(): void
    groupFieldId?: string
    format?: string
    searchByTap?: boolean
    fetchData?(): () => void
    onSearch?(): void
    hasCheckboxes?: boolean
    [key: string]: unknown
}

export const N2OSelectContainer = listContainer<Props>(Wrapper)
export default N2OSelectContainer
