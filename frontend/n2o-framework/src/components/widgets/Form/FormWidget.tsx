import React, { useContext, useMemo } from 'react'
import { useSelector } from 'react-redux'

import StandardWidget from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { ModelPrefix } from '../../../core/models/types'
import { isDirtyForm } from '../../../ducks/form/selectors'

import { getFieldsKeys } from './utils'
import Fieldsets from './fieldsets'
import { ReduxForm } from './ReduxForm'
import { FieldSetsProps, type FormWidgetProps } from './types'

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
    const fieldsets = useMemo(() => (resolveProps<FieldSetsProps>(form.fieldsets, Fieldsets.StandardFieldset)), [form, resolveProps])
    const { modelPrefix, prompt } = form
    const resolveModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.active, datasource))
    const editModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.edit, datasource))
    // FIXME: Удалить костыль с добалением resolveModel если нет editModel, после удаления edit из редюсера models
    const activeModel = useMemo(() => (
        modelPrefix === ModelPrefix.edit ? (editModel || resolveModel) : resolveModel
    ), [editModel, modelPrefix, resolveModel])
    const fields = useMemo(() => getFieldsKeys(fieldsets), [fieldsets])
    const dirty = useSelector(isDirtyForm(formName))
    const modelLink = useMemo(() => ({ id: datasource, prefix: modelPrefix }), [datasource, modelPrefix])

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
                modelLink={modelLink}
                dirty={dirty}
                fields={fields}
                fieldsets={fieldsets}
                prompt={prompt}
                needActiveModel
            />
        </StandardWidget>
    )
}

Widget.displayName = 'FormWidgetComponent'

export const FormWidget = WidgetHOC<FormWidgetProps>(Widget)
export default FormWidget

FormWidget.displayName = 'FormWidget'
