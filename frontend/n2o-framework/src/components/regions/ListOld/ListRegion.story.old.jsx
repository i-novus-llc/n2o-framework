/* eslint-disable */
import React from 'react'
import { storiesOf } from '@storybook/react'
import set from 'lodash/set'
import omit from 'lodash/omit'
import pullAt from 'lodash/pullAt'
import pick from 'lodash/pick'
import { InitWidgetsList } from 'N2oStorybook/json'
import fetchMock from 'fetch-mock'
import { getStubData } from 'N2oStorybook/fetchMock'

import { metadataSuccess } from '../../../actions/pages'
import HtmlWidgetJson from '../../widgets/Html/HtmlWidget.meta'
import AuthButtonContainer from '../../../core/auth/AuthLogin'
import { makeStore } from '../../../../.storybook/decorators/utils'
import cloneObject from '../../../utils/cloneObject'

import ListMetadata from './ListMetadata.meta'
import SecureListRegionJson from './ListRegion.meta'
import ListRegion from './ListRegion'

const stories = storiesOf('Регионы/Лист', module)

const ListRegionJson = set(
    cloneObject(SecureListRegionJson),
    'items',
    pullAt(cloneObject(SecureListRegionJson).items, 0),
)
const { store } = makeStore()

stories
    .add('Метаданные', () => {
        store.dispatch(metadataSuccess('Page', HtmlWidgetJson))

        return <ListRegion {...ListRegionJson} pageId="Page" />
    })
    .add('Ограничение доступа', () => {
        store.dispatch(metadataSuccess('Page', ListMetadata))

        return (
            <div>
                <small>
          Введите
                    {' '}
                    <mark>admin</mark>
, чтобы увидеть скрытый виджет региона
                </small>
                <AuthButtonContainer />
                <br />
                <ListRegion {...SecureListRegionJson} pageId="Page" />
            </div>
        )
    })

    .add('Инициализация виджетов', () => {
        fetchMock
            .restore()
            .get('begin:n2o/data/test', getStubData)
            .get('begin:n2o/data2/test', async (url) => {
                await new Promise(r => setTimeout(() => {
                    r()
                }, 2000))

                return getStubData(url)
            })

        store.dispatch(
            metadataSuccess('Page', { ...pick(InitWidgetsList, 'widgets') }),
        )

        return <ListRegion {...omit(InitWidgetsList, 'widgets')} pageId="Page" />
    })
