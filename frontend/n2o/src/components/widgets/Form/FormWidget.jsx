import React from 'react';
import PropTypes from 'prop-types';
import { values } from 'lodash';

import StandardWidget from '../StandardWidget';
import FormContainer from './FormContainer';
import Fieldsets from './fieldsets';
import dependency from '../../../core/dependency';

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
 */
class FormWidget extends React.Component {
  /**
   * Замена src на компонент с помощью factoryResolver
   */
  getWidgetProps() {
    const { resolveProps } = this.context;
    return {
      fieldsets: values(resolveProps(this.props.form.fieldsets, Fieldsets.StandardFieldset)),
      toolbar: this.props.toolbar,
      actions: this.props.actions,
      validation: this.props.form.validation,
      fetchOnInit: this.props.form.fetchOnInit,
      modelPrefix: this.props.form.modelPrefix,
      dataProvider: this.props.dataProvider
    };
  }

  /**
   * Базовый рендер
   * @return {XML}
   */
  render() {
    const { id: widgetId, disabled, toolbar, actions, pageId, className, style } = this.props;

    return (
      <StandardWidget
        disabled={disabled}
        widgetId={widgetId}
        toolbar={toolbar}
        actions={actions}
        className={className}
        style={style}
      >
        <FormContainer widgetId={widgetId} pageId={pageId} {...this.getWidgetProps()} />
      </StandardWidget>
    );
  }
}

FormWidget.defaultProps = {
  toolbar: {}
};

FormWidget.propTypes = {
  className: PropTypes.string,
  style: PropTypes.object,
  containerId: PropTypes.string.isRequired,
  pageId: PropTypes.string.isRequired,
  widgetId: PropTypes.string,
  visible: PropTypes.bool,
  disabled: PropTypes.bool,
  toolbar: PropTypes.object,
  actions: PropTypes.object,
  form: PropTypes.shape({
    fetchOnInit: PropTypes.bool,
    fieldsets: PropTypes.array,
    validation: PropTypes.object
  })
};

FormWidget.contextTypes = {
  resolveProps: PropTypes.func
};

FormWidget = dependency(FormWidget);
export default FormWidget;
