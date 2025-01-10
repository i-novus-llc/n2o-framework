import React, { useEffect, useRef, useState, useMemo, ComponentType, LegacyRef } from 'react'
import classNames from 'classnames'
import flowRight from 'lodash/flowRight'
import map from 'lodash/map'
import { withTranslation } from 'react-i18next'
import { connect } from 'react-redux'

import { withItemsResolver } from '../withItemsResolver/withItemResolver'
import { withTitlesResolver } from '../withTitlesResolver/withTitlesResolver'
import { WithDataSource } from '../../core/datasource/WithDataSource'
import { WithContextDataSource } from '../WithContextDataSource/WithContextDataSource'
import { dataSourceModelByPrefixSelector } from '../../ducks/datasource/selectors'
import { ModelPrefix } from '../../core/datasource/const'
import { State } from '../../ducks/State'

import { NavItemContainer } from './NavItemContainer'
import { LogoSection } from './LogoSection'
import { SIDEBAR_VIEW, SidebarProps, SidebarView } from './types'
import { toggleIconClassNames, sideBarClasses } from './utils'

export function SideBarBody({
    activeId,
    sidebarOpen,
    className,
    logo,
    subtitle,
    onMouseEnter,
    onMouseLeave,
    isStaticView,
    datasources,
    datasource,
    visible: propsVisible,
    side = 'left',
    controlled = false,
    menu = {} as never,
    extraMenu = {} as never,
    models = {} as never,
    defaultState = SIDEBAR_VIEW.mini as SidebarView,
    toggledState = SIDEBAR_VIEW.maxi as SidebarView,
}: SidebarProps) {
    const [visible, setVisible] = useState(propsVisible)
    const onToggle = () => setVisible(!visible)
    const sidebarRef = useRef() as LegacyRef<HTMLElement>
    const currentVisible = controlled ? sidebarOpen : visible

    useEffect(() => {
        setVisible(propsVisible)
    }, [propsVisible])

    const { items = [] } = menu
    const { items: extraItems = [] } = extraMenu

    const needShowContent = () => {
        if (isStaticView && defaultState !== 'micro') {
            return true
        }

        if (currentVisible) {
            return (toggledState === SIDEBAR_VIEW.mini) || (toggledState === SIDEBAR_VIEW.maxi)
        }

        return defaultState !== 'micro'
    }

    const showContent = needShowContent()

    const isMiniView = (defaultState === SIDEBAR_VIEW.mini && !currentVisible) ||
        (toggledState === SIDEBAR_VIEW.mini && currentVisible)

    const renderItems = (items: SidebarProps['menu']['items']) => map(items, (item, key) => (
        <NavItemContainer
            key={key}
            itemProps={item}
            activeId={activeId}
            sidebarOpen={isStaticView || currentVisible}
            showContent={showContent}
            isMiniView={isMiniView}
            isStaticView={isStaticView}
            datasources={datasources}
            datasource={item?.datasource || datasource}
            models={models}
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

const mapStateToProps = (state: State, { datasource }: SidebarProps) => {
    const dataSource = dataSourceModelByPrefixSelector(
        datasource,
        ModelPrefix.source,
    )(state) as Array<Record<string, unknown>>

    return { datasourceModel: dataSource?.[0] || {} }
}

const mapping = (props: SidebarProps) => ({ ...props, force: true, fetch: 'lazy' })

const mapProps = (Component: ComponentType<SidebarProps>) => (props: SidebarProps) => {
    const mappedProps = useMemo(() => mapping(props), [props])

    return <Component {...mappedProps} />
}

const SideBar = flowRight(
    withTranslation(),
    mapProps,
    WithDataSource,
    WithContextDataSource,
    withItemsResolver,
    connect(mapStateToProps),
    withTitlesResolver,
)(SideBarBody)

SideBar.displayName = 'Sidebar'

export { SideBar }
export default SideBar
