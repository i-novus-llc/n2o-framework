import React from 'react'
import PropTypes from 'prop-types'
import { Button, Badge } from 'reactstrap'
import cn from 'classnames'

import { Icon } from '../../snippets/Icon/Icon'

const convertCounter = count => (count > 100 ? '99+' : count)

const renderEntity = (badge, count, badgeColor) => {
    if (badge || (count || count === 0)) {
        return (
            <Badge
                className={cn({
                    'badge-counter': count,
                })}
                color={badgeColor}
            >
                {badge || convertCounter(count)}
            </Badge>
        )
    }

    return null
}

const SimpleButton = ({
    id,
    label,
    icon,
    size,
    color,
    outline,
    visible,
    disabled,
    count,
    children,
    tag,
    onClick,
    rounded,
    className,
    badge,
    badgeColor,
    ...rest
}) => (visible ? (
    <Button
        id={id}
        tag={tag}
        size={size}
        color={color}
        outline={outline}
        onClick={onClick}
        disabled={disabled}
        className={cn(className, {
            'btn-rounded': rounded && !label,
            'btn-disabled': disabled,
            'btn-rounded__with-content': rounded && label,
            'btn-with-entity': badge || (count || count === 0),
        })}
        {...rest}
    >
        {icon && <Icon name={icon} />}
        {children || label}
        {renderEntity(badge, count, badgeColor)}
    </Button>
) : null)

SimpleButton.propTypes = {
    id: PropTypes.string,
    tag: PropTypes.oneOfType([
        PropTypes.func,
        PropTypes.string,
        PropTypes.shape({ $$typeof: PropTypes.symbol, render: PropTypes.func }),
        PropTypes.arrayOf(
            PropTypes.oneOfType([
                PropTypes.func,
                PropTypes.string,
                PropTypes.shape({ $$typeof: PropTypes.symbol, render: PropTypes.func }),
            ]),
        ),
    ]),
    label: PropTypes.string,
    icon: PropTypes.string,
    size: PropTypes.string,
    color: PropTypes.string,
    outline: PropTypes.bool,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
    count: PropTypes.string,
    onClick: PropTypes.func,
    rounded: PropTypes.bool,
    className: PropTypes.string,
    children: PropTypes.node,
    badge: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
    badgeColor: PropTypes.string,
}

SimpleButton.defaultProps = {
    tag: 'button',
    badgeColor: 'primary',
    rounded: false,
    visible: true,
    onClick: () => {},
}

export default SimpleButton
