import React, { useContext, useMemo } from 'react'
import { useSelector } from 'react-redux'
import values from 'lodash/values'

import StandardWidget from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { isDirtyForm } from '../../../ducks/form/selectors'

import { getFieldsKeys } from './utils'
import Fieldsets from './fieldsets'
import { ReduxForm } from './ReduxForm'
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
    const { resolveProps } = useContext(FactoryContext)
    const resolvedForm = useMemo(() => ({
        ...form,
        fieldsets: values(
            resolveProps(form.fieldsets, Fieldsets.StandardFieldset),
        ),
    }), [form, resolveProps])
    const { modelPrefix, fieldsets } = resolvedForm
    const resolveModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.active, datasource))
    const editModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.edit, datasource))
    // FIXME: Удалить костыль с добалением resolveModel если нет editModel, после удаления edit из редюсера models
    const activeModel = useMemo(() => (
        modelPrefix === ModelPrefix.edit ? (editModel || resolveModel) : resolveModel
    ), [editModel, modelPrefix, resolveModel])
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
