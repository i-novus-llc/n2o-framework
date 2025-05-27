import {
    put,
    call,
    select,
    delay,
} from 'redux-saga/effects'

import { setModel } from '../../models/store'
// @ts-ignore ignore import error from js file
import { generateErrorMeta } from '../../../utils/generateErrorMeta'
import { id as generateId } from '../../../utils/id'
import { ModelPrefix } from '../../../core/datasource/const'
import { Meta, ValidationFieldMessage } from '../../../sagas/types'
import { ValidationsKey } from '../../../core/validation/types'
import { dataSourceByIdSelector } from '../selectors'
import {
    failValidate,
    rejectRequest,
    resolveRequest,
    startValidate,
    setAdditionalInfo,
} from '../store'
import type { Provider, QueryResult, Query } from '../Provider'
import { ProviderType } from '../Provider'
import { query as serviceQuery } from '../Providers/Service'
import { query as storageQuery } from '../Providers/Storage'
import { query as inheritedQuery } from '../Providers/Inherited'
import { query as cachedQuery } from '../Providers/Cached'
import type { DataRequestAction } from '../Actions'
import type { DataSourceState } from '../DataSource'

import { validate } from './validate'

function getQuery<
    TProvider extends Provider,
    TProviderType extends ProviderType = TProvider['type'],
>(provider: TProviderType): Query<TProvider> {
    switch (provider) {
        case undefined:
        case ProviderType.service: { return serviceQuery as unknown as Query<Provider> }
        case ProviderType.storage: { return storageQuery as unknown as Query<Provider> }
        case ProviderType.inherited: { return inheritedQuery as unknown as Query<Provider> }
        case ProviderType.cached: { return cachedQuery as unknown as Query<Provider> }
        default: { return () => { throw new Error(`hasn't implementation for provider type: "${provider}`) } }
    }
}

export function* dataRequest({ payload }: DataRequestAction, apiProvider: unknown) {
    const { id, options = {} } = payload

    try {
        const { provider, components }: DataSourceState = yield select(dataSourceByIdSelector(id))

        if (!provider) { throw new Error('Can\'t request data with empty provider') }
        if (!components.length) { throw new Error('Unnecessary request for datasource with empty components list ') }

        // @ts-ignore поправить типы
        const validateByPrefix = (prefix: ModelPrefix) => startValidate(id, ValidationsKey.FilterValidations, prefix, undefined, { touched: true })

        const filtersIsValid: boolean = yield call(validate, validateByPrefix(ModelPrefix.filter))
        // Хак в WidgetFilters, пока явно не нажата кнопка «Найти», данные будут храниться в ModelPrefix.edit
        const editFiltersIsValid: boolean = yield call(validate, validateByPrefix(ModelPrefix.edit))

        if (!filtersIsValid || !editFiltersIsValid) { throw new Error('Invalid filters, request canceled') }

        const query = getQuery(provider.type)

        const response: QueryResult = yield query(id, provider, options, apiProvider)

        /*
        * Костыль для локальных источников (тут чтобы не дублировать в несколько провайдеров)
        * Задержка, чтобы несколько запросов от разных виджетов при ините схлопнулись в один
        * Иначе может возникнуть ситуация:
        * получение данных => срабатывание зависимостей => повторное чтение данных => модель перетирается, теряется результат зависимостей
        * => нету повторного вызова зависимостей, т.к. поля, на которые ссылается on не изменялись между первым и последующим чтением данных
        */
        if (provider.type !== ProviderType.service) {
            yield delay(100)
        }

        yield put(setModel(ModelPrefix.source, id, response.list, true))

        // @ts-ignore сломана типизация стора datasource привела к кривому типу Aaction-creator, пофикшено в 7.29
        yield put(setAdditionalInfo(id, response.additionalInfo))

        if (response.active) {
            yield put(setModel(ModelPrefix.active, id, response.active, true))
        }

        // @ts-ignore Проблема с типизацией
        yield put(resolveRequest(id, response))
    } catch (error) {
        const err = error as { message: string, stack: string, json?: { meta: Meta } }
        const errorMeta = err?.json?.meta || {}

        if (errorMeta.messages) {
            const fields: Record<string, ValidationFieldMessage[]> = {}

            for (const [fieldName, error] of Object.entries(errorMeta.messages.fields)) {
                fields[fieldName] = Array.isArray(error) ? error : [error]
            }

            // @ts-ignore Проблема с типизацией
            yield put(failValidate(id, fields, ModelPrefix.filter, { touched: true }))
        }

        // eslint-disable-next-line no-console
        console.warn(`JS Error: DataSource(${id}) fetch saga. ${err.message}`)

        yield put(
            rejectRequest(
                id,
                // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                // @ts-ignore
                error,
                err.json?.meta ||
                {
                    meta: generateErrorMeta({
                        id: generateId(),
                        text: 'Произошла внутренняя ошибка',
                        stacktrace: err.stack,
                        closeButton: true,
                    }),
                },
            ),
        )
    }
}
