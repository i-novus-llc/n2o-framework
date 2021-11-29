import React from 'react'
import sinon from 'sinon'
import isEmpty from 'lodash/isEmpty'
import { Provider } from 'react-redux'
import { createStore } from 'redux'
import fetchMock from 'fetch-mock'

import FactoryProvider from '../../../core/factory/FactoryProvider'
import createFactoryConfig from '../../../core/factory/createFactoryConfig'
import reducers from '../../../reducers'
import history from '../../../history'

import ListWidgetMeta from './List.meta'
import ListContainer from './ListContainer'

fetchMock.get('*', () => ({
    list: [
        {
            test: 'test',
        },
    ],
}))

const store = createStore(reducers(history))
const NullComponent = () => null

const setup = propsOverride => mount(
    <Provider store={store}>
        <ListContainer
            dataProvider={ListWidgetMeta.List.dataProvider}
            list={ListWidgetMeta.List.list}
            models= {{
                "datasource": [],
                "multi": [],
                "filter": {}
            }}
            {...propsOverride}
        >
            <NullComponent />
        </ListContainer>
    </Provider>,
)

const factoryProvider = () => mount(
    <FactoryProvider config={createFactoryConfig({})}>
        <NullComponent />
    </FactoryProvider>,
)

describe('Проверка ListContainer', () => {
    it('Маппит данные в компоненты', () => {
        const wrapper = setup()
        const ListContainer = wrapper.find('ListContainer')
        ListContainer.setState({
            datasource: [
                {
                    leftTop: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
                    leftBottom: 'a little description',
                    header: 'It\'s a cat',
                    subHeader: 'The cat is stupid',
                    body: 'Some words about cats',
                    rightTop: 'What do you know about cats?',
                    rightBottom: 'But cats aren\'t only stupid they\'re still so sweet ',
                    extra: 'Extra?!',
                },
            ],
        })
        const mappedData = ListContainer.instance().mapSectionComponents(
            factoryProvider()
                .instance()
                .resolveProps(ListWidgetMeta.List.list, createFactoryConfig({})),
        )
        expect(!isEmpty(mappedData)).toEqual(true)
        expect(React.isValidElement(mappedData[0].leftTop)).toEqual(true)
        expect(React.isValidElement(mappedData[0].leftBottom)).toEqual(true)
        expect(React.isValidElement(mappedData[0].header)).toEqual(true)
        expect(React.isValidElement(mappedData[0].subHeader)).toEqual(true)
        expect(React.isValidElement(mappedData[0].body)).toEqual(true)
        expect(React.isValidElement(mappedData[0].rightTop)).toEqual(true)
        expect(React.isValidElement(mappedData[0].rightBottom)).toEqual(true)
        expect(React.isValidElement(mappedData[0].extra)).toEqual(true)
    })
})
