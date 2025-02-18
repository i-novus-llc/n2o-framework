import React from 'react'
import pick from 'lodash/pick'
import { Button, UncontrolledTooltip } from 'reactstrap'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import { MODIFIERS, withUID } from './helpers'
import { type HintButtonProps } from './types'

const HintButton = ({
    uId,
    title,
    hint,
    icon,
    onClick,
    action,
    visible = true,
    hintPosition = 'top',
    size = 'sm',
    active,
    color = 'link',
    disabled = false,
    delay = 100,
    hideArrow = false,
    offset = 0,
}: HintButtonProps) => {
    if (!visible) { return null }

    const otherBtnProps = pick(
        { size, active, color, disabled },
        ['size', 'active', 'color', 'disabled'],
    )

    const otherTooltipProps = pick(
        { delay, hideArrow, offset },
        ['delay', 'hideArrow', 'offset'],
    )

    const handleClick = (action: HintButtonProps['action']) => (e: React.MouseEvent<HTMLButtonElement>) => {
        e.stopPropagation()
        onClick(e.nativeEvent as MouseEvent, action)
    }

    return (
        <>
            <Button id={uId} onClick={handleClick(action)} {...otherBtnProps}>
                {icon && <Icon name={icon} />}
                {title}
            </Button>
            {hint && (
                <UncontrolledTooltip
                    target={uId}
                    {...otherTooltipProps}
                    modifiers={MODIFIERS}
                    placement={hintPosition}
                >
                    {hint}
                </UncontrolledTooltip>
            )}
        </>
    )
}

export default withUID(HintButton)
