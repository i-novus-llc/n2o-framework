import React from 'react';
import PropTypes from 'prop-types';
import isEmpty from 'lodash/isEmpty';
import filter from 'lodash/filter';
import map from 'lodash/map';
import isUndefined from 'lodash/isUndefined';
import find from 'lodash/find';
import get from 'lodash/get';
import pull from 'lodash/pull';
import { compose, setDisplayName } from 'recompose';
import withRegionContainer from '../withRegionContainer';
import Tabs from './Tabs';
import Tab from './Tab';
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
    this.state = {
      readyTabs: this.findReadyTabs(),
      visibleTabs: {},
    };
    this.handleChangeActive = this.handleChangeActive.bind(this);
  }

  handleChangeActive(id, prevId) {
    const {
      tabs,
      lazy,
      alwaysRefresh,
      getWidgetProps,
      fetchWidget,
      changeActiveEntity,
    } = this.props;
    const { readyTabs } = this.state;
    const widgetId = get(
      find(tabs, ({ id: tabId }) => tabId === id),
      'widgetId'
    );
    const widgetProps = getWidgetProps(widgetId);

    if (lazy) {
      if (alwaysRefresh) {
        pull(readyTabs, prevId);
      }
      readyTabs.push(id);
      this.setState(() => ({
        readyTabs: [...readyTabs],
      }));
    } else if (alwaysRefresh || isEmpty(widgetProps.datasource)) {
      widgetProps.dataProvider && fetchWidget(widgetId);
    }

    changeActiveEntity(id);
  }

  findReadyTabs() {
    return filter(
      map(this.props.tabs, tab => {
        if (tab.opened) {
          return tab.id;
        }
      }),
      item => item
    );
  }

  render() {
    const {
      tabs,
      getWidget,
      getWidgetProps,
      getVisible,
      pageId,
      lazy,
      activeEntity,
    } = this.props;
    const { readyTabs, visibleTabs } = this.state;

    return (
      <Tabs activeId={activeEntity} onChangeActive={this.handleChangeActive}>
        {tabs.map(tab => {
          const { security } = tab;
          const widgetProps = getWidgetProps(tab.widgetId);
          const widgetMeta = getWidget(pageId, tab.widgetId);
          const dependencyVisible = getVisible(pageId, tab.widgetId);
          const widgetVisible = get(widgetProps, 'isVisible', true);
          const tabVisible = get(visibleTabs, tab.widgetId, true);

          const tabProps = {
            key: tab.id,
            id: tab.id,
            title: tab.label || tab.widgetId,
            icon: tab.icon,
            active: tab.opened,
            visible: dependencyVisible && widgetVisible && tabVisible,
          };

          const tabEl = (
            <Tab {...tabProps}>
              {lazy ? (
                readyTabs.includes(tab.id) && (
                  <Factory id={tab.widgetId} level={WIDGETS} {...widgetMeta} />
                )
              ) : (
                <Factory id={tab.widgetId} level={WIDGETS} {...widgetMeta} />
              )}
            </Tab>
          );

          const onPermissionsSet = permissions => {
            this.setState(prevState => ({
              visibleTabs: {
                ...prevState.visibleTabs,
                [tab.widgetId]: !!permissions,
              },
            }));
          };

          return isEmpty(security) ? (
            tabEl
          ) : (
            <SecurityCheck
              {...tabProps}
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
  /**
   * Список табов
   */
  tabs: PropTypes.array.isRequired,
  getWidget: PropTypes.func.isRequired,
  /**
   * ID странцы
   */
  pageId: PropTypes.string.isRequired,
  alwaysRefresh: PropTypes.bool,
  mode: PropTypes.oneOf(['single', 'all']),
  /**
   * Флаг ленивого рендера
   */
  lazy: PropTypes.bool,
  resolveVisibleDependency: PropTypes.func,
};

TabRegion.defaultProps = {
  alwaysRefresh: false,
  lazy: false,
  mode: 'single',
};

export { TabRegion };
export default compose(
  setDisplayName('TabsRegion'),
  withRegionContainer({ listKey: 'tabs' }),
  withWidgetProps
)(TabRegion);
