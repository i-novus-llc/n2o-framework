import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';
import { pure } from 'recompose';

import StandardWidgetLayout from '../layouts/StandardWidgetLayout/StandardWidgetLayout';
import Section from '../layouts/Section';
import WidgetAlerts from './WidgetAlerts';
import WidgetFilters from './WidgetFilters';
import Toolbar from '../buttons/Toolbar';

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
    const { widgetId, toolbar, filter } = this.props;
    if (this.props[place] && React.isValidElement(this.props[place]))
      return this.props[place];
    const filterProps = {
      ...filter,
      fieldsets: filter.filterFieldsets,
    };
    switch (place) {
      case 'left':
        return <WidgetFilters widgetId={widgetId} {...filterProps} />;
      case 'top':
        return <WidgetFilters widgetId={widgetId} {...filterProps} />;
      case 'topLeft':
        return <Toolbar toolbar={toolbar.topLeft} entityKey={widgetId} />;
      case 'topRight':
        return <Toolbar toolbar={toolbar.topRight} entityKey={widgetId} />;
      case 'bottomLeft':
        return <Toolbar toolbar={toolbar.bottomLeft} entityKey={widgetId} />;
      case 'bottomRight':
        return <Toolbar toolbar={toolbar.bottomRight} entityKey={widgetId} />;
      case 'right':
        return <WidgetFilters widgetId={widgetId} {...filterProps} />;
      default:
        break;
    }
  }

  render() {
    const { widgetId, disabled, filter, className, style } = this.props;

    return (
      <StandardWidgetLayout
        className={cx(className, { 'n2o-disabled': disabled })}
        style={style}
      >
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
        <Section place="bottomRight">
          {this.renderSection('bottomRight')}
        </Section>
        {filter.filterPlace === 'right' && this.renderSection('right')}
      </StandardWidgetLayout>
    );
  }
}

StandardWidget.defaultProps = {
  toolbar: {},
  filter: {},
};

StandardWidget.propTypes = {
  className: PropTypes.string,
  style: PropTypes.object,
  widgetId: PropTypes.string,
  toolbar: PropTypes.object,
  actions: PropTypes.object,
  filter: PropTypes.object,
  disabled: PropTypes.bool,
  left: PropTypes.element,
  top: PropTypes.element,
  topLeft: PropTypes.oneOfType([PropTypes.bool, PropTypes.array]),
  topRight: PropTypes.oneOfType([PropTypes.bool, PropTypes.array]),
  bottomLeft: PropTypes.oneOfType([
    PropTypes.bool,
    PropTypes.array,
    PropTypes.node,
  ]),
  bottomRight: PropTypes.oneOfType([
    PropTypes.bool,
    PropTypes.array,
    PropTypes.node,
  ]),
  children: PropTypes.node,
};

export default pure(StandardWidget);
