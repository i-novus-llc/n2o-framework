import React from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import { Button } from 'reactstrap'
import cn from 'classnames'

import { Icon } from '../../snippets/Icon/Icon'
import { Badge } from '../../snippets/Badge/Badge'
import { isBadgeLeftPosition, isBadgeRightPosition } from '../../snippets/Badge/utils'

const convertCounter = count => (count > 100 ? '99+' : count)

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
    tooltipTriggerRef,
    ...rest
}) => {
    const { text, position } = badge || {}

    const badgeStyle = {
        marginLeft: isBadgeRightPosition(position) && 8,
        marginRight: isBadgeLeftPosition(position) && 8,
    }

    const needBadge = !isEmpty(badge) || typeof count === 'number'

    return visible ? (
        <div ref={tooltipTriggerRef}>
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
                'with-badge': badge && (text || typeof count === 'number'),
                'with-label': label,
                'with-icon': icon,
            })}
            {...rest}
        >
            {icon && <Icon name={icon} className="n2o-btn-icon" />}
            {needBadge && (
                <Badge
                    {...badge}
                    text={text || convertCounter(count)}
                    hasMargin={false}
                    style={badgeStyle}
                    className="n2o-btn-badge"
                >
                    <span>{children || label}</span>
                </Badge>
            )}
        </Button>
        </div>
    ) : null
}

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
    badge: PropTypes.object,
    tooltipTriggerRef: PropTypes.func,
}

SimpleButton.defaultProps = {
    tag: 'button',
    badge: {
        color: 'primary',
    },
    rounded: false,
    visible: true,
    onClick: () => {},
}

export default SimpleButton
