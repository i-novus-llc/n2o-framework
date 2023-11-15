import React, { useCallback, useMemo, useRef, useState } from 'react'
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
import PropTypes from 'prop-types'
import { withTranslation } from 'react-i18next'
import onClickOutsideHOC from 'react-onclickoutside'

import { Icon } from '../../snippets/Icon/Icon'
import InlineSpinner from '../../snippets/Spinner/InlineSpinner'
import Checkbox from '../Checkbox/Checkbox'

import TreeNode from './TreeSelectNode'
import { visiblePartPopup, getCheckedStrategy } from './until'

const renderSwitcherIcon = ({ isLeaf }) => (isLeaf ? null : <Icon name="fa fa-chevron-right" />)
// eslint-disable-next-line @typescript-eslint/no-explicit-any
const getPopupContainer = container => container
/**
 * Взять данные по ids.
 * ['id', 'id'] => [{ id: 'id', ... },{ id: 'id', ... }]
 * @param options
 * @param ids
 * @param valueFieldId
 */
const getDataByIds = (options, ids, valueFieldId) => filterF(options, node => some(ids, v => v === node[valueFieldId]))

const getSingleValue = (options, value, valueFieldId) => find(options, [valueFieldId, value])
const getMultiValue = (options, value, valueFieldId) => getDataByIds(options, value, valueFieldId)

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
    options,
    value,
    multiSelect,
    valueFieldId,
) => {
    if (!value) { return null }
    if (!multiSelect) {
        return getSingleValue(options, value, valueFieldId)
    }

    return getMultiValue(options, value, valueFieldId)
}

function getId(value, valueFieldId) {
    const numberValue = Number(value[valueFieldId])

    return isNaN(numberValue) ? value[valueFieldId] : numberValue
}

function mapValue2RC(value, valueFieldId) {
    if (!value) { return [] }
    if (isArray(value)) {
        return value.map(v => getId(v, valueFieldId))
    }

    return getId(value, valueFieldId)
}

function InputSelectTree({
    t,
    onOpen,
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
    multiSelect,
    children,
    onClose,
    onToggle,
    ajax,
    className,
    showCheckedStrategy,
    maxTagTextLength,
    maxTagCount,
    searchMinLength,
    disabled = false,
}) {
    const treeExpandedKeys = useRef([])
    const [searchValue, setSearchValue] = useState('');

    (InputSelectTree).handleClickOutside = () => setSearchValue('')

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
                [item[valueFieldId]]: {
                    ...item,
                    key: item[valueFieldId],
                    value: item[valueFieldId],
                    title: item.formattedTitle || visiblePartPopup(item, popupProps),
                    ...(ajax && { isLeaf: !item[hasChildrenFieldId] }),
                    children: [],
                    /* игнорирование встроенного параметра из rc-tree-select, иконку отрисовывает visiblePartPopup */
                    icon: null,
                },
            }),
            {},
        )

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
            .reduce((acc, key) => [...acc, { ...itemsByID[key] }], [])
    }, [
        ajax, badge, options, hasChildrenFieldId,
        iconFieldId, imageFieldId, labelFieldId,
        parentFieldId, valueFieldId,
    ])

    const handlerFilter = useCallback((input, node) => {
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
    const handleChange = useCallback((value) => {
        onChange(getItemByValue(options, value, multiSelect, valueFieldId))
        onBlur(getItemByValue(options, value, multiSelect, valueFieldId))
    }, [options, multiSelect, onChange, valueFieldId])

    /**
     * Функция для переопределения onSearch
     * @param value
     */
    const handleSearch = useCallback((value) => {
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
    const handleDropdownVisibleChange = useCallback((visible) => {
        if (visible) {
            onFocus()
        }
        if (visible) {
            onOpen()
        } else {
            onClose()
        }
        if (ajax) { treeExpandedKeys.current = [] }

        return false
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [ajax, onClose, onFocus, onOpen, onToggle])

    /**
     * Функция для контроля открытия/закрытия элемента дерева
     * @param keys
     */

    const onTreeExpand = useCallback(async (keys) => {
        const currentKey = difference(keys, treeExpandedKeys.current)

        if (ajax) {
            await onOpen({ 'filter.parent_id': currentKey[0] }, true)
        }
        treeExpandedKeys.current = keys
    }, [ajax, onOpen])

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
                showSearch
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
    hasCheckboxes: false,
    multiSelect: false,
    options: [],
    showCheckedStrategy: 'all',
    placeholder: '',
    maxTagTextLength: 10,
    onSearch: () => {},
    onChange: () => {},
    onClose: () => {},
    onToggle: () => {},
    onFocus: () => {},
    onBlur: () => {},
    t: () => {},
}

InputSelectTree.propTypes = {
    t: PropTypes.func,
    onToggle: PropTypes.func,
    onFocus: PropTypes.func,
    onKeyDown: PropTypes.func,
    onBlur: PropTypes.func,
    className: PropTypes.string,
    notFoundContent: PropTypes.any,

    children: PropTypes.node,
    /**
     * Значение ключа hasChildren в данных
     */
    hasChildrenFieldId: PropTypes.string,
    /**
     * Значение ключа parent в данных
     */
    parentFieldId: PropTypes.string,
    /**
     * Флаг анимации загрузки
     */
    loading: PropTypes.bool,
    /**
     * Данные для построения дерева
     */
    options: PropTypes.array,
    /**
     * Значение ключа value в данных
     */
    valueFieldId: PropTypes.string,
    /**
     * Значение ключа label в данных
     */
    labelFieldId: PropTypes.string,
    /**
     * Значение ключа icon в данных
     */
    iconFieldId: PropTypes.string,
    /**
     *  Значение ключа image в данных
     */
    imageFieldId: PropTypes.string,
    /**
     * Данные для badge
     */
    badge: PropTypes.object,
    /**
     * Флаг неактивности
     */
    disabled: PropTypes.bool,
    /**
     * Значение
     */
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
     * Calback изменения
     */
    onChange: PropTypes.func,
    /**
     * Placeholder контрола
     */
    placeholder: PropTypes.string,
    /**
     * Callback на открытие
     */
    onOpen: PropTypes.func,
    /**
     * Callback на закрытие
     */
    onClose: PropTypes.func,
    /**
     * Мульти выбор значений
     */
    multiSelect: PropTypes.bool,
    /**
     * Флаг для показа чекбоксов в элементах дерева. Переводит InputSelectTree в мульти режим
     */
    hasCheckboxes: PropTypes.bool,
    /**
     * Callback на поиск
     */
    onSearch: PropTypes.func,
    /**
     * Флаг динамичексой подгрузки данных. В данных обязательно указывать параметр hasChildrens
     */
    ajax: PropTypes.bool,
    showCheckedStrategy: PropTypes.string,
    /**
     * Количество символов выбранных элементов в chechbox режиме
     */
    maxTagTextLength: PropTypes.number,
}

export { TreeNode, InputSelectTree }

const clickOutsideConfig = {
    handleClickOutside: () => InputSelectTree.handleClickOutside,
}

export const InputSelectTreeComponent = (
    onClickOutsideHOC(withTranslation()(InputSelectTree), clickOutsideConfig)
)
