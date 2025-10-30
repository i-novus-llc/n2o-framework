import React, { ComponentType, useContext, forwardRef, cloneElement } from 'react'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'

export interface TooltipHocProps {
    hint?: string | number | React.Component | Element[] | ComponentType | JSX.Element[] | null
    placement?: string
    tooltipDelay?: number
    className?: string
    trigger?: string
    children?: { props?: { visible?: boolean } }
    container?: 'target' | 'body'
}

export function TooltipHOC<TProps extends TooltipHocProps>(Component: Function): ComponentType<TProps> {
    return function WithTooltip(props: TProps) {
        const { getComponent } = useContext(FactoryContext)
        const FactoryTooltip = getComponent('Tooltip', FactoryLevels.SNIPPETS)

        const { hint, className, placement = 'bottom', tooltipDelay = 0, trigger = 'hover', container } = props

        if (!hint || !FactoryTooltip || props?.children?.props?.visible === false) {
            return <Component {...props} />
        }

        return (
            <FactoryTooltip
                hint={hint}
                className={className}
                placement={placement}
                delay={tooltipDelay}
                trigger={trigger}
                container={container}
            >
                <Component {...props} />
            </FactoryTooltip>
        )
    }
}

const Expandable = forwardRef(
    // @ts-ignore FIXME разобраться
    ({ children, ...rest }, forwardedRef) => cloneElement(children, { ...rest, forwardedRef }),
)

/**
 * Wrapper with TooltipHOC
 * @example
 *  <Tooltip
       hint='target component hint'
       placement='top'
     >
 <TargetComponent />
 </Tooltip>
 */
export const Tooltip = TooltipHOC(Expandable)
