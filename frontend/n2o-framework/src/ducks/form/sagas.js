import { takeEvery, put, select, debounce, delay } from 'redux-saga/effects'
import { touch, actionTypes, focus, reset, change } from 'redux-form'
import get from 'lodash/get'
import set from 'lodash/set'
import values from 'lodash/values'
import includes from 'lodash/includes'
import merge from 'lodash/merge'
import entries from 'lodash/entries'
import isEmpty from 'lodash/isEmpty'
import first from 'lodash/first'
import isEqual from 'lodash/isEqual'

import { setModel, copyModel, clearModel } from '../models/store'
import {
    makeGetModelByPrefixSelector,
    modelsSelector,
} from '../models/selectors'
import { dataSourceByIdSelector } from '../datasource/selectors'
import evalExpression, { parseExpression } from '../../utils/evalExpression'
import { setTabInvalid } from '../regions/store'
import { failValidate, startValidate, submit } from '../datasource/store'
import { ModelPrefix } from '../../core/datasource/const'
import { generateFormFilterId } from '../../utils/generateFormFilterId'
import { ValidationsKey } from '../../core/validation/IValidation'

import { makeFormsByDatasourceSelector } from './selectors'
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

    // костыль, тк я убрал резолв зависимостей на redux-form/INITIALIZE из-за беспорядочных вызовов,
    // которые приводили к зацикливанию при applyOnInit: true и переинициализации, зависимости перестали вызываться
    // после копирования
    for (const [field, value] of entries(newModel)) {
        if (get(targetModel, field) !== value) {
            const form = target.prefix === ModelPrefix.filter ? generateFormFilterId(target.key) : target.key

            yield put(change(form, field, {
                keepDirty: true,
                value,
            }))
        }
    }
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

export function* clearForm({ payload }) {
    /*
    * FIXME: ХАК для быстрого фикса. Разобраться
    * если дёргать ресет формы разу после очистки модели, то форма сетает первый введёный в ней символ
    * поставил задержку, чтобы форма могла сначала принять в себя пустую модель, а потом уже ресетнуть всю мета инфу в себе
    */
    const { prefixes, key } = payload
    const formWidgets = yield select(makeFormsByDatasourceSelector(payload.key))

    yield delay(50)

    for (const formWidget of formWidgets) {
        const modelPrefix = get(formWidget, ['form', 'modelPrefix'], ModelPrefix.active)

        if (includes(prefixes, modelPrefix)) {
            yield put(reset(key))
        }
    }
}

/* TODO перенести в саги datasource
 * как вариант, чтобы не искать какая форма и событие вызывает автосейв можно сделать
 * autoSubmit: { action: ReduxAction, condition: expressionString(datasource, action) }
 */
export function* autoSubmit({ meta }) {
    const { form, field } = meta
    const datasource = yield select(dataSourceByIdSelector(form))

    if (isEmpty(datasource)) { return }

    const provider = (datasource.submit?.auto || datasource.submit?.autoSubmitOn)
        ? datasource.submit
        : datasource.fieldsSubmit[field]

    if (!isEmpty(provider)) {
        yield put(submit(form, provider))
    }
}

let prevModel = {}

export const formPluginSagas = [
    takeEvery(clearModel, clearForm),
    takeEvery(action => action.meta && action.meta.isTouched, addTouched),
    takeEvery(copyModel.type, copyAction),
    takeEvery(failValidate, function* touchOnFailValidate({ payload, meta }) {
        if (!meta?.touched) { return }

        const { id: datasource, fields } = payload
        const keys = Object.keys(fields)

        yield put(touch(datasource, ...keys))
    }),
    debounce(100, [
        actionTypes.CHANGE,
        actionTypes.BLUR,
        setRequired.type,
        unsetRequired.type,
    ], function* validateSaga({ meta }) {
        const { form: datasource, field } = meta
        const form = first(yield select(makeFormsByDatasourceSelector(datasource)))
        const currentFormPrefix = get(form, ['form', 'modelPrefix'], ModelPrefix.active)
        const model = yield select(makeGetModelByPrefixSelector(currentFormPrefix, datasource))

        const findDifferentValues = () => {
            const keys = [field]

            if (!model) {
                return keys
            }

            for (const [k] of Object.entries(model)) {
                if (!isEqual(model[k], prevModel[k])) {
                    keys.push(k)
                }
            }

            return keys
        }

        if (form) {
            const fields = findDifferentValues()

            /* blurValidation is used in the setFocus saga,
             this is needed to observing the field validation type */
            yield put(
                startValidate(
                    datasource,
                    ValidationsKey.Validations,
                    currentFormPrefix,
                    fields,
                    { blurValidation: true },
                ),
            )
        }

        prevModel = {
            ...model,
        }
    }),
    debounce(400, [
        actionTypes.CHANGE,
        // actionTypes.BLUR,
    ], autoSubmit),
    debounce(100, setTabInvalid, setFocus),
]
