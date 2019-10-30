import React from 'react';
import PropTypes from 'prop-types';
import { compose, setDisplayName } from 'recompose';
import {
  isEmpty,
  filter,
  map,
  pick,
  difference,
  pullAll,
  first,
  isNil,
} from 'lodash';
import Collapse, { Panel } from '../../snippets/Collapse/Collapse';
import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import SecurityCheck from '../../../core/auth/SecurityCheck';
import withWidgetProps from '../withWidgetProps';

/**
 * Регион Лист
 * @reactProps {array} items - массив из объектов, которые описывают виджет{id, name, opened, pageId, fetchOnInit, widget}
 * @reactProps {string} pageId - идентификатор страницы
 * @reactProps {function} getWidget - функция получения виджета
 */

class ListRegion extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(keys) {
    // const widgetId = first(difference(this.activeKeys, keys));
    // if (widgetId) {
    //   this.props.showWidget(widgetId);
    // }
  }

  /**
   * Рендер
   */
  render() {
    const {
      items,
      getWidget,
      getWidgetProps,
      pageId,
      collapsible,
    } = this.props;

    this.activeKeys = map(filter(items, 'opened'), 'widgetId');
    const collapseProps = pick(this.props, 'destroyInactivePanel', 'accordion');
    const panelProps = pick(this.props, ['type', 'forceRender', 'collapsible']);
    return (
      <Collapse
        defaultActiveKey={this.activeKeys}
        onChange={this.handleChange}
        collapsible={collapsible}
        {...collapseProps}
      >
        {items.map(item => {
          const widgetProps = getWidgetProps(item.widgetId);

          const listItemProps = {
            key: item.widgetId,
            id: item.widgetId,
            header: item.label || item.widgetId,
            active: item.opened,
          };

          const { security } = item;
          return isEmpty(security) ? (
            <Panel
              {...listItemProps}
              {...panelProps}
              style={{ display: widgetProps.isVisible === false ? 'none' : '' }}
            >
              <Factory
                id={item.widgetId}
                level={WIDGETS}
                {...getWidget(pageId, item.widgetId)}
              />
            </Panel>
          ) : (
            <SecurityCheck
              {...listItemProps}
              config={security}
              active={item.opened}
              id={item.widgetId}
              render={({ permissions, ...rest }) => {
                return permissions ? (
                  <Panel
                    {...panelProps}
                    {...listItemProps}
                    {...rest}
                    style={{
                      display: widgetProps.isVisible === false ? 'none' : '',
                    }}
                  >
                    <Factory
                      id={item.widgetId}
                      level={WIDGETS}
                      {...getWidget(pageId, item.widgetId)}
                    />
                  </Panel>
                ) : null;
              }}
            />
          );
        })}
      </Collapse>
    );
  }
}

ListRegion.propTypes = {
  /**
   * Элементы списка
   */
  items: PropTypes.array.isRequired,
  getWidget: PropTypes.func.isRequired,
  /**
   * ID страницы
   */
  pageId: PropTypes.string.isRequired,
  /**
   * Флаг отключения ленивого рендера
   */
  forceRender: PropTypes.bool,
  resolveVisibleDependency: PropTypes.func,
  collapsible: PropTypes.bool,
};

ListRegion.defaultProps = {
  collapsible: true,
};

export { ListRegion };
export default compose(
  setDisplayName('ListRegion'),
  withWidgetProps
)(ListRegion);
