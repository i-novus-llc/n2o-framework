import React, { useContext, useEffect, useMemo } from 'react'
import PropTypes from 'prop-types'
import values from 'lodash/values'
import { useDispatch, useSelector, useStore } from 'react-redux'
import isEmpty from 'lodash/isEmpty'

import WidgetLayout from '../StandardWidget'
import { widgetPropTypes } from '../../../core/widget/propTypes'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { isDirtyForm } from '../../../ducks/form/selectors'
import { setModel } from '../../../ducks/models/store'

import { getFieldsKeys } from './utils'
import Fieldsets from './fieldsets'
import ReduxForm from './ReduxForm'

export const Form = ({
    id: formName,
    disabled,
    toolbar,
    datasource = formName,
    className,
    style,
    form,
    loading,
}) => {
    const { getState } = useStore()
    const { resolveProps } = useContext(FactoryContext)
    const resolvedForm = useMemo(() => ({
        ...form,
        fieldsets: values(
            resolveProps(form.fieldsets, Fieldsets.StandardFieldset),
        ),
    }), [form, resolveProps])
    const { modelPrefix, fieldsets } = resolvedForm
    const datasourceModel = useSelector(
        state => getModelByPrefixAndNameSelector(ModelPrefix.source, datasource)(state)?.[0],
    )
    const resolveModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.active, datasource))
    const editModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.edit, datasource))
    // FIXME: Удалить костыль с добалением resolveModel если нет editModel, после удаления edit из редюсера models
    const activeModel = useMemo(() => (
        modelPrefix === ModelPrefix.edit ? (editModel || resolveModel) : resolveModel
    ), [editModel, modelPrefix, resolveModel])
    const initialValues = useMemo(() => {
        const state = getState()
        const resolveModel = getModelByPrefixAndNameSelector(ModelPrefix.active, datasource)(state)
        const editModel = getModelByPrefixAndNameSelector(ModelPrefix.edit, datasource)(state)
        // FIXME: Удалить костыль с добалением resolveModel если нет editModel, после удаления edit из редюсера models
        const activeModel = modelPrefix === ModelPrefix.edit ? (editModel || resolveModel) : resolveModel

        if (isEmpty(activeModel) && isEmpty(datasourceModel)) {
            // Возвращение null необходимо, поскольку если вернуть undefined redux-toolkit не вызовет экшен
            return null
        }

        return { ...activeModel, ...datasourceModel }
    }, [datasourceModel, datasource, getState, modelPrefix])
    const fields = useMemo(() => getFieldsKeys(fieldsets), [fieldsets])
    const dirty = useSelector(isDirtyForm(formName))
    const dispatch = useDispatch()

    useEffect(() => {
        if (modelPrefix === ModelPrefix.edit) {
            dispatch(setModel(ModelPrefix.edit, datasource, initialValues, true))
        }

        dispatch(setModel(ModelPrefix.active, datasource, initialValues, true))
    }, [dispatch, datasource, initialValues, modelPrefix])

    return (
        <WidgetLayout
            disabled={disabled}
            widgetId={formName}
            toolbar={toolbar}
            className={className}
            style={style}
            datasource={datasource}
            loading={loading}
            activeModel={activeModel}
        >
            <ReduxForm
                name={formName}
                datasource={datasource}
                modelPrefix={modelPrefix}
                dirty={dirty}
                fields={fields}
                {...resolvedForm}
            />
        </WidgetLayout>
    )
}

Form.propTypes = {
    ...widgetPropTypes,
    className: PropTypes.string,
    style: PropTypes.object,
    disabled: PropTypes.bool,
    form: PropTypes.shape({
        fieldsets: PropTypes.array,
        prompt: PropTypes.bool,
        modelPrefix: PropTypes.oneOf(['resolve', 'edit']),
    }),
    autoSubmit: PropTypes.bool,
}

Form.contextTypes = {
    store: PropTypes.object,
}

/**
 * @type ConnectedWidget
 */
export const FormWidget = WidgetHOC(Form)
