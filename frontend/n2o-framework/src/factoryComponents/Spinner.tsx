import React, { useContext } from 'react'
import { type Props as SpinnerProps, SpinnerType } from '@i-novus/n2o-components/lib/layouts/Spinner/Spinner'

import { FactoryContext } from '../core/factory/context'
import { FactoryLevels } from '../core/factory/factoryLevels'

import { useSpinnerContextProps } from './useSpinnerContextProps'

export type { SpinnerProps }
export { SpinnerType }

export function Spinner({ children, showDelayMs: propsShowDelayMs, ...rest }: SpinnerProps) {
    const { getComponent } = useContext(FactoryContext)
    const FactorySpinner = getComponent('Spinner', FactoryLevels.SNIPPETS)

    const { showDelayMs } = useSpinnerContextProps()

    if (!FactorySpinner) { return null }

    return <FactorySpinner {...rest} showDelayMs={propsShowDelayMs ?? showDelayMs}>{children}</FactorySpinner>
}
