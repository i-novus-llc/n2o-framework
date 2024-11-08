import React, { useRef } from 'react'
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
import mapProps from 'recompose/mapProps'
import { connect } from 'react-redux'

import { Logo } from '../Header/SimpleHeader/Logo'
import { withItemsResolver } from '../withItemsResolver/withItemResolver'
import { withTitlesResolver } from '../withTitlesResolver/withTitlesResolver'
import { WithDataSource } from '../../core/datasource/WithDataSource'
import { WithContextDataSource } from '../WithContextDataSource/WithContextDataSource'
import { dataSourceModelByPrefixSelector } from '../../ducks/datasource/selectors'
import { ModelPrefix } from '../../core/datasource/const'

import { NavItemContainer } from './NavItemContainer'

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
    subtitle,
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
            {logo && <Logo {...logo} subtitle={subtitle} showContent={showContent} isMiniView={isMiniView} />}
        </div>
    </div>
)

export function SideBar({
    activeId,
    visible,
    sidebarOpen,
    onToggle,
    className,
    logo,
    subtitle,
    onMouseEnter,
    onMouseLeave,
    isStaticView,
    datasources,
    datasource,
    side = 'left',
    menu = {},
    extraMenu = {},
    models = {},
    controlled = false,
    defaultState = sidebarView.mini,
    toggledState = sidebarView.maxi,
}) {
    const sidebarRef = useRef()
    const currentVisible = controlled ? sidebarOpen : visible

    const { items = [] } = menu
    const { items: extraItems = [] } = extraMenu

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
        <NavItemContainer
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
            datasource={item?.datasource || datasource}
            models={models}
            visible
        />
    ))

    return (
        <aside
            className={
                sideBarClasses(isStaticView, defaultState, toggledState, currentVisible, side, className)
            }
            onMouseEnter={onMouseEnter}
            onMouseLeave={onMouseLeave}
            ref={sidebarRef}
        >
            <LogoSection isMiniView={isMiniView} logo={logo} subtitle={subtitle} showContent={showContent} />
            <nav className={classNames('n2o-sidebar__nav', { visible: showContent })}>
                <ul className="n2o-sidebar__nav-list">{renderItems(items)}</ul>
            </nav>
            <div className="n2o-sidebar__footer">
                {showContent && (
                    <div className="n2o-sidebar__extra">
                        <ul className="n2o-sidebar__nav-list">{renderItems(extraItems)}</ul>
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

const mapStateToProps = (state, { datasource }) => ({
    datasourceModel:
        dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)(state)?.[0] || {},
})

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
    mapProps(props => ({
        ...props,
        force: true,
        fetch: 'lazy',
    })),
    WithDataSource,
    WithContextDataSource,
    withItemsResolver,
    connect(mapStateToProps),
    withTitlesResolver,
)(SideBar)
