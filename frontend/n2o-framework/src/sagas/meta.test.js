import { runSaga } from 'redux-saga'

import { addAlert, addMultiAlerts } from '../ducks/alerts/store'
import { GLOBAL_KEY } from '../ducks/alerts/constants'

import {
    alertEffect,
    redirectEffect,
    clearFormEffect,
} from './meta'

const setupAlertEffect = () => {
    const meta = {
        alert: {
            alertKey: 'Page_Table',
            messages: [
                {
                    id: 'ae9564ee-07ee-4e42-bf2e-a1f7c5d0d7dc',
                    placement: 'topRight',
                    text: 'Не удалось получить конфигурация приложения',
                    closeButton: false,
                    severity: 'danger',
                    timeout: 8000,
                },
            ],
        },
    }

    const metaWithoutPlacement = {
        alert: {
            alertKey: 'Page_Table',
            messages: [
                {
                    id: 'ae9564ee-07ee-4e42-bf2e-a1f7c5d0d7de',
                    text: 'Не удалось получить конфигурация приложения',
                    closeButton: false,
                    severity: 'danger',
                    timeout: 8000,
                },
            ],
        },
    }


    const metaMulti = {
        alert: {
            alertKey: 'Page_Table',
            messages: [
                {
                    id: 'ae9564ee-07ee-4e42-bf2e-a1f7c5d0d7da',
                    placement: 'topRight',
                    text: 'Успех',
                    closeButton: true,
                    severity: 'success',
                    timeout: 3000,
                },
                {
                    id: 'ae9564ee-07ee-4e42-bf2e-a1f7c5d0d7db',
                    placement: 'top',
                    text: 'Не удалось получить конфигурация приложения',
                    closeButton: true,
                    severity: 'danger',
                    timeout: 8000,
                },
            ],
        },
    }

    const alert = alertEffect({ meta })
    const alertWithoutPlacement = alertEffect({ meta: metaWithoutPlacement })
    const alertsMulti = alertEffect({ meta: metaMulti })

    return {
        meta,
        metaMulti,
        metaWithoutPlacement,
        alert,
        alertWithoutPlacement,
        alertsMulti,
    }
}

