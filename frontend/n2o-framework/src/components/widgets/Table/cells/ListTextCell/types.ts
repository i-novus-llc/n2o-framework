import { LegacyRef } from 'react'

interface Label {
    label: string
    /**
     * применить к label dashed underline
     */
    labelDashed?: boolean
}

export interface ListTextCellTriggerProps extends Omit<Label, 'label'> {
    label: string | number
    forwardedRef?: LegacyRef<HTMLSpanElement>
    tooltipClose?(): void
}

export interface ListTextCellProps extends Label {
    model: Record<string, unknown>
    /**
     * Склонение заголовка для единичных значений
     */
    oneLabel: string
    /**
     * Склонение заголовка для нескольких значений
     */
    fewLabel: string
    /**
     * Склонение заголовка для множества значений
     */
    manyLabel: string
    fieldKey: string
    disabled: boolean
    /**
     * trigger показывать tooltip по hover или click
     */
    trigger: 'hover' | 'click'
    placement: string
}
