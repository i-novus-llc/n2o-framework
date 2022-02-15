import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { ScrollSpyRegionTypes } from './ScrollSpyRegionTypes'
import { Title } from './utils'

export function Menu({ menu, menuTitle, active }) {
    return (
        <section className="n2o-scroll-spy-region__menu">
            <Title
                title={menuTitle}
                className="n2o-scroll-spy-region__menu-title"
            />
            <MenuList menu={menu} active={active} />
        </section>
    )
}

Menu.propTypes = {
    menuTitle: PropTypes.string,
    ...ScrollSpyRegionTypes,
}

function MenuList({ menu, active }) {
    return menu.map(({ title, id, menu }) => (
        <MenuItem
            title={title}
            id={id}
            menu={menu}
            active={active}
        />
    ))
}

MenuList.propTypes = ScrollSpyRegionTypes

function MenuItem({ id, title, menu = [], active }) {
    if (menu.length > 0) {
        return (
            <DropdownMenuItem
                id={id}
                title={title}
                menu={menu}
                active={active}
            />
        )
    }

    return (
        <Title
            id={id}
            title={title}
            className={classNames(
                'n2o-scroll-spy-region__menu-item',
                {
                    active: id === active,
                },
            )}
        />
    )
}

MenuItem.propTypes = ScrollSpyRegionTypes

function DropdownMenuItem({ title, menu, active }) {
    return (
        <div className="d-flex n2o-scroll-spy-region__dropdown-menu-item">
            <i className="fa fa-arrow-circle-down n2o-scroll-spy-region__dropdown-icon" />
            <div className="n2o-scroll-spy-region__dropdown-menu-items-wrapper">
                <Title title={title} className="n2o-scroll-spy-region__dropdown-menu-title" />
                <Menu menu={menu} active={active} />
            </div>
        </div>
    )
}

DropdownMenuItem.propTypes = ScrollSpyRegionTypes
