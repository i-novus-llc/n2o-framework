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
    defaultState = 'mini',
    toggledState = 'maxi',
    onMouseEnter,
    onMouseLeave,
    side = 'left',
}) {
    const sidebarRef = useRef()
    const currentVisible = controlled ? sidebarOpen : visible
    const { items = [] } = menu

    const showContent = (toggledState === 'mini' && currentVisible) ||
            (toggledState === 'maxi' && (currentVisible || defaultState === 'mini'))

    const isMiniView = (defaultState === 'mini' && !currentVisible) ||
            (toggledState === 'mini' && currentVisible)

    const toggleIconClassNames = classNames(
        {
            'fa fa-angle-double-left': (visible && side === 'left') || (!visible && side) === 'right',
            'fa fa-angle-double-right': (!visible && side === 'left') || (visible && side) === 'right',
        },
    )

    const renderItems = items => map(items, (item, i) => (
        <SidebarItemContainer
            key={i}
            item={item}
            activeId={activeId}
            sidebarOpen={currentVisible}
            defaultState={defaultState}
            toggledState={toggledState}
            showContent={showContent}
            isMiniView={isMiniView}
        />
    ))

    const sideBarClasses = classNames(
        'n2o-sidebar',
        side,
        className,
        {
            [sidebarView[defaultState]]: !currentVisible,
            [sidebarView[toggledState]]: currentVisible,
        },
    )

    return (
        <aside
            className={sideBarClasses}
            onMouseEnter={onMouseEnter}
            onMouseLeave={onMouseLeave}
            ref={sidebarRef}
        >
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
            <nav className={classNames('n2o-sidebar__nav', { visible: showContent })}>
                <ul className="n2o-sidebar__nav-list">{renderItems(items)}</ul>
            </nav>
            <div className="n2o-sidebar__footer">
                {showContent && (
                    <div className="n2o-sidebar__extra">
                        <ul className="n2o-sidebar__nav-list">{renderItems(extraMenu.items)}</ul>
                    </div>
                )}
                {!controlled && (
                    <div onClick={onToggle} className="n2o-sidebar__toggler">
                        <span className="n2o-sidebar__nav-item">
                            <span
                                className={classNames('n2o-sidebar__nav-item-icon', {
                                    'mr-1': visible,
                                })}
                            >
                                <i className={toggleIconClassNames} />
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
     * Блок пользователя
     */
    userBox: PropTypes.object,
    /**
     * Настройки лого и брэнда
     */
    logo: PropTypes.object,
    /**
     * Элементы сайдбара
     */
    items: PropTypes.array,
    /**
     * Флаг сжатия
     */
    visible: PropTypes.bool,
    /**
     * Флаг сжатия если controlled
     */
    sidebarOpen: PropTypes.bool,
    /**
     * Длина
     */
    width: PropTypes.number,
    /**
     * Флаг включения режима controlled
     */
    controlled: PropTypes.bool,
    /**
     * Callback на переключение сжатия
     */
    onToggle: PropTypes.func,
    /**
     * Extra элементы
     */
    extra: PropTypes.array,
    /**
     * Адрес ссылка бренда
     */
    side: PropTypes.string,
    className: PropTypes.string,
    menu: PropTypes.object,
    extraMenu: PropTypes.object,
    defaultState: PropTypes.string,
    toggledState: PropTypes.string,
    onMouseEnter: PropTypes.func,
    onMouseLeave: PropTypes.func,
    overlay: PropTypes.bool,
}

SideBar.defaultProps = {
    controlled: false,
    menu: {},
}

export default compose(
    withTranslation(),
    setDisplayName('Sidebar'),
    withState('visible', 'setVisible', ({ visible }) => visible),
    withHandlers({
        onToggle: ({ visible, setVisible }) => () => setVisible(!visible),
    }),
    lifecycle({
        componentDidUpdate(prevProps) {
            if (!isEqual(prevProps.visible, this.props.visible)) {
                this.setState({ visible: this.props.visible })
            }
        },
    }),
)(SideBar)
