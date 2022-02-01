import React from 'react'
import { UncontrolledTooltip } from 'reactstrap'

/**
 * Если есть подсказка, возвращаем с тултипом
 */

export default function placementwithTooltip(
    component,
    hint,
    hintPosition,
    id,
) {
    if (hint) {
        return (
            <>
                {component}
                <UncontrolledTooltip delay={0} placement={hintPosition} target={id}>
                    {hint}
                </UncontrolledTooltip>
            </>
        )
    }

    return component
}
