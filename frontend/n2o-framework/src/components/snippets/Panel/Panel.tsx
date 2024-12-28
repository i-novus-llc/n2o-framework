import React, { FC } from 'react'
import { Card, Collapse } from 'reactstrap'
import classNames from 'classnames'

import { PanelHeading } from './PanelHeading'
import { PanelTitle } from './PanelTitle'
import { PanelMenu } from './PanelMenu'
import { PanelNavItem } from './PanelNavItem'
import { PanelFooter } from './PanelFooter'
import { PanelBody } from './PanelBody'
import { PanelTabBody } from './PanelTabBody'
import { PANEL_COLORS, type PanelProps } from './types'

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

function Component({
    className,
    style,
    onToggle,
    disabled,
    onKeyPress,
    children,
    innerRef,
    t,
    isFullScreen = false,
    color = PANEL_COLORS.DEFAULT,
}: PanelProps) {
    return (
        <Card
            className={classNames(
                'n2o-panel-region',
                className,
                'text-dark',
                { 'panel-fullscreen': isFullScreen, 'n2o-disabled': disabled },
            )
            }
            style={style}
            onToggle={onToggle}
            color={color}
            outline
            onKeyDown={onKeyPress}
            tabIndex={-1}
            innerRef={innerRef}
        >
            {children}
            <div className="panel-fullscreen-help"><span>{t?.('panelFullScreenHelp')}</span></div>
        </Card>
    )
}

Object.assign(Component, {
    Heading: PanelHeading,
    Title: PanelTitle,
    Menu: PanelMenu,
    NavItem: PanelNavItem,
    Footer: PanelFooter,
    Body: PanelBody,
    TabBody: PanelTabBody,
    Collapse,
})

export interface PanelComponent extends FC<PanelProps> {
    Heading: typeof PanelHeading
    Title: typeof PanelTitle
    Menu: typeof PanelMenu
    NavItem: typeof PanelNavItem
    Footer: typeof PanelFooter
    Body: typeof PanelBody
    TabBody: typeof PanelTabBody
    Collapse: typeof Collapse
}

export const Panel = Component as PanelComponent

export default Panel
