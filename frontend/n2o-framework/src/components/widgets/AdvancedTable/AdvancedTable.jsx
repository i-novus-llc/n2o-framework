import React, { Component } from 'react'
import { compose, pure, setDisplayName } from 'recompose'
import ReactDom from 'react-dom'
import PropTypes from 'prop-types'
import Table from 'rc-table'
import { HotKeys } from 'react-hotkeys/cjs'
import classNames from 'classnames'
import find from 'lodash/find'
import some from 'lodash/some'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import map from 'lodash/map'
import flattenDeep from 'lodash/flattenDeep'
import isArray from 'lodash/isArray'
import findIndex from 'lodash/findIndex'
import values from 'lodash/values'
import eq from 'lodash/eq'
import get from 'lodash/get'

import propsResolver from '../../../utils/propsResolver'
import SecurityCheck from '../../../core/auth/SecurityCheck'
// eslint-disable-next-line import/no-named-as-default
import CheckboxN2O from '../../controls/Checkbox/CheckboxN2O'
import RadioN2O from '../../controls/Radio/RadioN2O'

// eslint-disable-next-line import/no-named-as-default
import AdvancedTableExpandIcon from './AdvancedTableExpandIcon'
import AdvancedTableExpandedRenderer from './AdvancedTableExpandedRenderer'
import AdvancedTableRow from './AdvancedTableRow'
import AdvancedTableRowWithAction from './AdvancedTableRowWithAction'
import AdvancedTableHeaderCell from './AdvancedTableHeaderCell'
import AdvancedTableEmptyText from './AdvancedTableEmptyText'
import AdvancedTableCell from './AdvancedTableCell'
import AdvancedTableHeaderRow from './AdvancedTableHeaderRow'
// eslint-disable-next-line import/no-named-as-default
import AdvancedTableSelectionColumn from './AdvancedTableSelectionColumn'
import withAdvancedTableRef from './withAdvancedTableRef'

export const getIndex = (data, selectedId) => {
    const index = findIndex(data, model => model.id === selectedId)

    return index >= 0 ? index : 0
}

const KEY_CODES = {
    DOWN: 'down',
    UP: 'up',
    SPACE: 'space',
}

const rowSelectionType = {
    CHECKBOX: 'checkbox',
    RADIO: 'radio',
}

const getFocusIndex = ({ autoFocus, data, selectedId, hasFocus }) => {
    if (autoFocus) {
        if (data && data[selectedId]) {
            return get(data[selectedId], 'id')
        }

        return get(data[0], 'id')
    }
    if (hasFocus) {
        return 0
    }

    return 1
}

/**
 * Компонент Таблица
 * @reactProps {boolean} hasFocus - флаг наличия фокуса
 * @reactProps {boolean} textWrap - флаг на запрет/разрешение переноса текста в cell
 * @reactProps {string} className - класс таблицы
 * @reactProps {Array.<Object>} columns - настройки колонок
 * @reactProps {Array.<Object>} data - данные
 * @reactProps {Object} components - компоненты обертки
 * @reactProps {Node} emptyText - компонент пустых данных
 * @reactProps {object} hotKeys - настройка hot keys
 * @reactProps {any} expandedComponent - кастомный компонент подстроки
 * @reactProps {string} children - флаг раскрыт ли список дочерних записей (приходит из props children, expand - открыт)
 * @reactProps {string} width - кастомная ширина таблицы
 * @reactProps {string} height - кастомная высота таблицы
 */
class AdvancedTable extends Component {
    constructor(props) {
        super(props)
        this.state = {
            focusIndex: getFocusIndex(props),
            selectIndex: props.hasSelect
                ? getIndex(props.data, props.selectedId)
                : -1,
            data: props.data || [],
            expandedRowKeys: [],
            columns: [],
            checked: props.data && !isEmpty(props.multi) ? this.mapChecked(props.multi) : {},
            children: get(props, 'children', 'collapse'),
        }

        this.rows = {}
        this.dataStorage = []

        this.components = {
            header: {
                row: AdvancedTableHeaderRow,
                cell: AdvancedTableHeaderCell,
                ...get(props.components, 'header', {}),
            },
            body: {
                row: this.renderTableRow(props),
                cell: AdvancedTableCell,
                ...get(props.components, 'body', {}),
            },
        }
    }

