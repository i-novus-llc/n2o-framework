import React from 'react'
import { storiesOf } from '@storybook/react'
import pick from 'lodash/pick'
import { getStubData, page } from 'N2oStorybook/fetchMock'
import fetchMock from 'fetch-mock'

import Factory from '../../../core/factory/Factory'
import { WIDGETS } from '../../../core/factory/factoryLevels'
import withPage from '../../../../.storybook/decorators/withPage'

import List from './List'
import metadata from './List.meta'

const stories = storiesOf('Виджеты/Лист', module)
stories.addParameters({
    info: {
        propTables: [List],
        propTablesExclude: [Factory],
    },
})

const urlPattern = '*'
const delay = ms => new Promise(r => setTimeout(() => r(), ms))
stories
    .addDecorator(withPage(metadata))

    .add(
        'Компонент со стандартной реализацией',
        () => {
            const data = [
                {
                    id: 1,
                    leftTop: {
                        src: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                    },
                    header: 'It\'s a cat',
                    subHeader: 'The cat is stupid',
                    body: 'Some words about cats',
                    rightTop: '14',
                    rightBottom: '01.01.2019',
                    extra: 'Extra?!',
                },
                {
                    id: 2,
                    leftTop: {
                        src: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                    },
                    leftBottom: 'a little description',
                    header: 'It\'s a cat',
                    subHeader: 'The cat is stupid',
                    body: 'Some words about cats',
                    rightTop: '14',
                    rightBottom: '01.01.2019',
                    extra: 'Extra?!',
                },
            ]

            return <List data={data} selectedId={2} />
        },
        {
            info: {
                text: `
      Компонент 'Список'
      ~~~js
      import List from 'n2o-framework/lib/components/widgets/List/List';
      
      <List data={data} selectedId={2} />
      ~~~
      `,
            },
        },
    )
    .add(
        'Компонент без разделителя строк',
        () => {
            const data = [
                {
                    leftTop: {
                        src: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                    },
                    header: 'It\'s a cat',
                    subHeader: 'The cat is stupid',
                    body: 'Some words about cats',
                    rightTop: '14',
                    rightBottom: '01.01.2019',
                    extra: 'Extra?!',
                },
                {
                    leftTop: {
                        src: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                    },
                    header: 'It\'s a cat',
                    subHeader: 'The cat is stupid',
                    body: 'Some words about cats',
                    rightTop: '14',
                    rightBottom: '01.01.2019',
                    extra: 'Extra?!',
                },
            ]

            return <List data={data} divider={false} />
        },
        {
            info: {
                text: `
      Компонент 'Список'
      ~~~js
      import List from 'n2o-framework/lib/components/widgets/List/List';
      
      <List data={data} divider={false} />
      ~~~
      `,
            },
        },
    )
    .add('Метаданные с cells', () => {
        const data = []
        for (let i = 0; i < 3; i++) {
            data.push({
                leftTop: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                leftBottom: 'a little description',
                header: 'It\'s a cat',
                subHeader: 'The cat is stupid',
                body: 'Some words about cats',
                rightTop: '14',
                rightBottom: '01.01.2019',
                extra: 'Extra?!',
            })
        }
        fetchMock.restore().get(urlPattern, url => ({
            ...getStubData(url),
            list: data,
        }))

        return (
            <Factory
                level={WIDGETS}
                {...metadata.List}
                hasMoreNutton
                id="List"
            />
        )
    })
    .add('Кастомный клик по строке', () => {
        fetchMock.restore().get(urlPattern, url => ({
            list: [
                {
                    leftTop: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                    header: 'It\'s a cat',
                    subHeader: 'The cat is stupid',
                    body: 'Some words about cats',
                    rightTop: '14',
                    rightBottom: '01.01.2019',
                    extra: 'Extra?!',
                },
                {
                    leftTop: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                    header: 'It\'s a cat',
                    subHeader: 'The cat is stupid',
                    body: 'Some words about cats',
                    rightTop: '14',
                    rightBottom: '01.01.2019',
                    extra: 'Extra?!',
                },
            ],
        }))
        fetchMock.get('begin:n2o/page', page)
        const rowClick = {
            rowClick: {
                action: {
                    type: 'n2o/overlays/INSERT',
                    payload: {
                        pageUrl: '/Uid',
                        size: 'sm',
                        visible: true,
                        closeButton: true,
                        title: 'Новое модальное окно',
                        pageId: 'Uid',
                    },
                },
            },
        }
        const props = pick({ ...metadata.List }, [
            'src',
            'list',
            'dataProvider',
        ])
        return (
            <Factory
                level={WIDGETS}
                {...props}
                hasMoreButton={false}
                {...rowClick}
                id="List"
            />
        )
    })
    .add('Кнопка "Еще"', () => {
        fetchMock.restore().get(urlPattern, url => delay(1000).then(() => ({
            list: [
                {
                    leftTop: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                    header: 'It\'s a cat',
                    subHeader: 'The cat is stupid',
                    body: 'Some words about cats',
                    rightTop: '14',
                    rightBottom: '01.01.2019',
                    extra: 'Extra?!',
                },
                {
                    leftTop: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                    header: 'It\'s a cat',
                    subHeader: 'The cat is stupid',
                    body: 'Some words about cats',
                    rightTop: '14',
                    rightBottom: '01.01.2019',
                    extra: 'Extra?!',
                },
            ],
        })))
        const props = pick({ ...metadata.List }, [
            'src',
            'list',
            'dataProvider',
            'paging',
            'hasMoreButton',
        ])
        return <Factory level={WIDGETS} {...props} id="List" />
    })
    .add('Загрузка по скроллу', () => {
        const data = []
        for (let i = 0; i < 10; i++) {
            data.push({
                leftTop: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                header: 'It\'s a cat',
                subHeader: 'The cat is stupid',
                body: 'Some words about cats',
                rightTop: '14',
                rightBottom: '01.01.2019',
                extra: 'Extra?!',
            })
        }
        fetchMock.restore().get(urlPattern, url => delay(1000).then(() => ({
            list: data,
        })))
        const props = pick({ ...metadata.List }, [
            'src',
            'list',
            'dataProvider',
            'paging',
            'fetchOnScroll',
        ])
        return (
            <Factory
                level={WIDGETS}
                maxHeight={290}
                {...props}
                fetchOnScroll
                id="List"
            />
        )
    })
    .add('Компонент с paging', () => {
        const data = []
        for (let i = 0; i < 5; i++) {
            data.push({
                leftTop: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                header: 'It\'s a cat',
                subHeader: 'The cat is stupid',
                body: 'Some words about cats',
                rightTop: '14',
                rightBottom: '01.01.2019',
                extra: 'Extra?!',
            })
        }
        fetchMock.restore().get(urlPattern, url => delay(1000).then(() => ({
            ...getStubData(url),
            list: data,
        })))
        const props = pick({ ...metadata.List }, [
            'src',
            'list',
            'dataProvider',
            'paging',
        ])
        return (
            <Factory
                level={WIDGETS}
                {...props}
                paging={{
                    size: 10,
                    prev: true,
                    next: true,
                }}
                id="List"
            />
        )
    })
    .add('Компонент с 1000 записей', () => {
        const data = []
        for (let i = 0; i < 1000; i++) {
            data.push({
                leftTop: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                header: 'It\'s a cat',
                subHeader: 'The cat is stupid',
                body: 'Some words about cats',
                rightTop: '14',
                rightBottom: '01.01.2019',
                extra: 'Extra?!',
            })
        }
        fetchMock.restore().get(urlPattern, url => delay(1000).then(() => ({
            ...getStubData(url),
            list: data,
        })))
        const props = pick({ ...metadata.List }, [
            'src',
            'list',
            'dataProvider',
            'paging',
        ])
        return <Factory level={WIDGETS} {...props} id="List" />
    })
    .add('Placeholder', () => {
        const data = []
        for (let i = 0; i < 3; i++) {
            data.push({
                leftTop: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                leftBottom: 'a little description',
                header: 'It\'s a cat',
                subHeader: 'The cat is stupid',
                body: 'Some words about cats',
                rightTop: '14',
                rightBottom: '01.01.2019',
                extra: 'Extra?!',
            })
        }
        fetchMock.restore().get(urlPattern, url => new Promise((res, rej) => setTimeout(
            () => res({
                ...getStubData(url),
                list: data,
            }),
            3000,
        )))

        const newMeta = { ...metadata }
        newMeta.List.placeholder = {
            rows: 3,
            paragraph: 4,
            avatar: true,
            type: 'list',
        }

        return (
            <Factory
                level={WIDGETS}
                {...metadata.List}
                hasMoreNutton
                id="List"
            />
        )
    })
