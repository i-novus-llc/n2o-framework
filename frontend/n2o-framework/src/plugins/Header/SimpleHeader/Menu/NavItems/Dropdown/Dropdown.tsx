import React, { useCallback, useContext, useState } from 'react'
import {
    Dropdown as DropdownParent,
    DropdownMenu,
    DropdownItem,
    DropdownToggle,
} from 'reactstrap'
import classNames from 'classnames'

import { LinkBody } from '../Links/LinkBody'
import { Dropdown as DropdownProps } from '../../Item'
import { FactoryComponent } from '../../../../../CommonMenuTypes'
import { ITEM_SRC } from '../../../../../constants'
import { FactoryContext } from '../../../../../../core/factory/context'
import { FactoryLevels } from '../../../../../../core/factory/factoryLevels'
import { ICON_POSITIONS } from '../../../../../../components/snippets/IconContainer/IconContainer'

export function Dropdown(props: DropdownProps) {
    const [isOpen, setOpen] = useState(false)

    const toggle = useCallback(() => setOpen(!isOpen), [isOpen])
    const forceParentClose = useCallback(() => setOpen(false), [setOpen])
    const { getComponent } = useContext(FactoryContext)

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
        iconPosition = ICON_POSITIONS.LEFT,
    } = props

    if (!items.length) {
        return null
    }

    return (
        <DropdownParent
            className={classNames('dropdown', className, `dropdown-level-${level}`, { isOpen })}
            isOpen={isOpen}
            toggle={toggle}
            direction={direction}
            tag="li"
        >
            <DropdownToggle className={classNames({ 'dropdown-item': level > 0 })} caret>
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
                    const { items: nestedItems, title, src } = item

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
                                className={classNames(className, 'dropdown-item')}
                                level={level + 1}
                                recursiveClose={recursiveClose}
                                onItemClick={onItemClick}
                                direction="right"
                                iconPosition={iconPosition}
                                nested
                            />
                        )
                    }

                    const FactoryComponent: FactoryComponent = getComponent(src, FactoryLevels.HEADER_ITEMS)

                    if (!FactoryComponent) {
                        return null
                    }

                    return (
                        <DropdownItem active={active} onClick={onItemClick} disabled={src === ITEM_SRC.STATIC}>
                            <FactoryComponent
                                item={item}
                                from="HEADER"
                                className={classNames('dropdown-item', className)}
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
