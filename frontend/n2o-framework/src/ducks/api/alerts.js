import { put, takeEvery } from 'redux-saga/effects'

import { guid } from '../../utils/id'
import { addMultiAlerts } from '../alerts/store'

import { API_PREFIX } from './constants'

export const API_ALERTS_ADD = `${API_PREFIX}alerts/add`

/**
 * @description Сага заменяет id алерта на новый.
 * Когда это необходимо:
 * Есть кнопка вывода алерта, action назначен кнопке из метаданных страницы, т.е. кнопка всегда будет диспатчить один и тот же action.
 * А значит при каждом клике будут выводиться алерты с одинаковыми id, что сломает сайдэффекты и вывод алертов в стеке на UI
 * (id используется в качестве key при выводе алертов в списке)
 * Для решения этой проблемы таким кнопкам назначен специальный служебный тип 'n2o/api/alerts/add'.
 * Слушатель ловит диспатч экшена с типом 'n2o/api/alerts/add', сага mutateAlertId заменяет id алертов на новый и диспатчит
 * экшен добавления алертов (теперь уже с уникальными id) в стор.
 * @param action
 * @returns {Generator<*, void, *>}
 */
function* mutateAlertId(action) {
    const { alerts, key } = action.payload

    const preparedAlerts = alerts.map(alert => ({ ...alert, id: guid() }))

    yield put(addMultiAlerts(key, preparedAlerts))
}

export const sagas = [
    takeEvery(API_ALERTS_ADD, mutateAlertId),
]
