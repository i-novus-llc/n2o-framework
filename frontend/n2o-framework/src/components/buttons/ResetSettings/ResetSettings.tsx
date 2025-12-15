import React from 'react'
import classNames from 'classnames'

import { StandardButton } from '../StandardButton/StandardButton'
import { useTableWidget } from '../../widgets/AdvancedTable'
import { type Props } from '../Simple/types'

export function ResetSettings({ className, ...rest }: Props) {
    const { resetSettings } = useTableWidget()

    return <StandardButton {...rest} className={classNames('reset-settings-btn', className)} onClick={resetSettings} />
}
