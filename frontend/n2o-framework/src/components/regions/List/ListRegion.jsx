import React from 'react';
import PropTypes from 'prop-types';
import { compose, setDisplayName } from 'recompose';
import classNames from 'classnames';
import pick from 'lodash/pick';
import every from 'lodash/every';
import get from 'lodash/get';

import Collapse, { Panel } from '../../snippets/Collapse/Collapse';
import withWidgetProps from '../withWidgetProps';
import RegionContent from '../RegionContent';

/**
 * Регион Лист
 * @reactProps {array} content - массив из объектов, которые описывают виджет{id, name, opened, pageId, fetchOnInit, widget}
 * @reactProps {bool} expand - флаг открыт ли при загрузке (default = true)
 * @reactProps {bool} hasSeparator - есть ли разделительная линия (default = true)
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
    const { label, content, isVisible, hasSeparator } = this.props;

    const key = props.expand ? 'open' : 'close';

    return (
      <Panel
        {...props}
        key={key}
        header={<span className="n2o-list-region__collapse-name">{label}</span>}
        style={{ display: isVisible === false ? 'none' : '' }}
        className={classNames({ line: hasSeparator })}
      >
        <RegionContent content={content} />
      </Panel>
    );
  };

  /**
   * Рендер
   */
  render() {
    const {
      collapsible,
      expand,
      content,
      getWidgetProps,
      className,
      style,
    } = this.props;

    const collapseProps = pick(this.props, 'destroyInactivePanel', 'accordion');
    const panelProps = pick(this.props, [
      'type',
      'forceRender',
      'collapsible',
      'expand',
    ]);
    const isVisible = every(content, meta => {
      return get(getWidgetProps(meta.id), 'datasource') === undefined
        ? true
        : get(getWidgetProps(meta.id), 'isVisible');
    });

    return (
      <div
        className={classNames('n2o-list-region', className)}
        style={{ display: !isVisible && 'none', ...style }}
      >
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
  hasSeparator: true,
  expand: true,
};

export { ListRegion };
export default compose(
  setDisplayName('ListRegion'),
  withWidgetProps
)(ListRegion);
