import React from 'react'
import PropTypes from 'prop-types'
import values from 'lodash/values'

import StandardWidget from '../StandardWidget'
import dependency from '../../../core/dependency'

import FormContainer from './FormContainer'
import Fieldsets from './fieldsets'

/**
 * Виджет формы
 * @reactProps {string} pageId - id страницы
 * @reactProps {string} widgetId - id виджета
 * @reactProps {object} actions
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

        return {
            fieldsets: values(
                resolveProps(this.props.form.fieldsets, Fieldsets.StandardFieldset),
            ),
            toolbar: this.props.toolbar,
            placeholder: this.props.placeholder,
            actions: this.props.actions,
            validation: this.props.form.validation,
            fetchOnInit: this.props.form.fetchOnInit,
            modelPrefix: this.props.form.modelPrefix,
            dataProvider: this.props.dataProvider,
            prompt: this.props.form.prompt,
            autoFocus: this.props.form.autoFocus,
            autoSubmit: this.props.autoSubmit,
        }
    }

    /**
   * Базовый рендер
   * @return {XML}
   */
    render() {
        const {
            id: widgetId,
            disabled,
            toolbar,
            actions,
            pageId,
            className,
            style,
            form,
        } = this.props

        return (
            <StandardWidget
                disabled={disabled}
                widgetId={widgetId}
                toolbar={toolbar}
                actions={actions}
                className={className}
                style={style}
            >
                <FormContainer
                    widgetId={widgetId}
                    pageId={pageId}
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
    pageId: PropTypes.string.isRequired,
    widgetId: PropTypes.string,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
    toolbar: PropTypes.object,
    actions: PropTypes.object,
    form: PropTypes.shape({
        fetchOnInit: PropTypes.bool,
        fieldsets: PropTypes.array,
        validation: PropTypes.object,
        prompt: PropTypes.bool,
    }),
}

FormWidget.contextTypes = {
    resolveProps: PropTypes.func,
}

FormWidget = dependency(FormWidget)
export default FormWidget
