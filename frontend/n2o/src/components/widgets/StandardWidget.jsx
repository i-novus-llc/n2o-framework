import React from 'react';
import PropTypes from 'prop-types';
import { values, isEqual } from 'lodash';
import cx from 'classnames';
import { pure } from 'recompose';
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
        return (
          <Actions
            toolbar={toolbar.topLeft}
            actions={actions}
            containerKey={widgetId}
          />
        );
      case 'topRight':
        return (
          <Actions
            toolbar={toolbar.topRight}
            actions={actions}
            containerKey={widgetId}
          />
        );
      case 'bottomLeft':
        return (
          <Actions
            toolbar={toolbar.bottomLeft}
            actions={actions}
            containerKey={widgetId}
          />
        );
      case 'bottomRight':
        return (
          <Actions
            toolbar={toolbar.bottomRight}
            actions={actions}
            containerKey={widgetId}
          />
        );
      case 'right':
        return <WidgetFilters widgetId={widgetId} {...filterProps} />;
      default:
        break;
    }
  }

  render() {
    const { widgetId, disabled, filter, className, style } = this.props;

    const classes = cx([
      'n2o-standard-widget-layout',
      {
        [className]: className,
        'n2o-disabled': disabled,
      },
    ]);

    return (
      <div className={classes}>
        {filter.filterPlace === 'left' && this.renderSection('left')}
        <div className="n2o-standard-widget-layout-center">
          <div>
            {filter.filterPlace === 'top' && this.renderSection('top')}
            <WidgetAlerts widgetId={widgetId} />
          </div>
          <div className="d-flex justify-content-between">
            <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--left">
              {this.renderSection('topLeft')}
            </div>
            <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--right">
              {this.renderSection('topRight')}
            </div>
          </div>
          <div>
            <div />
            <div>{this.props.children}</div>
            <div />
          </div>
          <div className={'d-flex justify-content-between'}>
            <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--left">
              {this.renderSection('bottomLeft')}
            </div>
            <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--right">
              {this.renderSection('bottomRight')}
            </div>
          </div>
          <div>
            <div />
          </div>
        </div>
        <div className="n2o-standard-widget-layout-aside n2o-standard-widget-layout-aside--right">
          {filter.filterPlace === 'right' && this.renderSection('right')}
        </div>
      </div>
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
