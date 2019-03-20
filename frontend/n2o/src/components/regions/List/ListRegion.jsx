import React from 'react';
import PropTypes from 'prop-types';
import { compose } from 'recompose';
import { isEmpty, filter, map, pick, difference, pullAll, first } from 'lodash';
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
    const { items, getWidget, getWidgetProps, pageId } = this.props;

    this.activeKeys = map(filter(items, 'opened'), 'widgetId');
    const collapseProps = pick(this.props, 'destroyInactivePanel', 'accordion');
    const panelProps = pick(this.props, 'type', 'forceRender');
    return (
      <Collapse defaultActiveKey={this.activeKeys} onChange={this.handleChange} {...collapseProps}>
        {items.map(item => {
          const listItem = (
            <Panel
              key={item.widgetId}
              id={item.widgetId}
              header={item.label || item.widgetId}
              {...panelProps}
            >
              <Factory id={item.widgetId} level={WIDGETS} {...getWidget(pageId, item.widgetId)} />
            </Panel>
          );
          const { security } = item;
          return isEmpty(security) ? (
            listItem
          ) : (
            <SecurityCheck
              config={security}
              active={item.opened}
              id={item.widgetId}
              render={({ permissions, onClick, active }) =>
                permissions ? React.cloneElement(listItem, { onClick, active }) : null
              }
            />
          );
        })}
      </Collapse>
    );
  }
}

ListRegion.propTypes = {
  items: PropTypes.array.isRequired,
  getWidget: PropTypes.func.isRequired,
  pageId: PropTypes.string.isRequired,
  forceRender: PropTypes.bool,
  resolveVisibleDependency: PropTypes.func
};

export default compose(withWidgetProps)(ListRegion);
