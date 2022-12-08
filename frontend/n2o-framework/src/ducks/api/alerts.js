import { put, takeEvery, select } from 'redux-saga/effects'
import get from 'lodash/get'

import { guid } from '../../utils/id'
import { addMultiAlerts } from '../alerts/store'
import propsResolver from '../../utils/propsResolver'

import { ALERTS_PREFIX } from './constants'

export const API_ALERTS_ADD = `${ALERTS_PREFIX}add`
/**
 * @description Сага заменяет id алерта на новый.
 * Когда это необходимо:
 * Есть кнопка вывода алерта, action назначен кнопке из метаданных страницы, т.е. кнопка всегда будет диспатчить один и тот же action.
 * А значит при каждом клике будут выводиться алерты с одинаковыми id, что сломает сайдэффекты и вывод алертов в стеке на UI
 * (id используется в качестве key при выводе алертов в списке)
 * Для решения этой проблемы таким кнопкам назначен специальный служебный тип 'n2o/api/alerts/add'.
 * Слушатель ловит диспатч экшена с типом 'n2o/api/alerts/add', сага alertsResolver заменяет id алертов на новый и диспатчит
 * экшен добавления алертов (теперь уже с уникальными id) в стор.
 * Резолвит payload через modelLink
 * @param action
 * @returns {Generator<*, void, *>}
 */
function* alertsResolver(action) {
    const { alerts, key } = action.payload
    const state = yield select()

    const preparedAlerts = alerts.map((alert) => {
        const { modelLink } = alert

        let resolvedAlert = null

        if (modelLink) {
            const model = get(state, modelLink)

            resolvedAlert = propsResolver(alert, model)
        } else {
            resolvedAlert = alert
        }

        return { ...resolvedAlert, id: guid() }
    })

    yield put(addMultiAlerts(key, preparedAlerts))
}

export const sagas = [
    takeEvery(API_ALERTS_ADD, alertsResolver),
]
