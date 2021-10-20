import React from 'react'
import ReactDOM from 'react-dom'
import renderer from 'react-test-renderer'
import { Provider } from 'react-redux'
import configureMockStore from 'redux-mock-store'
import { withContext } from 'recompose'
import PropTypes from 'prop-types'
import { BrowserRouter } from 'react-router-dom'
import sinon from 'sinon'

import FactoryProvider from '../../core/factory/FactoryProvider'

import DefaultBreadcrumb from './Breadcrumb/DefaultBreadcrumb'
import { PageContainer } from './Page'

const defaultProps = {
    getMetadata: () => {},
}

const testDataToProps = {
    props: {},
    newProps: {},
}

const testMetadata = {
    id: 'pageId',
    page: {
        title: 'title',
    },
    routes: {
        list: [
            {
                path: '/test',
                exact: true,
            },
        ],
    },
    breadcrumb: [
        {
            label: 'Первая',
            path: '/',
        },
        {
            label: '`\'Вторая: \' + id`',
            modelLink: 'pages[\'testSimplePageJson\'].metadata',
        },
    ],
    widgets: {
        Page_Wireframe: {
            src: 'WireframeWidget',
            name: 'name',
            toolbar: {
                topLeft: [
                    {
                        buttons: [
                            {
                                id: 'testButton',
                                title: 'test',
                            },
                        ],
                    },
                ],
            },
            wireframe: {
                className: '',
                title: 'Виджет первой страницы',
                height: 300,
                fetchOnInit: false,
            },
        },
    },
}

const setup = (propOverrides) => {
    const props = {
        ...defaultProps,
        ...propOverrides,
    }

    const wrapper = mount(<PageContainer {...props} />)

    return {
        wrapper,
        props,
    }
}

describe.skip('Тесты Page', () => {
    it('Установка Page', () => {
        const { wrapper } = setup()
        expect(wrapper).toBeTruthy()
    })

    it('Проверка вызова getMetadata при установке компонента', () => {
        const getMetadata = sinon.spy()
        setup({ getMetadata })
        expect(getMetadata.calledOnce).toEqual(true)
        expect(getMetadata.calledWithMatch()).toEqual(true)
    })

    it('Проверка вызова reset и getMetadate если метадата поменялась', () => {
        const getMetadata = sinon.spy()
        const reset = sinon.spy()

        const stubFn = sinon
            .stub(PageContainer.prototype, 'shouldGetPageMetadata')
            .returns(true)
        const { wrapper } = setup({ metadata: 'test', pageId: 'pageId' })
        wrapper.setProps({ metadata: 'test2', reset, getMetadata })
        expect(reset.calledOnce).toEqual(true)
        expect(reset.calledWithMatch('pageId')).toEqual(true)
        expect(getMetadata.calledOnce).toEqual(true)
        stubFn.restore()
    })

    it('Проверка вызова routeMap если pageUrl поменялся и getMetadata если есть error', () => {
        const routeMap = sinon.spy()
        const getMetadata = sinon.spy()

        const stubFn = sinon
            .stub(PageContainer.prototype, 'shouldGetPageMetadata')
            .returns(false)
        const { wrapper } = setup({ metadata: 'test', pageId: 'pageId' })
        wrapper.setProps({
            metadata: 'test',
            pageUrl: 'newPageUrl',
            routeMap,
            getMetadata,
            error: true,
        })
        expect(routeMap.calledOnce).toEqual(true)
        expect(routeMap.calledWithMatch()).toEqual(true)
        expect(getMetadata.calledWithMatch()).toEqual(true)
        stubFn.restore()
    })

    it('shouldGetPageMetadata возвращает true при смене route если route есть в метаданных', () => {
        const spyFn = sinon.spy(PageContainer.prototype, 'shouldGetPageMetadata')
        const { wrapper } = setup({ metadata: 'test', pageId: 'pageId' })

        wrapper.setProps({
            reset: () => null,
            metadata: {
                routes: {
                    list: [
                        {
                            path: '/test',
                            exact: true,
                            isOtherPage: true,
                        },
                    ],
                },
            },
            location: {
                pathname: '/test',
            },
        })

        expect(spyFn.calledOnce).toEqual(true)
        expect(spyFn.returnValues[0]).toEqual(true)
        spyFn.restore()
    })

    it('shouldGetPageMetadata возвращает false если silent = true', () => {
        const spyFn = sinon.spy(PageContainer.prototype, 'shouldGetPageMetadata')
        const { wrapper } = setup({ metadata: 'test', pageId: 'pageId' })

        wrapper.setProps({
            reset: () => null,
            metadata: {
                routes: {
                    list: [
                        {
                            path: '/test',
                            exact: true,
                            isOtherPage: true,
                        },
                    ],
                },
            },
            location: { state: { silent: true } },
        })

        expect(spyFn.calledOnce).toEqual(true)
        expect(spyFn.returnValues[0]).toEqual(false)
        spyFn.restore()
    })

    it('shouldGetPageMetadata возвращает true если route есть в метаданных но нет isOtherPage', () => {
        const spyFn = sinon.spy(PageContainer.prototype, 'shouldGetPageMetadata')
        const { wrapper } = setup({ metadata: 'test', pageId: 'pageId' })

        wrapper.setProps({
            reset: () => null,
            metadata: {
                routes: {
                    list: [
                        {
                            path: '/test',
                            exact: true,
                        },
                    ],
                },
            },
            location: { pathname: '/test' },
        })

        expect(spyFn.calledOnce).toEqual(true)
        expect(spyFn.returnValues[0]).toEqual(false)
        spyFn.restore()
    })
    it('Вызов reset при unmount компонента', () => {
        const reset = sinon.spy()
        const component = shallow(
            <PageContainer pageId="pageId" reset={reset} getMetadata={() => null} />,
        )
        component.unmount()
        expect(reset.calledOnce).toEqual(true)
        expect(reset.calledWithMatch('pageId')).toEqual(true)
    })
})
