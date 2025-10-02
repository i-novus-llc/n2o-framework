import { ChangeEvent, FocusEvent, useEffect, useState } from 'react'
import classNames from 'classnames'

import { type useInputControllerProps } from './types'

const COMMON_INPUT_CLASS_NAMES = ['form-control', 'n2o-input-mask']

/**
 * Хук для управления вводом данных
 * @param params - Параметры хука
 * @param params.value - Начальное значение
 * @param params.onChange - Callback при изменении значения
 * @param params.onBlur - Callback при потере фокуса
 * @param params.validate - Функция валидации значения
 * @param params.onMessage - Callback для показа сообщений
 * @param params.invalidText - Текст ошибки при невалидном значении
 * @param params.clearOnBlur - Очищать поле при невалидном значении
 * @param params.storeCleanValue - Сохранять очищенное значение
 * @param params.className - className
 * @returns Объект с состоянием и обработчиками
 */
export function useInputController(
    {
        value: defaultValue,
        onChange,
        onBlur,
        validate,
        onMessage,
        invalidText,
        clearOnBlur,
        storeCleanValue = false,
        className = '',
    }: useInputControllerProps,
) {
    const [stateValue, setStateValue] = useState<string>(defaultValue || '')

    useEffect(() => {
        setStateValue(defaultValue || '')

        if (defaultValue && !validate(defaultValue)) {
            onMessage?.(new Error(invalidText))
        }
    }, [defaultValue])

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { value } = e.target

        setStateValue(value)

        if (!value) { onChange?.(value) }

        if (validate(value)) {
            const stored = storeCleanValue ? value.replace(/\D/g, '') : value

            return onChange?.(stored)
        }
    }

    const handleBlur = (e: FocusEvent<HTMLInputElement>) => {
        const { value } = e.target

        if (!value) { return onBlur?.(value) }

        if (validate(value)) {
            const stored = storeCleanValue ? value.replace(/\D/g, '') : value

            return onBlur?.(stored)
        }

        if (clearOnBlur) {
            onChange?.(null)
            setStateValue('')

            return onBlur?.(null)
        }

        onMessage?.(new Error(invalidText))
    }

    return {
        stateValue,
        handleChange,
        handleBlur,
        inputClassName: classNames(COMMON_INPUT_CLASS_NAMES, className),
    }
}
