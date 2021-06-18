import React from 'react'
import PropTypes from 'prop-types'
import Card from 'reactstrap/lib/Card'
import Collapse from 'reactstrap/lib/Collapse'
import classNames from 'classnames'

import panelStyles from './panelStyles'
import PanelHeading from './PanelHeading'
import PanelTitle from './PanelTitle'
import PanelMenu from './PanelMenu'
import { PanelNavItem } from './PanelNavItem'
import { PanelFooter } from './PanelFooter'
import { PanelBody } from './PanelBody'
import { PanelTabBody } from './PanelTabBody'

/**
 * Компонент панели
 * @reactProps {string} className - имя класса для родительского элемента
 * @reactProps {object} style - стили для родительского элемента
 * @reactProps {string} color - стиль для панели
 * @reactProps {boolean} isFullScreen - флаг режима на весь экран
 * @reactProps {function} onToggle - callback для скрытия/раскрытия панели
 * @reactProps {boolean} - open
 * @reactProps {function} onKeyPress - callback при нажатии на кнопку
 * @reactProps {node} children - вставляемый в панель элемент
 */

export function Panel({
    className,
    isFullScreen,
    style,
    onToggle,
    color,
    onKeyPress,
    children,
    innerRef,
    t,
}) {
    const panelClass = classNames('n2o-panel-region', className, 'text-dark', {
        'panel-fullscreen': isFullScreen,
    })

    return (
        <Card
            className={panelClass}
            style={style}
            onToggle={onToggle}
            color={color}
            // expanded={open}
            outline
            onKeyDown={onKeyPress}
            tabIndex="-1"
            innerRef={innerRef}
        >
            {children}
            <div className="panel-fullscreen-help">
                <span>{t('panelFullScreenHelp')}</span>
            </div>
        </Card>
    )
}

Panel.propTypes = {
    className: PropTypes.string,
    style: PropTypes.object,
    color: PropTypes.string,
    isFullScreen: PropTypes.bool,
    onToggle: PropTypes.func,
    onKeyPress: PropTypes.func,
    children: PropTypes.node,
    innerRef: PropTypes.func,
    t: PropTypes.func,
}

Panel.defaultProps = {
    isFullScreen: false,
    color: panelStyles.DEFAULT,
}

Object.assign(Panel, {
    Heading: PanelHeading,
    Title: PanelTitle,
    Menu: PanelMenu,
    NavItem: PanelNavItem,
    Footer: PanelFooter,
    Body: PanelBody,
    TabBody: PanelTabBody,
    Collapse,
})

export default Panel
