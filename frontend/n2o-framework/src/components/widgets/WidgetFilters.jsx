import PropTypes from 'prop-types'
import React, { useCallback, useEffect, useMemo, createContext } from 'react'
import { useDispatch, useSelector, useStore } from 'react-redux'
import classNames from 'classnames'
import difference from 'lodash/difference'
import map from 'lodash/map'
import unset from 'lodash/unset'
import cloneDeep from 'lodash/cloneDeep'
import { isEmpty, isEqual } from 'lodash'

import { makeWidgetFilterVisibilitySelector } from '../../ducks/widgets/selectors'
import { ModelPrefix } from '../../core/datasource/const'
import { getModelByPrefixAndNameSelector } from '../../ducks/models/selectors'
import { setModel } from '../../ducks/models/store'
import { failValidate, reset as dataSourceReset } from '../../ducks/datasource/store'
import { ValidationsKey } from '../../core/validation/types'
import { dataSourceErrors } from '../../ducks/datasource/selectors'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

import { flatFields, getFieldsKeys } from './Form/utils'
import ReduxForm from './Form/ReduxForm'
import { modelLinkMapper } from './helpers'

export const WidgetFilterContext = createContext({
    formName: '',
    search() {},
    reset() {},
})

const WidgetFilters = (props) => {
    const {
        widgetId,
        fieldsets,
        fetchData,
        fetchOnChange,
        blackResetList,
        filterFieldsets: propsFilterFieldsets,
        datasource,
        style,
        fetchOnClear = true,
    } = props

    const { getState } = useStore()
    const dispatch = useDispatch()
    /*
     * временно использовал edit модель, чтобы не сетить в фильтр пока явно не нажата кнопка "поиск"
     * хак не имеет смысла, когда включено авто обновление по мере ввода
     */
    const modelPrefix = fetchOnChange ? ModelPrefix.filter : ModelPrefix.edit
    /*
     * филды с подмененными links в DataProvider (c filter на edit)
     */
    const modifiedFilterFieldsets = useMemo(() => modelLinkMapper(propsFilterFieldsets), [propsFilterFieldsets])
    const filterFieldsets = fetchOnChange ? propsFilterFieldsets : modifiedFilterFieldsets
    const fieldsKeys = useMemo(() => getFieldsKeys(fieldsets), [fieldsets])
    const visible = useSelector(makeWidgetFilterVisibilitySelector(widgetId))
    const reduxFormFilter = useSelector(getModelByPrefixAndNameSelector(modelPrefix, datasource))
    const filterMessages = useSelector(dataSourceErrors(datasource, ModelPrefix.filter))
    const reduxFilterModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.filter, datasource, EMPTY_OBJECT))

    // update default-values on init for edit-model
    useEffect(() => {
        if (modelPrefix === ModelPrefix.filter) { return }

        const reduxEditModel = getModelByPrefixAndNameSelector(ModelPrefix.edit, datasource, EMPTY_OBJECT)(getState())

        if (isEqual(reduxEditModel, reduxFilterModel)) { return }

        dispatch(setModel(modelPrefix, datasource, reduxFilterModel, true))
    }, [datasource, dispatch, getState, modelPrefix, reduxFilterModel])

    const clearDatasourceModel = useCallback(() => {
        dispatch(dataSourceReset(datasource))
    }, [dispatch, datasource])

    const search = useCallback((forceUpdate) => {
        if (modelPrefix === ModelPrefix.edit) {
            dispatch(setModel(ModelPrefix.filter, datasource, reduxFormFilter))
        }
        fetchData({ page: 1 }, forceUpdate)
    }, [dispatch, fetchData, datasource, modelPrefix, reduxFormFilter])

    const reset = useCallback(() => {
        const filterModel = getModelByPrefixAndNameSelector(modelPrefix, datasource)(getState())
        const newReduxForm = cloneDeep(filterModel)
        const resetList = difference(
            map(flatFields(fieldsets, []), 'id'),
            blackResetList,
        )

        resetList.forEach((field) => { unset(newReduxForm, field) })

        dispatch(setModel(modelPrefix, datasource, newReduxForm))
        if (modelPrefix === ModelPrefix.edit) {
            dispatch(setModel(ModelPrefix.filter, datasource, newReduxForm))
        }

        if (fetchOnClear) {
            fetchData({ page: 1 }, true)

            return
        }

        clearDatasourceModel()
    }, [
        fetchOnClear, modelPrefix, datasource, getState, fieldsets,
        blackResetList, dispatch, fetchData, clearDatasourceModel,
    ])

    useEffect(() => {
        if (fetchOnChange && reduxFormFilter) { fetchData({ page: 1 }) }
    }, [fetchData, reduxFormFilter, fetchOnChange])

    /*
     * Хак, чтобы скопировать валидацию с фильтр-модели, когда запрос делается не по изменению формы
     * прим: сброс фильтров, apply-on-init
     */
    useEffect(() => {
        if (modelPrefix === ModelPrefix.edit) {
            const filterModel = getModelByPrefixAndNameSelector(ModelPrefix.filter, datasource)(getState())
            const model = getModelByPrefixAndNameSelector(ModelPrefix.edit, datasource)(getState())

            if (isEqual(filterModel, model) && !isEmpty(filterMessages)) {
                dispatch(failValidate(datasource, filterMessages, ModelPrefix.edit, { touched: true }))
            }
        }
    }, [datasource, dispatch, filterMessages, getState, modelPrefix])

    const onKeyDown = useCallback((e) => {
        if (e.key === 'Enter') {
            e.stopPropagation()
            e.preventDefault()

            search(e)
        }
    }, [search])

    return (
        <WidgetFilterContext.Provider value={{ search, reset }}>
            <div onKeyDown={onKeyDown} className={classNames('n2o-filter', { 'd-none': !visible })} style={style}>
                <ReduxForm
                    name={widgetId}
                    datasource={datasource}
                    modelPrefix={modelPrefix}
                    fieldsets={filterFieldsets}
                    fields={fieldsKeys}
                    validationKey={ValidationsKey.FilterValidations}
                />
            </div>
        </WidgetFilterContext.Provider>
    )
}

WidgetFilters.propTypes = {
    widgetId: PropTypes.string,
    fieldsets: PropTypes.array,
    blackResetList: PropTypes.array,
    fetchData: PropTypes.func,
    fetchOnChange: PropTypes.bool,
    datasource: PropTypes.string,
    filterFieldsets: PropTypes.array,
}

export default WidgetFilters
