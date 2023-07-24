import React, { ComponentType } from 'react'

type WithRightPlaceholderProps = {
    measure?: string,
}

export const withRightPlaceholder =
    <Props extends object = Record<string, unknown>>(WrappedComponent: ComponentType<Props>) => {
        const displayName = WrappedComponent.displayName || WrappedComponent.name || 'Component'
        const WrappedComponentWithRightPlaceholder = ({ measure = '', ...props }: Props & WithRightPlaceholderProps) => (
            <div className="n2o-control-container">
                <WrappedComponent {...(props as Props)} />
                {measure && <div className="n2o-control-container-placeholder">{measure}</div>}
            </div>
        )

        WrappedComponentWithRightPlaceholder.displayName = `withRightPlaceholder(${displayName})`

        return WrappedComponentWithRightPlaceholder
    }
