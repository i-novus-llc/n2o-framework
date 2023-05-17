import React from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import { Button } from 'reactstrap'
import cn from 'classnames'

import { Icon } from '../../snippets/Icon/Icon'
import { Badge } from '../../snippets/Badge/Badge'
import { isBadgeLeftPosition, isBadgeRightPosition } from '../../snippets/Badge/utils'
import { dataSourceLoadingSelector } from '../../../ducks/datasource/selectors'

const convertCounter = count => (count > 100 ? '99+' : count)

const SimpleButtonBody = ({
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
    dataSourceIsLoading,
    ...rest
}) => {
    const { text, position } = badge || {}

    const badgeStyle = {
        marginLeft: isBadgeRightPosition(position) && 8,
        marginRight: isBadgeLeftPosition(position) && 8,
    }

    const needBadge = !isEmpty(badge) || typeof count === 'number'
    const currentDisabled = dataSourceIsLoading || disabled

    return visible ? (
        <div ref={tooltipTriggerRef}>
            <Button
                id={id}
                tag={tag}
                size={size}
                color={color}
                outline={outline}
                onClick={onClick}
                disabled={currentDisabled}
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
                {(children || label) && <span>{children || label}</span>}
                {needBadge && (
                    <Badge
                        {...badge}
                        text={text || convertCounter(count)}
                        hasMargin={false}
                        color={badge?.color || 'primary'}
                        style={badgeStyle}
                        className="n2o-btn-badge"
                    />
                )}
            </Button>
        </div>
    ) : null
}

SimpleButtonBody.propTypes = {
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
    dataSourceIsLoading: PropTypes.func,
}

SimpleButtonBody.defaultProps = {
    tag: 'button',
    rounded: false,
    visible: true,
    onClick: () => {},
}

const mapStateToProps = (state, { datasource }) => ({
    dataSourceIsLoading: datasource ? dataSourceLoadingSelector(datasource)(state) : false,
})

export { SimpleButtonBody }

export default connect(mapStateToProps)(SimpleButtonBody)
export const SimpleButton = connect(mapStateToProps)(SimpleButtonBody)
