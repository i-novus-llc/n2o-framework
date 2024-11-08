import React from 'react'
import classNames from 'classnames'

import { TabNav } from './TabNav'
import { PanelNavItem } from './PanelNavItem'

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
    onToggle,
    onFullScreenClick,
    isOpen,
    fullScreenIcon,
    fullScreen = false,
    collapsible = false,
}) {
    return (
        <div className="panel-block-flex">
            <TabNav className="panel-block-flex panel-tab-nav">
                {collapsible && (
                    <PanelNavItem
                        onClick={onToggle}
                        className={classNames('collapse-toggle', {
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
                        <i className={classNames('fa', fullScreenIcon)} aria-hidden />
                    </PanelNavItem>
                )}
                {children}
            </TabNav>
        </div>
    )
}

export default PanelMenu
