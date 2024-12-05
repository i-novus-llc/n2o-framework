import React, { CSSProperties, ReactNode } from 'react'
import classNames from 'classnames'
import { Badge as ReactstrapBadge } from 'reactstrap'

import { TBaseProps } from '../../types'

import { Position, Shape } from './enums'
import { isBadgeLeftPosition, isBadgeRightPosition } from './utils'

export type Props = TBaseProps & Partial<{
    children: ReactNode
    color: string
    hasMargin: boolean
    image: string
    imagePosition: Position
    imageShape: Shape
    position: Position | string
    shape: Shape
    style: CSSProperties
    text: string | number | ReactNode
}>

export const Badge = React.memo((props: Props) => {
    const {
        children,
        text,
        color = 'light',
        position = Position.Right,
        shape = Shape.Circle,
        image,
        imagePosition = Position.Left,
        imageShape = Shape.Circle,
        hasMargin = true,
        className,
        style,
        visible = true,
    } = props

    const isBadgeSquare = shape === Shape.Square

    const badgeContainerClassNames = classNames('n2o-badge-container', {
        'flex-row-reverse': isBadgeLeftPosition(position),
    })

    const badgeClassNames = classNames('n2o-badge', className, {
        [isBadgeRightPosition(position) ? 'ml-1' : 'mr-1']: !isBadgeSquare && hasMargin,
        'rounded-pill': shape === Shape.Rounded || shape === Shape.Circle,
        [`n2o-badge--${shape}`]: shape,
        'with-image': image,
    })

    const badgeImageClassNames = classNames('n2o-badge-image', {
        [`n2o-badge-image--${imagePosition}`]: imagePosition,
        [`n2o-badge-image--${imageShape}`]: imageShape,
        'rounded-pill': imageShape === Shape.Circle || imageShape === Shape.Rounded,
    })

    // eslint-disable-next-line react/no-unstable-nested-components
    const BadgeComponent = () => (
        <ReactstrapBadge
            color={color}
            className={badgeClassNames}
            style={style}
        >
            {image && (
                <img
                    src={image}
                    alt="badge"
                    className={badgeImageClassNames}
                />
            )}
            {text}
        </ReactstrapBadge>
    )

    // eslint-disable-next-line react/jsx-no-useless-fragment
    if (!visible && children) { return <>{children}</> }

    if (!children) { return <BadgeComponent /> }

    return (
        <div className={badgeContainerClassNames}>
            {children}
            <BadgeComponent />
        </div>
    )
})
