import React, { useState } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { ScrollSpyTypes } from './ScrollSpyTypes'
import { Title } from './Title'

export function Menu({ menu: items, title, active, setActive, isOpen = true }) {
    return (
        <section className="n2o-scroll-spy-region__menu-group">
            <div className={classNames('n2o-scroll-spy-region__menu-wrapper', { visible: isOpen })}>
                <Title
                    title={title}
                    className="n2o-scroll-spy-region__menu-title"
                />
                <MenuList items={items} active={active} setActive={setActive} isOpen={isOpen} />
            </div>
        </section>
    )
}

Menu.propTypes = {
    title: PropTypes.string,
    ...ScrollSpyTypes,
}

function MenuList({ items, active, setActive }) {
    return items.map(({ title, id, menu }) => (
        <MenuItem
            key={id}
            title={title}
            id={id}
            menu={menu}
            active={active}
            setActive={setActive}
        />
    ))
}

MenuList.propTypes = ScrollSpyTypes

function MenuItem({ id, title, menu = [], active, setActive }) {
    if (menu.length > 0) {
        return (
            <DropdownMenuItem
                id={id}
                title={title}
                menu={menu}
                active={active}
                setActive={setActive}
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
            onClick={() => setActive(id)}
        />
    )
}

MenuItem.propTypes = ScrollSpyTypes

function DropdownMenuItem({ title, menu, active, setActive }) {
    const [isOpen, setOpen] = useState(true)

    const toggle = () => setOpen(!isOpen)

    return (
        <div className="d-flex n2o-scroll-spy-region__dropdown-menu-item">
            <div className="n2o-scroll-spy-region__dropdown-menu-items-wrapper">
                <section className="n2o-scroll-spy-region__dropdown-toggle" onClick={toggle}>
                    <i className={classNames('n2o-scroll-spy-region__dropdown-icon', { 'fa fa-sort-asc up': isOpen, 'fa fa-sort-desc down': !isOpen })} />
                    <Title title={title} className="n2o-scroll-spy-region__dropdown-menu-title" />
                </section>
                <Menu menu={menu} active={active} setActive={setActive} isOpen={isOpen} />
            </div>
        </div>
    )
}

DropdownMenuItem.propTypes = ScrollSpyTypes
