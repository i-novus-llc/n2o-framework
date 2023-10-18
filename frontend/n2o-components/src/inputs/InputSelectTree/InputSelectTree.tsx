import React, { useCallback, ReactNode, useMemo, useRef, useState, KeyboardEvent } from 'react'
import TreeSelect from 'rc-tree-select'
import difference from 'lodash/difference'
import filterF from 'lodash/filter'
import find from 'lodash/find'
import isArray from 'lodash/isArray'
import isEmpty from 'lodash/isEmpty'
import keys from 'lodash/keys'
import map from 'lodash/map'
import some from 'lodash/some'
import isNaN from 'lodash/isNaN'
import classNames from 'classnames'
import { withTranslation } from 'react-i18next'
import onClickOutsideHOC from 'react-onclickoutside'

import { Filter, TOption } from '../InputSelect/types'
import { Icon } from '../../display/Icon'
import { InlineSpinner } from '../../layouts/Spinner/InlineSpinner'
import { Checkbox } from '../Checkbox/Checkbox'

import { TreeNode } from './TreeSelectNode'
import { visiblePartPopup, getCheckedStrategy } from './until'

const renderSwitcherIcon = ({ isLeaf }: {isLeaf: boolean}) => (isLeaf ? null : <Icon name="fa fa-chevron-right" />)
// eslint-disable-next-line @typescript-eslint/no-explicit-any
const getPopupContainer = (container: any) => container
/**
 * Взять данные по ids.
 * ['id', 'id'] => [{ id: 'id', ... },{ id: 'id', ... }]
 * @param options
 * @param ids
 * @param valueFieldId
 */
const getDataByIds = (options: Props['options'], ids: string[], valueFieldId: Props['valueFieldId']) => filterF(options, node => some(ids, v => v === node[valueFieldId as keyof TOption]))

const getSingleValue = (options: Props['options'], value: Props['value'], valueFieldId: Props['valueFieldId']) => find(options, [valueFieldId, value])
const getMultiValue = (options: Props['options'], value: Props['value'], valueFieldId: Props['valueFieldId']) => getDataByIds(options, value, valueFieldId)

/**
 * Функция преобразования value rcTreeSelect в формат n2o
 * Производит поиск по родителям и потомкам.
 * rcTreeSelect не дает информации о выделенных потомках при моде 'SHOW_PARENT'
 * и о выделенных родителях при 'SHOW_CHILD'
 * ['id', 'id'] => [{ id: 'id', ... },{ id: 'id', ... }]
 * @param options
 * @param value
 * @param multiSelect
 * @param valueFieldId
 * @returns {*}
 */
const getItemByValue = (
    options: Props['options'],
    value: Props['value'],
    multiSelect: Props['multiSelect'],
    valueFieldId: Props['valueFieldId']
) => {
    if (!value) { return null }
    if (!multiSelect) {
        return getSingleValue(options, value, valueFieldId)
    }

    return getMultiValue(options, value, valueFieldId)
}

function getId<
    TValue extends Record<string, unknown>,
    FieldId extends keyof TValue
>(value: TValue, valueFieldId: FieldId) {
    const numberValue = Number(value[valueFieldId])

    return isNaN(numberValue) ? value[valueFieldId] : numberValue
}

function mapValue2RC<
    TValue extends Record<string, unknown>,
    FieldId extends keyof TValue
>(value: void | TValue | TValue[], valueFieldId: FieldId) {
    if (!value) { return [] }
    if (isArray(value)) {
        return value.map((v) => getId(v, valueFieldId))
    }

    return getId(value, valueFieldId)
}

/**
 * @reactProps {function} onBlur
 * @reactProps {string} placeholder
 * @reactProps {function} setTreeExpandedKeys
 * @reactProps {array} treeExpandedKeys
 * @reactProps {node} children
 * @returns {*}
 * @constructor
 * @param props
 */
