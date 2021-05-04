import { ADD, ADD_MULTI, REMOVE, REMOVE_ALL } from '../constants/alerts'

import { addAlert, addAlerts, removeAlert, removeAlerts } from './alerts'

const key = 'alertKey'
const id = 'alertId'
const alerts = [
    {
        severity: 'danger',
        label: 'Тестовый алерт',
        text: 'Это тестовый алерт',
        details: {},
        timeout: 1000,
        closeButton: true,
    },
]

describe('Тесты для экшенов alerts', () => {
    describe('Экшен addAlert', () => {
        it('Генирирует правильное событие', () => {
            const action = addAlert(key, alerts[0])
            expect(action.type).toEqual(ADD)
        })
        it('Проверяет правильность возвращаемых данных', () => {
            const action = addAlert(key, alerts[0])
            expect(action.payload.severity).toEqual(alerts[0].severity)
            expect(action.payload.label).toEqual(alerts[0].label)
            expect(action.payload.text).toEqual(alerts[0].text)
            expect(action.payload.details).toEqual(alerts[0].details)
            expect(action.payload.timeout).toEqual(alerts[0].timeout)
        })
    })

    describe('Экшен addAlerts', () => {
        it('Генирирует правильное событие', () => {
            const action = addAlerts(key, alerts)
            expect(action.type).toEqual(ADD_MULTI)
        })
        it('Проверяет правильность возвращаемых данных', () => {
            const action = addAlerts(key, alerts)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.alerts).toEqual(alerts)
        })
    })

    describe('Экшен removeAlert', () => {
        it('Генирирует правильное событие', () => {
            const action = removeAlert(key, id)
            expect(action.type).toEqual(REMOVE)
        })
        it('Проверяет правильность возвращаемых данных', () => {
            const action = removeAlert(key, id)
            expect(action.payload.id).toEqual(id)
            expect(action.payload.key).toEqual(key)
        })
    })

    describe('Экшен removeAlerts', () => {
        it('Генирирует правильное событие', () => {
            const action = removeAlerts(key)
            expect(action.type).toEqual(REMOVE_ALL)
        })
        it('Проверяет правильность возвращаемых данных', () => {
            const action = removeAlerts(key)
            expect(action.payload.key).toEqual(key)
        })
    })
})
