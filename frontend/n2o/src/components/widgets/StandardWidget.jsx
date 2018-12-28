import React from 'react';
import PropTypes from 'prop-types';
import { values, isEqual } from 'lodash';
import cx from 'classnames';
import { pure } from 'recompose';

import StandardWidgetLayout from '../layouts/StandardWidgetLayout/StandardWidgetLayout';
import Section from '../layouts/Section';
import WidgetAlerts from './WidgetAlerts';
import WidgetFilters from './WidgetFilters';
import Actions from '../actions/Actions';

/**
 * Виджет таблица
 * @reactProps {string} widgetId - id виджета
 * @reactProps {Object} toolbar
 * @reactProps {Object} actions
 * @reactProps {Object} filter
 * @reactProps {boolean} disabled - флаг активности
 * @reactProps {node} children - элемент потомок компонента StandardWidget
 */
class StandardWidget extends React.Component {
  renderSection(place) {
    const { widgetId, toolbar, actions, filter } = this.props;
    if (this.props[place] && React.isValidElement(this.props[place])) return this.props[place];
    switch (place) {
      case 'left':
        return (
          <WidgetFilters
            widgetId={widgetId}
            blackResetList={filter.blackResetList}
            fieldsets={filter.filterFieldsets}
            filterButtonId={filter.filterButtonId}
          />
        );
      case 'top':
        return (
          <WidgetFilters
            widgetId={widgetId}
            blackResetList={filter.blackResetList}
            fieldsets={filter.filterFieldsets}
            filterButtonId={filter.filterButtonId}
          />
        );
      case 'topLeft':
        return <Actions toolbar={toolbar.topLeft} actions={actions} containerKey={widgetId} />;
      case 'topRight':
        return <Actions toolbar={toolbar.topRight} actions={actions} containerKey={widgetId} />;
      case 'bottomLeft':
        return <Actions toolbar={toolbar.bottomLeft} actions={actions} containerKey={widgetId} />;
      case 'bottomRight':
        return <Actions toolbar={toolbar.bottomRight} actions={actions} containerKey={widgetId} />;
      case 'right':
        return (
          <WidgetFilters
            widgetId={widgetId}
            blackResetList={filter.blackResetList}
            fieldsets={filter.filterFieldsets}
            filterButtonId={filter.filterButtonId}
          />
        );
      default:
        break;
    }
  }

  render() {
    const { widgetId, disabled, filter } = this.props;
    return (
      <StandardWidgetLayout className={cx({ 'n2o-disabled': disabled })}>
        {filter.filterPlace === 'left' && (
          <Section place="left">{this.renderSection('left')}</Section>
        )}
        <Section place="top">
          {filter.filterPlace === 'top' && this.renderSection('top')}
          <WidgetAlerts widgetId={widgetId} />
        </Section>
        <Section place="topLeft">{this.renderSection('topLeft')}</Section>
        <Section place="topRight">{this.renderSection('topRight')}</Section>
        <Section place="center">{this.props.children}</Section>
        <Section place="bottomLeft">{this.renderSection('bottomLeft')}</Section>
        <Section place="bottomRight">{this.renderSection('bottomRight')}</Section>
        {filter.filterPlace === 'right' && this.renderSection('right')}
      </StandardWidgetLayout>
    );
  }
}

StandardWidget.defaultProps = {
  toolbar: {},
  filter: {}
};

StandardWidget.propTypes = {
  widgetId: PropTypes.string,
  toolbar: PropTypes.object,
  actions: PropTypes.object,
  filter: PropTypes.object,
  disabled: PropTypes.bool,
  left: PropTypes.element,
  top: PropTypes.element,
  topLeft: PropTypes.element,
  topRight: PropTypes.element,
  bottomLeft: PropTypes.element,
  bottomRight: PropTypes.element,
  children: PropTypes.node
};

export default pure(StandardWidget);
