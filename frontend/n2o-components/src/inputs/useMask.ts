import { type MaskitoMask } from '@maskito/core/src/lib/types/mask'
import { useMaskito } from '@maskito/react'
import { maskitoEventHandler, maskitoWithPlaceholder } from '@maskito/kit'
import { maskitoUpdateElement, maskitoTransform } from '@maskito/core'

export function useMask(mask: string, initialValue?: string | number | null) {
    const maskitoMask: MaskitoMask = Array.from(mask).map(char => (char === '9' ? /\d/ : char))
    const placeholder = Array.from(mask).map(char => (char === '9' ? '_' : char)).join((''))

    const {
        removePlaceholder,
        plugins,
        ...placeholderOptions
    } = maskitoWithPlaceholder(placeholder)

    const options = {
        preprocessors: placeholderOptions.preprocessors,
        postprocessors: [...placeholderOptions.postprocessors],
        mask: maskitoMask,
        plugins: [
            ...plugins,
            maskitoEventHandler('focus', (element) => {
                const initialValue = element.value

                maskitoUpdateElement(element, initialValue + placeholder.slice(initialValue.length))
            }),
            maskitoEventHandler('blur', (element) => {
                const cleanValue = removePlaceholder(element.value)

                maskitoUpdateElement(element, cleanValue)
            }),
        ],

    }

    const maskRef = useMaskito({ options })

    function isMaskFilled(value: string): boolean {
        if (value === '') { return true }

        // Количество цифровых символов в маске (количество '9')
        const digitsCount = mask.split(/\d/g).length - 1
        // Количество фактически введенных цифр
        const enteredDigits = value.replace(/\D/g, '').length

        return enteredDigits === digitsCount
    }

    const maskedValue = initialValue ? maskitoTransform(String(initialValue), options) : ''

    return { maskRef, isMaskFilled, maskedValue }
}