    componentDidMount() {
        const { rowClick, columns, rowSelection, setSelectionType } = this.props
        const {
            isAnyTableFocused,
            isActive,
            focusIndex,
            selectIndex,
            data,
            autoFocus,
            children,
        } = this.state

        if (!isAnyTableFocused && isActive && !rowClick && autoFocus) {
            this.setSelectAndFocus(
                get(data[selectIndex], 'id'),
                get(data[focusIndex], 'id'),
            )
        }

        if (children === 'expand') {
            this.openAllRows()
        }

        this.setState({
            columns: this.mapColumns(columns),
        })

        this.dataStorage = this.getModelsFromData(data)

        if (rowSelection) {
            setSelectionType(rowSelection)
        }
    }

    // eslint-disable-next-line complexity
    componentDidUpdate(prevProps, prevState) {
        const {
            hasSelect,
            data,
            isAnyTableFocused,
            isActive,
            selectedId,
            autoFocus,
            columns,
            multi,
            rowSelection,
            resolveModel,
            onSetSelection,
            filters,
        } = this.props

        const { checked, children } = this.state

        if (
            !isEqual(prevProps.data, data) &&
            !isEmpty(filters) &&
            rowSelection === 'checkbox'
        ) {
            onSetSelection(data.filter(model => checked[model.id]))
        }

        if (hasSelect && !isEmpty(data) && !isEqual(data, prevProps.data)) {
            const id = selectedId || data[0].id

            if (isAnyTableFocused && !isActive && autoFocus) {
                this.setNewSelectIndex(id)
            } else if (autoFocus) {
                this.setSelectAndFocus(id, id)
            }
        }

        if (!isEqual(prevProps, this.props)) {
            let state = {}

            if (
                isEqual(prevProps.filters, filters) &&
                !isEmpty(prevProps.filters) &&
                !isEmpty(filters)
            ) {
                this.closeAllRows()
            }
            if (data && !isEqual(prevProps.data, data)) {
                const checked = this.mapChecked(multi)

                state = {
                    data: isArray(data) ? data : [data],
                    checked,
                }
                this.dataStorage = this.getModelsFromData(data)

                if (children === 'expand') {
                    this.openAllRows()
                }
            }
            if (!isEqual(prevProps.columns, columns)) {
                state = {
                    ...state,
                    columns: this.mapColumns(columns),
                }
            }
            if (!isEqual(prevProps.selectedId, selectedId) && autoFocus) {
                this.setNewSelectIndex(selectedId)
                this.setNewFocusIndex(selectedId)
            }
            this.setState({ ...state })
        }

        if (
            (!isEqual(prevState.checked, checked) || !isEqual(data, prevProps.data)) &&
            rowSelection === rowSelectionType.CHECKBOX
        ) {
            // eslint-disable-next-line react/no-find-dom-node
            const selectAllCheckbox = ReactDom.findDOMNode(
                this.selectAllCheckbox,
            ).querySelector('input')

            const isSomeOneChecked = data.some(item => checked[item.id])
            const isAllChecked = data.every(item => checked[item.id])

            selectAllCheckbox.indeterminate = isSomeOneChecked && !isAllChecked
            selectAllCheckbox.checked = isAllChecked

            this.setState({})
        }

        if (
            resolveModel &&
            rowSelection === rowSelectionType.RADIO &&
            !isEqual(resolveModel, prevProps.resolveModel) &&
            autoFocus
        ) {
            this.setState({ checked: { [resolveModel.id]: resolveModel } })
        }
    }

