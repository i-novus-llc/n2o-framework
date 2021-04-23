import React from 'react'
import PropTypes from 'prop-types'
import cn from 'classnames'

import TabNav from '../../regions/Tabs/TabNav'

import PanelNavItem from './PanelNavItem'

/**
 * Компонент меню для {@link Panel}
 * @reactProps {boolean} fullScreen - флаг наличия кнопки перехода в полный экран
 * @reactProps {boolean} collapsible - флаг наличия кнопки сворачивания панели
 * @reactProps {function} onFullScreenClick - callback при нажатии на кнопку полного экрана
 * @reactProps {function} onToggle - callback при нажатии на кнопку свернуть
 * @reactProps {string} fullScreenIcon - класс иконки кнопки полного экрана
 * @reactProps {boolean} isOpen - флаг открытия панели
 * @reactProps {node} children - элемент вставляемый внутрь PanelMenu
 */

function PanelMenu({
    children,
    collapsible,
    onToggle,
    fullScreen,
    onFullScreenClick,
    isOpen,
    fullScreenIcon,
}) {
    return (
        <div className="panel-block-flex">
            <TabNav className="panel-block-flex panel-tab-nav">
                {collapsible && (
                    <PanelNavItem
                        onClick={onToggle}
                        className={cn('collapse-toggle', {
                            'collapse-toggle--up': !isOpen,
                        })}
                        isToolBar={false}
                    >
                        <i className="fa fa-angle-down" aria-hidden />
                    </PanelNavItem>
                )}
                {fullScreen && (
                    <PanelNavItem
                        onClick={onFullScreenClick}
                        className="fullscreen-toggle"
                        isToolBar={false}
                    >
                        <i className={cn('fa', fullScreenIcon)} aria-hidden />
                    </PanelNavItem>
                )}
                {children}
            </TabNav>
        </div>
    )
}

PanelMenu.propTypes = {
    fullScreen: PropTypes.bool,
    onFullScreenClick: PropTypes.func,
    fullScreenIcon: PropTypes.string,
    children: PropTypes.node,
    collapsible: PropTypes.bool,
    onToggle: PropTypes.func,
    isOpen: PropTypes.bool,
}

PanelMenu.defaultProps = {
    fullScreen: false,
    collapsible: false,
}

export default PanelMenu
