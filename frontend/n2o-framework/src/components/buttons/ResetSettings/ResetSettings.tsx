import React from 'react'

import { StandardButton } from '../StandardButton/StandardButton'
import { useTableWidget } from '../../widgets/AdvancedTable'

export function ResetSettings(props: Record<string, unknown>) {
    const { resetSettings } = useTableWidget()

    return <StandardButton {...props} className="reset-settings-btn" onClick={resetSettings} />
}