describe('Сага для перехвата меты, сайд-эффектов из меты', () => {
    // TODO: Починить тест, как только починится сага clearFormEffect
    // describe('Проверка саги clearFormEffect', () => {
    //     it('должен вызывать экшен сброса формы', () => {
    //         const gen = clearFormEffect({
    //             meta: {
    //                 clearForm: 'testForm',
    //             },
    //         })
    //
    //         const value = gen.next()
    //         expect(value.value.type).toBe('PUT')
    //         expect(value.value.payload.action).toEqual({
    //             type: 'n2o/models/RESET_FORM',
    //             meta: {
    //                 form: 'testForm',
    //             },
    //         })
    //         expect(gen.next().done).toBeTruthy()
    //     })
    // })
    describe('Проверка саги redirectEffect', () => {
        it('должен вызвать push', async () => {
            const dispatched = []
            const fakeStore = {
                getState: () => ({}),
                dispatch: action => dispatched.push(action),
            }
            const action = {
                meta: {
                    redirect: {
                        path: '/n2o/data/1',
                        pathMapping: {},
                        queryMapping: {},
                        target: 'application',
                    },
                },
            }
            await runSaga(fakeStore, redirectEffect, action)
            expect(dispatched[0].type).toBe('@@router/CALL_HISTORY_METHOD')
            expect(dispatched[0].payload.method).toBe('push')
            expect(dispatched[0].payload.args[0]).toBe('/n2o/data/1')
        })
    })

    describe('Проверка саги alertEffect', () => {
        it('Проверяет диспатч экшена создания Alert - ADD', () => {
            const { alert } = setupAlertEffect()
            let gen= alert.next()
            expect(gen.value.payload.action.type).toBe(addAlert.type)
        })

        it('Проверяет payload при одном Alert', () => {
            const { alert, meta } = setupAlertEffect()
            let gen = alert.next()
            expect(gen.value.payload.action.payload.key).toEqual('topRight')
            expect(gen.value.payload.action.payload.alerts[0].closeButton).toEqual(
                meta.alert.messages[0].closeButton,
            )
            expect(gen.value.payload.action.payload.alerts[0].id).toEqual(
                meta.alert.messages[0].id,
            )
            expect(gen.value.payload.action.payload.alerts[0].text).toEqual(
                meta.alert.messages[0].text,
            )
            expect(gen.value.payload.action.payload.alerts[0].severity).toEqual(
                meta.alert.messages[0].severity,
            )
            expect(gen.value.payload.action.payload.alerts[0].placement).toEqual(
                meta.alert.messages[0].placement,
            )
            expect(gen.value.payload.action.payload.alerts[0].timeout).toEqual(
                meta.alert.messages[0].timeout,
            )
        })

        it('Проверяет подстановку дефолтного значения в key', () => {
            const { alertWithoutPlacement } = setupAlertEffect()
            let gen = alertWithoutPlacement.next()
            expect(gen.value.payload.action.payload.key).toBe(GLOBAL_KEY)

        })

        it('Проверяет отсутствие placement в payload, если его не было в meta', () => {
            const { alertWithoutPlacement } = setupAlertEffect()
            let gen = alertWithoutPlacement.next()
            expect(gen.value.payload.action.payload.alerts[0].placement).toBeUndefined()
        })

        it('Проверяет диспатч экшена создания нескольких Alert - ADD_MULTI', () => {
            const { alertsMulti } = setupAlertEffect()
            let gen= alertsMulti.next()
            expect(gen.value.payload.action.type).toBe(addMultiAlerts.type)
        })

        it('Проверяет payload при мультиалертах', () => {
            const { alertsMulti, metaMulti } = setupAlertEffect()
            let gen = alertsMulti.next()
            expect(gen.value.payload.action.payload.key).toEqual(metaMulti.alert.messages[0].placement)
            expect(gen.value.payload.action.payload.alerts[0].closeButton).toEqual(
                metaMulti.alert.messages[0].closeButton,
            )
            expect(gen.value.payload.action.payload.alerts[0].id).toEqual(
                metaMulti.alert.messages[0].id,
            )
            expect(gen.value.payload.action.payload.alerts[0].text).toEqual(
                metaMulti.alert.messages[0].text,
            )
            expect(gen.value.payload.action.payload.alerts[0].severity).toEqual(
                metaMulti.alert.messages[0].severity,
            )
            expect(gen.value.payload.action.payload.alerts[0].placement).toEqual(
                metaMulti.alert.messages[0].placement,
            )
            expect(gen.value.payload.action.payload.alerts[0].timeout).toEqual(
                metaMulti.alert.messages[0].timeout,
            )

            gen = alertsMulti.next()
            expect(gen.value.payload.action.payload.key).toEqual(metaMulti.alert.messages[1].placement)
            expect(gen.value.payload.action.payload.alerts[0].closeButton).toEqual(
                metaMulti.alert.messages[1].closeButton,
            )
            expect(gen.value.payload.action.payload.alerts[0].id).toEqual(
                metaMulti.alert.messages[1].id,
            )
            expect(gen.value.payload.action.payload.alerts[0].text).toEqual(
                metaMulti.alert.messages[1].text,
            )
            expect(gen.value.payload.action.payload.alerts[0].severity).toEqual(
                metaMulti.alert.messages[1].severity,
            )
            expect(gen.value.payload.action.payload.alerts[0].placement).toEqual(
                metaMulti.alert.messages[1].placement,
            )
            expect(gen.value.payload.action.payload.alerts[0].timeout).toEqual(
                metaMulti.alert.messages[1].timeout,
            )
        })
    })
})