    renderTableRow = (props) => {
        const { rows } = props

        return (props) => {
            if (isEmpty(rows)) {
                return get(props, 'rowClick', false) ? (
                    <AdvancedTableRowWithAction {...props} />
                ) : (
                    <AdvancedTableRow {...props} />
                )
            }

            return (
                <SecurityCheck
                    config={rows.security}
                    render={({ permissions }) => (permissions ? (
                        <AdvancedTableRowWithAction {...props} />
                    ) : (
                        <AdvancedTableRow {...props} />
                    ))}
                />
            )
        }
    }

    mapChecked = (multi = []) => {
        const checked = {}

        multi.forEach((item) => {
            checked[item.id] = item
        })

        return checked
    }

    getModelsFromData = (data) => {
        const dataStorage = []
        const getChildren = children => map(children, (model) => {
            let array = [...children]

            if (model.children) {
                array = [...array, getChildren(model.children)]
            }

            return array
        })

        map(data, (item) => {
            if (item.children) {
                const children = getChildren(item.children)

                dataStorage.push(...flattenDeep(children))
            }
            dataStorage.push(item)
        })

        return dataStorage
    }

    setTableRef = (el) => {
        const { height, width } = this.props

        if (height) {
            el.bodyTable.style.height = height
            el.bodyTable.style.overflow = 'auto'
        }

        if (width) {
            el.bodyTable.style.width = width
            el.bodyTable.style.overflow = 'auto'
        }

        this.table = el
    }

    setSelectionRef = (el) => {
        this.selectAllCheckbox = el
    }

    setRowRef = (ref, id) => {
        if (ref && ref !== this.rows[id]) {
            this.rows[id] = ref
        }
    }

    // eslint-disable-next-line consistent-return
    handleKeyDown = (e, keyName) => {
        const { data, children, hasFocus, hasSelect, autoFocus, onResolve } = this.props
        const { focusIndex } = this.state

        const modelIndex = findIndex(data, i => i.id === focusIndex)

        if (eq(keyName, KEY_CODES.UP) || eq(keyName, KEY_CODES.DOWN)) {
            if (!React.Children.count(children) && hasFocus) {
                const newFocusIndex = eq(keyName, KEY_CODES.UP)
                    ? modelIndex - 1
                    : modelIndex + 1

                if (newFocusIndex >= data.length || newFocusIndex < 0) { return false }
                const nextData = data[newFocusIndex]

                if (hasSelect && autoFocus) {
                    this.setSelectAndFocus(nextData.id, nextData.id)
                    onResolve(nextData)
                } else {
                    this.setNewFocusIndex(nextData.id)
                }
            }
        } else if (eq(keyName, KEY_CODES.SPACE) && (hasSelect && !autoFocus)) {
            onResolve(data[modelIndex])
            this.setNewSelectIndex(focusIndex)
        }
    }

    handleFilter = (filter) => {
        const { onFilter } = this.props

        if (onFilter) {
            onFilter(filter)
        }
    }

    handleRowClick(id, needReturn, noResolve, model) {
        const {
            hasFocus,
            hasSelect,
            onResolve,
            isActive,
            rowSelection,
        } = this.props

        const needToReturn = isActive === needReturn

        if (!needToReturn && hasSelect && !noResolve) {
            onResolve(find(this.dataStorage, { id }))
        }

        if (needToReturn) { return }
        if (
            rowSelection === rowSelectionType.RADIO ||
            (!rowSelection && hasSelect)
        ) { this.selectModel(model) }

        if (!noResolve && hasSelect && hasFocus) {
            this.setSelectAndFocus(id, id)
        } else if (hasFocus) {
            this.setNewFocusIndex(id)
        } else if (hasSelect) {
            this.setNewSelectIndex(id)
        }
    }

