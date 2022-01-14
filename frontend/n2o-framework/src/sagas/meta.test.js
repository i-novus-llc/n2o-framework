import { runSaga } from 'redux-saga'

import { addFieldMessage } from '../ducks/form/store'
import { addMultiAlerts, removeAllAlerts } from '../ducks/alerts/store'

import {
    alertEffect,
    redirectEffect,
    messagesFormEffect,
    clearFormEffect,
} from './meta'

const setupAlertEffect = () => {
    const meta = {
        alert: {
            alertKey: 'Page_Table',
            messages: [
                {
                    label: 'Ошибка',
                    text: 'Не удалось получить конфигурация приложения',
                    closeButton: false,
                    severity: 'danger',
                },
            ],
        },
    }
    const alert = alertEffect({
        meta,
    })
    return {
        meta,
        alert,
    }
}

const setupMessageFormEffect = () => {
    const meta = {
        'messages.form': 'Page_Form',
        'messages.fields': [
            {
                name: 'field1',
                message: {
                    severity: 'success',
                    text: 'Успешное действие',
                },
            },
        ],
    }
    const messageForm = messagesFormEffect({
        meta,
    })
    return {
        meta,
        messageForm,
    }
}

describe('Сага для перехвата меты, сайд-эффектов из меты', () => {
    describe('Проверка саги clearFormEffect', () => {
        it('должен вызывать экшен сброса формы', () => {
            const gen = clearFormEffect({
                meta: {
                    clearForm: 'testForm',
                },
            })

            const value = gen.next()
            expect(value.value.type).toBe('PUT')
            expect(value.value.payload.action).toEqual({
                type: '@@redux-form/RESET',
                meta: {
                    form: 'testForm',
                },
            })
            expect(gen.next().done).toBeTruthy()
        })
    })
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
        it('Проверяет диспатч экшена создания Alert', () => {
            const { alert } = setupAlertEffect()
            let gen = alert.next()
            expect(gen.value.payload.action.type).toEqual(removeAllAlerts.type)
            gen = alert.next()
            expect(gen.value.payload.action.type).toEqual(addMultiAlerts.type)
        })

        it('Проверяет payload саги alertEffect', () => {
            const { alert, meta } = setupAlertEffect()
            let gen = alert.next()
            gen = alert.next()
            expect(gen.value.payload.action.payload.key).toEqual(meta.alert.alertKey)
            expect(gen.value.payload.action.payload.alerts[0].closeButton).toEqual(
                meta.alert.messages[0].closeButton,
            )
            expect(gen.value.payload.action.payload.alerts[0].label).toEqual(
                meta.alert.messages[0].label,
            )
            expect(gen.value.payload.action.payload.alerts[0].text).toEqual(
                meta.alert.messages[0].text,
            )
            expect(gen.value.payload.action.payload.alerts[0].severity).toEqual(
                meta.alert.messages[0].severity,
            )
        })
    })

    describe('Проверяет сагу messagesFormEffect', () => {
        it('Проверка диспатча саги messagesFormEffect', () => {
            const { messageForm } = setupMessageFormEffect()
            let gen = messageForm.next()
            gen = messageForm.next()
            expect(gen.value.payload.action.payload[0].type).toEqual(
                addFieldMessage.type,
            )
        })

        it('Проверка payload саги messageFormEffect', () => {
            const { messageForm, meta } = setupMessageFormEffect()
            let gen = messageForm.next()
            gen = messageForm.next()
            expect(gen.value.payload.action.payload[0].payload.form).toEqual(
                meta['messages.form'],
            )
            expect(
                gen.value.payload.action.payload[0].payload.message.severity,
            ).toEqual(meta['messages.fields'].severity)
            expect(gen.value.payload.action.payload[0].payload.message.text).toEqual(
                meta['messages.fields'].text,
            )
        })
    })
})
