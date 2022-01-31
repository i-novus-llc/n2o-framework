import { takeEvery, put, select, debounce, delay } from 'redux-saga/effects'
import { touch, actionTypes, focus, reset } from 'redux-form'
import get from 'lodash/get'
import set from 'lodash/set'
import values from 'lodash/values'
import includes from 'lodash/includes'
import merge from 'lodash/merge'
import { isEmpty } from 'lodash'

import { setModel, copyModel, clearModel } from '../models/store'
import {
    makeGetModelByPrefixSelector,
    modelsSelector,
} from '../models/selectors'
import { makeDatasourceIdSelector, makeWidgetByIdSelector, makeFormModelPrefixSelector } from '../widgets/selectors'
import { dataSourceByIdSelector } from '../datasource/selectors'
import evalExpression, { parseExpression } from '../../utils/evalExpression'
import { setTabInvalid } from '../regions/store'
import { failValidate, startValidate } from '../datasource/store'
import { startInvoke } from '../../actions/actionImpl'
import { MODEL_PREFIX } from '../../core/datasource/const'

import { formsSelector } from './selectors'
import {
    setRequired,
    unsetRequired,
} from './store'

export function* addTouched({ payload: { form, name } }) {
    yield put(touch(form, name))
}

export function* copyAction({ payload }) {
    const { target, source, mode = 'replace', sourceMapper } = payload
    const state = yield select(modelsSelector)
    let sourceModel = get(state, values(source).join('.'))
    const selectedTargetModel = yield select(
        makeGetModelByPrefixSelector(target.prefix, target.key),
    )
    const targetModel = selectedTargetModel || []
    const expression = parseExpression(sourceMapper)
    let newModel = {}
    const targetModelField = get(targetModel, [target.field], [])
    const treePath = includes(target.field, '.')

    if (expression) {
        sourceModel = evalExpression(expression, sourceModel)
    }

    if (mode === 'merge') {
        newModel = target.field
            ? {
                ...targetModel,
                [target.field]: {
                    ...targetModelField,
                    ...sourceModel,
                },
            }
            : { ...targetModel, ...sourceModel }
    } else if (mode === 'add') {
        if (!Array.isArray(sourceModel) || !Array.isArray(targetModelField)) {
            // eslint-disable-next-line no-console
            console.warn('Source or target is not an array!')
        }

        sourceModel = Object.values(sourceModel)

        newModel = target.field
            ? {
                ...targetModel,
                [target.field]: [...targetModelField, ...sourceModel],
            }
            : [...targetModelField, ...sourceModel]
    } else if (treePath) {
        newModel = merge({}, targetModel)
        set(newModel, target.field, sourceModel)
    } else {
        newModel = target.field
            ? {
                ...targetModel,
                [target.field]: sourceModel,
            }
            : sourceModel
    }

    yield put(setModel(target.prefix, target.key, newModel))
}

/* it uses in tabs region */
function* setFocus({ payload }) {
    const { validation } = payload
    const { form, fields, blurValidation } = validation

    if (!blurValidation) {
        /* set focus to first invalid field */
        yield put(focus(form, Object.keys(fields)[0]))
    }
}

export function* clearForm(action) {
    /*
    * FIXME: ХАК для быстрого фикса. Разобраться
    * если дёргать ресет формы разу после очистки модели, то форма сетает первый введёный в ней символ
    * поставил задержку, чтобы форма могла сначала принять в себя пустую модель, а потом уже ресетнуть всю мета инфу в себе
    */
    yield delay(50)
    yield put(reset(action.payload.key))
}

/* TODO перенести в саги datasource
 * как вариант, чтобы не искать какая форма и событие вызывает автосейв можно сделать
 * autoSubmit: { action: ReduxAction, condition: expressionString(datasource, action) }
 */
export function* autoSubmit({ meta }) {
    const { form, field } = meta
    const datasourceId = yield select(makeDatasourceIdSelector(form))
    const datasource = yield select(dataSourceByIdSelector(datasourceId))

    if (!datasource) { return }

    const submit = datasource.submit || datasource.fieldsSubmit?.[field]

    if (!isEmpty(submit)) {
        yield put(startInvoke(datasourceId, submit, MODEL_PREFIX.active, datasource.pageId))
    }
}

export const formPluginSagas = [
    takeEvery(clearModel, clearForm),
    takeEvery(action => action.meta && action.meta.isTouched, addTouched),
    takeEvery(copyModel.type, copyAction),
    takeEvery(failValidate, function* touchOnFailValidate({ payload, meta }) {
        if (!meta?.touched) { return }

        const { id: datasource, fields } = payload
        const keys = Object.keys(fields)
        const forms = yield select(formsSelector)

        for (const formName of Object.keys(forms)) {
            const widget = yield select(makeWidgetByIdSelector(formName))

            if (datasource === widget?.datasource) {
                yield put(touch(formName, ...keys))
            }
        }
    }),
    debounce(100, [
        actionTypes.CHANGE,
        actionTypes.BLUR,
        setRequired.type,
        unsetRequired.type,
    ], function* validateSaga({ meta }) {
        const { form, field } = meta
        const datasource = yield select(makeDatasourceIdSelector(form))
        const currentFormPrefix = yield select(makeFormModelPrefixSelector(form))

        if (datasource) {
            /* blurValidation is used in the setFocus saga,
             this is needed to observing the field validation type */
            yield put(startValidate(datasource, [field], currentFormPrefix, { blurValidation: true }))
        }
    }),
    debounce(400, [
        actionTypes.CHANGE,
        // actionTypes.BLUR,
    ], autoSubmit),
    debounce(100, setTabInvalid, setFocus),
]
