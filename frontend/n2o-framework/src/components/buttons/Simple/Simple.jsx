import React from 'react'
import PropTypes from 'prop-types'
import Button from 'reactstrap/lib/Button'
import Badge from 'reactstrap/lib/Badge'
import cn from 'classnames'

import Icon from '../../snippets/Icon/Icon'

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
        })}
        {...rest}
    >
        {icon && <Icon name={icon} />}
        {children || label}
        {count ? <Badge color="secondary">{count}</Badge> : ''}
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
}

SimpleButton.defaultProps = {
    tag: 'button',
    rounded: false,
    visible: true,
    onClick: () => {},
}

export default SimpleButton
