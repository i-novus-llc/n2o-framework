import { ComponentType, ReactNode } from 'react'

import { Props as BadgeProps } from '../../display/Badge/Badge'

export enum Filter {
    endsWith = 'endsWith',
    includes = 'includes',
    server = 'server',
    startsWith = 'startsWith',
}
export type idField = keyof Pick<Option, 'id'>
export type parentIdField = keyof Pick<Option, 'parentId'>

export type getSearchMinLengthHintType = (
    customHint?: string,
    component?: ComponentType
) => null | string | JSX.Element
export type BadgeType = BadgeProps & {
    colorFieldId: string
    fieldId: string
    imageFieldId: string
}

export interface Option<T = string> {
    id: string | number
    className?: string
    disabled?: boolean
    formattedTitle?: string
    parentId?: string | number
    label?: string
    value?: T
    enabled?: boolean
}

export type Options = Option[]

export interface Props {
    searchPlaceholder: string
    transitionName: string
    choiceTransitionName: string
    allowClear: boolean
    showSearch: boolean
    onSelect?(item: Option): void;
    onToggle(): void;
    onFocus(): void
    onBlur(): void
    /**
   * Флаг динамичексой подгрузки данных. В данных обязательно указывать параметр hasChildrens
   */
    ajax?: boolean,
    /**
   * Значение ключа badgeColor в данных
   */
    badgeColorFieldId: string,
    /**
   * Значение ключа badge в данных
   */
    badgeFieldId: string,
    children: ReactNode,
    /**
   * Флаг закрытия попапа при выборе элемента
   */
    closePopupOnSelect: boolean,
    /**
   * Данные для построения дерева
   */
    data: Options,
    /**
   * Флаг неактивности
   */
    disabled: boolean,
    /**
   * Неактивные данные
   */
    disabledValues?: [],
    /**
   * Выравнивание попапа
   */
    dropdownPopupAlign: object,
    expandPopUp?: boolean,
    /**
   * Варианты фильтрации
   */
    filter?: Filter,
    /**
   * Формат
   */
    format?: string,
    groupFieldId?: string,
    /**
   * Флаг для показа чекбоксов в элементах дерева. Переводит InputSelectTree в мульти режим
   */
    hasCheckboxes: boolean,
    /**
   * Значение ключа hasChildren в данных
   */
    hasChildrenFieldId: string,
    /**
   * Значение ключа icon в данных
   */
    iconFieldId: string,
    /**
   *  Значение ключа image в данных
   */
    imageFieldId?: string,
    /**
   * Значение ключа label в данных
   */
    labelFieldId: string,
    /**
   * От скольки элементов сжимать выбранные элементы
   */
    maxTagCount?: number,
    /**
   * Флаг анимации загрузки
   */
    loading: boolean,
    /**
   * Мульти выбор значений
   */
    multiSelect: boolean,
    /**
   * Calback изменения
   */
    onChange(): void,
    /**
   * Callback на закрытие
   */
    onClose(): void,
    onInput(): void,
    /**
   * Callback на открытие
   */
    onOpen(): void,
    /**
   * Callback на поиск
   */
    onSearch(): void,
    options: Options,
    /**
   * Значение ключа parent в данных
   */
    parentFieldId: parentIdField,
    /**
   * Placeholder контрола
   */
    placeholder: string,
    showCheckedStrategy: string,
    /**
   * Значение ключа сортировки в данных
   */
    sortFieldId: string,
    /**
   * Значение
   */
    value?: Options,
    /**
   * Значение ключа value в данных
   */
    valueFieldId: idField
}
