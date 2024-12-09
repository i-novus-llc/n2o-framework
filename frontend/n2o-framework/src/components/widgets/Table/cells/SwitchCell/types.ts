import { CSSProperties } from 'react'

export interface Props {
    model: Record<string, unknown>
    switchFieldId: string
    switchList: string
    switchDefault: string
    style: CSSProperties
}
