import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty, each, map } from 'lodash';
import { compose } from 'recompose';
import Tabs from './Tabs';
import Tab from './Tab';
import WidgetFactory from '../../widgets/WidgetFactory';
import withWidgetProps from '../withWidgetProps';
import { WIDGETS } from '../../../core/factory/factoryLevels';

import Factory from '../../../core/factory/Factory';
import SecurityCheck from '../../../core/auth/SecurityCheck';

/**
 * Регион Таб
 * @reactProps {array} tabs - массив из объектов, которые описывают виджет {id, name, opened, pageId, fetchOnInit, widget}
 * @reactProps {function} getWidget - функция получения виджета
 * @reactProps {string} pageId - идентификатор страницы
 * @reactProps {function} resolveVisibleDependency - резол видимости таба
 */
class TabRegion extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
    if (props.mode === 'single') {
      this.hideAllWidgets();
    }
    this.handleChangeActive = this.handleChangeActive.bind(this);
  }

  handleChangeActive(widgetId, prevWidgetId) {
    if (this.props.mode === 'single') {
      if (this.props.alwaysRefresh) {
        this.props.hideWidget(prevWidgetId);
      }
      this.props.showWidget(widgetId);
    }
  }

  hideAllWidgets() {
    each(this.props.tabs, tab => {
      if (!tab.opened) {
        this.props.hideWidget(tab.widgetId);
      }
    });
  }

  render() {
    const { tabs, getWidget, getWidgetProps, pageId } = this.props;
    return (
      <Tabs
        ref={el => (this._tabsEl = el)}
        onChangeActive={this.handleChangeActive}
      >
        {tabs.map(tab => {
          const widgetProps = getWidgetProps(tab.widgetId);
          const tabProps = {
            key: tab.widgetId,
            id: tab.widgetId,
            title: tab.label || tab.widgetId,
            icon: tab.icon,
            active: tab.opened,
            visible:
              typeof this.state[tab.widgetId] !== 'undefined'
                ? this.state[tab.widgetId]
                : widgetProps.isVisible,
          };
          const tabEl = (
            <Tab {...tabProps}>
              <Factory
                id={tab.widgetId}
                level={WIDGETS}
                {...getWidget(pageId, tab.widgetId)}
              />
            </Tab>
          );

          const { security } = tab;

          const onPermissionsSet = permissions => {
            this.setState({
              [tab.widgetId]: permissions,
            });
          };

          return isEmpty(security) ? (
            tabEl
          ) : (
            <SecurityCheck
              {...tabProps}
              visible={!!this.state[tab.widgetId]}
              config={security}
              onPermissionsSet={onPermissionsSet}
              render={({ permissions, active, visible }) => {
                return permissions
                  ? React.cloneElement(tabEl, { active, visible })
                  : null;
              }}
            />
          );
        })}
      </Tabs>
    );
  }
}

TabRegion.propTypes = {
  tabs: PropTypes.array.isRequired,
  getWidget: PropTypes.func.isRequired,
  pageId: PropTypes.string.isRequired,
  alwaysRefresh: PropTypes.bool,
  mode: PropTypes.oneOf(['single', 'all']),
  resolveVisibleDependency: PropTypes.func,
};

TabRegion.defaultProps = {
  alwaysRefresh: false,
  mode: 'single',
};

export default compose(withWidgetProps)(TabRegion);
