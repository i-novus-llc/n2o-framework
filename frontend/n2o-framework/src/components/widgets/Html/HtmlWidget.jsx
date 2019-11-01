import React from 'react';
import PropTypes from 'prop-types';
import StandardWidget from '../StandardWidget';
import Html from './Html';
import dependency from '../../../core/dependency';

/**
 * Виджет таблица
 * @reactProps {string} containerId - id конейтенера
 * @reactProps {string} pageId - id страницы
 * @reactProps {boolean} fetchOnInit - фетчить / не фетчить данные при инициализации
 * @reactProps {boolean} url - url для фетчинга
 * @reactProps {string} widgetId - id виджета
 * @reactProps {object} toolbar
 * @reactProps {object} actions
 * @reactProps {string} html - html код
 * @reactProps {object} dataProvider
 */
class HtmlWidget extends React.Component {
  /**
   * Мэппинг пропсов
   */
  getWidgetProps() {
    return {
      id: this.props.widgetId,
      ...this.props.html,
      dataProvider: this.props.dataProvider,
    };
  }

  /**
   * Базовый рендер
   */
  render() {
    const {
      fetchOnInit,
      id: widgetId,
      toolbar,
      actions,
      size,
      className,
      style,
    } = this.props;
    return (
      <StandardWidget
        widgetId={widgetId}
        toolbar={toolbar}
        actions={actions}
        className={className}
        style={style}
      >
        <Html {...this.getWidgetProps()} />
      </StandardWidget>
    );
  }
}

HtmlWidget.defaultProps = {
  toolbar: {},
};

HtmlWidget.propTypes = {
  containerId: PropTypes.string.isRequired,
  pageId: PropTypes.string.isRequired,
  fetchOnInit: PropTypes.bool,
  url: PropTypes.bool,
  widgetId: PropTypes.string,
  toolbar: PropTypes.object,
  actions: PropTypes.object,
  html: PropTypes.string,
  dataProvider: PropTypes.object,
};

export default dependency(HtmlWidget);
