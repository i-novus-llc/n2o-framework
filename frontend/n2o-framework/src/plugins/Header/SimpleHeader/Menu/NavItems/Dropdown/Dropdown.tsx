import React, { useCallback, useState } from 'react'
import {
    ButtonDropdownProps,
    Dropdown as DropdownParent,
    DropdownMenu,
    DropdownItem,
    DropdownToggle,
} from 'reactstrap'
import classNames from 'classnames'

import { LinkBody } from '../Links/LinkBody'
import { Common, Item as ItemProps } from '../../../../../CommonMenuTypes'
import { ITEM_SRC } from '../../../../../constants'
import { ICON_POSITIONS } from '../../../../../../components/snippets/IconContainer/IconContainer'
import NavItemContainer from '../../../NavItemContainer'

export type DropdownProps = Common & {
    items: ItemProps[]
    nested?: boolean
    direction?: ButtonDropdownProps['direction']
    level?: number
    iconPosition?: ICON_POSITIONS
}

export function Dropdown({
    items,
    active,
    title,
    className,
    nested,
    icon,
    imageSrc,
    imageShape = 'square',
    direction = 'down',
    level = 0,
    iconPosition = ICON_POSITIONS.LEFT,
    disabled,
}: DropdownProps) {
    const [isOpen, setOpen] = useState(false)

    const toggle = useCallback(() => setOpen(!isOpen), [isOpen])

    if (!items.length) { return null }

    return (
        <DropdownParent
            className={classNames('dropdown', className, `dropdown-level-${level}`, { isOpen })}
            isOpen={isOpen}
            toggle={toggle}
            direction={direction}
            tag="li"
        >
            <DropdownToggle className={classNames({ 'dropdown-item': level > 0 })} disabled={disabled} caret>
                <LinkBody
                    imageSrc={imageSrc}
                    icon={icon}
                    title={title}
                    imageShape={imageShape}
                    iconPosition={iconPosition}
                />
            </DropdownToggle>
            <DropdownMenu flip className={classNames(`menu-level-${level}`, { nested })}>
                {items.map((item) => {
                    if (item.items) {
                        return (
                            <NavItemContainer
                                itemProps={item}
                                active={active}
                                className={classNames(className, 'dropdown-item')}
                                level={level + 1}
                                direction="right"
                                iconPosition={iconPosition}
                                nested
                            />
                        )
                    }

                    return (
                        <DropdownItem
                            active={active}
                            disabled={item.src === ITEM_SRC.STATIC || item.disabled}
                        >
                            <NavItemContainer
                                itemProps={item}
                                className={classNames(className, 'dropdown-item')}
                                active={active}
                            />
                        </DropdownItem>
                    )
                })
                }
            </DropdownMenu>
        </DropdownParent>
    )
}
