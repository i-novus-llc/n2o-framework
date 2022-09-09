import * as React from 'react'
import { ComponentType, useContext } from 'react'
import { Config, usePopperTooltip } from 'react-popper-tooltip'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'

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
        const FactoryTooltip = getComponent('Tooltip', FactoryLevels.SNIPPETS)

        const { hint, isControlledTooltip = false } = props
        const {
            getArrowProps,
            getTooltipProps,
            setTooltipRef,
            setTriggerRef,
            visible,
        } = usePopperTooltip({ ...props })

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

interface IExpandableProps {
    Component?: ComponentType,
    children?: React.ReactChildren
}

type expandable = React.ReactChildren | JSX.Element | null

function Expandable({
    Component,
    children,
    ...props
}: IExpandableProps): expandable {
    if (children) {
        return children
    }

    if (Component) {
        return <Component {...props} />
    }

    return null
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
 * @example2
 *  <ExtendedTooltipComponent
       {...targetComponentProps}
       hint='target component hint'
       placement='top'
     >
 <TargetComponent />
 </ExtendedTooltipComponent>
 */
export const ExtendedTooltipComponent = TooltipHOC(Expandable)
