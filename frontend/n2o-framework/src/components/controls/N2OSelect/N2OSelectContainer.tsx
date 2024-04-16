import React from 'react'

import { Filter, TOption } from '@i-novus/n2o-components/lib/inputs/InputSelect/types'
import { BadgeType } from '@i-novus/n2o-components/lib/inputs/InputSelect/PopupList'
import { Select } from '@i-novus/n2o-components/lib/inputs/Select/Select'

import listContainer from '../listContainer'

/**
 * Контейнер для {@link N2OSelect}
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {array} options - данные
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {object} badge - данные для баджа
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {string} filter - варианты фильтрации
 * @reactProps {string} value - текущее значение
 * @reactProps {function} onInput - callback при вводе в инпут
 * @reactProps {function} onSelect - callback при выборе значения из popup
 * @reactProps {function} onScrollENd - callback при прокрутке скролла popup
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {boolean} resetOnBlur - фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
 * @reactProps {function} onOpen - callback на открытие попапа
 * @reactProps {function} onClose - callback на закрытие попапа
 * @reactProps {string} queryId - queryId
 * @reactProps {number} size - size
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {boolean} closePopupOnSelect - флаг закрытия попапа при выборе
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {string} format - формат
 */

function N2OSelectContainer(props: Props) {
    const { filter, loading } = props
    const filterType = filter === 'server' ? false : filter

    // @ts-ignore FIXME нужно привести типы в порядок, объеденить с i select
    return <Select {...props} filter={filterType as Filter} loading={loading} />
}

type Props = {
    options?: TOption[],
    loading: boolean,
    valueFieldId?: string,
    labelFieldId?: string,
    iconFieldId?: string,
    imageFieldId?: string,
    badge?: BadgeType,
    disabled: boolean,
    disabledValues: [],
    filter?: Filter,
    value?: string | number,
    onInput(): void,
    onSelect(): void,
    placeholder?: string,
    resetOnBlur: boolean,
    onOpen(): void,
    onClose(): void,
    groupFieldId?: string,
    format?: string,
    searchByTap: boolean,
    fetchData(): () => void,
    onSearch(): void,
}

N2OSelectContainer.defaultProps = {
    loading: false,
    disabled: false,
    disabledValues: [],
    resetOnBlur: false,
    hasCheckboxes: false,
    searchByTap: false,
    onInput() {},
    onSelect() {},
    onOpen() {},
    onClose() {},
    fetchData: () => () => {},
    onSearch() {},
} as Props

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default listContainer(N2OSelectContainer as any)
