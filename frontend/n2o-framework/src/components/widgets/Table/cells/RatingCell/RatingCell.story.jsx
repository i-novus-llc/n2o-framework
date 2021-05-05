import React from 'react'
import { storiesOf } from '@storybook/react'
import fetchMock from 'fetch-mock'
import withPage from 'N2oStorybook/decorators/withPage'
import set from 'lodash/set'

import TextTableHeader from '../../headers/TextTableHeader'
import Table from '../../Table'
import Factory from '../../../../../core/factory/Factory'
import { WIDGETS } from '../../../../../core/factory/factoryLevels'

import RatingCell, { RatingCell as RatingCellComponent } from './RatingCell'
import RatingCellJson from './RatingCell.meta'

const stories = storiesOf('Ячейки/Рейтинг', module)

stories.addParameters({
    info: {
        propTables: [RatingCellComponent],
        propTablesExclude: [Table, RatingCell, Factory],
    },
})

const metadata = {
    Page_Table: {
        src: 'TableWidget',
        dataProvider: {
            url: 'n2o/data/test',
            pathMapping: {},
            queryMapping: {},
        },
        paging: {},
        table: {
            size: 10,
            fetchOnInit: true,
            hasSelect: true,
            className: 'n2o',
            colorFieldId: '',
            style: {},
            hasFocus: true,
            autoFocus: false,
            cells: [
                {
                    src: 'TextCell',
                    id: 'description',
                },
                {
                    ...RatingCellJson,
                    src: 'RatingCell',
                },
            ],
            headers: [
                {
                    src: 'TextTableHeader',
                    id: 'description',
                    label: 'Свойства',
                },
                {
                    src: 'TextTableHeader',
                    id: 'Rating',
                    label: 'Отображение',
                },
            ],
        },
    },
}

stories.addDecorator(withPage(metadata)).add('Компонент', () => {
    let data = {
        list: [{ id: 0, description: 'Рейтинг', rating: 5 }],
        count: 1,
    }

    const getStub = () => data

    const postStub = (url, xhr) => {
        const json = JSON.parse(xhr.body)

        data = set(data, `list[${json.id}].rating`, json.rating)

        return data
    }

    fetchMock
        .restore()
        .get('begin:n2o/data', getStub)
        .post('begin:n2o/data', postStub)

    return (
        <Factory level={WIDGETS} {...metadata.Page_Table} id="Page_Table" />
    )
})
