import { ReactNode } from 'react'

// TODO временное решение для изменения ui элементов внутри input
export interface InputElementsProps {
    spinner?: ReactNode | {
        color?: string
    }
    buttons?: {
        // иконки управление инпутом
        up?: string | ReactNode
        down?: string | ReactNode
        clear?: string | ReactNode
        switcher?: string | ReactNode
    }
    item?: {
        // иконка удаления элемента
        close?: ReactNode | string
    }
}

export enum DefaultIcons {
    SWITCHER = 'fa fa-chevron-right',
    CLEAR = 'fa fa-times',
    SUFFIX = 'fa fa-chevron-down',
}

export enum ShowClearTrigger {
    FOCUS = 'focus',
    HOVER = 'hover',
    ALWAYS = 'always',
}

export const SHOW_CLEAR_TRIGGER_PREFIX = 'show-clear-trigger'
export const getShowClearTriggerClass = (showClearTrigger: ShowClearTrigger) => `${SHOW_CLEAR_TRIGGER_PREFIX}-${showClearTrigger}`
