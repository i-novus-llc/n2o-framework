import React from 'react'
import { storiesOf, forceReRender } from '@storybook/react'
import { StateDecorator, Store } from '@sambego/storybook-state'
import { Row, Col, Button, Input } from 'reactstrap'
import { page } from 'N2oStorybook/fetchMock'
import { ShowModalTitle, ShowModal } from 'N2oStorybook/json'
import fetchMock from 'fetch-mock'

import Actions from '../actions/Actions'
import Factory from '../../core/factory/Factory'
import { WIDGETS } from '../../core/factory/factoryLevels'
import withPage from '../../../.storybook/decorators/withPage'

import OverlayPages from './OverlayPages'
import { ModalWindow } from './ModalPage'

const store = new Store({
    visible: true,
    className: 'n2o-custom-modal-dialog',
    hasHeader: true,
})

store.subscribe(forceReRender)

const stories = storiesOf('Действия/Модальное окно', module)

stories.addDecorator(withPage(ShowModalTitle))
stories.addDecorator(StateDecorator(store))

stories.addParameters({
    info: {
        propTables: [ModalWindow],
        propTablesExclude: [Factory, OverlayPages],
    },
})

stories

    .add('Компонент', () => (
        <ModalWindow
            backdrop
            loading={false}
            title="Заголовок компонента"
            src="OutputText"
            visible={store.get('visible')}
            close={() => store.set({ visible: false })}
            renderFromSrc={() => {}}
        />
    ))
    .add('Открытие модального окна', () => {
        fetchMock.restore().get('begin:n2o/page', page)

        return (
            <Factory level={WIDGETS} {...ShowModal.Page_Form} id="Page_Form" />
        )
    })
// .add('Заголовок модального окна', () => {
//   fetchMock.restore().get('begin:n2o/page', page);
//
//   return <Factory level={WIDGETS} {...ShowModalTitle['Page_Form']} id="Page_Form" />;
// })
    .add('Прелоадер модального окна', () => <ModalWindow />)
    .add('Prompt в модальном окне', () => {
        fetchMock.restore().get('begin:n2o/page', {
            id: 'Uid',
            routes: {
                list: [
                    {
                        path: '/',
                        exact: true,
                    },
                ],
                pathMapping: {},
                queryMapping: {},
            },
            widgets: {
                Page_Html: {
                    src: 'FormWidget',
                    form: {
                        fieldsets: [
                            {
                                src: 'StandardFieldset',
                                rows: [
                                    {
                                        cols: [
                                            {
                                                fields: [
                                                    {
                                                        id: 'name',
                                                        src: 'StandardField',
                                                        label: 'Имя',
                                                        control: {
                                                            src: 'InputText',
                                                        },
                                                    },
                                                ],
                                            },
                                        ],
                                    },
                                ],
                            },
                        ],
                    },
                },
            },
            actions: {
                close: {
                    id: 'close',
                    src: 'perform',
                    options: {
                        type: 'n2o/overlays/CLOSE',
                        payload: {
                            name: 'Uid',
                            prompt: true,
                        },
                        meta: {
                            modalsToClose: 1,
                        },
                    },
                },
            },
            toolbar: {
                bottomRight: [
                    {
                        buttons: [
                            {
                                id: 'close',
                                actionId: 'close',
                                conditions: {},
                                title: 'Закрыть',
                            },
                        ],
                    },
                ],
            },
            layout: {
                src: 'SingleLayout',
                regions: {
                    single: [
                        {
                            src: 'NoneRegion',
                            items: [
                                {
                                    widgetId: 'Page_Html',
                                },
                            ],
                        },
                    ],
                },
            },
        })
        const props = {
            ...ShowModal.Page_Form,
        }

        props.actions = {
            showModal: {
                src: 'perform',
                options: {
                    type: 'n2o/overlays/INSERT',
                    payload: {
                        name: 'Uid',
                        pageUrl: '/Uid',
                        pathMapping: {},
                        modelLink: 'models.resolve[\'Uid\']',
                        title: '`\'Заголовок: \' + modalTitle`',
                        size: 'sm',
                        visible: true,
                        closeButton: true,
                        pageId: 'Uid',
                        prompt: true,
                        mode: 'modal',
                    },
                },
            },
        }

        return <Factory level={WIDGETS} {...props} id="Page_Form" />
    })
    .add('Кастомный css класс', () => (
        <div>
            <Input
                placeholder="Enter class name"
                onChange={(e) => {
                    store.set({ className: e.target.value })
                }}
            />
            <br />
            <Button
                onClick={() => {
                    store.set({ visible: true })
                }}
            >
          Open window
            </Button>
            <ModalWindow
                hasHeader
                visible={store.get('visible')}
                close={() => {
                    store.set({ visible: false })
                }}
                className={store.get('className')}
            />
        </div>
    ))
    .add('Смена фона окна', () => (
        <Row>
            <Col md={4}>
                <code>"backdrop" : "true"</code>
                <div>
                    <Button
                        onClick={() => {
                            store.set({ visible: true })
                            store.set({ backdrop: true })
                        }}
                    >
              Open
                    </Button>
                </div>
            </Col>
            <Col md={4}>
                <code>"backdrop" : "false"</code>
                <div>
                    <Button
                        onClick={() => {
                            store.set({ visible: true })
                            store.set({ backdrop: false })
                        }}
                    >
              Open
                    </Button>
                </div>
            </Col>
            <Col md={4}>
                <code>"backdrop" : "static"</code>
                <div>
                    <Button
                        onClick={() => {
                            store.set({ visible: true })
                            store.set({ backdrop: 'static' })
                        }}
                    >
              Open
                    </Button>
                </div>
            </Col>
            <ModalWindow
                hasHeader
                backdrop={store.get('backdrop')}
                visible={store.get('visible')}
                close={() => store.set({ visible: false })}
            />
        </Row>
    ))
    .add('Отображение хедара on/off', () => (
        <div>
            <div>
                <Input
                    onChange={(e) => {
                        store.set({
                            hasHeader: e.target.value !== 'false',
                        })
                    }}
                    type="select"
                >
                    <option value="true">Header On</option>
                    <option value="false">Header Off</option>
                </Input>
                <br />
                <Button
                    onClick={() => {
                        store.set({
                            visible: true,
                        })
                    }}
                >
            Open window
                </Button>
            </div>
            <ModalWindow
                backdrop
                headerTitle="Header title"
                hasHeader={store.get('hasHeader')}
                visible={store.get('visible')}
                close={() => store.set({ visible: false })}
            />
        </div>
    ))
