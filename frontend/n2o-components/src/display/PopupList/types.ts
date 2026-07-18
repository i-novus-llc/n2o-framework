import { CSSProperties, MutableRefObject, ReactNode, UIEvent } from 'react'

import { Props as BadgeProps } from '../Badge/Badge'

export interface PopUpProps {
    popUpStyle?: CSSProperties
    popUpItemRef?: MutableRefObject<HTMLDivElement | null> | null
}

export enum Filter {
    endsWith = 'endsWith',
    includes = 'includes',
    server = 'server',
    startsWith = 'startsWith',
}

export interface Option<T = string> {
    id: string | number
    className?: string
    disabled?: boolean
    formattedTitle?: string
    parentId?: string | number
    label?: string
    value?: T
}

export type idField = keyof Pick<Option, 'id'>
export type parentIdField = keyof Pick<Option, 'parentId'>

export type Options = Option[]

export interface PopUpContentCommonProps {
    activeValueId?: string | number | null
    badge?: BadgeType
    descriptionFieldId?: string
    enabledFieldId?: string
    format?: string
    groupFieldId?: string
    hasCheckboxes?: boolean
    iconFieldId?: string
    imageFieldId?: string
    isExpanded?: boolean
    labelFieldId?: string
    loading?: boolean
    multiSelect?: boolean
    onRemoveItem?(item: Option): void
    onSelect?(item: Option): void
    options?: Options
    popUpItemRef?: PopUpProps['popUpItemRef']
    renderIfEmpty?: boolean
    selected?: Options
    setActiveValueId?(id: string | number | null): void
    statusFieldId?: string
    valueFieldId?: idField
    searchMinLengthHint?: string | null | JSX.Element
}

export type BadgeType = BadgeProps & {
    colorFieldId: string
    fieldId: string
    imageFieldId: string
}

export interface PopUpItemsProps extends PopUpContentCommonProps {
    autocomplete?: boolean
    children?: ReactNode
}

export interface PopUpListProps extends PopUpContentCommonProps {
    className?: string
    children?: ReactNode
    disabledValues?: Array<string | number>
    setMenuElement?(): void
    style?: object
    maxTagCount?: number
    valueFieldId: idField
    onScroll?(event: UIEvent<HTMLDivElement>): void
}
