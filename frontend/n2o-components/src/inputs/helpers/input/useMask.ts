import { type MaskitoMask } from '@maskito/core/src/lib/types/mask'
import { useMemo } from 'react'
import { useMaskito } from '@maskito/react'
import { maskitoEventHandler, maskitoWithPlaceholder } from '@maskito/kit'
import {
    maskitoUpdateElement,
    maskitoTransform,
    MaskitoPreprocessor,
    MaskitoPostprocessor,
    MaskitoPlugin,
    MaskitoElement,
} from '@maskito/core'

interface Enhancer {
    placeholder: string
    defaultValue?: string | number | null
}

export interface onInputProcessorParams extends Enhancer {
    element: MaskitoElement
}

interface Params extends Enhancer {
    mask: MaskitoMask
    processors?: {
        preprocessors?: MaskitoPreprocessor[]
        postprocessors?: MaskitoPostprocessor[]
        plugins?: MaskitoPlugin[]
    }
    onInputProcessor?(params: onInputProcessorParams): void
}

export function useMask(params: Params) {
    const { mask, placeholder, defaultValue, processors, onInputProcessor } = params

    const options = useMemo(() => {
        const {
            removePlaceholder,
            plugins,
            ...placeholderOptions
        } = maskitoWithPlaceholder(placeholder)

        const {
            preprocessors: presetPreprocessors = [],
            postprocessors: presetPostprocessors = [],
            plugins: presetPlugins = [],
        } = processors || {}

        return {
            preprocessors: [...placeholderOptions.preprocessors, ...presetPreprocessors],
            postprocessors: [...placeholderOptions.postprocessors, ...presetPostprocessors],
            mask,
            plugins: [
                ...plugins,
                ...presetPlugins,
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
                maskitoEventHandler('input', (element) => {
                    onInputProcessor?.({
                        element,
                        placeholder,
                        defaultValue,
                    })
                }),
            ],
        }
    }, [mask])

    const maskRef = useMaskito({ options })

    const maskedValue = defaultValue ? maskitoTransform(String(defaultValue), options) : ''

    return { maskRef, maskedValue }
}
