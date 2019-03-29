import React from 'react';
import PropTypes from 'prop-types';

import TabNav from '../../regions/Tabs/TabNav';
import PanelNavItem from './PanelNavItem';

/**
 * Компонент меню для {@link Panel}
 * @reactProps {boolean} fullScreen - флаг наличия кнопки перехода в полный экран
 * @reactProps {function} onFullScreenClick - callback при нажатии на кнопку полного экрана
 * @reactProps {string} fullScreenIcon - класс иконки кнопки полного экрана
 * @reactProps {node} children - элемент вставляемый внутрь PanelMenu
 */

class PanelMenu extends React.Component {
  /**
   * Рендер
   */

  render() {
    return (
      <div className="panel-block-flex">
        <TabNav className="panel-block-flex panel-tab-nav">
          {this.props.children}
          {this.props.fullScreen && (
            <PanelNavItem
              onClick={this.props.onFullScreenClick}
              className="fullscreen-toggle"
              isToolBar={true}
            >
              <i className={`fa fa-${this.props.fullScreenIcon}`} aria-hidden={true} />
            </PanelNavItem>
          )}
        </TabNav>
      </div>
    );
  }
}

PanelMenu.propTypes = {
  fullScreen: PropTypes.bool,
  onFullScreenClick: PropTypes.func,
  fullScreenIcon: PropTypes.string,
  children: PropTypes.node
};

PanelMenu.defaultProps = {
  fullScreen: false
};

export default PanelMenu;
