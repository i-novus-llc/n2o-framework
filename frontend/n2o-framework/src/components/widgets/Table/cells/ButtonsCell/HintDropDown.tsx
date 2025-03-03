import React, { useState } from 'react'
import defaultTo from 'lodash/defaultTo'
import { Button, UncontrolledTooltip } from 'reactstrap'
import { Manager, Reference } from 'react-popper'
import { DropdownCustomItem } from '@i-novus/n2o-components/lib/display/DropdownCustomItem'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'
import { Action } from 'redux'

import { MODIFIERS, withUID } from './helpers'
import { HintDropDownBody } from './HintDropDownBody'
import { type HintDropdownProps, MenuItem } from './types'

function HintDropDown({
    uId,
    title,
    hint,
    icon,
    onClick,
    hintPosition,
    model,
    direction,
    active,
    menu = [],
    modifiers = MODIFIERS as never,
    disabled = false,
    placement = 'top',
    size = 'sm',
    color = 'link',
    delay = 100,
    hideArrow = false,
    offset = 0,
    positionFixed = true,
    visible = true,
    resolveWidget = () => {},
}: HintDropdownProps) {
    const [open, setOpen] = useState(false)

    if (!visible) { return null }

    const tooltipProps = {
        delay,
        hideArrow,
        offset,
    }

    const dropdownProps = {
        disabled,
        direction,
        active,
        color,
        size,
    }
    const onToggleDropdown = (e?: MouseEvent) => {
        if (e) { e.stopPropagation() }
        resolveWidget(model)
        setOpen(!open)
    }

    const createDropDownMenu = ({ title, visible, icon, action, color, ...rest }: MenuItem) => {
        const handleClick = (action: Action) => (e: MouseEvent) => {
            e.stopPropagation()
            onClick(e, action)

            onToggleDropdown()
        }

        return (defaultTo(visible, true) ? (
            <DropdownCustomItem
                color={color}
                onClick={handleClick(action)}
                {...rest}
            >
                {icon && <Icon name={icon} />}
                {title}
            </DropdownCustomItem>
        ) : null)
    }

    return (
        <Manager>
            {hint && (
                <UncontrolledTooltip
                    target={uId}
                    modifiers={MODIFIERS}
                    placement={hintPosition}
                    {...tooltipProps}
                >
                    {hint}
                </UncontrolledTooltip>
            )}
            <Reference>
                {({ ref }) => (
                    <Button
                        id={uId}
                        className="n2o-buttons-cell-toggler btn btn-link border-0 bg-transparent"
                        innerRef={ref}
                        onClick={onToggleDropdown as never}
                        {...dropdownProps}
                    >
                        {icon && <Icon name={icon} />}
                        {title}
                        <Icon className="n2o-dropdown-icon" name="fa fa-angle-down" />
                    </Button>
                )}
            </Reference>
            <HintDropDownBody
                modifiers={modifiers}
                positionFixed={positionFixed}
                menu={menu}
                onToggleDropdown={onToggleDropdown}
                createDropDownMenu={createDropDownMenu}
                open={open}
                placement={placement}
            />
        </Manager>
    )
}

export default withUID(HintDropDown as never)
