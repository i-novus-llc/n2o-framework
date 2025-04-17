import { ReactNode } from 'react'

import { Filter, TOption, type Props as InputSelectTreeProps } from '../InputSelect/types'

export interface Props {
    searchPlaceholder: string
    transitionName: string
    choiceTransitionName: string
    allowClear: boolean
    showSearch: boolean
    onSelect: InputSelectTreeProps['onSelect']
    onToggle: InputSelectTreeProps['onToggle']
    onFocus(): void
    onBlur: InputSelectTreeProps['onBlur']
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
    data: TOption[],
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
    options: TOption[],
    /**
   * Значение ключа parent в данных
   */
    parentFieldId: keyof TOption,
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
    value?: TOption[],
    /**
   * Значение ключа value в данных
   */
    valueFieldId: keyof TOption
}
