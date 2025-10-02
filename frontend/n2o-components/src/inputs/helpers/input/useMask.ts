import { type MaskitoMask } from '@maskito/core/src/lib/types/mask'
import { useMaskito } from '@maskito/react'
import { maskitoEventHandler, maskitoWithPlaceholder } from '@maskito/kit'
import { maskitoUpdateElement, maskitoTransform } from '@maskito/core'
import { useMemo } from 'react'

export function useMask(
    mask: MaskitoMask,
    placeholder: string,
    initialValue?: string | number | null
) {
    const options = useMemo(() => {
        const {
            removePlaceholder,
            plugins,
            ...placeholderOptions
        } = maskitoWithPlaceholder(placeholder)

        return {
            preprocessors: placeholderOptions.preprocessors,
            postprocessors: [...placeholderOptions.postprocessors],
            mask,
            plugins: [
                ...plugins,
                maskitoEventHandler('focus', (element) => {
                    const initialValue = element.value

                    maskitoUpdateElement(element, initialValue + placeholder.slice(initialValue.length))
                }),
                maskitoEventHandler('blur', (element) => {
                    const cleanValue = element.value === placeholder
                        ? ''
                        : removePlaceholder(element.value)

                    maskitoUpdateElement(element, cleanValue)
                }),
            ],

        }
    }, [mask])

    const maskRef = useMaskito({ options })

    const maskedValue = initialValue ? maskitoTransform(String(initialValue), options) : ''

    return { maskRef, maskedValue }
}
