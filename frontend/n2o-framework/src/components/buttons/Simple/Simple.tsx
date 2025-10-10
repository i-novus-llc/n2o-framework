import React, { useContext } from 'react'
import isEmpty from 'lodash/isEmpty'
import { Button } from 'reactstrap'
import classNames from 'classnames'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import { Position } from '../../snippets/Badge/enums'
import { IconContainer, ICON_POSITIONS } from '../../snippets/IconContainer/IconContainer'
import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { NOOP_FUNCTION } from '../../../utils/emptyTypes'

import { type Props } from './types'

const convertCounter = (count?: number) => {
    if (!count) { return '' }

    return count > 100 ? '99+' : count
}

export enum CLICK_EVENTS {
    ENTER = 'Enter',
    NUMPAD_ENTER = 'NumpadEnter',
}

export const SimpleButton = ({
    id,
    label,
    icon,
    size,
    color,
    outline,
    disabled,
    count,
    children,
    className,
    badge,
    forwardedRef,
    url,
    onKeyDown,
    visible = true,
    tag = 'button',
    onClick = NOOP_FUNCTION,
    rounded = false,
    iconPosition = ICON_POSITIONS.LEFT,
    ...rest
}: Props) => {
    const { getComponent } = useContext(FactoryContext)
    const FactoryBadge = getComponent('Badge', FactoryLevels.SNIPPETS)

    if (!visible) { return null }
    const { text, position = Position.Right } = badge || {}

    const needBadge = FactoryBadge && (!isEmpty(badge) || typeof count === 'number')

    return (
        <div ref={forwardedRef}>
            <Button
                id={id}
                tag={tag}
                size={size}
                color={color}
                outline={outline}
                onClick={onClick}
                disabled={disabled}
                className={classNames(className, {
                    'btn-rounded': rounded && !label,
                    'btn-disabled': disabled,
                    'btn-rounded__with-content': rounded && label,
                    'btn-with-entity': badge || (count || count === 0),
                    'with-badge': badge && (text || typeof count === 'number'),
                    'with-label': label,
                    'with-icon': icon,
                    [`btn-badge-position--${position}`]: position,
                })}
                onKeyDown={(event) => {
                    if (onKeyDown) {
                        onKeyDown(event)

                        return
                    }

                    if (event.code === CLICK_EVENTS.ENTER || event.code === CLICK_EVENTS.NUMPAD_ENTER) {
                        onClick(event as never)
                    }
                }}
                href={url}
                {...rest}
            >
                <IconContainer icon={icon} iconPosition={iconPosition}>
                    {icon && <Icon name={icon} className="n2o-btn-icon" />}
                    <Text>{children || label}</Text>
                </IconContainer>
                {needBadge && (
                    <FactoryBadge
                        {...badge}
                        text={text || convertCounter(count)}
                        hasMargin={false}
                        color={badge?.color || 'primary'}
                        className="n2o-btn-badge"
                    />
                )}
            </Button>
        </div>
    )
}

SimpleButton.displayName = 'SimpleButton'

export default SimpleButton