    handleRowClickWithAction(id, needReturn, noResolve, model) {
        const {
            hasFocus,
            hasSelect,
            rowClick,
            onRowClickAction,
            onResolve,
            isActive,
            autoCheckboxOnSelect,
            rowSelection,
        } = this.props
        const needToReturn = isActive === needReturn

        if (!needToReturn && hasSelect && !noResolve) {
            onResolve(find(this.dataStorage, { id }))
        }

        if (
            !noResolve &&
            rowClick &&
            !(autoCheckboxOnSelect && rowSelection === rowSelectionType.CHECKBOX)
        ) {
            if (!hasSelect) {
                onResolve(find(this.dataStorage, { id }))
            }
            onRowClickAction(model)
        }

        if (
            rowSelection === rowSelectionType.RADIO ||
            (!rowSelection && hasSelect)
        ) { this.selectModel(model) }

        if (needToReturn) { return }

        if (!noResolve && hasSelect && hasFocus && !rowClick) {
            this.setSelectAndFocus(id, id)
        } else if (hasFocus) {
            this.setNewFocusIndex(id)
        } else if (hasSelect && !rowClick) {
            this.setNewSelectIndex(id)
        }
    }

    setNewFocusIndex(index) {
        this.setState({ focusIndex: index }, () => this.focusActiveRow())
    }

    setNewSelectIndex(index) {
        this.setState({ selectIndex: index })
    }

    setSelectAndFocus(selectIndex, focusIndex) {
        this.setState({ selectIndex, focusIndex }, () => {
            this.focusActiveRow()
        })
    }

    focusActiveRow() {
        const { focusIndex } = this.state

        if (this.rows[focusIndex]) {
            this.rows[focusIndex].focus()
        }
    }

    openAllRows = () => {
        const { data } = this.props
        const keys = []
        const getKeys = array => map(array, (item) => {
            keys.push(item.id)
            if (item.children) {
                getKeys(item.children)
            }
        })

        getKeys(data)
        this.setState({
            expandedRowKeys: keys,
        })
    }

    closeAllRows = () => {
        this.setState({
            expandedRowKeys: [],
        })
    }

    handleExpandedRowsChange = (rows) => {
        this.setState({
            expandedRowKeys: rows,
        })
    }

    checkAll = (status) => {
        const { onSetSelection, data } = this.props
        const { checked } = this.state
        const newChecked = { ...checked }

        data.forEach((item) => {
            if (status) {
                newChecked[item.id] = item
            } else {
                delete newChecked[item.id]
            }
        })
        onSetSelection(Object.values(newChecked))
        this.setState(() => ({
            checked: newChecked,
        }))
    }

    handleChangeChecked = (model) => {
        const index = model.id
        const { onSetSelection } = this.props
        const { checked } = this.state
        const newChecked = { ...checked }

        if (checked[index]) {
            delete newChecked[index]
        } else {
            newChecked[index] = model
        }

        onSetSelection(Object.values(newChecked))
        this.setState(() => ({
            checked: newChecked,
        }))
    }

    selectModel(model) {
        const { onSetSelection } = this.props

        onSetSelection([model])
        this.setState(() => ({
            checked: {
                [model.id]: model,
            },
        }))
    }

    handleResize(index) {
        return (e, { size }) => {
            this.setState(({ columns }) => {
                const nextColumns = [...columns]

                nextColumns[index] = {
                    ...nextColumns[index],
                    width: size.width,
                }

                return { columns: nextColumns }
            })
        }
    }

    handleEdit = (value, index, id) => {
        const { onEdit } = this.props
        const { data } = this.state

        data[index][id] = value
        this.setState({
            data,
        })
        onEdit(value, index, id)
    }

