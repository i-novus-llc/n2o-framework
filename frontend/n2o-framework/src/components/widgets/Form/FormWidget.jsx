import React from 'react'
import PropTypes from 'prop-types'
import values from 'lodash/values'

import StandardWidget from '../StandardWidget'
// eslint-disable-next-line import/no-named-as-default
import dependency from '../../../core/dependency'

import FormContainer from './FormContainer'
import Fieldsets from './fieldsets'

/**
 * Виджет формы
 * @reactProps {string} pageId - id страницы
 * @reactProps {string} id - id виджета
 * @reactProps {object} toolbar
 * @reactProps {boolean} disabled
 * @reactProps {object} form
 * @reactProps {boolean} form.fetchOnInit - фетчить / не фетчить данные при инициализации
 * @reactProps {number} form.size - размер выборки
 * @reactProps {array} form.fieldsets
 * @reactProps {object} form.validation
 * @reactProps {object} form.prompt - флаг включения обработки выхода с несохраненной формы
 */
class FormWidget extends React.Component {
    /**
     * Замена src на компонент с помощью resolveProps
     */
    getWidgetProps() {
        const { resolveProps } = this.context
        const { form, toolbar, placeholder, dataProvider, autoSubmit } = this.props

        return {
            fieldsets: values(
                resolveProps(form.fieldsets, Fieldsets.StandardFieldset),
            ),
            toolbar,
            placeholder,
            validation: form.validation,
            fetchOnInit: form.fetchOnInit,
            modelPrefix: form.modelPrefix,
            dataProvider,
            prompt: form.prompt,
            autoFocus: form.autoFocus,
            autoSubmit,
        }
    }

    render() {
        const {
            id: widgetId,
            datasource: modelId = widgetId,
            disabled,
            toolbar,
            pageId,
            className,
            style,
        } = this.props

        return (
            <StandardWidget
                disabled={disabled}
                widgetId={widgetId}
                toolbar={toolbar}
                className={className}
                style={style}
                modelId={modelId}
            >
                <FormContainer
                    widgetId={widgetId}
                    pageId={pageId}
                    modelId={modelId}
                    {...this.getWidgetProps()}
                />
            </StandardWidget>
        )
    }
}

FormWidget.defaultProps = {
    toolbar: {},
}

FormWidget.propTypes = {
    className: PropTypes.string,
    style: PropTypes.object,
    containerId: PropTypes.string,
    pageId: PropTypes.string,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
    toolbar: PropTypes.object,
    form: PropTypes.shape({
        fetchOnInit: PropTypes.bool,
        fieldsets: PropTypes.array,
        validation: PropTypes.object,
        prompt: PropTypes.bool,
    }),
    placeholder: PropTypes.string,
    dataProvider: PropTypes.object,
    autoSubmit: PropTypes.bool,
    id: PropTypes.string.isRequired,
    datasource: PropTypes.string,
}

FormWidget.contextTypes = {
    resolveProps: PropTypes.func,
}

export default dependency(FormWidget)
