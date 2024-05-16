import React, { useContext } from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import { Button } from 'reactstrap'
import classNames from 'classnames'

import { Icon } from '../../snippets/Icon/Icon'
import { dataSourceLoadingSelector } from '../../../ducks/datasource/selectors'
import { Position } from '../../snippets/Badge/enums'
import { IconContainer, ICON_POSITIONS } from '../../snippets/IconContainer/IconContainer'
import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'

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
    dataSourceIsLoading,
    iconPosition,
    forwardedRef,
    ...rest
}) => {
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
                disabled={dataSourceIsLoading || disabled}
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
                {...rest}
            >
                <IconContainer icon={icon} iconPosition={iconPosition}>
                    {icon && <Icon name={icon} className="n2o-btn-icon" />}
                    {children || label}
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
    iconPosition: PropTypes.string,
}

SimpleButtonBody.displayName = 'SimpleButtonBody'
SimpleButtonBody.defaultProps = {
    tag: 'button',
    rounded: false,
    visible: true,
    iconPosition: ICON_POSITIONS.LEFT,
    onClick: () => {},
}

const mapStateToProps = (state, { datasource }) => ({
    dataSourceIsLoading: datasource ? dataSourceLoadingSelector(datasource)(state) : false,
})

export { SimpleButtonBody }

export default connect(mapStateToProps)(SimpleButtonBody)
export const SimpleButton = connect(mapStateToProps)(SimpleButtonBody)
