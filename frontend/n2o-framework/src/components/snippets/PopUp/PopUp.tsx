import React, { useContext, Children, isValidElement } from 'react'
import { TooltipTheme } from '@i-novus/n2o-components/lib/display/Tooltip/TooltipComponent'
import { TooltipProps } from '@i-novus/n2o-components/lib/display/Tooltip/Tooltip'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'

import type { PopUpProps, PopUpChildProps } from './types'

// eslint-disable-next-line react/jsx-no-useless-fragment
const PopUpTrigger = ({ children }: PopUpChildProps) => <>{children}</>

PopUpTrigger.displayName = 'PopUpTrigger'

// eslint-disable-next-line react/jsx-no-useless-fragment
const PopUpContent = ({ children }: PopUpChildProps) => <>{children}</>

PopUpContent.displayName = 'PopUpContent'

export function PopUp({ children, open, className, triggerClassName, triggerRef, onTriggerClick, placement = 'bottom-start' }: PopUpProps) {
    const { getComponent } = useContext(FactoryContext)
    const FactoryTooltip = getComponent<TooltipProps>('Tooltip', FactoryLevels.SNIPPETS)

    if (!FactoryTooltip) { return null }

    let trigger = null
    let content = null

    if (children) {
        Children.forEach(children, (child) => {
            if (!isValidElement(child)) { return }

            if (child.type === PopUpTrigger) {
                trigger = child.props.children
            } else if (child.type === PopUpContent) {
                content = child.props.children
            }
        })
    }

    return (
        <FactoryTooltip
            placement={placement}
            hint={content}
            open={open}
            theme={TooltipTheme.NONE}
            className={className}
        >
            <div ref={triggerRef} className={triggerClassName} onClick={onTriggerClick}>{trigger}</div>
        </FactoryTooltip>
    )
}

PopUp.Trigger = PopUpTrigger
PopUp.Content = PopUpContent
