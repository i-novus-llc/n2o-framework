import React from 'react'
import cn from 'classnames'
import { Provider } from 'react-redux'
import mockStore from 'redux-mock-store'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'

import ButtonContainer from '../ButtonContainer'

function test({ disabled }) {
    return <div className={cn('btn-toolbar', { disabled })}>test</div>
}

const setup = (store, component) => mount(
    <Provider store={mockStore()(store)}>
        <ButtonContainer
            containerKey="test"
            id="testId"
            component={component}
            isInit
            visible={false}
            disabled={false}
            color="success"
            size="lg"
            count={1}
            title="test"
            icon="fa fa-plus"
            children={[]}
            initialProps={{
                visible: false,
            }}
        />
    </Provider>,
)

describe('Проверка ButtonContainer', () => {
    it('Отрисовывается по visible = true', () => {
        const store = {
            toolbar: {
                test: {
                    testId: {
                        id: 'testId',
                        visible: true,
                    },
                },
            },
        }
        const wrapper = setup(store, test)

        expect(wrapper.find('.btn-toolbar').exists()).toEqual(true)
    })
    it('Не отрисовывается по visible = false', () => {
        const store = {
            toolbar: {
                test: {
                    testId: {
                        id: 'testId',
                        visible: false,
                    },
                },
            },
        }
        const wrapper = setup(store, test)

        expect(wrapper.find('.btn-toolbar').exists()).toEqual(false)
    })
    it('отрисовывает без disabled', () => {
        const store = {
            toolbar: {
                test: {
                    testId: {
                        id: 'testId',
                        disabled: false,
                    },
                },
            },
        }
        const wrapper = setup(store, test)

        expect(wrapper.find('.disabled').exists()).toEqual(false)
    })
    it('отрисовывает с disabled', () => {
        const store = {
            toolbar: {
                test: {
                    testId: {
                        id: 'testId',
                        disabled: true,
                    },
                },
            },
        }
        const wrapper = setup(store, test)

        expect(wrapper.find('.disabled').exists()).toEqual(true)
    })
    it('Отрисовывается dropDown при visible = true', () => {
        const store = {
            toolbar: {
                test: {
                    testId: {
                        id: 'testId',
                        visible: true,
                    },
                },
            },
        }
        const wrapper = setup(store, DropdownMenu)

        expect(wrapper.find('.dropdown-menu').exists()).toEqual(true)
    })
    it('Отрисовывается dropDown при visible = false', () => {
        const store = {
            toolbar: {
                test: {
                    testId: {
                        id: 'testId',
                        visible: false,
                    },
                },
            },
        }
        const wrapper = setup(store, DropdownMenu)

        expect(wrapper.find('.dropdown-menu').exists()).toEqual(true)
    })
    it('отрисовывает dropdown без disabled', () => {
        const store = {
            toolbar: {
                test: {
                    testId: {
                        id: 'testId',
                        disabled: false,
                    },
                },
            },
        }
        const wrapper = setup(store, DropdownMenu)

        expect(wrapper.find('.disabled').exists()).toEqual(false)
    })
})
