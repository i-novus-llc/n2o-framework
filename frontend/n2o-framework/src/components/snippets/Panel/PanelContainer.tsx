import React, { useState, useCallback, useEffect } from 'react'
import isFunction from 'lodash/isFunction'
import classNames from 'classnames'
import { useTranslation } from 'react-i18next'

import { usePrevious } from '../../../utils/usePrevious'

import { Panel } from './Panel'
import { PANEL_COLORS, type PanelContainerProps } from './types'

/**
 * Shorthand для создания панели
 * @reactProps {array} tabs - массив с табами
 * @reactProps {array} toolbar - массив для тулбара
 * @reactProps {string} className - имя класса для родительского элемента
 * @reactProps {object} style - стили для родительского элемента
 * @reactProps {string} color - стиль для панели
 * @reactProps {string} icon - класс для иконки
 * @reactProps {string} headerTitle - заголовок для шапки
 * @reactProps {string} footerTitle - заголовок для футера
 * @reactProps {boolean} collapsible - флаг возможности скрывать содержимое панели
 * @reactProps {boolean} open - флаг открытости панели
 * @reactProps {boolean} hasTabs - флаг наличия табов
 * @reactProps {boolean} fullScreen - флаг возможности открывать на полный экран
 * @reactProps {node} children - элемент вставляемый в PanelContainer
 * @reactProps {boolean} - флаг показа заголовка
 * @example <caption>Структура tabs</caption>
 * {
 *  id - id таба
 *  header - содержимое нава
 *  content - содержимое таба
 * }
 */

export function PanelContainer({
    toolbar,
    className,
    style,
    icon,
    headerTitle,
    footerTitle,
    disabled,
    children,
    innerRef,
    onVisibilityChange,
    onKeyPress = () => {},
    tabs = [],
    isFullScreen = false,
    open = true,
    collapsible = false,
    hasTabs = false,
    fullScreen = false,
    header = true,
    color = PANEL_COLORS.DEFAULT,
}: PanelContainerProps) {
    const { t } = useTranslation()

    const prevIsFullScreen = usePrevious<boolean>(isFullScreen)
    const prevOpen = usePrevious<boolean>(open)

    const [fullScreenState, setFullScreenState] = useState<boolean>(isFullScreen)
    const [openState, setOpenState] = useState<boolean>(open)
    const [activeTabState, setActiveTabState] = useState<string | undefined>(tabs.length > 0 ? tabs[0].id : undefined)

    const fullScreenIcon = fullScreenState ? 'fa-compress' : 'fa-expand'

    const handleFullScreen = useCallback(() => setFullScreenState(!fullScreenState), [fullScreenState])
    const changeActiveTab = (id: string) => setActiveTabState(id)
    const toggleCollapse = useCallback(() => {
        if (isFunction(onVisibilityChange)) {
            onVisibilityChange(!openState)

            return
        }

        setOpenState(!openState)
    }, [onVisibilityChange, openState])

    const handleKeyPress = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === 'Escape') {
            setFullScreenState(false)
            onKeyPress(false)
        }
    }

    useEffect(() => {
        if (prevOpen !== open) {
            setOpenState(open)
        }

        if (prevIsFullScreen !== isFullScreen) {
            setFullScreenState(isFullScreen)
        }
    }, [isFullScreen, open, prevIsFullScreen, prevOpen])

    return (
        <Panel
            color={color}
            style={style}
            className={classNames(className, { 'n2o-panel-region--tabs': hasTabs })}
            open={openState}
            isFullScreen={fullScreenState}
            disabled={disabled}
            onKeyPress={handleKeyPress}
            innerRef={innerRef}
            t={t}
        >
            {header && (
                <Panel.Heading>
                    <Panel.Title icon={icon} toggleCollapse={toggleCollapse}>{headerTitle}</Panel.Title>
                    <Panel.Menu
                        fullScreen={fullScreen}
                        onFullScreenClick={handleFullScreen}
                        fullScreenIcon={fullScreenIcon}
                        isOpen={openState}
                        onToggle={toggleCollapse}
                        collapsible={collapsible}
                    >
                        {hasTabs && tabs.map((tab, i) => {
                            const activeTab = activeTabState

                            if (!activeTab && i === 0) { changeActiveTab(tab.id as string) }

                            return (
                                <Panel.NavItem
                                    id={tab.id}
                                    active={activeTab === tab.id}
                                    disabled={tab.disabled}
                                    className={classNames('nav-item--tab', tab.className)}
                                    style={tab.style}
                                    onClick={() => changeActiveTab(tab.id as string)}
                                >
                                    {tab.header}
                                </Panel.NavItem>
                            )
                        })}
                        {toolbar?.map(item => (
                            <Panel.NavItem
                                id={item.id}
                                disabled={item.disabled}
                                className={classNames('nav-item--toolbar', item.className)}
                                style={item.style}
                                onClick={item.onClick}
                                isToolBar
                            >
                                {item.header}
                            </Panel.NavItem>
                        ))}
                    </Panel.Menu>
                </Panel.Heading>
            )}
            <Panel.Collapse
                className={classNames({
                    'd-flex flex-column n2o-panel-region--grow': openState,
                })}
                isOpen={openState}
            >
                <Panel.Body hasTabs={hasTabs} activeKey={activeTabState}>
                    {hasTabs ? tabs.map(tab => <Panel.TabBody eventKey={tab.id}>{tab.content}</Panel.TabBody>)
                        : children}
                </Panel.Body>
                {footerTitle && <Panel.Footer>{footerTitle}</Panel.Footer>}
            </Panel.Collapse>
        </Panel>
    )
}

PanelContainer.displayName = 'PanelContainer'

export default PanelContainer
