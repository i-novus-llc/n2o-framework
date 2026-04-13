import React, { CSSProperties } from 'react'
import flowRight from 'lodash/flowRight'
import { withResizeDetector } from 'react-resize-detector'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'
import isUndefined from 'lodash/isUndefined'
import get from 'lodash/get'
import { Navbar, Nav, NavbarToggler, Collapse } from 'reactstrap'

import { WithComponentId } from '../../utils'
import SearchBarContainer from '../../../components/snippets/SearchBar/SearchBarContainer'
import { WithDataSource } from '../../../core/datasource/WithDataSource'
import { withTitlesResolver } from '../../withTitlesResolver/withTitlesResolver'
import { WithContextDataSource } from '../../WithContextDataSource/WithContextDataSource'
import { WindowType } from '../../../components/core/WindowType'
import { VISIBILITY_EVENT } from '../../constants'
import { ModelPrefix } from '../../../core/models/types'

import { Logo } from './Logo'
import { SidebarSwitcher } from './SidebarSwitcher'
import { Menu } from './Menu/Menu'

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
    disabled?: boolean
    datasource: string
    model: ModelPrefix
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
    extraMenu: MenuItem[]
    sidebarSwitcher?: {
        defaultIcon: string
        toggledIcon: string
    }
    toggleSidebar(): void
    sidebarOpen: boolean
    datasource?: string
}

type State = { isOpen: boolean, style: CSSProperties }

function getHeaderVisibilityStyle(): CSSProperties {
    const { N2O_ELEMENT_VISIBILITY } = window as WindowType
    const isVisible = N2O_ELEMENT_VISIBILITY ? !!N2O_ELEMENT_VISIBILITY.header : true

    return isVisible ? {} : { display: 'none' }
}

class SimpleHeaderBody extends React.Component<SimpleHeaderBodyProps, State> {
    state: State = { isOpen: false, style: getHeaderVisibilityStyle() }

    toggle = () => {
        const { isOpen } = this.state

        this.setState({ isOpen: !isOpen })
    }

    handleVisibilityUpdate = () => {
        const nextStyle = getHeaderVisibilityStyle()

        const { style } = this.state

        const prevDisplay = style?.display
        const nextDisplay = (nextStyle as CSSProperties)?.display

        if (prevDisplay === nextDisplay) { return }

        this.setState({ style: nextStyle })
    }

    componentDidMount() {
        window.addEventListener(VISIBILITY_EVENT, this.handleVisibilityUpdate)

        this.handleVisibilityUpdate()
    }

    componentWillUnmount() {
        window.removeEventListener(VISIBILITY_EVENT, this.handleVisibilityUpdate)
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
            extraMenu = [],
            sidebarSwitcher,
            toggleSidebar,
            sidebarOpen,
            className,
            search = null,
        } = this.props

        const items = get(menu, 'items', [])

        const { style: propsStyle } = this.props
        const { isOpen, style } = this.state

        const isInversed = color === 'inverse'
        const navColor = isInversed ? 'primary' : 'light'

        const pathname = get(location, 'pathname', '')

        const trigger = !isUndefined(get(search, 'dataProvider')) ? 'CHANGE' : 'ENTER'

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
            <div style={isEmpty(style) ? propsStyle : style} className={simpleHeaderClassNames}>
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
                            <Menu items={items} pathname={pathname} />
                        </Nav>
                        <Nav className="ml-auto main-nav-extra" navbar>
                            {/* @ts-ignore FIXME items type */}
                            <Menu items={extraMenu} pathname={pathname} />
                            {/* @ts-ignore import from js file */}
                            {search && <SearchBarContainer trigger={trigger} {...search} />}
                        </Nav>
                    </Collapse>
                </Navbar>
            </div>
        )
    }

    static displayName = 'SimpleHeaderBody'
}

export const SimpleHeader = flowRight(
    WithDataSource,
    // @INFO нужно для WithContextDataSource, иначе не добавит в addComponents
    WithComponentId('n2o-simple-header'),
    WithContextDataSource,
    withTitlesResolver,
)(withResizeDetector(SimpleHeaderBody, {
    handleHeight: false,
    refreshMode: 'debounce',
    refreshRate: 100,
}))

export default SimpleHeader

SimpleHeader.displayName = 'SimpleHeader'
