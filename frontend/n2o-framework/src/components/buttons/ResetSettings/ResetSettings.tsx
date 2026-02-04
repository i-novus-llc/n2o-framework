import React from 'react'
import classNames from 'classnames'

import { useTableWidget } from '../../widgets/AdvancedTable'
import { FactoryStandardButton, type Props } from '../FactoryStandardButton'

export function ResetSettings({ className, ...rest }: Props) {
    const { resetSettings } = useTableWidget()

    return <FactoryStandardButton {...rest} className={classNames('reset-settings-btn', className)} onClick={resetSettings} />
}
