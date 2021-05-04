import React from 'react'
import {
    compose,
    lifecycle,
    withHandlers,
    withState,
    defaultProps,
    setDisplayName,
} from 'recompose'
import PropTypes from 'prop-types'
import isEqual from 'lodash/isEqual'
import isFunction from 'lodash/isFunction'
import cn from 'classnames'
import { useTranslation } from 'react-i18next'

import Panel from './Panel'
import panelStyles from './panelStyles'

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

function PanelContainer({
    tabs,
    toolbar,
    className,
    style,
    color,
    icon,
    headerTitle,
    footerTitle,
    collapsible,
    hasTabs,
    fullScreen,
    header,
    fullScreenState,
    openState,
    activeTabState,
    children,
    handleFullScreen,
    changeActiveTab,
    toggleCollapse,
    handleKeyPress,
    innerRef,
}) {
    const { t } = useTranslation()
    const fullScreenIcon = fullScreenState ? 'fa-compress' : 'fa-expand'

    return (
        <Panel
            color={color}
            style={style}
            className={cn(className, {
                'n2o-panel-region--tabs': hasTabs,
            })}
            open={openState}
            isFullScreen={fullScreenState}
            onKeyPress={handleKeyPress}
            innerRef={innerRef}
            t={t}
        >
            {header && (
                <Panel.Heading>
                    <Panel.Title collapsible={collapsible} icon={icon}>
                        {headerTitle}
                    </Panel.Title>
                    <Panel.Menu
                        fullScreen={fullScreen}
                        onFullScreenClick={handleFullScreen}
                        fullScreenIcon={fullScreenIcon}
                        isOpen={openState}
                        onToggle={toggleCollapse}
                        collapsible={collapsible}
                    >
                        {hasTabs &&
              tabs.map((tab, i) => {
                  const activeTab = activeTabState

                  if (!activeTab && i === 0) {
                      changeActiveTab(tab.id)
                  }

                  return (
                      <Panel.NavItem
                          id={tab.id}
                          active={activeTab === tab.id}
                          disabled={tab.disabled}
                          className={cn('nav-item--tab', tab.className)}
                          style={tab.style}
                          onClick={() => changeActiveTab(tab.id)}
                      >
                          {tab.header}
                      </Panel.NavItem>
                  )
              })}
                        {toolbar &&
              toolbar.map(item => (
                  <Panel.NavItem
                      id={item.id}
                      disabled={item.disabled}
                      className={cn('nav-item--toolbar', item.className)}
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
                className={cn({
                    'd-flex flex-column n2o-panel-region--grow': openState,
                })}
                isOpen={openState}
            >
                <Panel.Body hasTabs={hasTabs} activeKey={activeTabState}>
                    {hasTabs
                        ? tabs.map(tab => (
                            <Panel.TabBody eventKey={tab.id}>{tab.content}</Panel.TabBody>
                        ))
                        : children}
                </Panel.Body>
                {footerTitle && <Panel.Footer>{footerTitle}</Panel.Footer>}
            </Panel.Collapse>
        </Panel>
    )
}

PanelContainer.propTypes = {
    tabs: PropTypes.array,
    toolbar: PropTypes.array,
    className: PropTypes.string,
    style: PropTypes.object,
    color: PropTypes.oneOf(Object.values(panelStyles)),
    icon: PropTypes.string,
    headerTitle: PropTypes.string,
    footerTitle: PropTypes.string,
    open: PropTypes.bool,
    collapsible: PropTypes.bool,
    hasTabs: PropTypes.bool,
    fullScreen: PropTypes.bool,
    children: PropTypes.node,
    header: PropTypes.bool,
    isFullScreen: PropTypes.bool,
    onKeyPress: PropTypes.func,
    innerRef: PropTypes.func,
    onVisibilityChange: PropTypes.func,
}

export default compose(
    setDisplayName('Panel'),
    defaultProps({
        open: true,
        collapsible: false,
        hasTabs: false,
        fullScreen: false,
        tabs: [],
        color: panelStyles.DEFAULT,
        header: true,
        onKeyPress: () => {},
    }),
    withState(
        'fullScreenState',
        'setFullScreenState',
        ({ isFullScreen }) => isFullScreen,
    ),
    withState('activeTabState', 'setActiveTabState', ({ tabs }) => (tabs.length > 0 ? tabs[0].id : null)),
    withState('openState', 'setOpenState', ({ open }) => open),
    withHandlers({
        handleFullScreen: ({ fullScreenState, setFullScreenState }) => () => setFullScreenState(!fullScreenState),
        changeActiveTab: ({ setActiveTabState }) => id => setActiveTabState(id),
        toggleCollapse: ({ openState, setOpenState, onVisibilityChange }) => () => {
            if (isFunction(onVisibilityChange)) {
                onVisibilityChange(!openState)
            } else {
                setOpenState(!openState)
            }
        },
        handleKeyPress: ({ setFullScreenState, onKeyPress }) => (event) => {
            if (event.key === 'Escape') {
                setFullScreenState(false)
                onKeyPress(false)
            }
        },
    }),
    lifecycle({
        componentDidUpdate(prevProps) {
            if (!isEqual(prevProps, this.props)) {
                const {
                    open,
                    isFullScreen,
                    setFullScreenState,
                    setOpenState,
                } = this.props

                if (prevProps.open !== open) {
                    setOpenState(open)
                }

                if (prevProps.isFullScreen !== isFullScreen) {
                    setFullScreenState(isFullScreen)
                }
            }
        },
    }),
)(PanelContainer)
