import PropTypes from 'prop-types'
import React, { useCallback, useEffect, useMemo, useRef } from 'react'
import { useDispatch, useSelector, useStore } from 'react-redux'
import difference from 'lodash/difference'
import map from 'lodash/map'
import unset from 'lodash/unset'
import clone from 'lodash/clone'
import { createContext, useContext } from 'use-context-selector'

import { Filter } from '../snippets/Filter/Filter'
import { makeWidgetFilterVisibilitySelector } from '../../ducks/widgets/selectors'
import propsResolver from '../../utils/propsResolver'
import { ModelPrefix } from '../../core/datasource/const'
import { getModelByPrefixAndNameSelector } from '../../ducks/models/selectors'
import { setModel } from '../../ducks/models/store'

import { flatFields, getFieldsKeys } from './Form/utils'
import ReduxForm from './Form/ReduxForm'

export const WidgetFilterContext = createContext(null)

const WidgetFilters = (props) => {
    const {
        hideButtons = false,
        widgetId,
        fieldsets,
        fetchData,
        searchOnChange,
        blackResetList,
        filterFieldsets,
        datasource: formName,
    } = props
    const { getState } = useStore()
    const dispatch = useDispatch()
    const fieldsKeys = useMemo(() => {
        const resolved = Object.values(propsResolver(fieldsets) || {})

        return getFieldsKeys(resolved)
    }, [fieldsets])
    const visible = useSelector(makeWidgetFilterVisibilitySelector(widgetId))
    const reduxFormFilter = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.filter, formName))

    const clearDatasourceModel = useCallback(() => {
        dispatch(setModel(ModelPrefix.source, formName, []))
    }, [dispatch, formName])

    const defaultValues = useRef(reduxFormFilter)

    const handleFilter = useCallback(() => {
        fetchData({ page: 1 })
    }, [fetchData])
    const handleReset = useCallback((fetchOnClear = true) => {
        if (fetchOnClear) {
            const filterModel = getModelByPrefixAndNameSelector(ModelPrefix.filter, formName)(getState())
            const newReduxForm = clone(filterModel)
            const toReset = difference(
                map(flatFields(fieldsets, []), 'id'),
                blackResetList,
            )

            toReset.forEach((field) => {
                unset(newReduxForm, field)
            })

            dispatch(setModel(ModelPrefix.filter, formName, newReduxForm))
            handleFilter(newReduxForm)
        } else {
            clearDatasourceModel()
        }
    }, [formName, getState, fieldsets, blackResetList, dispatch, handleFilter, clearDatasourceModel])

    useEffect(() => {
        if (searchOnChange && reduxFormFilter) {
            handleFilter(reduxFormFilter)
        }
    }, [handleFilter, reduxFormFilter, searchOnChange])

    return (
        <WidgetFilterContext.Provider value={{
            formName,
            filter: handleFilter,
            reset: handleReset,
        }}
        >
            <Filter
                style={{ display: visible ? '' : 'none' }}
                visible={visible}
                hideButtons={hideButtons}
                onSearch={handleFilter}
                onReset={handleReset}
            >
                <ReduxForm
                    form={formName}
                    fieldsets={filterFieldsets}
                    fields={fieldsKeys}
                    activeModel={reduxFormFilter}
                    initialValues={defaultValues.current}
                    modelPrefix={ModelPrefix.filter}
                />
            </Filter>
        </WidgetFilterContext.Provider>
    )
}

WidgetFilters.propTypes = {
    widgetId: PropTypes.string,
    fieldsets: PropTypes.array,
    blackResetList: PropTypes.array,
    fetchData: PropTypes.func,
    hideButtons: PropTypes.bool,
    searchOnChange: PropTypes.bool,
    datasource: PropTypes.string,
    filterFieldsets: PropTypes.array,
}

export const useWidgetFilterContext = () => {
    const context = useContext(WidgetFilterContext)

    if (!context) {
        throw new Error('useWidgetFilterContext must be used with WidgetFilterContext')
    }

    return context
}

export default WidgetFilters
