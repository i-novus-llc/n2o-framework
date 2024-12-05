import { LegacyRef } from 'react'
import { StatusTextProps } from '@i-novus/n2o-components/lib/display/StatusText/types'

export interface Props {
    id: string
    className: string
    visible: boolean
    color: StatusTextProps['color']
    model: Record<string, string>
    fieldKey: string
    textPosition: string
    forwardedRef: LegacyRef<HTMLDivElement>
    tooltipFieldId: string
}
