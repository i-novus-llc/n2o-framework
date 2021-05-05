import React from 'react'
import get from 'lodash/get'
import set from 'lodash/set'
import { storiesOf } from '@storybook/react'
import { getStubData, page } from 'N2oStorybook/fetchMock'
import { filterMetadata, newEntry } from 'N2oStorybook/json'
import fetchMock from 'fetch-mock'

import Factory from '../../../core/factory/Factory'
import { WIDGETS } from '../../../core/factory/factoryLevels'
import withPage from '../../../../.storybook/decorators/withPage'
import CheckboxN2O from '../../controls/Checkbox/CheckboxN2O'

import metadata from './json/AdvancedTableWidget.meta'
import resizable from './json/Resizable.meta'
import rowSelection from './json/RowSelection.meta'
import filterable from './json/Filterable.meta'
import expandedRow from './json/ExpandedRow.meta'
import colSpanRowSpan from './json/ColspanRowspan.meta'
import treeView from './json/TreeView.meta'
import treeViewTableExpand from './json/TreeViewTableExpand.meta'
import fixedHeader from './json/FixedHeader.meta'
import fixedColumns from './json/FixedColumns.meta'
import multiLevelHeader from './json/MultiLevelHeader.meta'
import editableCell from './json/EditableCell.meta'
import nested from './json/Nested.meta'
import customRowClick from './json/CustomRowClick.meta'
import allFeatures from './json/AllFeatures.meta'
import AdvancedTable, {
    AdvancedTable as AdvancedTableComponent,
} from './AdvancedTable'
import percentWidth from './json/PercentWidth.meta'
import pixelWidth from './json/PixelWidth.meta'
import EditableCellWithTooltip from './json/EditableCellWithTooltip.meta'

const stories = storiesOf('Виджеты/Advanced Table', module)

stories.addParameters({
    info: {
        propTables: [AdvancedTableComponent],
        propTablesExclude: [Factory],
    },
})

const urlPattern = '*'

const columns = [
    {
        id: 'name',
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        width: 100,
    },
    {
        id: 'surname',
        title: 'Surname',
        dataIndex: 'surname',
        key: 'surname',
        width: 200,
    },
    {
        id: 'age',
        title: 'Age',
        dataIndex: 'age',
        key: 'age',
    },
]

const data = [
    {
        id: 1,
        name: 'name1',
        surname: 'surname1',
        age: 1,
    },
    {
        id: 2,
        name: 'name2',
        surname: 'surname2',
        age: 2,
    },
    {
        id: 3,
        name: 'name3',
        surname: 'surname3',
        age: 3,
    },
]