function InputSelectTree({
    t,
    fetchData,
    onFocus,
    value,
    onBlur,
    placeholder,
    notFoundContent = t('noData'),
    loading,
    parentFieldId,
    valueFieldId,
    labelFieldId,
    iconFieldId,
    imageFieldId,
    badge,
    hasChildrenFieldId,
    options,
    onSearch,
    onChange,
    onKeyDown,
    hasCheckboxes,
    filter,
    multiSelect,
    children,
    onClose,
    onToggle,
    ajax,
    className,
    showCheckedStrategy,
    maxTagTextLength,
    maxTagCount,
    disabled = false,
}: Props) {
    const treeExpandedKeys = useRef([])
    const [searchValue, setSearchValue] = useState('');

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    (InputSelectTree as any).handleClickOutside = () => setSearchValue('')

    /**
     * Функуия для создания дерева.
     * Преобразует коллекцию вида [..., { ... }] в [ ..., {..., children: [...]}]
     * Вложение происходит при совпадении valueFieldId и parentFieldId.
     * @param items
     * @returns {*}
     */
    const treeData = useMemo(() => {
        const popupProps = {
            prefixCls: 'n2o-select-tree',
            iconFieldId,
            imageFieldId,
            labelFieldId,
            badge,
        }
        const items = options || []
        const itemsByID = [...items].reduce(
            (acc, item) => ({
                ...acc,
                [item[valueFieldId as keyof TOption]]: {
                    ...item,
                    key: item[valueFieldId as keyof TOption],
                    value: item[valueFieldId as keyof TOption],
                    title: item.formattedTitle || visiblePartPopup(item, popupProps),
                    ...(ajax && { isLeaf: !item[hasChildrenFieldId as keyof TOption] }),
                    children: [],
                    /* игнорирование встроенного параметра из rc-tree-select, иконку отрисовывает visiblePartPopup */
                    icon: null,
                },
            }),
            {},
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        ) as any

        keys(itemsByID).forEach((key) => {
            if (
                itemsByID[key][parentFieldId] &&
                itemsByID[itemsByID[key][parentFieldId]] &&
                itemsByID[itemsByID[key][parentFieldId]].children
            ) {
                itemsByID[itemsByID[key][parentFieldId]].children.push({
                    ...itemsByID[key],
                })
            }
        })

        return keys(itemsByID)
            .filter((key) => {
                if (!itemsByID[key][parentFieldId]) {
                    return true
                }

                return !!(itemsByID[key][parentFieldId] &&
                    // eslint-disable-next-line no-prototype-builtins
                    !itemsByID.hasOwnProperty(itemsByID[key][parentFieldId]))
            })
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            .reduce((acc, key) => [...acc, { ...itemsByID[key] }] as any, [])
    }, [
        ajax, badge, options, hasChildrenFieldId,
        iconFieldId, imageFieldId, labelFieldId,
        parentFieldId, valueFieldId,
    ])

    /**
     * Функция для поиска.
     * При поиске вызов функции происходит для каждого элемента дерева.
     * @param input
     * @param node
     * @returns {*}
     */
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const handlerFilter = useCallback((input: string, node: any) => {
        const mode = ['includes', 'startsWith', 'endsWith']

        if (mode.includes(filter)) {
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            return (String.prototype[filter as any] as any).call(
                node.props[labelFieldId].toLowerCase(),
                input.toLowerCase(),
            )
        }

        return true
    }, [filter, labelFieldId])

    const rcValue = useMemo(() => mapValue2RC(value, valueFieldId), [value, valueFieldId])

    /**
     * Функция для переопределения onChange
     * @param value
     */
    const handleChange = useCallback((value: Props['value']) => {
        onChange(getItemByValue(options, value, multiSelect, valueFieldId))
        onBlur(getItemByValue(options, value, multiSelect, valueFieldId))
    }, [options, multiSelect, onChange, valueFieldId])

    /**
     * Функция для переопределения onSearch
     * @param value
     */
    const handleSearch = useCallback((value: Props['value']) => {
        setSearchValue(value)
        onSearch(value)

        return true
    }, [onSearch])

    const clearSearch = useCallback(() => {
        setSearchValue('')
    }, [])

    /**
     * Функция для контроля открытия/закрытия popup
     * @param visible
     * @returns {boolean}
     */
    const handleDropdownVisibleChange = useCallback((visible: boolean) => {
        if (visible) {
            onFocus()
        }
        if (visible) {
            fetchData()
        } else {
            onClose()
        }
        if (ajax) { treeExpandedKeys.current = [] }

        return false
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [ajax, onClose, onFocus, fetchData, onToggle])

    /**
     * Функция для контроля открытия/закрытия элемента дерева
     * @param keys
     */
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const onTreeExpand = useCallback(async (keys: any) => {
        const currentKey = difference(keys, treeExpandedKeys.current)

        if (ajax) {
            await fetchData({ 'filter.parent_id': currentKey[0] }, true)
        }
        treeExpandedKeys.current = keys
    }, [ajax, fetchData])

    const clearIcon = (
        <Icon
            onClick={clearSearch}
            name="fa fa-times n2o-input-select-tree__clear-icon"
        />
    )

    const maxTagPlaceholder = useCallback((options = []) => {
        if (maxTagCount) { return `+ ${options.length}...` }

        return `${t('selected')}: ${options.length}`
    }, [maxTagCount])

    return (
        <div className={classNames(
            'n2o-select-tree-container',
            className,
            {
                empty: isEmpty(value), multi: multiSelect, single: !multiSelect,
            },
        )}
        >
            <TreeSelect
                allowClear={!isEmpty(value)}
                value={rcValue}
                onDropdownVisibleChange={handleDropdownVisibleChange}
                switcherIcon={renderSwitcherIcon}
                suffixIcon={<Icon name="fa fa-chevron-down" visible={!disabled} />}
                multiple={multiSelect}
                treeCheckable={hasCheckboxes && <Checkbox inline />}
                treeData={treeData}
                filterTreeNode={handlerFilter}
                treeNodeFilterProp={labelFieldId}
                treeNodeLabelProp={labelFieldId}
                maxTagTextLength={maxTagTextLength}
                maxTagCount={maxTagCount}
                maxTagPlaceholder={maxTagPlaceholder}
                clearIcon={clearIcon} // иконка очищения всего инпута
                removeIcon={isEmpty(value) ? null : clearIcon} // иконка очищения итема
                onChange={handleChange}
                onSearch={handleSearch}
                onKeyDown={onKeyDown}
                onTreeExpand={onTreeExpand}
                showCheckedStrategy={getCheckedStrategy(showCheckedStrategy)}
                getPopupContainer={getPopupContainer}
                notFoundContent={loading ? <InlineSpinner /> : notFoundContent}
                placeholder={searchValue ? null : placeholder}
                disabled={disabled}
                searchValue={searchValue}
                showSearch={!multiSelect}
                listHeight={400}
                prefixCls="n2o-select-tree"
            >
                {children}
            </TreeSelect>
        </div>
    )
}

InputSelectTree.defaultProps = {
    children: null,
    hasChildrenFieldId: 'hasChildren',
    disabled: false,
    loading: false,
    parentFieldId: 'parentId',
    valueFieldId: 'id',
    labelFieldId: 'name',
    iconFieldId: 'icon',
    badge: {
        fieldId: 'badge',
        colorFieldId: 'color',
    },
    imageFieldId: 'image',
    sortFieldId: 'name',
    filter: 'startsWith',
    hasCheckboxes: false,
    multiSelect: false,
    options: [],
    transitionName: 'slide-up',
    choiceTransitionName: 'zoom',
    showCheckedStrategy: 'all',
    allowClear: true,
    placeholder: '',
    showSearch: true,
    maxTagTextLength: 10,
    onSearch: () => {},
    onChange: () => {},
    onClose: () => {},
    onToggle: () => {},
    onFocus: () => {},
    onBlur: () => {},
    onInput: () => {},
    t: () => {},
} as Partial<Props>

type Props = {
    /**
     * Флаг динамичексой подгрузки данных. В данных обязательно указывать параметр hasChildrens
     */
    ajax?: boolean,
    /**
     * Данные для badge
     */
    badge: object,
    children: ReactNode,
    className?: string,
    /**
     * Флаг неактивности
     */
    disabled: boolean,
    /**
     * Callback на открытие
     */
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    fetchData?: any,
    /**
     * Варианты фильтрации
     */
    filter: Filter,
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
    imageFieldId: string,
    /**
     * Значение ключа label в данных
     */
    labelFieldId: string,
    /**
     * Флаг анимации загрузки
     */
    loading: boolean,
    /**
     * Количество символов выбранных элементов в chechbox режиме
     */
    maxTagTextLength: number,
    maxTagCount: number,
    /**
     * Мульти выбор значений
     */
    multiSelect: boolean,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    notFoundContent?: any,
    onBlur(arg?: Props['value']): void,
    /**
     * Calback изменения
     */
    onChange(arg: Props['value']): void,
    /**
     * Callback на закрытие
     */
    onClose(): void,
    onFocus(): void,
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void,
    /**
     * Callback на поиск
     */
    onSearch(arg: Props['value']): void,
    onToggle(arg: boolean): void,
    /**
     * Данные для построения дерева
     */
    options: TOption[],
    /**
     * Значение ключа parent в данных
     */
    parentFieldId: string,
    /**
     * Placeholder контрола
     */
    placeholder: string,
    showCheckedStrategy: string,
    t(arg: string): void,
    /**
     * Значение
     */
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    value?: any,
    /**
     * Значение ключа value в данных
     */
    valueFieldId: string
}

export { TreeNode, InputSelectTree }

const clickOutsideConfig = {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    handleClickOutside: () => (InputSelectTree as any).handleClickOutside,
}

export const InputSelectTreeComponent = (
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    onClickOutsideHOC(withTranslation()(InputSelectTree as any), clickOutsideConfig) as any
)
