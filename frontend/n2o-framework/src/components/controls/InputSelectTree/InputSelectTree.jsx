import React, { useCallback, useMemo, useRef, useState } from 'react'
import TreeSelect from 'rc-tree-select'
import difference from 'lodash/difference'
import filterF from 'lodash/filter'
import find from 'lodash/find'
import isArray from 'lodash/isArray'
import isEmpty from 'lodash/isEmpty'
import keys from 'lodash/keys'
import map from 'lodash/map'
import some from 'lodash/some'
import classNames from 'classnames'
import PropTypes from 'prop-types'
import { compose, setDisplayName } from 'recompose'
import { withTranslation } from 'react-i18next'
import onClickOutsideHOC from 'react-onclickoutside'

import { Icon } from '../../snippets/Icon/Icon'
import InlineSpinner from '../../snippets/Spinner/InlineSpinner'
// eslint-disable-next-line import/no-named-as-default
import CheckboxN2O from '../Checkbox/CheckboxN2O'
import propsResolver from '../../../utils/propsResolver'

import TreeNode from './TreeSelectNode'
import { visiblePartPopup, getCheckedStrategy } from './until'

const renderSwitcherIcon = ({ isLeaf }) => (isLeaf ? null : <Icon name="fa fa-chevron-right" />)
const getPopupContainer = container => container
/**
 * Взять данные по ids.
 * ['id', 'id'] => [{ id: 'id', ... },{ id: 'id', ... }]
 * @param data
 * @param ids
 * @param valueFieldId
 */
const getDataByIds = (data, ids, valueFieldId) => filterF(data, node => some(ids, v => v === node[valueFieldId]))

const getSingleValue = (data, value, valueFieldId) => find(data, [valueFieldId, value])
const getMultiValue = (data, value, valueFieldId) => getDataByIds(data, value, valueFieldId)

/**
 * Функция преобразования value rcTreeSelect в формат n2o
 * Производит поиск по родителям и потомкам.
 * rcTreeSelect не дает информации о выделенных потомках при моде 'SHOW_PARENT'
 * и о выделенных родителях при 'SHOW_CHILD'
 * ['id', 'id'] => [{ id: 'id', ... },{ id: 'id', ... }]
 * @param data
 * @param value
 * @param multiSelect
 * @param valueFieldId
 * @returns {*}
 */
const getItemByValue = (data, value, multiSelect, valueFieldId) => {
    if (!value) { return null }
    if (!multiSelect) {
        return getSingleValue(data, value, valueFieldId)
    }

    return getMultiValue(data, value, valueFieldId)
}

/**
 * @reactProps {function} onBlur
 * @reactProps {any} searchPlaceholder
 * @reactProps {string} placeholder
 * @reactProps {function} setTreeExpandedKeys
 * @reactProps {array} treeExpandedKeys
 * @reactProps {node} children
 * @returns {*}
 * @constructor
 * @param props
 */
