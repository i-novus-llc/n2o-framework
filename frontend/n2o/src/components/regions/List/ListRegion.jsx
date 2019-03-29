import React from 'react';
import PropTypes from 'prop-types';
import { compose } from 'recompose';
import withVisibleDependency from '../withVisibleDependency';
import { isEmpty, filter, map, pick, difference, pullAll, first } from 'lodash';
import Collapse, { Panel } from '../../snippets/Collapse/Collapse';
import withGetWidget from '../withGetWidget';
import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import SecurityCheck from '../../../core/auth/SecurityCheck';

/**
 * Регион Лист
 * @reactProps {array} items - массив из объектов, которые описывают виджет{id, name, opened, pageId, fetchOnInit, widget}
 * @reactProps {string} pageId - идентификатор страницы
 * @reactProps {function} getWidget - функция получения виджета
 * @reactProps {function} resolveVisibleDependency - резол видимости листа
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
    const { items, getWidget, pageId, resolveVisibleDependency } = this.props;

    this.activeKeys = map(filter(items, 'opened'), 'widgetId');
    const collapseProps = pick(this.props, 'destroyInactivePanel', 'accordion');
    const panelProps = pick(this.props, 'type', 'forceRender');
    return (
      <Collapse defaultActiveKey={this.activeKeys} onChange={this.handleChange} {...collapseProps}>
        {items.map(item => {
          const listItemProps = {
            key: item.widgetId,
            id: item.widgetId,
            header: item.label || item.widgetId,
            active: item.opened
          };

          if (item.dependency && !resolveVisibleDependency(item.dependency)) {
            return null;
          }

          const ListItem = props => {
            return (
              <Panel {...props} {...panelProps}>
                <Factory id={props.id} level={WIDGETS} {...getWidget(pageId, props.id)} />
              </Panel>
            );
          };
          const { security } = item;
          return isEmpty(security) ? (
            React.createElement(ListItem, listItemProps)
          ) : (
            <SecurityCheck
              {...listItemProps}
              config={security}
              active={item.opened}
              id={item.widgetId}
              render={({ permissions, ...rest }) => {
                return permissions ? React.createElement(ListItem, rest) : null;
              }}
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

export default compose(
  withVisibleDependency,
  withGetWidget
)(ListRegion);
