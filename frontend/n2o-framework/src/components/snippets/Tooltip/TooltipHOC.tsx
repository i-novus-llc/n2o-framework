import React, { useContext } from 'react'
import { usePopperTooltip, Config } from 'react-popper-tooltip'

import { FactoryContext } from '../../../core/factory/context'

interface ITooltipHocProps extends Config {
    hint?: string | number | React.Component
    trigger?: 'click' | 'double-click' | 'right-click' | 'hover' | 'focus'
    isControlledTooltip?: boolean
    className?: string
}
function Tooltip<TProps extends ITooltipHocProps>({ ...props }: TProps): React.ReactElement {
    const { getComponent } = useContext(FactoryContext)
    const FactoryTooltip = getComponent('Tooltip', 'SNIPPETS')

    return <FactoryTooltip {...props} />
}

export function TooltipHOC<TProps extends ITooltipHocProps>(
    Component: React.ComponentType<TProps> | React.FunctionComponent<TProps>,
): React.ReactNode {
    return function WithTooltip(props: TProps) {
        const { hint, isControlledTooltip = false } = props

        const {
            getArrowProps,
            getTooltipProps,
            setTooltipRef,
            setTriggerRef,
            visible,
        } = usePopperTooltip({ ...props })

        if (!hint) {
            return <Component {...props} />
        }

        const tooltipProps = {
            setTooltipRef,
            getTooltipProps,
            getArrowProps,
        }

        if (isControlledTooltip) {
            return (
                <>
                    <Component {...props} tooltipTriggerRef={setTriggerRef} />
                    {visible && (
                        <Tooltip {...tooltipProps} {...props} />
                    )}
                </>
            )
        }

        return (
            <>
                <div ref={setTriggerRef}>
                    <Component {...props} />
                </div>
                {visible && (
                    <Tooltip {...tooltipProps} {...props} />
                )}
            </>
        )
    }
}

type component = React.ReactElement | React.ReactNode | React.FunctionComponent

function Expandable({ Component, ...props }: {Component: component}): React.ReactElement {
    return <Component {...props} />
}

export const ExtendedTooltipComponent = TooltipHOC(Expandable)