// TODO переделать в класс
function InputSelectTree(props) {
    const {
        t,
        onOpen,
        onFocus,
        value,
        onBlur,
        searchPlaceholder,
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
        format,
        data,
        onSearch,
        onSelect,
        onChange,
        onKeyDown,
        hasCheckboxes,
        filter,
        multiSelect,
        children,
        onClose,
        onToggle,
        handleItemOpen,
        ajax,
        className,
        showCheckedStrategy,
        maxTagTextLength,
        disabled,
    } = props

    const treeExpandedKeys = useRef([])
    const [searchValue, setSearchValue] = useState('')

    InputSelectTree.handleClickOutside = () => setSearchValue('')

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
        const items = data || []
        const itemsByID = [...items].reduce(
            (acc, item) => ({
                ...acc,
                [item[valueFieldId]]: {
                    ...item,
                    key: item[valueFieldId],
                    value: item[valueFieldId],
                    title: format
                        ? propsResolver({ format }, item).format
                        : visiblePartPopup(item, popupProps),
                    ...(ajax && { isLeaf: !item[hasChildrenFieldId] }),
                    children: [],
                },
            }),
            {},
        )

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
            .reduce((acc, key) => [...acc, { ...itemsByID[key] }], [])
    }, [
        ajax, badge, data,
        format, hasChildrenFieldId,
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
    const handlerFilter = useCallback((input, node) => {
        const mode = ['includes', 'startsWith', 'endsWith']

        if (mode.includes(filter)) {
            return String.prototype[filter].call(
                node.props[labelFieldId].toLowerCase(),
                input.toLowerCase(),
            )
        }

        return true
    }, [filter, labelFieldId])

    /**
     * Функция для обратного преобразования value n2o в формат rcTreeSelect
     * ['id', 'id'] => [{ id: 'id', ... },{ id: 'id', ... }]
     * @param value
     * @returns {*}
     */
    const setValue = (value) => {
        if (!value) { return [] }
        if (isArray(value)) {
            return map(value, v => v[valueFieldId])
        }

        return value[valueFieldId]
    }

    /**
     * Функция для переопределения onChange
     * @param value
     */
    const handleChange = useCallback((value) => {
        onChange(getItemByValue(data, value, multiSelect, valueFieldId))
        onBlur(getItemByValue(data, value, multiSelect, valueFieldId))
    }, [data, multiSelect, onChange, valueFieldId])

    /**
     * Функция для переопределения onSelect
     * @param value
     */
    const handleSelect = useCallback((value) => {
        onSelect(getItemByValue(data, value, multiSelect, valueFieldId))
        onBlur()
    }, [data, multiSelect, onSelect, valueFieldId])

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
        // onToggle(visible)
        // setDropdownExpanded(visible)

        if (visible && !data.length) {
            onOpen()
        } else {
            onClose()
        }
        if (ajax) { treeExpandedKeys.current = [] }

        return false
    }, [ajax, onClose, onFocus, onOpen, onToggle])

    /**
     * Функция для контроля открытия/закрытия элемента дерева
     * @param keys
     */
    const onTreeExpand = useCallback(async (keys) => {
        const currentKey = difference(keys, treeExpandedKeys.current)

        if (ajax) {
            await handleItemOpen(currentKey[0])
        }
        treeExpandedKeys.current = keys
    }, [ajax, handleItemOpen])

    const clearIcon = (
        <Icon
            onClick={clearSearch}
            name="fa fa-times n2o-input-select-tree__clear-icon"
        />
    )

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
                value={setValue(value)}
                onDropdownVisibleChange={handleDropdownVisibleChange}
                switcherIcon={renderSwitcherIcon}
                suffixIcon={<Icon name="fa fa-chevron-down" />}
                multiple={multiSelect}
                treeCheckable={hasCheckboxes && <CheckboxN2O inline />}
                treeData={treeData}
                filterTreeNode={handlerFilter}
                treeNodeFilterProp={labelFieldId}
                treeNodeLabelProp={labelFieldId}
                maxTagTextLength={maxTagTextLength}
                clearIcon={clearIcon} // иконка очищения всего инпута
                removeIcon={isEmpty(value) ? null : clearIcon} // иконка очищения итема
                onChange={handleChange}
                onSelect={handleSelect}
                onSearch={handleSearch}
                onKeyDown={onKeyDown}
                onTreeExpand={onTreeExpand}
                showCheckedStrategy={getCheckedStrategy(showCheckedStrategy)}
                getPopupContainer={getPopupContainer}
                notFoundContent={loading ? <InlineSpinner /> : notFoundContent}
                placeholder={searchValue ? null : placeholder}
                searchPlaceholder={searchPlaceholder}
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
    filter: 'startsWith',
    hasCheckboxes: false,
    multiSelect: false,
    data: [],
    searchPlaceholder: '',
    // eslint-disable-next-line react/default-props-match-prop-types
    transitionName: 'slide-up',
    // eslint-disable-next-line react/default-props-match-prop-types
    choiceTransitionName: 'zoom',
    showCheckedStrategy: 'all',
    // eslint-disable-next-line react/default-props-match-prop-types
    allowClear: true,
    placeholder: '',
    // eslint-disable-next-line react/default-props-match-prop-types
    showSearch: true,
    maxTagTextLength: 10,
    onSearch: () => {},
    onSelect: () => {},
    onChange: () => {},
    onClose: () => {},
    onToggle: () => {},
    onOpen: () => {},
    onFocus: () => {},
    onBlur: () => {},
    onKeyDown: () => {},
    t: () => {},
}

InputSelectTree.propTypes = {
    t: PropTypes.func,
    onSelect: PropTypes.func,
    onToggle: PropTypes.func,
    onFocus: PropTypes.func,
    onKeyDown: PropTypes.func,
    onBlur: PropTypes.func,
    className: PropTypes.string,
    searchPlaceholder: PropTypes.string,
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
    data: PropTypes.array,
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
     * Варианты фильтрации
     */
    filter: PropTypes.oneOf(['includes', 'startsWith', 'endsWith', 'server']),
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
     * Формат
     */
    format: PropTypes.string,
    /**
     * Callback на поиск
     */
    onSearch: PropTypes.func,
    /**
     * Флаг динамичексой подгрузки данных. В данных обязательно указывать параметр hasChildrens
     */
    ajax: PropTypes.bool,
    /**
     * Сallback функция вызываемая ajax true. Передает value открывающего элемента дерева
     */
    handleItemOpen: PropTypes.func,
    showCheckedStrategy: PropTypes.string,
    /**
     * Количество символов выбранных элементов в chechbox режиме
     */
    maxTagTextLength: PropTypes.number,
}

export { TreeNode, InputSelectTree }

const enhance = compose(
    withTranslation(),
    setDisplayName('InputSelectTree'),
)(InputSelectTree)

const clickOutsideConfig = {
    handleClickOutside: () => InputSelectTree.handleClickOutside,
}

export default onClickOutsideHOC(enhance, clickOutsideConfig)
