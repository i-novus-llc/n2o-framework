import React from 'react'
import flowRight from 'lodash/flowRight'
import { withResizeDetector } from 'react-resize-detector'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'
import isUndefined from 'lodash/isUndefined'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import { Navbar, Nav, NavbarToggler, Collapse } from 'reactstrap'

import SearchBarContainer from '../../../components/snippets/SearchBar/SearchBarContainer'
import { withItemsResolver } from '../../withItemsResolver/withItemResolver'
import { WithDataSource } from '../../../core/datasource/WithDataSource'
import { withTitlesResolver } from '../../withTitlesResolver/withTitlesResolver'
import { WithContextDataSource } from '../../WithContextDataSource/WithContextDataSource'

import { Logo } from './Logo'
import { SidebarSwitcher } from './SidebarSwitcher'
import { Menu, Menu as ExtraMenu } from './Menu/Menu'

/**
 * Хедер-плагин
 * @param {Object} props - пропсы
 * @param {string|element} props.brand - брэнд
 * @param {string|element} props.brandImage - изображение брэнда
 * @param {array} props.items - элементы навбар-меню (левое меню)
 * @param {boolean} props.fixed - фиксированный хэдер или нет
 * @param {array} props.extraMenu - элементы навбар-меню (правое меню)
 * @param {boolean} props.collapsed - находится в состоянии коллапса или нет
 * @param {boolean} props.search - есть поле поиска / нет поля поиска
 * @param {boolean} props.color - стиль хэдера (default или inverse)
 * @param {boolean} props.className - css-класс
 * @param {boolean} props.style - объект стиля
 * @example
 */

class SimpleHeader extends React.Component {
    state = { isOpen: false }

    toggle = () => {
        const { isOpen } = this.state

        this.setState({ isOpen: !isOpen })
    }

    componentDidUpdate(prevProps) {
        const { width } = this.props

        if (width !== prevProps.width && width >= 992) {
            this.setState({ isOpen: false })
        }
    }

    render() {
        const {
            color,
            fixed,
            location,
            logo,
            menu,
            extraMenu,
            sidebarSwitcher,
            toggleSidebar,
            sidebarOpen,
            className,
            search,
            datasources,
        } = this.props

        const { items } = menu
        const { items: extraItems } = extraMenu

        let { style } = this.props
        const { isOpen } = this.state

        const isInversed = color === 'inverse'
        const navColor = isInversed ? 'primary' : 'light'

        const pathname = get(location, 'pathname', '')

        const trigger = !isUndefined(get(search, 'dataProvider'))
            ? 'CHANGE'
            : 'ENTER'

        const { N2O_ELEMENT_VISIBILITY } = window

        if (N2O_ELEMENT_VISIBILITY && N2O_ELEMENT_VISIBILITY.header === false) {
            style = { ...style, display: 'none' }
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
            <div
                style={style}
                className={simpleHeaderClassNames}
            >
                <Navbar
                    color={navColor}
                    light={!isInversed}
                    dark={isInversed}
                    expand="lg"
                >
                    {!isEmpty(sidebarSwitcher) && (
                        <SidebarSwitcher
                            toggleSidebar={toggleSidebar}
                            sidebarOpen={sidebarOpen}
                            isInversed
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
                            <Menu
                                items={items}
                                pathname={pathname}
                                datasources={datasources}
                            />
                        </Nav>
                        <Nav className="ml-auto main-nav-extra" navbar>
                            <ExtraMenu
                                items={extraItems}
                                pathname={pathname}
                                datasources={datasources}
                            />
                            {search && (
                                <SearchBarContainer trigger={trigger} {...search} />
                            )}
                        </Nav>
                    </Collapse>
                </Navbar>
            </div>
        )
    }
}

const menuType = PropTypes.shape(
    {
        src: PropTypes.string,
        items: PropTypes.arrayOf(
            PropTypes.shape({
                id: PropTypes.string.isRequired,
                title: PropTypes.string.isRequired,
                href: PropTypes.string,
                linkType: PropTypes.oneOf(['inner', 'outer']),
                type: PropTypes.oneOf(['dropdown', 'link', 'text']).isRequired,
                items: PropTypes.array,
                badge: PropTypes.string,
                badgeColor: PropTypes.string,
                target: PropTypes.string,
                security: PropTypes.object,
            }),
        ) },
)

SimpleHeader.propTypes = {
    /**
     * location object
     */
    location: PropTypes.object,
    /**
     * Строка поиска
     */
    search: PropTypes.bool,
    /**
     * Цвет хедера
     */
    color: PropTypes.oneOf(['inverse', 'default']),
    /**
     * Флаг фиксированного хедера
     */
    fixed: PropTypes.bool,
    /**
     * Флаг сжатости хедера
     */
    collapsed: PropTypes.bool,
    /**
     * Класс
     */
    className: PropTypes.string,
    /**
     * Стили
     */
    style: PropTypes.object,
    /**
     * Включение показа контрола смены локализации
     */
    localeSelect: PropTypes.bool,
    width: PropTypes.number,
    logo: PropTypes.object,
    menu: menuType,
    extraMenu: menuType,
    sidebarSwitcher: PropTypes.object,
    datasources: PropTypes.object,
    toggleSidebar: PropTypes.func,
    sidebarOpen: PropTypes.bool,
    datasource: PropTypes.string,
}

SimpleHeader.defaultProps = {
    color: 'default',
    fixed: true,
    collapsed: true,
    className: '',
    extraMenu: {},
    search: false,
    style: {},
    menu: {},
    localeSelect: false,
}

const Enhancer = flowRight(
    WithDataSource,
    WithContextDataSource,
    withItemsResolver,
    withTitlesResolver,
)(withResizeDetector(SimpleHeader, {
    handleHeight: false,
    refreshMode: 'debounce',
    refreshRate: 100,
}))

export default Enhancer
