import { ReactNode, LegacyRef } from 'react'
import { type ButtonProps } from 'reactstrap'

import { ICON_POSITIONS } from '../../snippets/IconContainer/IconContainer'
import { type UseActionProps } from '../withActionButton'

type Extension = ButtonProps & Partial<UseActionProps>

export interface Props extends Extension {
    id?: string
    label?: string
    icon?: string
    color?: string
    children?: ReactNode
    rounded?: boolean
    className?: string
    badge?: Record<string, unknown>
    dataSourceIsLoading?: boolean
    iconPosition?: ICON_POSITIONS
    forwardedRef: LegacyRef<HTMLDivElement>
}