    getRowProps = (model, index) => {
        const {
            rowClick,
            rowClass,
            rowSelection,
            autoCheckboxOnSelect,
            multi,
            hasSelect,
            resolveModel,
        } = this.props
        const isRowActive = () => {
            if (!hasSelect) {
                return false
            }

            if (multi?.length === 1 && rowSelection === rowSelectionType.RADIO) {
                return multi[0].id === model.id
            }

            return model.id === get(resolveModel, 'id')
        }

        return {
            index,
            rowClick,
            isRowActive: isRowActive(),
            rowClass: rowClass && propsResolver(rowClass, model),
            model,
            setRef: this.setRowRef,
            handleRowClick: () => {
                this.handleRowClick(model.id, false, false, model)
                if (
                    autoCheckboxOnSelect &&
                    rowSelection === rowSelectionType.CHECKBOX
                ) {
                    this.handleChangeChecked(model)
                }
            },
            handleRowClickFocus: () => this.handleRowClick(model.id, true, false, model),
            clickWithAction: () => this.handleRowClickWithAction(model.id, false, false, model),
            clickFocusWithAction: () => this.handleRowClickWithAction(model.id, true, true, model),
        }
    }

    createSelectionColumn(columns, rowSelection) {
        const isSomeFixed = some(columns, column => column.fixed)
        const title = rowSelection === rowSelectionType.CHECKBOX
            ? (
                <AdvancedTableSelectionColumn
                    setRef={this.setSelectionRef}
                    onChange={this.checkAll}
                />
            )
            : null

        return {
            title,
            dataIndex: 'row-selection',
            key: 'row-selection',
            className: 'n2o-advanced-table-selection-container',
            width: 30,
            fixed: isSomeFixed && 'left',
            render: (value, model) => {
                const { checked } = this.state

                if (rowSelection === rowSelectionType.CHECKBOX) {
                    return (
                        <CheckboxN2O
                            className="n2o-advanced-table-row-checkbox"
                            inline
                            checked={!!checked[model.id]}
                            onChange={() => this.handleChangeChecked(model)}
                        />
                    )
                }
                if (rowSelection === rowSelectionType.RADIO) {
                    return (
                        <RadioN2O
                            className="n2o-advanced-table-row-radio"
                            inline
                            value={model.id}
                            checked={!!checked[model.id]}
                            onChange={() => this.selectModel(model)}
                        />
                    )
                }

                return null
            },
        }
    }

    getRowKey = row => row.key

    renderIcon = ({ record, expanded, onExpand }) => {
        const { expandedFieldId, expandedComponent } = this.props

        return (
            <AdvancedTableExpandIcon
                record={record}
                expanded={expanded}
                onExpand={onExpand}
                expandedFieldId={expandedFieldId}
                expandedComponent={expandedComponent}
            />
        )
    }

    renderExpandedRow = () => {
        const { expandable, expandedComponent, expandedFieldId } = this.props

        return (
            expandable && (expandedComponent
                ? (record, index, indent) => React.createElement(expandedComponent, {
                    record,
                    index,
                    indent,
                    expandedFieldId,
                })
                : (record, index, indent) => (
                    <AdvancedTableExpandedRenderer
                        record={record}
                        index={index}
                        indent={indent}
                        expandedFieldId={expandedFieldId}
                    />
                ))
        )
    }

    mapColumns = (columns = []) => {
        const { rowSelection, filters, textWrap } = this.props

        let newColumns = columns

        newColumns = map(newColumns, (col, columnIndex) => ({
            ...col,
            onHeaderCell: column => ({
                ...column,
                onFilter: this.handleFilter,
                onResize: this.handleResize(columnIndex),
                filters,
            }),
            onCell: record => ({
                record,
                editable: col.editable && record.editable,
                hasSpan: col.hasSpan,
                needRender: col.needRender,
                textWrap,
            }),
        }))

        if (rowSelection) {
            newColumns = [
                this.createSelectionColumn(columns, rowSelection),
                ...newColumns,
            ]
        }

        return newColumns
    }

    getScroll = () => ({ x: false, y: false })

