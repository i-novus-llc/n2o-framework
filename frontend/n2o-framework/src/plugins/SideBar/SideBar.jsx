import React, { useRef } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import isEqual from 'lodash/isEqual'
import map from 'lodash/map'
import {
    compose,
    withState,
    lifecycle,
    withHandlers,
    setDisplayName,
} from 'recompose'
import { withTranslation } from 'react-i18next'

import { Logo } from '../Header/SimpleHeader/Logo'

import { SidebarItemContainer } from './SidebarItemContainer'

/**
 * Sidebar
 * @param activeId - id активного элемента
 * @param brand - текст бренда
 * @param brandImage - картинка бренда
 * @param userBox - настройка userBox
 * @param items - массив итемов
 * @param visible - видимость
 * @param sidebarOpen - видимость если controlled
 * @param width - длина сайдбара
 * @param controlled - флаг контроллед режима
 * @param onToggle - переключение compressed
 * @param extra - екстра итемы
 * @param className - class
 * @param logo - настройки лого
 * @param t - функция перевода
 * @returns {*}
 * @constructor
 */
export const sidebarView = {
    none: 'none',
    micro: 'micro',
    mini: 'mini',
    maxi: 'maxi',
}

const toggleIconClassNames = (visible, side) => {
    const isLeftIcon = (visible && side === 'left') || (!visible && (side === 'right'))

    return isLeftIcon ? 'fa fa-angle-double-left' : 'fa fa-angle-double-right'
}

const sideBarClasses = (isStaticView, defaultState, toggledState, currentVisible, side, className) => {
    const viewMode = isStaticView || !currentVisible ? sidebarView[defaultState] : sidebarView[toggledState]

    return classNames(
        'n2o-sidebar',
        side,
        className,
        viewMode,
    )
}

const LogoSection = ({
    isMiniView,
    logo,
    showContent,
}) => (
    <div className={classNames(
        'n2o-sidebar__nav-brand n2o-nav-brand',
        {
            'justify-content-center': isMiniView,
        },
    )}
    >
        <div className="d-flex align-items-center">
            {logo && <Logo {...logo} showContent={showContent} isMiniView={isMiniView} />}
        </div>
    </div>
)

export function SideBar({
    activeId,
    visible,
    sidebarOpen,
    controlled,
    onToggle,
    className,
    logo,
    menu,
    extraMenu = {},
    defaultState = sidebarView.mini,
    toggledState = sidebarView.maxi,
    onMouseEnter,
    onMouseLeave,
    side = 'left',
    isStaticView,
    datasources,
}) {
    const sidebarRef = useRef()

    const currentVisible = controlled ? sidebarOpen : visible

    const { items = [] } = menu

    const needShowContent = () => {
        if (isStaticView && defaultState !== 'micro') {
            return true
        }

        if (currentVisible) {
            return (toggledState === sidebarView.mini) || (toggledState === sidebarView.maxi)
        }

        return defaultState !== 'micro'
    }

    const showContent = needShowContent()

    const isMiniView = (defaultState === sidebarView.mini && !currentVisible) ||
        (toggledState === sidebarView.mini && currentVisible)

    const renderItems = items => map(items, (item, key) => (
        <SidebarItemContainer
            key={key}
            itemProps={item}
            activeId={activeId}
            sidebarOpen={isStaticView || currentVisible}
            defaultState={defaultState}
            toggledState={toggledState}
            showContent={showContent}
            isMiniView={isMiniView}
            isStaticView={isStaticView}
            datasources={datasources}
            datasource={item.datasource}
        />
    ))

    return (
        <aside
            className={
                sideBarClasses(
                    isStaticView,
                    defaultState,
                    toggledState,
                    currentVisible,
                    side,
                    className,
                )
            }
            onMouseEnter={onMouseEnter}
            onMouseLeave={onMouseLeave}
            ref={sidebarRef}
        >
            <LogoSection isMiniView={isMiniView} logo={logo} showContent={showContent} />
            <nav className={classNames('n2o-sidebar__nav', { visible: showContent })}>
                <ul className="n2o-sidebar__nav-list">{renderItems(items)}</ul>
            </nav>
            <div className="n2o-sidebar__footer">
                {showContent && (
                    <div className="n2o-sidebar__extra">
                        <ul className="n2o-sidebar__nav-list">{renderItems(extraMenu.items)}</ul>
                    </div>
                )}
                {!controlled && !isStaticView && (
                    <div onClick={onToggle} className="n2o-sidebar__toggler">
                        <span className="n2o-sidebar__nav-item">
                            <span
                                className={classNames('n2o-sidebar__nav-item-icon', {
                                    'mr-1': visible,
                                })}
                            >
                                <i className={toggleIconClassNames(visible, side)} />
                            </span>
                        </span>
                    </div>
                )}
            </div>
        </aside>
    )
}

SideBar.propTypes = {
    /**
     * ID активного элемента
     */
    activeId: PropTypes.string,
    /**
     * Настройки лого и брэнда
     */
    logo: PropTypes.object,
    /**
     * Флаг сжатия
     */
    visible: PropTypes.bool,
    /**
     * Флаг сжатия если controlled
     */
    sidebarOpen: PropTypes.bool,
    /**
     * Флаг включения режима controlled
     */
    controlled: PropTypes.bool,
    /**
     * Callback на переключение сжатия
     */
    onToggle: PropTypes.func,
    /**
     * Адрес ссылка бренда
     */
    side: PropTypes.string,
    className: PropTypes.string,
    menu: PropTypes.object,
    extraMenu: PropTypes.object,
    datasources: PropTypes.object,
    defaultState: PropTypes.string,
    toggledState: PropTypes.string,
    onMouseEnter: PropTypes.func,
    onMouseLeave: PropTypes.func,
    isStaticView: PropTypes.bool,
}

SideBar.defaultProps = {
    controlled: false,
    menu: {},
}

LogoSection.propTypes = {
    isMiniView: PropTypes.bool,
    logo: PropTypes.object,
    showContent: PropTypes.bool,
}

export default compose(
    withTranslation(),
    setDisplayName('Sidebar'),
    withState('visible', 'setVisible', ({ visible }) => visible),
    withHandlers({
        onToggle: ({
            visible,
            setVisible,
        }) => () => setVisible(!visible),
    }),
    lifecycle({
        componentDidUpdate(prevProps) {
            if (!isEqual(prevProps.visible, this.props.visible)) {
                this.setState({ visible: this.props.visible })
            }
        },
    }),
)(SideBar)
