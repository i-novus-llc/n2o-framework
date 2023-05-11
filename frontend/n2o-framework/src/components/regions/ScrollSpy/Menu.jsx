import React, { useState } from 'react'
import classNames from 'classnames'

import { Divider } from '../../snippets/Divider/Divider'

import { ScrollSpyTypes } from './ScrollSpyTypes'
import { Title } from './Title'
import { hasVisibleWidget } from './utils'

export function Menu({
    items,
    widgets,
    active,
    setActive,
    isOpen = true,
    nested = false,
}) {
    return (
        <section className={classNames('n2o-scroll-spy-region__menu-group', { nested })}>
            <div className={classNames('n2o-scroll-spy-region__menu-wrapper', { visible: isOpen, nested })}>
                <MenuList items={items} widgets={widgets} active={active} setActive={setActive} isOpen={isOpen} />
            </div>
        </section>
    )
}

Menu.propTypes = {
    ...ScrollSpyTypes,
}

function MenuList({ items, active, setActive, widgets }) {
    return items.map(({ title, id, menu: items, content, group = null, headline = false }) => {
        if (!group) {
            return (
                <MenuItem
                    key={id}
                    title={title}
                    id={id}
                    items={items}
                    active={active}
                    setActive={setActive}
                    content={content}
                    widgets={widgets}
                />
            )
        }

        const isGroupVisible = group.some(item => hasVisibleWidget(item, widgets))

        if (!isGroupVisible) {
            return null
        }

        return (
            <section className="n2o-scroll-spy-region__group-items">
                {headline && <Divider className="n2o-scroll-spy-region__group-items-divider" /> }
                <Title title={title} className="n2o-scroll-spy-region__group-items-title" />
                <MenuList items={group} active={active} setActive={setActive} widgets={widgets} />
            </section>
        )
    })
}

MenuList.propTypes = ScrollSpyTypes

function MenuItem({
    id,
    title,
    items = [],
    active,
    setActive,
    content,
    widgets,
}) {
    const isActive = id === active

    if (items.length > 0) {
        return (
            <DropdownMenuItem
                id={id}
                title={title}
                items={items}
                active={active}
                setActive={setActive}
                widgets={widgets}
                nested
            />
        )
    }

    const itemVisible = hasVisibleWidget(content, widgets)

    return (
        <Title
            id={id}
            title={title}
            className={classNames(
                'n2o-scroll-spy-region__menu-item',
                {
                    active: isActive,
                },
            )}
            onClick={() => setActive(id)}
            visible={itemVisible}
        />
    )
}

MenuItem.propTypes = ScrollSpyTypes

function DropdownMenuItem({ title, active, setActive, widgets, items = [], nested = false }) {
    const [isOpen, setOpen] = useState(false)

    const isDropdownVisible = items.some(item => hasVisibleWidget(item, widgets))

    if (!isDropdownVisible) {
        return false
    }

    const toggle = () => setOpen(!isOpen)

    const hasActive = items.some(({ id }) => id === active)

    return (
        <div className={classNames('d-flex n2o-scroll-spy-region__dropdown-menu-item', { nested })}>
            <div className="n2o-scroll-spy-region__dropdown-menu-items-wrapper">
                <section className={classNames('n2o-scroll-spy-region__dropdown-toggle', { hasActive })} onClick={toggle}>
                    <Title title={title} className="n2o-scroll-spy-region__dropdown-menu-title" />
                    <i className={classNames('n2o-scroll-spy-region__dropdown-icon', { 'fa fa-chevron-up': isOpen, 'fa fa-chevron-down': !isOpen })} />
                </section>
                <Menu
                    items={items}
                    active={active}
                    setActive={setActive}
                    isOpen={isOpen}
                    nested={nested}
                    widgets={widgets}
                />
            </div>
        </div>
    )
}

DropdownMenuItem.propTypes = ScrollSpyTypes
