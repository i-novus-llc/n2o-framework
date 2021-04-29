import {
    put,
    select,
    takeEvery,
    take,
    fork,
    cancel,
    all,
} from 'redux-saga/effects'
import { push, LOCATION_CHANGE } from 'connected-react-router'
import isArray from 'lodash/isArray'
import map from 'lodash/map'
import get from 'lodash/get'
import toPairs from 'lodash/toPairs'
import flow from 'lodash/flow'
import keys from 'lodash/keys'
import has from 'lodash/has'
import { reset, touch } from 'redux-form'
import { batchActions } from 'redux-batched-actions'

import { GLOBAL_KEY } from '../constants/alerts'
import { addAlerts, removeAlerts } from '../actions/alerts'
import { addFieldMessage } from '../actions/formPlugin'
import { metadataRequest } from '../actions/pages'
import { dataRequestWidget } from '../actions/widgets'
import { updateWidgetDependency } from '../actions/dependency'
import { insertDialog, destroyOverlays } from '../actions/overlays'
import { id } from '../utils/id'
import { CALL_ALERT_META } from '../constants/meta'
import { dataProviderResolver } from '../core/dataProviderResolver'

export function* alertEffect(action) {
    try {
        const { alertKey = GLOBAL_KEY, messages, stacked } = action.meta.alert

        if (!stacked) { yield put(removeAlerts(alertKey)) }
        const alerts = isArray(messages)
            ? messages.map(message => ({ ...message, id: message.id || id() }))
            : [{ ...messages, id: messages.id || id() }]

        yield put(addAlerts(alertKey, alerts))
    } catch (e) {
        // eslint-disable-next-line no-console
        console.error(e)
    }
}

export function* redirectEffect(action) {
    try {
        const { path, pathMapping, queryMapping, target } = action.meta.redirect

        const state = yield select()

        const { url: newUrl } = dataProviderResolver(state, {
            url: path,
            pathMapping,
            queryMapping,
        })

        if (target === 'application') {
            yield put(push(newUrl))
            yield put(destroyOverlays())
        } else if (target === 'self') {
            window.location = newUrl
        } else {
            window.open(newUrl)
        }
    } catch (e) {
        // eslint-disable-next-line no-console
        console.error(e)
    }
}

function* fetchFlow(options, action) {
    const state = yield select()

    const rootPageId = get(state, 'global.rootPageId')
    const meta = get(action, 'meta')
    const redirectPath = get(action, 'meta.redirect.path')

    // eslint-disable-next-line sonarjs/no-one-iteration-loop
    while (true) {
        yield take([LOCATION_CHANGE])

        if (has(meta, 'alert')) {
            return yield put(dataRequestWidget(options.widgetId, options.options))
        }

        return yield all([
            put(metadataRequest(rootPageId, rootPageId, redirectPath)),
            put(dataRequestWidget(options.widgetId, options.options)),
        ])
    }
}

export function* refreshEffect(action) {
    let lastTask

    try {
        const { type, options } = action.meta.refresh

        // eslint-disable-next-line default-case
        switch (type) {
            case 'widget':
                if (
                    action.meta.redirect &&
                    action.meta.redirect.target === 'application'
                ) {
                    if (lastTask) {
                        yield cancel(lastTask)
                    }

                    lastTask = yield fork(fetchFlow, options, action)
                } else {
                    yield put(
                        dataRequestWidget(options.widgetId, {
                            ...options.options,
                            withoutSelectedId: action.meta.withoutSelectedId,
                        }),
                    )
                }

                break
            case 'metadata':
                yield put(metadataRequest(...options))

                break
        }
    } catch (e) {
        // eslint-disable-next-line no-console
        console.log(e)
    }
}

export function* messagesFormEffect({ meta }) {
    try {
        const formID = get(meta, 'messages.form', false)
        const fields = get(meta, 'messages.fields', false)
        const putBatchActions = flow([batchActions, put])

        if (formID && fields) {
            const serializeData = map(
                toPairs(fields),
                ([name, ...message]) => addFieldMessage(formID, name, ...message),
            )

            yield put(touch(formID, ...keys(fields)))
            yield putBatchActions(serializeData)
        }
    } catch (e) {
        // eslint-disable-next-line no-console
        console.error(e)
    }
}

export function* clearFormEffect(action) {
    yield put(reset(action.meta.clearForm))
}

export function* updateWidgetDependencyEffect({ meta }) {
    const { widgetId } = meta

    yield put(updateWidgetDependency(widgetId))
}

export function* userDialogEffect({ meta }) {
    const { title, description, toolbar, ...rest } = meta.dialog

    yield put(
        insertDialog('dialog', true, {
            title,
            description,
            toolbar,
            ...rest,
        }),
    )
}

export const metaSagas = [
    takeEvery(
        [action => action.meta && action.meta.alert, CALL_ALERT_META],
        alertEffect,
    ),
    takeEvery(action => action.meta && action.meta.redirect, redirectEffect),
    takeEvery(action => action.meta && action.meta.refresh, refreshEffect),
    takeEvery(action => action.meta && action.meta.clearForm, clearFormEffect),
    takeEvery(action => action.meta && action.meta.messages, messagesFormEffect),
    takeEvery(
        action => action.meta && action.meta.updateWidgetDependency,
        updateWidgetDependencyEffect,
    ),
    takeEvery(action => action.meta && action.meta.dialog, userDialogEffect),
]
