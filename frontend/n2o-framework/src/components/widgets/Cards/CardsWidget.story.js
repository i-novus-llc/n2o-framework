import React from 'react'
import { storiesOf } from '@storybook/react'
import fetchMock from 'fetch-mock'

import metadata from './CardsWidget.meta.json'
import Cards from './Cards'
import CardsWidget from './CardsWidget'

const stories = storiesOf('Виджеты/Виджет Cards', module)

stories.addParameters({
    info: {
        propTables: [Cards],
        propTablesExclude: [CardsWidget],
    },
})

const urlPattern = '*'

stories.add('Компонент', () => {
    fetchMock.restore().get(urlPattern, url => ({
        meta: {},
        page: 0,
        size: 10,
        list: [
            {
                card1: 'Карточка 1',
                card2:
            'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTivmPCOe7GabP3hXYjSilJL1rkyrKF65PLV-oFw4MsOagkurzQ',
                card21: 'Text 1',
                card211: 'Text 2',
                card31: 'Карточка 1',
            },
            {
                card1: 'Карточка 2',
                card2:
            'https://upload.wikimedia.org/wikipedia/commons/2/2e/Not_boyfriends_computer.jpg',
                card21: 'Text 1',
                card211: 'Text 2',
                card31: 'Карточка 2',
            },
            {
                card1: 'Карточка 3',
                card2:
            'https://www.zastavki.com/pictures/1024x1024/2015/Girls_Smiling_beautiful_girl__photo_George_Chernyad_ev_111193_31.jpg',
                card21: 'Text 1',
                card211: 'Text 2',
                card31: 'Карточка 3',
            },
        ],
    }))

    const withDataProvider = {
        dataProvider: {
            url: 'n2o/data/test',
            pathMapping: {},
            queryMapping: {
                'filter.name': {
                    link: 'models.filter[\'Page_Table\'].name',
                },
                'filter.surname': {
                    link: 'models.filter[\'Page_Table\'].surname',
                },
            },
        },
        paging: {
            size: 10,
        },
        ...metadata.Page_Cards,
    }

    return <CardsWidget {...withDataProvider} id="Page_Cards" />
})
