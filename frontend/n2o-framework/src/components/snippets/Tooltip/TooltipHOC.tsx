import React from 'react'
import PropTypes from 'prop-types'
import { usePopperTooltip, Config } from 'react-popper-tooltip'
import { getContext, compose } from 'recompose'

interface ITooltipHocProps extends Config {
    hint?: string | number | React.Component
    trigger?: 'click' | 'double-click' | 'right-click' | 'hover' | 'focus'
    controlled?: boolean
    className?: string
}

interface ITooltipProps extends ITooltipHocProps {
    defaultTooltip: React.Component
}

function Tooltip({ defaultTooltip: DefaultTooltip, ...props }: ITooltipProps): React.Component {
    return <DefaultTooltip {...props} />
}

const ContextTooltip = compose(getContext({ defaultTooltip: PropTypes.node }))(Tooltip)

export function TooltipHOC<TProps extends ITooltipHocProps>(
    Component: React.ComponentType<TProps> | React.FunctionComponent<TProps>,
): React.ReactNode {
    return function WithTooltip(props: TProps) {
        const { hint, controlled = false } = props

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

        if (controlled) {
            return (
                <>
                    <Component {...props} tooltipTriggerRef={setTriggerRef} />
                    {visible && (
                        <ContextTooltip {...tooltipProps} {...props} />
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
                    <ContextTooltip {...tooltipProps} {...props} />
                )}
            </>
        )
    }
}
