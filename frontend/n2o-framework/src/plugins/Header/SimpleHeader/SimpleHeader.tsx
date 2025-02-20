import React, { CSSProperties } from 'react'
import flowRight from 'lodash/flowRight'
import { withResizeDetector } from 'react-resize-detector'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'
import isUndefined from 'lodash/isUndefined'
import get from 'lodash/get'
import { Navbar, Nav, NavbarToggler, Collapse } from 'reactstrap'

import SearchBarContainer from '../../../components/snippets/SearchBar/SearchBarContainer'
import { withItemsResolver } from '../../withItemsResolver/withItemResolver'
import { WithDataSource } from '../../../core/datasource/WithDataSource'
import { withTitlesResolver } from '../../withTitlesResolver/withTitlesResolver'
import { WithContextDataSource } from '../../WithContextDataSource/WithContextDataSource'
import { WindowType } from '../../../components/core/WindowType'

import { Logo } from './Logo'
import { SidebarSwitcher } from './SidebarSwitcher'
import { Menu, Menu as ExtraMenu } from './Menu/Menu'

interface MenuItem {
    id: string
    title: string
    href?: string
    linkType?: 'inner' | 'outer'
    type: 'dropdown' | 'link' | 'text'
    items?: MenuItem[]
    badge?: string
    badgeColor?: string
    target?: string
}

interface MenuProps {
    src?: string
    items: MenuItem[]
}

export interface SimpleHeaderBodyProps {
    location: { pathname: string }
    search?: Record<string, unknown>
    color?: 'inverse' | 'default'
    fixed?: boolean
    collapsed?: boolean
    className?: string
    style?: CSSProperties
    localeSelect?: boolean
    width: number
    logo?: object
    menu: MenuProps
    extraMenu: MenuProps
    sidebarSwitcher?: {
        defaultIcon: string
        toggledIcon: string
    }
    datasources?: Array<Record<string, unknown>>
    toggleSidebar(): void
    sidebarOpen: boolean
    datasource?: string
}

type State = { isOpen: boolean }

class SimpleHeaderBody extends React.Component<SimpleHeaderBodyProps, State> {
    state: State = { isOpen: false }

    toggle = () => {
        const { isOpen } = this.state

        this.setState({ isOpen: !isOpen })
    }

    componentDidUpdate(prevProps: SimpleHeaderBodyProps) {
        const { width } = this.props

        if (width !== prevProps.width && width >= 992) {
            this.setState({ isOpen: false })
        }
    }

    render() {
        const {
            color = 'default',
            fixed = true,
            location,
            logo,
            menu = {},
            extraMenu = {},
            sidebarSwitcher,
            toggleSidebar,
            sidebarOpen,
            className,
            search = null,
            datasources,
        } = this.props

        const items = get(menu, 'items', {})
        const extraItems = get(extraMenu, 'items', {})

        let { style } = this.props
        const { isOpen } = this.state

        const isInversed = color === 'inverse'
        const navColor = isInversed ? 'primary' : 'light'

        const pathname = get(location, 'pathname', '')

        const trigger = !isUndefined(get(search, 'dataProvider')) ? 'CHANGE' : 'ENTER'

        const { N2O_ELEMENT_VISIBILITY } = window as WindowType

        if (N2O_ELEMENT_VISIBILITY && !N2O_ELEMENT_VISIBILITY.header) {
            style = { display: 'none' }
        }

        const simpleHeaderClassNames = classNames(
            'n2o-header',
            `n2o-header-${color}`,
            className,
            {
                'navbar-container-fixed': fixed,
                'navbar-container-relative': !fixed,
                open: isOpen,
            },
        )

        return (
            <div style={style} className={simpleHeaderClassNames}>
                <Navbar color={navColor} light={!isInversed} dark={isInversed} expand="lg">
                    {!isEmpty(sidebarSwitcher) && (
                        <SidebarSwitcher
                            toggleSidebar={toggleSidebar}
                            sidebarOpen={sidebarOpen}
                            {...sidebarSwitcher}
                        />
                    )}
                    {logo && <Logo {...logo} />}
                    {!isEmpty(items) && <NavbarToggler onClick={this.toggle} />}
                    <Collapse
                        isOpen={isOpen}
                        className={classNames({
                            'n2o-navbar-collapse-open': isOpen,
                        })}
                        navbar
                    >
                        <Nav className="main-nav" navbar>
                            {!isEmpty(items) && <Menu items={items} pathname={pathname} datasources={datasources} />}
                        </Nav>
                        <Nav className="ml-auto main-nav-extra" navbar>
                            <ExtraMenu items={extraItems} pathname={pathname} datasources={datasources} />
                            {/* @ts-ignore import from js file */}
                            {search && <SearchBarContainer trigger={trigger} {...search} />}
                        </Nav>
                    </Collapse>
                </Navbar>
            </div>
        )
    }
}

export const SimpleHeader = flowRight(
    WithDataSource,
    WithContextDataSource,
    withItemsResolver,
    withTitlesResolver,
)(withResizeDetector(SimpleHeaderBody, {
    handleHeight: false,
    refreshMode: 'debounce',
    refreshRate: 100,
}))

export default SimpleHeader
