import React, { useContext, useEffect, useMemo } from 'react'
import { useDispatch, useSelector, useStore } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import values from 'lodash/values'

import StandardWidget from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { isDirtyForm } from '../../../ducks/form/selectors'
import { setModel } from '../../../ducks/models/store'
import { State } from '../../../ducks/State'

import { getFieldsKeys } from './utils'
import Fieldsets from './fieldsets'
import ReduxForm from './ReduxForm'
import { type FormWidgetProps } from './types'

const Widget = ({
    id: formName,
    disabled,
    toolbar,
    datasource = formName,
    className,
    style,
    form,
    loading,
}: FormWidgetProps) => {
    const dispatch = useDispatch()
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
        (state: State) => getModelByPrefixAndNameSelector(ModelPrefix.source, datasource)(state)?.[0],
    )
    const resolveModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.active, datasource))
    const editModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.edit, datasource))
    // FIXME: Удалить костыль с добалением resolveModel если нет editModel, после удаления edit из редюсера models
    const activeModel = useMemo(() => (
        modelPrefix === ModelPrefix.edit ? (editModel || resolveModel) : resolveModel
    ), [editModel, modelPrefix, resolveModel])

    useEffect(() => {
        const state = getState()
        const resolveModel = getModelByPrefixAndNameSelector(ModelPrefix.active, datasource)(state)
        const editModel = getModelByPrefixAndNameSelector(ModelPrefix.edit, datasource)(state)
        // FIXME: Удалить костыль с добалением resolveModel если нет editModel, после удаления edit из редюсера models
        const activeModel = modelPrefix === ModelPrefix.edit ? (editModel || resolveModel) : resolveModel

        const initialValues = isEmpty(activeModel) && isEmpty(datasourceModel)
            // Возвращение null необходимо, поскольку если вернуть undefined redux-toolkit не вызовет экшен
            ? null
            : { ...activeModel, ...datasourceModel }

        if (modelPrefix === ModelPrefix.edit) {
            dispatch(setModel(ModelPrefix.edit, datasource, initialValues, true))
        }

        dispatch(setModel(ModelPrefix.active, datasource, initialValues, true))
    }, [datasourceModel, datasource, getState, modelPrefix, dispatch])
    const fields = useMemo(() => getFieldsKeys(fieldsets), [fieldsets])
    const dirty = useSelector(isDirtyForm(formName))

    return (
        <StandardWidget
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
                dirty={dirty}
                fields={fields}
                {...resolvedForm}
                modelPrefix={modelPrefix}
            />
        </StandardWidget>
    )
}

Widget.displayName = 'FormWidgetComponent'

export const FormWidget = WidgetHOC<FormWidgetProps>(Widget)
export default FormWidget

FormWidget.displayName = 'FormWidget'
