import { ComponentType, CSSProperties, ReactNode } from 'react'

import { type HeaderFilterProps } from '../../types/props'

export const VALIDATION_MAP: Record<string, string> = {
    'is-valid': 'text-success',
    'is-invalid': 'text-danger',
    'has-warning': 'text-warning',
} as const

export type ValidationClass = keyof typeof VALIDATION_MAP

export interface ErrorProps {
    validationClass?: ValidationClass
    message?: {
        text?: string
    };
}

export interface AdvancedTableFilterPopupProps {
    value?: string | null
    touched?: boolean
    onChange(value: string): void
    onBlur?(): void
    onSearchClick(): void
    onResetClick(): void
    component?: ComponentType<Record<string, unknown>>
    componentProps?: Record<string, unknown>
    error?: ErrorProps
    style?: CSSProperties
}

export interface FieldProps {
    component?: AdvancedTableFilterPopupProps['component'];
    control?: Record<string, unknown>
    style?: CSSProperties
}

export interface AdvancedTableFilterProps {
    id: string
    onFilter(filter: { id: string; value?: string | null }): void
    children?: ReactNode
    value?: string | null
    validateFilterField: HeaderFilterProps['validateFilterField']
    field?: HeaderFilterProps['filterField']
    error?: ErrorProps
}
