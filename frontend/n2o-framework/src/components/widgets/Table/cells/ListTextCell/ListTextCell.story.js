import React from 'react'
import { storiesOf } from '@storybook/react'

import TextTableHeader from '../../headers/TextTableHeader'
import Table from '../../Table'
import Factory from '../../../../../core/factory/Factory'

import ListTextCellJson from './ListTextCell.meta'
import ListTextCell, { ListTextCell as ListTextCellComponent } from './ListTextCell'

const stories = storiesOf('Ячейки/ListTextCell', module)

stories.addParameters({
    info: {
        propTables: [ListTextCellComponent],
        propTablesExclude: [Table, Factory],
    },
})

stories
    .add(
        'Компонент',
        () => (
            <ListTextCell
                id="testId"
                label={'Объектов {size} шт'}
                fieldKey="test"
                model={{
                    test: [
                        'Первый объект',
                        'Второй объект',
                        'Третий объект',
                        'Четвертый объект',
                        'Пятый объект',
                    ],
                }}
            />
        ),
        {
            info: {
                text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{size} объектов, текст с плейсхолдером. {size} - длина массива. Если {size} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
        theme="light или dark(default)"
       />
      ~~~
      `,
            },
        },
    )
    .add('Склонение', () => [
        <ListTextCell
            id="nounId"
            label="Объектов {size} шт"
            oneLabel="{size} объект"
            fewLabel="{size} объекта"
            manyLabel="{size} объектов"
            fieldKey="test"
            model={{
                test: ['1'],
            }}
        />,
        <ListTextCell
            id="nounId"
            label="Объектов {size} шт"
            oneLabel="{size} объект"
            fewLabel="{size} объекта"
            manyLabel="{size} объектов"
            fieldKey="test"
            model={{
                test: ['1', '2'],
            }}
        />,
        <ListTextCell
            id="nounId"
            label="Объектов {size} шт"
            oneLabel="{size} объект"
            fewLabel="{size} объекта"
            manyLabel="{size} объектов"
            fieldKey="test"
            model={{
                test: ['1', '2', '3', '4', '5', '6'],
            }}
        />,
    ])
    .add(
        'light theme',
        () => (
            <ListTextCell
                id="testId"
                label={'Объектов {size} шт'}
                fieldKey="test"
                model={{
                    test: [
                        'Первый объект',
                        'Второй объект',
                        'Третий объект',
                        'Четвертый объект',
                        'Пятый объект',
                    ],
                }}
                theme="light"
            />
        ),
        {
            info: {
                text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{size} объектов, текст с плейсхолдером. {size} - длина массива. Если {size} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
        theme="light или dark(default)"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'LabelDashed',
        () => {
            const { id, src, fieldKey } = ListTextCellJson
            const props = { id, src, fieldKey }
            return (
                <ListTextCell
                    {...props}
                    label={'Объектов {size} шт'}
                    placement="bottom"
                    labelDashed
                    model={{
                        test: [
                            'Первый объект',
                            'Второй объект',
                            'Третий объект',
                            'Четвертый объект',
                        ],
                    }}
                />
            )
        },
        {
            info: {
                text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{size} объектов, текст с плейсхолдером. {size} - длина массива. Если {size} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
        theme="light или dark(default)"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'replace placeholder',
        () => {
            const { id, src, fieldKey } = ListTextCellJson
            const props = { id, src, fieldKey }
            return (
                <ListTextCell
                    {...props}
                    label={'в листе {size} объекта'}
                    placement="bottom"
                    model={{
                        test: [
                            'Первый объект',
                            'Второй объект',
                            'Третий объект',
                            'Четвертый объект',
                        ],
                    }}
                />
            )
        },
        {
            info: {
                text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{size} объектов, текст с плейсхолдером. {size} - длина массива. Если {size} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
        theme="light или dark(default)"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'hover или click',
        () => {
            const props = {
                headers: [
                    {
                        id: 'ListTextCell',
                        component: TextTableHeader,
                        label: 'hover',
                    },
                ],
                cells: [
                    {
                        id: 'hover',
                        component: ListTextCell,
                        fieldKey: 'test',
                        label: 'объекта',
                        src: 'ListTextCell',
                        placement: 'bottom',
                    },
                ],
                datasource: [
                    {
                        test: [
                            'Первый объект',
                            'Второй объект',
                            'Третий объект',
                            'Четвертый объект',
                        ],
                    },
                ],
            }
            const props2 = {
                headers: [
                    {
                        id: 'ListTextCell',
                        component: TextTableHeader,
                        label: 'click',
                    },
                ],
                cells: [
                    {
                        id: 'click',
                        component: ListTextCell,
                        fieldKey: 'test',
                        trigger: 'click',
                        label: 'Объектов {size} шт',
                        src: 'ListTextCell',
                        placement: 'bottom',
                    },
                ],
                datasource: [
                    {
                        test: ['Первый объект', 'Второй объект', 'Третий объект'],
                    },
                ],
            }
            return (
                <div
                    style={{
                        display: 'flex',
                        width: '250px',
                        justifyContent: 'space-between',
                    }}
                >
                    <Table
                        headers={props.headers}
                        cells={props.cells}
                        datasource={props.datasource}
                    />
                    <Table
                        headers={props2.headers}
                        cells={props2.cells}
                        datasource={props2.datasource}
                    />
                </div>
            )
        },
        {
            info: {
                text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{size} объектов, текст с плейсхолдером. {size} - длина массива. Если {size} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
        theme="light или dark(default)"
       />
      ~~~
      `,
            },
        },
    )

    .add(
        'placement',
        () => {
            const props = {
                headers: [
                    {
                        id: 'ListTextCell',
                        component: TextTableHeader,
                        label: 'bottom',
                    },
                ],
                cells: [
                    {
                        id: 'secondary',
                        component: ListTextCell,
                        fieldKey: 'test',
                        label: 'Объектов {size} шт',
                        src: 'ListTextCell',
                        placement: 'bottom',
                        labelDashed: true,
                    },
                ],
                datasource: [
                    {
                        test: ['Первый объект', 'Второй объект', 'Третий объект'],
                    },
                ],
            }
            const props2 = {
                headers: [
                    {
                        id: 'ListTextCell',
                        component: TextTableHeader,
                        label: 'right',
                    },
                ],
                cells: [
                    {
                        id: 'secondary',
                        component: ListTextCell,
                        fieldKey: 'test',
                        label: 'Объектов {size} шт',
                        src: 'ListTextCell',
                        placement: 'right',
                    },
                ],
                datasource: [
                    {
                        test: ['Первый объект', 'Второй объект', 'Третий объект'],
                    },
                ],
            }
            const props3 = {
                headers: [
                    {
                        id: 'ListTextCell',
                        component: TextTableHeader,
                        label: 'top',
                    },
                ],
                cells: [
                    {
                        id: 'secondary',
                        component: ListTextCell,
                        fieldKey: 'test',
                        label: 'Объектов {size} шт',
                        src: 'ListTextCell',
                        placement: 'top',
                    },
                ],
                datasource: [
                    {
                        test: ['Первый объект', 'Второй объект', 'Третий объект'],
                    },
                ],
            }
            const props4 = {
                headers: [
                    {
                        id: 'ListTextCell',
                        component: TextTableHeader,
                        label: 'left',
                    },
                ],
                cells: [
                    {
                        id: 'secondary',
                        component: ListTextCell,
                        fieldKey: 'test',
                        label: 'Объектов {size} шт',
                        src: 'ListTextCell',
                        placement: 'left',
                    },
                ],
                datasource: [
                    {
                        test: ['Первый объект', 'Второй объект', 'Третий объект'],
                    },
                ],
            }
            return (
                <div
                    style={{
                        display: 'flex',
                        width: '750px',
                        justifyContent: 'space-between',
                    }}
                >
                    <Table
                        headers={props.headers}
                        cells={props.cells}
                        datasource={props.datasource}
                    />
                    <Table
                        headers={props2.headers}
                        cells={props2.cells}
                        datasource={props2.datasource}
                    />
                    <Table
                        headers={props3.headers}
                        cells={props3.cells}
                        datasource={props3.datasource}
                    />
                    <Table
                        headers={props4.headers}
                        cells={props4.cells}
                        datasource={props4.datasource}
                    />
                </div>
            )
        },
        {
            info: {
                text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{size} объектов, текст с плейсхолдером. {size} - длина массива. Если {size} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
        theme="light или dark(default)"
       />
      ~~~
      `,
            },
        },
    )

    .add(
        'placement light theme',
        () => {
            const props = {
                headers: [
                    {
                        id: 'ListTextCell',
                        component: TextTableHeader,
                        label: 'bottom',
                    },
                ],
                cells: [
                    {
                        id: 'secondary',
                        component: ListTextCell,
                        fieldKey: 'test',
                        label: 'Объектов {size} шт',
                        src: 'ListTextCell',
                        placement: 'bottom',
                        theme: 'light',
                    },
                ],
                datasource: [
                    {
                        test: ['Первый объект', 'Второй объект', 'Третий объект'],
                    },
                ],
            }
            const props2 = {
                headers: [
                    {
                        id: 'ListTextCell',
                        component: TextTableHeader,
                        label: 'right',
                    },
                ],
                cells: [
                    {
                        id: 'secondary',
                        component: ListTextCell,
                        fieldKey: 'test',
                        label: 'Объектов {size} шт',
                        src: 'ListTextCell',
                        placement: 'right',
                        theme: 'light',
                    },
                ],
                datasource: [
                    {
                        test: ['Первый объект', 'Второй объект', 'Третий объект'],
                    },
                ],
            }
            const props3 = {
                headers: [
                    {
                        id: 'ListTextCell',
                        component: TextTableHeader,
                        label: 'top',
                    },
                ],
                cells: [
                    {
                        id: 'secondary',
                        component: ListTextCell,
                        fieldKey: 'test',
                        label: 'Объектов {size} шт',
                        src: 'ListTextCell',
                        placement: 'top',
                        theme: 'light',
                    },
                ],
                datasource: [
                    {
                        test: ['Первый объект', 'Второй объект', 'Третий объект'],
                    },
                ],
            }
            const props4 = {
                headers: [
                    {
                        id: 'ListTextCell',
                        component: TextTableHeader,
                        label: 'left',
                    },
                ],
                cells: [
                    {
                        id: 'secondary',
                        component: ListTextCell,
                        fieldKey: 'test',
                        label: 'Объектов {size} шт',
                        src: 'ListTextCell',
                        placement: 'left',
                        theme: 'light',
                    },
                ],
                datasource: [
                    {
                        test: ['Первый объект', 'Второй объект', 'Третий объект'],
                    },
                ],
            }
            return (
                <div
                    style={{
                        display: 'flex',
                        width: '750px',
                        justifyContent: 'space-between',
                    }}
                >
                    <Table
                        headers={props.headers}
                        cells={props.cells}
                        datasource={props.datasource}
                    />
                    <Table
                        headers={props2.headers}
                        cells={props2.cells}
                        datasource={props2.datasource}
                    />
                    <Table
                        headers={props3.headers}
                        cells={props3.cells}
                        datasource={props3.datasource}
                    />
                    <Table
                        headers={props4.headers}
                        cells={props4.cells}
                        datasource={props4.datasource}
                    />
                </div>
            )
        },
        {
            info: {
                text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{size} объектов, текст с плейсхолдером. {size} - длина массива. Если {size} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
        theme="light или dark(default)"
       />
      ~~~
      `,
            },
        },
    )

    .add(
        'Метаданные',
        () => {
            const ListTextCellProps = {
                placement: ListTextCellJson.placement,
                fieldKey: ListTextCellJson.fieldKey,
                label: ListTextCellJson.label,
                id: ListTextCellJson.id,
                src: ListTextCellJson.id,
                trigger: ListTextCellJson.trigger,
            }

            const props = {
                headers: [
                    {
                        id: 'LinkTextCell',
                        component: TextTableHeader,
                        label: 'LinkTextCell',
                    },
                ],
                cells: [
                    {
                        id: 'secondary',
                        component: ListTextCell,
                        ...ListTextCellProps,
                    },
                ],
                datasource: [
                    {
                        test: ['Первый объект', 'Второй объект', 'Третий объект'],
                    },
                ],
            }
            return (
                <Table
                    headers={props.headers}
                    cells={props.cells}
                    datasource={props.datasource}
                />
            )
        },
        {
            info: {
                text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{size} объектов, текст с плейсхолдером. {size} - длина массива. Если {size} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
        theme="light или dark(default)"
       />
      ~~~
      `,
            },
        },
    )