class AdvancedTableWidgetStory extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            ...props.json,
            filterable: get(props.json, 'columns[1].filterable'),
            resizable: get(props.json, 'columns[1].resizable'),
            fixedLeft: get(props.json, 'columns[0].fixed'),
            fixedRight: get(props.json, 'columns[3].fixed'),
        }

        this.toggleHeaderParam = this.toggleHeaderParam.bind(this)
        this.toggleFixed = this.toggleFixed.bind(this)
        this.toggleScroll = this.toggleScroll.bind(this)
    }

    toggleHeaderParam(name) {
        const { columns } = this.state

        columns[1][name] = !this.state[name]
        this.setState({
            columns,
            [name]: !this.state[name],
        })
    }

    toggleFixed(name, index) {
        const { columns } = this.state
        let newDirection = null

        if (name === 'fixedLeft') {
            newDirection = this.state[name] ? false : 'left'
        } else {
            newDirection = this.state[name] ? false : 'right'
        }
        columns[index].fixed = newDirection
        this.setState({
            columns,
            [name]: newDirection,
        })
    }

    toggleScroll(name) {
        let newScroll = this.state.scroll

        if (name === 'x') {
            newScroll = this.state.scroll.x ? false : '200%'
        } else {
            newScroll = this.state.scroll.y ? false : 200
        }
        this.setState({
            scroll: {
                ...this.state.scroll,
                [name]: newScroll,
            },
        })
    }

    render() {
        return (
            <div>
                <div
                    style={{
                        display: 'flex',
                        flexWrap: 'wrap',
                        justifyContent: 'space-between',
                        marginBottom: 20,
                    }}
                >
                    <CheckboxN2O
                        checked={this.state.resizable}
                        onChange={() => this.toggleHeaderParam('resizable')}
                        inline
                        label="Функция resizable"
                    />
                    <CheckboxN2O
                        checked={this.state.filterable}
                        onChange={() => this.toggleHeaderParam('filterable')}
                        inline
                        label="Функция filterable"
                    />
                    <CheckboxN2O
                        checked={this.state.rowSelection}
                        onChange={() => this.setState({ rowSelection: !this.state.rowSelection })
                        }
                        inline
                        label="Функция выбора строк"
                    />
                    <CheckboxN2O
                        checked={this.state.fixedLeft}
                        onChange={() => this.toggleFixed('fixedLeft', 0)}
                        inline
                        label="Фиксирование первой колонки слева"
                    />
                    <CheckboxN2O
                        checked={this.state.fixedRight}
                        onChange={() => this.toggleFixed('fixedRight', 3)}
                        inline
                        label="Фиксирование последней колонки справа"
                    />
                    <CheckboxN2O
                        checked={this.state.scroll.x}
                        onChange={() => this.toggleScroll('x')}
                        inline
                        label="Скролл-x"
                    />
                    <CheckboxN2O
                        checked={this.state.scroll.y}
                        onChange={() => this.toggleScroll('y')}
                        inline
                        label="Скролл-y"
                    />
                </div>
                <AdvancedTable {...this.state} rowKey={record => record.id} />
            </div>
        )
    }
}

