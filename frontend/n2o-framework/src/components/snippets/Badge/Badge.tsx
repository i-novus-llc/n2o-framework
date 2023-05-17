import React, { CSSProperties, ReactNode } from 'react'
import classNames from 'classnames'
import { Badge as ReactstrapBadge } from 'reactstrap'

import { Position, Shape } from './enums'
import { isBadgeLeftPosition, isBadgeRightPosition } from './utils'

export type IBadgeProps = Partial<{
    children: ReactNode;
    text: string | number | ReactNode;
    color: string;
    position: Position;
    shape: Shape;
    image: string;
    imagePosition: Position;
    imageShape: Shape;
    hasMargin: boolean;
    className: string;
    style: CSSProperties;
    visible?: boolean;
}>

export const Badge = React.memo((props: IBadgeProps) => {
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

    const BadgeComponent = () => (
        <ReactstrapBadge
            color={color}
            className={badgeClassNames}
            style={style}
        >
            {image && (
                <img
                    src={image}
                    alt="not found"
                    className={badgeImageClassNames}
                />
            )}
            {text}
        </ReactstrapBadge>
    )

    if (!visible && children) {
        return <>{children}</>
    }

    if (!children) {
        return <BadgeComponent />
    }

    return (
        <div className={badgeContainerClassNames}>
            {children}
            <BadgeComponent />
        </div>
    )
})
