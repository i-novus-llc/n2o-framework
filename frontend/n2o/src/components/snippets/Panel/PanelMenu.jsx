import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import TabNav from '../../regions/Tabs/TabNav';
import PanelNavItem from './PanelNavItem';

/**
 * Компонент меню для {@link Panel}
 * @reactProps {boolean} fullScreen - флаг наличия кнопки перехода в полный экран
 * @reactProps {boolean} collapsible - флаг наличия кнопки сворачивания панели
 * @reactProps {function} onFullScreenClick - callback при нажатии на кнопку полного экрана
 * @reactProps {function} onToggle - callback при нажатии на кнопку свернуть
 * @reactProps {string} fullScreenIcon - класс иконки кнопки полного экрана
 * @reactProps {string} collapseIcon - класс иконки кнопки сворачивания панели
 * @reactProps {node} children - элемент вставляемый внутрь PanelMenu
 */

function PanelMenu({
  children,
  collapsible,
  onToggle,
  collapseIcon,
  fullScreen,
  onFullScreenClick,
  fullScreenIcon,
}) {
  return (
    <div className="panel-block-flex">
      <TabNav className="panel-block-flex panel-tab-nav">
        {children}
        {collapsible && (
          <PanelNavItem
            onClick={onToggle}
            className="collapse-toggle"
            isToolBar={true}
          >
            <i className={cn('fa', collapseIcon)} aria-hidden={true} />
          </PanelNavItem>
        )}
        {fullScreen && (
          <PanelNavItem
            onClick={onFullScreenClick}
            className="fullscreen-toggle"
            isToolBar={true}
          >
            <i className={cn('fa', fullScreenIcon)} aria-hidden={true} />
          </PanelNavItem>
        )}
      </TabNav>
    </div>
  );
}

PanelMenu.propTypes = {
  fullScreen: PropTypes.bool,
  onFullScreenClick: PropTypes.func,
  fullScreenIcon: PropTypes.string,
  children: PropTypes.node,
  collapsible: PropTypes.bool,
  onToggle: PropTypes.func,
  collapseIcon: PropTypes.string,
};

PanelMenu.defaultProps = {
  fullScreen: false,
  collapsible: false,
};

export default PanelMenu;
