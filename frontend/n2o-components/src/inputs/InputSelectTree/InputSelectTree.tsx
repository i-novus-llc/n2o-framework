import React, { useCallback, ReactNode, useMemo, useRef, useState, KeyboardEvent } from 'react'
import TreeSelect from 'rc-tree-select'
import difference from 'lodash/difference'
import filterF from 'lodash/filter'
import find from 'lodash/find'
import isArray from 'lodash/isArray'
import isEmpty from 'lodash/isEmpty'
import keys from 'lodash/keys'
import some from 'lodash/some'
import isNaN from 'lodash/isNaN'
import classNames from 'classnames'
import { withTranslation } from 'react-i18next'
import onClickOutsideHOC from 'react-onclickoutside'

import { TOption, getSearchMinLengthHintType } from '../InputSelect/types'
import { Icon } from '../../display/Icon'
import { InlineSpinner } from '../../layouts/Spinner/InlineSpinner'
import { Checkbox } from '../Checkbox/Checkbox'
import { EMPTY_ARRAY, NOOP_FUNCTION } from '../../utils/emptyTypes'

import { type Options, TreeNode } from './TreeSelectNode'
import { visiblePartPopup, getCheckedStrategy } from './helpers'

const renderSwitcherIcon = ({ isLeaf }: { isLeaf: boolean }) => (isLeaf ? null : <Icon name="fa fa-chevron-right" />)
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
    valueFieldId: Props['valueFieldId'],
) => {
    if (!value) { return null }
    if (!multiSelect) {
        return getSingleValue(options, value, valueFieldId)
    }

    return getMultiValue(options, value, valueFieldId)
}

function getId<
    TValue extends Record<string, unknown>,
    FieldId extends keyof TValue,
>(value: TValue, valueFieldId: FieldId) {
    const numberValue = Number(value[valueFieldId])

    return isNaN(numberValue) ? value[valueFieldId] : numberValue
}

function mapValue2RC<
    TValue extends Record<string, unknown>,
    FieldId extends keyof TValue,
>(value: void | TValue | TValue[], valueFieldId: FieldId) {
    if (!value) { return [] }
    if (isArray(value)) {
        return value.map(v => getId(v, valueFieldId))
    }

    return getId(value, valueFieldId)
}

function InputSelectTree({
    t = NOOP_FUNCTION,
    fetchData,
    onFocus = NOOP_FUNCTION,
    value,
    onBlur = NOOP_FUNCTION,
    placeholder = '',
    notFoundContent = 'Нет данных для отображения',
    loading = false,
    parentFieldId = 'parentId',
    valueFieldId = 'id',
    labelFieldId = 'name',
    enabledFieldId,
    iconFieldId = 'icon',
    imageFieldId = 'image',
    badge = {
        fieldId: 'badge',
        colorFieldId: 'color',
    },
    hasChildrenFieldId = 'hasChildren',
    options = EMPTY_ARRAY,
    onSearch = NOOP_FUNCTION,
    onChange = NOOP_FUNCTION,
    onKeyDown,
    hasCheckboxes = false,
    multiSelect = false,
    children = null,
    onClose = NOOP_FUNCTION,
    onToggle = NOOP_FUNCTION,
    ajax,
    className,
    showCheckedStrategy = 'all',
    maxTagTextLength = 10,
    maxTagCount,
    searchMinLength,
    getSearchMinLengthHint,
    disabled = false,
}: Props) {
    const searchMinLengthHint = getSearchMinLengthHint()
    const treeExpandedKeys = useRef<Array<string | number>>([])
    const [searchValue, setSearchValue] = useState('')
    const handleClickOutside = () => { setSearchValue('') }

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    (InputSelectTree as any).handleClickOutside = () => handleClickOutside

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

        if (searchMinLengthHint) { return [] }

        const items = options || []
        const itemsByID = [...items].reduce(
            (acc, item) => {
                const enabled = item[enabledFieldId as keyof TOption]
                const disabled = typeof enabled === 'boolean' ? !enabled : false

                return ({
                    ...acc,
                    [item[valueFieldId as keyof TOption]]: {
                        ...item,
                        key: item[valueFieldId as keyof TOption],
                        value: item[valueFieldId as keyof TOption],
                        disabled,
                        title: item.formattedTitle || visiblePartPopup(item, popupProps as unknown as Options),
                        ...(ajax && { isLeaf: !item[hasChildrenFieldId as keyof TOption] }),
                        children: [],
                        /* игнорирование встроенного параметра из rc-tree-select, иконку отрисовывает visiblePartPopup */
                        icon: null,
                    },
                })
            },
            {},
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        ) as any

        keys(itemsByID).forEach((key) => {
            if (
                itemsByID[key][parentFieldId] &&
                itemsByID[itemsByID[key][parentFieldId]]
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

    const handlerFilter = useCallback((input: string, node) => {
        if (searchMinLength && input.length < searchMinLength) {
            return true
        }

        return String.prototype.includes.call(
            node.props[labelFieldId].toLowerCase(),
            input.toLowerCase(),
        )
    }, [labelFieldId])

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

    const getNotFoundContent = () => {
        if (searchMinLengthHint) {
            return searchMinLengthHint
        }

        if (loading) {
            return <InlineSpinner />
        }

        return notFoundContent
    }

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
                treeCheckable={hasCheckboxes && <Checkbox preventDefault inline />}
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
                notFoundContent={getNotFoundContent()}
                placeholder={searchValue ? null : placeholder}
                disabled={disabled}
                searchValue={searchValue}
                showSearch
                listHeight={400}
                prefixCls="n2o-select-tree"
            >
                {children}
            </TreeSelect>
        </div>
    )
}

export interface Props {
    ajax?: boolean
    badge: object
    children: ReactNode
    className?: string
    disabled: boolean
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    fetchData?: any
    hasCheckboxes: boolean
    hasChildrenFieldId: string
    iconFieldId: string
    imageFieldId: string
    labelFieldId: string
    enabledFieldId: string
    loading: boolean
    maxTagTextLength: number
    maxTagCount: number
    multiSelect: boolean
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    notFoundContent?: any
    onBlur(arg?: Props['value']): void
    onChange(arg: Props['value']): void
    onClose(): void
    onFocus(): void
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void
    onSearch(arg: Props['value']): void
    onToggle(arg: boolean): void
    options: TOption[]
    parentFieldId: string
    placeholder: string
    showCheckedStrategy: string
    t(arg: string): void
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    value?: any
    valueFieldId: keyof TOption
    searchMinLength?: number
    getSearchMinLengthHint: getSearchMinLengthHintType
}

export { TreeNode, InputSelectTree }

const clickOutsideConfig = {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    handleClickOutside: () => (InputSelectTree as any).handleClickOutside,
}

export const InputSelectTreeComponent = (
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    onClickOutsideHOC(withTranslation()(InputSelectTree as any), clickOutsideConfig)
)
