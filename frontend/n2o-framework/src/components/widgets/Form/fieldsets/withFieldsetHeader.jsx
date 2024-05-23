import React from 'react'
import classNames from 'classnames'

import { useResolved } from '../../../../core/Expression/useResolver'

import { FieldsetHeader } from './FieldsetHeader'

export function withFieldsetHeader(Component) {
    function WithFieldsetHeaderComponent(props) {
        const {
            className,
            style,
            needLabel,
            needDescription,
            description,
            label,
            help,
            type,
            childrenLabel,
            enabled,
            activeModel,
            render,
            visible,
            badge: badgeProps,
            ...rest
        } = props

        const badge = useResolved(badgeProps, activeModel)

        return (
            <div className={classNames(className, { 'd-none': visible === false })} style={style}>
                <FieldsetHeader
                    visible={type !== 'line' && (needLabel || needDescription || badge)}
                    label={label}
                    needLabel={needLabel}
                    description={description}
                    needDescription={needDescription}
                    badge={badge}
                    help={help}
                />
                <Component
                    childrenLabel={childrenLabel}
                    enabled={enabled}
                    label={label}
                    type={type}
                    activeModel={activeModel}
                    description={description}
                    badge={badge}
                    {...rest}
                    visible={visible}
                    render={render}
                    help={help}
                />
            </div>
        )
    }

    return WithFieldsetHeaderComponent
}
