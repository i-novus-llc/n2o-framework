import React from 'react';
import PropTypes from 'prop-types';

import { compose, setDisplayName } from 'recompose';

import pick from 'lodash/pick';

import Collapse, { Panel } from '../../snippets/Collapse/Collapse';
import withWidgetProps from '../withWidgetProps';
import RegionContent from '../RegionContent';

/**
 * Регион Лист
 * @reactProps {array} content - массив из объектов, которые описывают виджет{id, name, opened, pageId, fetchOnInit, widget}
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

  renderList = props => {
    const { name, content, isVisible } = this.props;
    const key = props.open ? 'open' : 'close';
    return (
      <>
        <span className="n2o-list-region__collapse-name">{name}</span>
        <Panel
          key={key}
          {...props}
          style={{ display: isVisible === false ? 'none' : '' }}
        >
          <RegionContent content={content} />
        </Panel>
      </>
    );
  };

  /**
   * Рендер
   */
  render() {
    const { collapsible } = this.props;

    const collapseProps = pick(this.props, 'destroyInactivePanel', 'accordion');
    const panelProps = pick(this.props, [
      'type',
      'forceRender',
      'collapsible',
      'open',
    ]);
    return (
      <div className="n2o-list-region">
        <Collapse
          defaultActiveKey={'open'}
          onChange={this.handleChange}
          collapsible={collapsible}
          className="n2o-list-region__collapse"
          {...collapseProps}
        >
          {this.renderList(panelProps)}
        </Collapse>
      </div>
    );
  }
}

ListRegion.propTypes = {
  /**
   * Элементы списка
   */
  content: PropTypes.array.isRequired,
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
