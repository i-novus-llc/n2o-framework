import { ReactNode, LegacyRef } from 'react'
import { ButtonProps } from '@i-novus/n2o-components/lib/button/Button'
import { type Props as BadgeProps } from '@i-novus/n2o-components/lib/display/Badge/Badge'

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
    wrapperClassName?: string
    badge?: BadgeProps
    dataSourceIsLoading?: boolean
    iconPosition?: ICON_POSITIONS
    forwardedRef: LegacyRef<HTMLDivElement>
}
