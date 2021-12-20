import React, { useContext, useMemo } from 'react'
import PropTypes from 'prop-types'
import values from 'lodash/values'

import WidgetLayout from '../StandardWidget'
import { widgetPropTypes } from '../../../core/widget/propTypes'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'

import FormContainer from './FormContainer'
import Fieldsets from './fieldsets'

export const Form = (props) => {
    const { id, disabled, toolbar, datasource, className, style, form } = props
    const { resolveProps } = useContext(FactoryContext)
    const resolvedForm = useMemo(() => ({
        ...form,
        fieldsets: values(
            resolveProps(form.fieldsets, Fieldsets.StandardFieldset),
        ),
    }), [form, resolveProps])

    return (
        <WidgetLayout
            disabled={disabled}
            widgetId={id}
            toolbar={toolbar}
            className={className}
            style={style}
            datasource={datasource}
        >
            <FormContainer
                {...props}
                form={resolvedForm}
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
        validation: PropTypes.object,
        prompt: PropTypes.bool,
    }),
    autoSubmit: PropTypes.bool,
}

Form.contextTypes = {
    store: PropTypes.object,
}

export const FormWidget = WidgetHOC(Form)