    render() {
        const {
            hasFocus,
            className,
            expandable,
            onExpand,
            tableSize,
            useFixedHeader,
            bordered,
            isActive,
            onFocus,
            rowSelection,
            t,
            width,
            height,
        } = this.props
        const { columns, data, expandedRowKeys } = this.state
        const style = width ? { width } : {}

        return (
            <HotKeys
                keyMap={{ events: values(KEY_CODES) }}
                handlers={{ events: this.handleKeyDown }}
            >
                <div onFocus={!isActive ? onFocus : undefined}>
                    <Table
                        style={style}
                        ref={this.setTableRef}
                        prefixCls="n2o-advanced-table"
                        className={classNames('n2o-table table table-hover', className, {
                            'has-focus': hasFocus,
                            [`table-${tableSize}`]: tableSize,
                            'table-bordered': bordered,
                            'has-static-height': height,
                            'has-static-width': width,
                        })}
                        columns={columns}
                        data={data}
                        onRow={this.getRowProps}
                        components={this.components}
                        rowKey={this.getRowKey}
                        expandIcon={this.renderIcon}
                        expandIconAsCell={!!rowSelection && expandable}
                        expandedRowRender={this.renderExpandedRow()}
                        expandedRowKeys={expandedRowKeys}
                        onExpandedRowsChange={this.handleExpandedRowsChange}
                        onExpand={onExpand}
                        useFixedHeader={useFixedHeader}
                        indentSize={20}
                        emptyText={AdvancedTableEmptyText(t)}
                        scroll={this.getScroll()}
                    />
                </div>
            </HotKeys>
        )
    }
}

AdvancedTable.propTypes = {
    children: PropTypes.any,
    selectedId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    rowClass: PropTypes.string,
    resolveModel: PropTypes.any,
    filters: PropTypes.any,
    textWrap: PropTypes.any,
    useFixedHeader: PropTypes.any,
    tableSize: PropTypes.string,
    rowClick: PropTypes.bool,
    autoCheckboxOnSelect: PropTypes.bool,
    isActive: PropTypes.bool,
    isAnyTableFocused: PropTypes.bool,
    hasSelect: PropTypes.bool,
    multi: PropTypes.array,
    setSelectionType: PropTypes.func,
    onRowClickAction: PropTypes.func,
    onResolve: PropTypes.func,
    onEdit: PropTypes.func,
    onSetSelection: PropTypes.func,
    onExpand: PropTypes.func,
    onFilter: PropTypes.func,
    onFocus: PropTypes.func,
    t: PropTypes.func,
    /**
     * Наличие фокуса на строке при клике
     */
    hasFocus: PropTypes.bool,
    /**
     * Класс
     */
    className: PropTypes.string,
    /**
     * Массив колонок
     */
    columns: PropTypes.arrayOf(PropTypes.object),
    /**
     * Данные
     */
    data: PropTypes.arrayOf(PropTypes.object),
    /**
     * Кастомные компоненты
     */
    components: PropTypes.object,
    /**
     * Флаг включения border у таблицы
     */
    bordered: PropTypes.bool,
    /**
     * Тип выбора строк
     */
    rowSelection: PropTypes.string,
    /**
     * Флаг включения саб контента
     */
    expandable: PropTypes.bool,
    /**
     * Ключ к саб контенту в данных
     */
    expandedFieldId: PropTypes.string,
    /**
     * Кастомный компонент саб строки
     */
    expandedComponent: PropTypes.any,
    /**
     * Автофокус на строке
     */
    autoFocus: PropTypes.bool,
    /**
     * Конфиг для SecurityCheck
     */
    rows: PropTypes.object,
    /**
     * Кастом ширина таблицы
     */
    width: PropTypes.string,
    /**
     * Кастом высота таблицы
     */
    height: PropTypes.string,
}

AdvancedTable.defaultProps = {
    expandedFieldId: 'expandedContent',
    data: [],
    bordered: false,
    tableSize: 'sm',
    rowSelection: '',
    expandable: false,
    onFocus: () => {},
    onSetSelection: () => {},
    setSelectionType: () => {},
    t: () => {},
    autoFocus: false,
    rows: {},
    // eslint-disable-next-line react/default-props-match-prop-types
    scroll: {},
}

export { AdvancedTable }
export default compose(
    setDisplayName('AdvancedTable'),
    pure,
    withAdvancedTableRef,
)(AdvancedTable)
