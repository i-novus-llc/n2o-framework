import React, { forwardRef, ForwardedRef, useContext } from 'react'
import { type SelectControlsProps } from '@i-novus/n2o-components/lib/inputs/Input/SelectControls'
import classNames from 'classnames'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { Spinner, SpinnerType } from '../../../factoryComponents/Spinner'

export interface SelectPostfixProps extends SelectControlsProps { loading: boolean, visible?: boolean }

export const SelectPostfix = forwardRef<HTMLDivElement, SelectPostfixProps>(
    ({ loading, visible = true, clear, ...rest }, ref) => {
        const { getComponent } = useContext(FactoryContext)
        const FactorySelectControls = getComponent<SelectControlsProps & { ref: ForwardedRef<HTMLDivElement> }>('SelectControls', FactoryLevels.SNIPPETS)

        if (!FactorySelectControls || !visible) { return null }

        if (loading) {
            return <Spinner type={SpinnerType.cover} showDelayMs={0} className={classNames('select-loader', { extended: clear })} loading />
        }

        return <FactorySelectControls {...rest} ref={ref} clear={clear} />
    },
)

SelectPostfix.displayName = 'SelectPostfix'
