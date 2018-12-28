import React from 'react';
import PropTypes from 'prop-types';
import { values } from 'lodash';
import StandardWidgetLayout from '../../layouts/StandardWidgetLayout/StandardWidgetLayout';

import Section from '../../layouts/Section';
import Alerts from '../../snippets/Alerts/Alerts';
import Actions from '../../actions/Actions';
import dependency from '../../../core/dependency';
import StandardWidget from '../StandardWidget';
import WireframeContainer from './WireframeContainer';

/**
 * Виджет вайфрейм
 * @reactProps {string} pageId - id страницы
 * @reactProps {boolean} fetchOnInit - фетчить / не фетчить данные при инициализации
 * @reactProps {boolean} url - url для фетчинга
 * @reactProps {string} widgetId - id виджета
 */
class WireframeWidget extends React.Component {
  /**
   * Мэппинг пропсов
   */
  getWidgetProps() {
    return {
      id: this.props.widgetId,
      ...this.props.wireframe
    };
  }

  /**
   * Базовый рендер
   */
  render() {
    const { fetchOnInit, id: widgetId, toolbar, actions, size } = this.props;
    return (
      <StandardWidget widgetId={widgetId} toolbar={toolbar} actions={actions}>
        <WireframeContainer
          widgetId={widgetId}
          fetchOnInit={fetchOnInit}
          {...this.getWidgetProps()}
        />
      </StandardWidget>
    );
  }
}

WireframeWidget.defaultProps = {
  toolbar: {}
};

WireframeWidget.propTypes = {
  containerId: PropTypes.string.isRequired,
  pageId: PropTypes.string.isRequired,
  fetchOnInit: PropTypes.bool,
  widgetId: PropTypes.string,
  toolbar: PropTypes.object,
  actions: PropTypes.object
};

export default dependency(WireframeWidget);
