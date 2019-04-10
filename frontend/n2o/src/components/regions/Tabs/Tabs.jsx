/**
 * Created by emamoshin on 01.06.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';
import _ from 'lodash';

import TabNav from './TabNav';
import TabNavItem from './TabNavItem';
import TabContent from './TabContent';

/**
 * Компонент контейнера табов
 * @reactProps {string} className - css-класс
 * @reactProps {string} navClassName - css-класс для нава
 * @reactProps {function} onChangeActive
 * @reactProps {node} children - элемент потомок компонента Tabs
 * @example
 * <Tabs>
 * {
 *   containers.map((cnt) =>
 *     <Tab id={cnt.id}
 *          title={cnt.name || cnt.id}
 *          active={cnt.opened}>
 *       <WidgetFactory containerId={cnt.id} pageId={cnt.pageId} fetchOnInit={cnt.fetchOnInit} {...cnt.widget} />
 *     </Tab>
 *   )
 * }
 * </Tabs>
 */

class Tabs extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      activeId: this.defaultOpenedId,
    };

    this.handleChangeActive = this.handleChangeActive.bind(this);
  }

  /**
   * установка активного таба
   * @param e
   * @param id
   */
  handleChangeActive(e, id) {
    const prevId = this.state.activeId;
    this.setState(
      {
        activeId: id,
      },
      () => this.props.onChangeActive(id, prevId)
    );
  }

  /**
   * getter для айдишника активного таба
   * @return {Array|*}
   */
  get defaultOpenedId() {
    const { children } = this.props;
    const foundChild = _.find(React.Children.toArray(children), child => {
      return child.props.active;
    });
    return foundChild && foundChild.props.id;
  }

  /**
   * Базовый рендер
   * @return {XML}
   */
  render() {
    const { className, navClassName, children } = this.props;
    const { activeId } = this.state;
    const tabNavItems = React.Children.map(children, child => {
      const { id, title, icon, disabled, visible } = child.props;
      if (!visible) {
        return null;
      }
      return (
        <TabNavItem
          id={id}
          title={title}
          icon={icon}
          disabled={disabled}
          active={activeId === id}
          onClick={this.handleChangeActive}
        />
      );
    });
    const style = { marginBottom: 2 };
    return (
      <div className={className} style={style}>
        {!_.isEmpty(tabNavItems) && (
          <TabNav className={navClassName}>{tabNavItems}</TabNav>
        )}
        <TabContent>
          {React.Children.map(children, child =>
            React.cloneElement(child, {
              active: activeId === child.props.id,
            })
          )}
        </TabContent>
      </div>
    );
  }
}

Tabs.propTypes = {
  className: PropTypes.string,
  navClassName: PropTypes.string,
  onChangeActive: PropTypes.func,
  children: PropTypes.node,
};

export default Tabs;
