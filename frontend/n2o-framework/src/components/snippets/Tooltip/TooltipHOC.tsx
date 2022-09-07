import * as React from 'react'
import { useContext, ComponentType } from 'react'
import { usePopperTooltip, Config } from 'react-popper-tooltip'

import { FactoryContext } from '../../../core/factory/context'

interface ITooltipHocProps extends Config {
    hint?: string | number | React.Component
    trigger?: 'click' | 'double-click' | 'right-click' | 'hover' | 'focus',
    /**
     * if this option is enabled, TooltipHOC will give the incoming component tooltipTriggerRef
     * @default false
     */
    isControlledTooltip?: boolean
    className?: string,
    setTriggerRef?: React.Dispatch<React.SetStateAction<HTMLElement | null>>
}

// eslint-disable-next-line @typescript-eslint/ban-types
export function TooltipHOC<TProps extends ITooltipHocProps>(Component: Function): ComponentType<TProps> {
    return function WithTooltip(props: TProps) {
        const { getComponent } = useContext(FactoryContext)
        const FactoryTooltip = getComponent('Tooltip', 'SNIPPETS')

        const { hint, isControlledTooltip = false } = props
        const {
            getArrowProps,
            getTooltipProps,
            setTooltipRef,
            setTriggerRef,
            visible,
        } = usePopperTooltip({ ...props, trigger: 'click' })

        if (!hint || !FactoryTooltip) {
            return <Component {...props} />
        }

        const tooltipProps = {
            setTooltipRef,
            getTooltipProps,
            getArrowProps,
            ...props,
        }

        if (isControlledTooltip) {
            return (
                <>
                    <Component {...props} tooltipTriggerRef={setTriggerRef} />
                    {visible && (
                        <FactoryTooltip {...tooltipProps} />
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
                    <FactoryTooltip {...tooltipProps} />
                )}
            </>
        )
    }
}

function Expandable({ Component, ...props }: {Component: ComponentType}): JSX.Element {
    return <Component {...props} />
}

/**
 * Wrapper with TooltipHOC
 * @example
 *  <ExtendedTooltipComponent
       Component={TargetComponent}
       {...targetComponentProps}
       hint='target component hint'
       placement='top'
    />
 */
export const ExtendedTooltipComponent = TooltipHOC(Expandable)
