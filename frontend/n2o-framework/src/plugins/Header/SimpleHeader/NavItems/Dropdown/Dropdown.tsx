import React, { useCallback, useState } from 'react'
import {
    Dropdown as DropdownParent,
    DropdownMenu,
    DropdownItem,
    ButtonDropdownProps,
    DropdownToggle,
} from 'reactstrap'
import classNames from 'classnames'

import { IItem } from '../../../../utils'
import { Link } from '../Links/Link'
import { LinkBody } from '../Links/LinkBody'

interface IDropdown {
    items: IItem[]
    active: boolean
    title: string
    className?: string
    nested?: boolean
    icon?: string
    imageSrc?: string
    imageShape?: string
    direction?: ButtonDropdownProps['direction']
    recursiveClose?: boolean
    onItemClick?(): void
    level?: number
}

export function Dropdown(props: IDropdown) {
    const [isOpen, setOpen] = useState(false)

    const toggle = useCallback(() => setOpen(!isOpen), [isOpen])
    const forceParentClose = useCallback(() => setOpen(false), [setOpen])

    const {
        items,
        active,
        title,
        className,
        nested,
        icon,
        imageSrc,
        imageShape = 'square',
        direction = 'down',
        recursiveClose = true,
        onItemClick = () => {},
        level = 0,
    } = props

    return (
        <DropdownParent
            className={classNames('dropdown', className, `dropdown-level-${level}`, { isOpen })}
            isOpen={isOpen}
            toggle={toggle}
            direction={direction}
            tag="li"
        >
            <DropdownToggle caret>
                <LinkBody imageSrc={imageSrc} icon={icon} title={title} imageShape={imageShape} />
            </DropdownToggle>
            <DropdownMenu flip className={classNames(`menu-level-${level}`, { nested })}>
                {items.map((item) => {
                    const { items: nestedItems, title } = item

                    if (nestedItems) {
                        const onItemClick = () => {
                            if (recursiveClose) {
                                forceParentClose()
                            }
                        }

                        return (
                            <Dropdown
                                items={nestedItems}
                                active={active}
                                title={title}
                                className={className}
                                level={level + 1}
                                recursiveClose={recursiveClose}
                                onItemClick={onItemClick}
                                nested
                            />
                        )
                    }

                    return (
                        <DropdownItem onClick={onItemClick}>
                            <Link item={item} active={active} className="dropdown-item" />
                        </DropdownItem>
                    )
                })
                }
            </DropdownMenu>
        </DropdownParent>
    )
}
