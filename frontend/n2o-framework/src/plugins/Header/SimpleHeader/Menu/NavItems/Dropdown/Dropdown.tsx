import React, { useCallback, useState, MouseEvent } from 'react'
import {
    ButtonDropdownProps,
    Dropdown as DropdownParent,
    DropdownMenu,
    DropdownItem as DropdownChildren,
    DropdownToggle,
} from 'reactstrap'
import classNames from 'classnames'

import { LinkBody } from '../Links/LinkBody'
import { type Common, type Item as ItemProps } from '../../../../../CommonMenuTypes'
import { ITEM_SRC } from '../../../../../constants'
import { ICON_POSITIONS } from '../../../../../../components/snippets/IconContainer/IconContainer'
import NavItemContainer from '../../../NavItemContainer'
import { useLinkPropsResolver } from '../../../../../../components/navigation/useLinkPropsResolver'

export interface DropdownProps extends Common {
    items: ItemProps[]
    nested?: boolean
    direction?: ButtonDropdownProps['direction']
    level?: number
    iconPosition?: ICON_POSITIONS
}

export interface DropdownItemProps {
    active: boolean
    item: ItemProps
    className?: string
}

function DropdownItem({ active, item, className }: DropdownItemProps) {
    const {
        className: itemClassName,
        disabled,
        visible,
    } = useLinkPropsResolver({ ...item, url: item.href })

    if (!visible) { return null }

    const itemProps = { ...item, visible, enabled: !disabled }

    const handleClick = (e: MouseEvent) => {
        /* @INFO HACK для закрытия вложенных DropdownMenu */
        e.preventDefault()
    }

    return (
        <DropdownChildren
            active={active}
            disabled={item.src === ITEM_SRC.STATIC || item.disabled || disabled}
            onClick={handleClick}
        >
            <NavItemContainer
                itemProps={itemProps}
                className={classNames(className, itemClassName, 'dropdown-item')}
                active={active}
            />
        </DropdownChildren>
    )
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
    enabled,
    style,
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
            style={style}
            disabled={disabled || !enabled}
        >
            <DropdownToggle className={classNames({ 'dropdown-item': level > 0 })} disabled={disabled || !enabled} caret>
                <LinkBody
                    imageSrc={imageSrc}
                    icon={icon}
                    title={title}
                    imageShape={imageShape}
                    iconPosition={iconPosition}
                />
            </DropdownToggle>
            <DropdownMenu
                onClick={(event) => {
                    // HACK для закрытия вложенных DropdownMenu
                    if (event.isDefaultPrevented()) { setOpen(false) }
                }
            }
                flip
                className={classNames(`menu-level-${level}`, { nested })}
            >
                {items.map((item) => {
                    if (item.items) {
                        const { visible = true, enabled = true } = item
                        const itemProps = { ...item, enabled, visible }

                        return (
                            <NavItemContainer
                                itemProps={itemProps}
                                active={active}
                                className={classNames(className, 'dropdown-item')}
                                level={level + 1}
                                direction="right"
                                iconPosition={iconPosition}
                                nested
                            />
                        )
                    }

                    return <DropdownItem active={active} item={item} className={className} />
                })}
            </DropdownMenu>
        </DropdownParent>
    )
}
