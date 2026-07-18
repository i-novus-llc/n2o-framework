import { useState, KeyboardEvent } from 'react'

import type { UseSelectKeyboardNavigationProps, Option, Options, PossibleSelected } from '../types'

export const isOptionSelected = (option: Option, selected?: PossibleSelected): boolean => {
    if (!selected) { return false }

    if (Array.isArray(selected)) { return selected.some(sel => sel.id === option.id) }

    return selected.id === option.id
}

export const getSelectableOption = (options: Options, selected?: PossibleSelected) => options.filter(option => !isOptionSelected(option, selected))

/**
 * Хук для перемещения по списку стрелками клавиатуры,
 * возвращает активный элемент и onKeyDown
 */
export function useSelectKeyboardNavigation({ options, selected, open, onToggle }: UseSelectKeyboardNavigationProps) {
    const [activeValueId, setActiveValueId] = useState<number | string | null>(null)

    const onKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
        const { key } = e

        if ((key === 'ArrowDown' || key === 'ArrowUp') && !open) {
            e.preventDefault()
            onToggle()

            return
        }

        const selectableOptions = getSelectableOption(options, selected)

        if (selectableOptions.length === 0) { return }

        const currentIndex = activeValueId !== null
            ? selectableOptions.findIndex(opt => opt.id === activeValueId)
            : -1

        if (key === 'Enter') {
            setActiveValueId(null)

            return
        }

        if (key === 'ArrowDown') {
            e.preventDefault()
            let nextIndex = currentIndex + 1

            if (nextIndex >= selectableOptions.length) {
                nextIndex = selectableOptions.length - 1
            }

            if (nextIndex !== currentIndex) {
                setActiveValueId(selectableOptions[nextIndex].id)

                return
            }

            if (currentIndex === -1 && selectableOptions.length > 0) {
                setActiveValueId(selectableOptions[0].id)
            }
        }

        if (key === 'ArrowUp') {
            e.preventDefault()
            const prevIndex = currentIndex - 1

            if (prevIndex < 0) { return }

            if (prevIndex !== currentIndex) {
                setActiveValueId(selectableOptions[prevIndex].id)
            }
        }
    }

    return { onKeyDown, activeValueId }
}