stories
// .addDecorator(withPage(metadata))

    .add('Метаданные', () => {
        fetchMock.restore().get(urlPattern, url => getStubData(url))

        return (
            <Factory level={WIDGETS} {...metadata.Page_Table} id="Page_Table" />
        )
    })
    .add('Resizable колонки', () => {
        const newColumns = columns.slice()

        set(newColumns, '[0].resizable', true)
        set(newColumns, '[1].resizable', true)

        return <AdvancedTable columns={columns} data={data} />
    })
    .add('Выбор строк чекбоксом', () => (
        <AdvancedTable columns={columns} data={data} rowSelection="checkbox" />
    ))
    .add('Выбор строк чекбоксом по клику на строку', () => (
        <AdvancedTable
            columns={columns}
            data={data}
            rowSelection="checkbox"
            autoCheckboxOnSelect
        />
    ))
    .add('Выбор строк радио', () => <AdvancedTable columns={columns} data={data} rowSelection="radio" />)
    .add('Выбор строк радио по клику на строку', () => <AdvancedTable columns={columns} data={data} rowSelection="radio" />)
    .add('Фильтр в заголовках', () => (
        <Factory level={WIDGETS} {...filterable.Page_Table} id="Page_Table" />
    ))
    .add('Контент в подстроке', () => (
        <AdvancedTable
            columns={columns}
            data={expandedRow.datasource}
            table={expandedRow.Page_Table.table}
            expandedFieldId="expandedContent"
            expandable
        />
    ))
    .add('Colspan rowspan', () => {
        fetchMock.restore().get(urlPattern, () => ({
            count: 3,
            list: colSpanRowSpan.datasource,
            page: 1,
            size: 10,
        }))

        return (
            <Factory
                level={WIDGETS}
                {...colSpanRowSpan.Page_Table}
                id="Page_Table"
            />
        )
    })
    .add('Вид дерево', () => <AdvancedTable columns={columns} data={treeView.datasource} />)
    .add('expand список', () => (
        <AdvancedTable
            columns={columns}
            data={treeViewTableExpand.datasource}
            table={treeViewTableExpand.Page_Table.table}
        />
    ))
    .add('Фиксированный заголовок', () => (
        <AdvancedTable
            columns={columns}
            data={[...data, ...data, ...data]}
            useFixedHeader
            scroll={{ y: 100 }}
        />
    ))
    .add('Фиксированные колонки', () => {
        const newColumns = columns.slice()

        set(newColumns, '[0].width', 300)
        set(newColumns, '[1].width', 700)
        set(newColumns, '[2].width', 300)

        set(newColumns, '[0].fixed', 'left')
        set(newColumns, '[2].fixed', 'right')

        return (
            <AdvancedTable
                columns={columns}
                data={[...data, ...data, ...data]}
                useFixedHeader
            />
        )
    })
    .add('Многоуровневый заголовок', () => {
        fetchMock.restore().get(urlPattern, url => getStubData(url))

        return (
            <Factory
                level={WIDGETS}
                {...multiLevelHeader.Page_Table}
                id="Page_Table"
            />
        )
    })
    .add('Редактируемая ячейка', () => {
        fetchMock.restore().get(urlPattern, url => getStubData(url))

        return (
            <Factory
                level={WIDGETS}
                {...editableCell.Page_Table}
                id="Page_Table"
            />
        )
    })
    .add('Редактируемая ячейка с тултипом', () => {
        fetchMock.restore().get(urlPattern, url => getStubData(url))

        return (
            <Factory
                level={WIDGETS}
                {...EditableCellWithTooltip.Page_Table}
                datasource={EditableCellWithTooltip.datasource}
                id="Page_Table"
            />
        )
    })
    .add('Подтаблица', () => {
        fetchMock.restore().get(urlPattern, () => ({
            count: 1,
            page: 1,
            size: 10,
            list: nested.datasource,
        }))

        return (
            <Factory level={WIDGETS} {...nested.Page_Table} id="Page_Table" />
        )
    })
    .add('Компонент с кастомным Expanded компонентом', () => {
        const props = {
            expandable: true,
            expandedFieldId: 'expandedContent',
            columns: [
                {
                    id: 'test',
                    key: 'test',
                    dataIndex: 'test',
                    title: 'test',
                },
                {
                    id: 'anotherTest',
                    key: 'anotherTest',
                    dataIndex: 'anotherTest',
                    title: 'anotherTest',
                },
            ],
            data: [
                {
                    test: 'test1',
                    anotherTest: 'anotherTest1',
                },
            ],
        }

        const expandedComponent = () => <div>any custom content</div>

        return <AdvancedTable {...props} expandedComponent={expandedComponent} />
    })
    .add('Экшен AdvancedTable', () => {
        fetchMock.restore().get(urlPattern, url => getStubData(url))
        fetchMock.get('begin:n2o/page', page)

        return (
            <Factory
                level={WIDGETS}
                {...customRowClick.Page_Table}
                id="Page_Table"
            />
        )
    })
    .add('Компонент со всеми фичами', () => <AdvancedTableWidgetStory json={allFeatures} />)
    .add('Placeholder', () => {
        fetchMock.restore().get(urlPattern, url => new Promise((res, rej) => setTimeout(() => res(getStubData(url)), 3000)))

        const newMeta = { ...metadata }

        newMeta.Page_Table.placeholder = { rows: 6, cols: 3, type: 'table' }

        return (
            <Factory level={WIDGETS} {...newMeta.Page_Table} id="Page_Table" />
        )
    })
    .add('Колонки с длиной в процентах', () => {
        fetchMock.restore().get(urlPattern, url => new Promise((res, rej) => setTimeout(() => res(getStubData(url)), 3000)))

        return (
            <Factory
                level={WIDGETS}
                {...percentWidth.Page_Table}
                id="Page_Table"
            />
        )
    })
    .add('Колонки с длиной в пикселях', () => {
        fetchMock.restore().get(urlPattern, url => new Promise((res, rej) => setTimeout(() => res(getStubData(url)), 3000)))

        return (
            <Factory level={WIDGETS} {...pixelWidth.Page_Table} id="Page_Table" />
        )
    })
