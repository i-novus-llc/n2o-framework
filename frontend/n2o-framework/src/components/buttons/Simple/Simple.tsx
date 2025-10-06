import React, { ComponentType, useContext } from 'react'
import { connect } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'
import { Button } from '@i-novus/n2o-components/lib/button/Button'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'
import { ROOT_CLASS_NAME_PARAM } from '@i-novus/n2o-components/lib/navigation/helpers'

import { dataSourceLoadingSelector } from '../../../ducks/datasource/selectors'
import { Position } from '../../snippets/Badge/enums'
import { IconContainer, ICON_POSITIONS } from '../../snippets/IconContainer/IconContainer'
import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { State } from '../../../ducks/State'
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

const Component = ({
    id,
    label,
    icon,
    disabled,
    count,
    children,
    className,
    [ROOT_CLASS_NAME_PARAM]: rootClassName,
    badge,
    dataSourceIsLoading,
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
    const FactoryBadge = getComponent<ComponentType<Props['badge']>>(
        'Badge',
        FactoryLevels.SNIPPETS,
    )

    if (!visible) { return null }

    const { text, position = Position.Right } = badge || {}

    const showBadge = FactoryBadge && (!isEmpty(badge) || typeof count === 'number')

    return (
        <div ref={forwardedRef} className={rootClassName}>
            <Button
                id={id}
                tag={tag}
                onClick={onClick}
                disabled={dataSourceIsLoading || disabled}
                rounded={rounded && !label}
                className={classNames(className, {
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
                {showBadge && (
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

Component.displayName = 'SimpleButtonComponent'

const mapStateToProps = (state: State, { datasource }: Props) => ({
    dataSourceIsLoading: datasource ? dataSourceLoadingSelector(datasource)(state) : false,
})

export const SimpleButton = connect(mapStateToProps)(Component)
export default SimpleButton

SimpleButton.displayName = 'SimpleButton'
