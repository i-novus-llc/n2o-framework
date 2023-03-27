import React, { useContext, useMemo } from 'react'
import PropTypes from 'prop-types'
import values from 'lodash/values'
import { useSelector, useStore } from 'react-redux'
import isEmpty from 'lodash/isEmpty'

import WidgetLayout from '../StandardWidget'
import { widgetPropTypes } from '../../../core/widget/propTypes'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { isDirtyForm } from '../../../ducks/form/selectors'

import { getFieldsKeys } from './utils'
import Fieldsets from './fieldsets'
import ReduxForm from './ReduxForm'

export const Form = (props) => {
    const { getState } = useStore()
    const { id, disabled, toolbar, datasource, className, style, form, loading } = props
    const { resolveProps } = useContext(FactoryContext)
    const resolvedForm = useMemo(() => ({
        ...form,
        fieldsets: values(
            resolveProps(form.fieldsets, Fieldsets.StandardFieldset),
        ),
    }), [form, resolveProps])
    const { modelPrefix, fieldsets } = resolvedForm
    const formName = datasource || id
    const datasourceModel = useSelector(
        state => getModelByPrefixAndNameSelector(ModelPrefix.source, formName)(state)?.[0],
    )
    const resolveModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.active, formName))
    const editModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.edit, formName))
    // FIXME: Удалить костыль с добалением resolveModel если нет editModel, после удаления edit из редюсера models
    const activeModel = useMemo(() => (
        modelPrefix === ModelPrefix.edit ? (editModel || resolveModel) : resolveModel
    ), [editModel, modelPrefix, resolveModel])
    const initialValues = useMemo(() => {
        const state = getState()
        const resolveModel = getModelByPrefixAndNameSelector(ModelPrefix.active, formName)(state)
        const editModel = getModelByPrefixAndNameSelector(ModelPrefix.edit, formName)(state)
        // FIXME: Удалить костыль с добалением resolveModel если нет editModel, после удаления edit из редюсера models
        const activeModel = modelPrefix === ModelPrefix.edit ? (editModel || resolveModel) : resolveModel

        if (isEmpty(activeModel) && isEmpty(datasourceModel)) {
            // Возвращение null необходимо, поскольку если вернуть undefined redux-toolkit не вызовет экшен
            return null
        }

        return { ...activeModel, ...datasourceModel }
    }, [datasourceModel, formName, getState, modelPrefix])
    const fields = useMemo(() => getFieldsKeys(fieldsets), [fieldsets])
    const dirty = useSelector(isDirtyForm(formName))

    return (
        <WidgetLayout
            disabled={disabled}
            widgetId={id}
            toolbar={toolbar}
            className={className}
            style={style}
            datasource={datasource}
            loading={loading}
            activeModel={activeModel}
        >
            <ReduxForm
                form={formName}
                dirty={dirty}
                fields={fields}
                {...resolvedForm}
                activeModel={activeModel}
                initialValues={initialValues}
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

export const FormWidget = WidgetHOC(Form)
