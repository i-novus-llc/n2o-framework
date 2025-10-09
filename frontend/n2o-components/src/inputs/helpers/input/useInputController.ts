import { ChangeEvent, FocusEvent, useEffect, useState, ClipboardEvent } from 'react'
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
 * @param params.prepareToStore - Дополнительная обработка значения перед сохранением в store
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
        prepareToStore,
        className = '',
    }: useInputControllerProps,
) {
    const [stateValue, setStateValue] = useState<string>(defaultValue || '')

    useEffect(() => {
        setStateValue(defaultValue || '')

        if (defaultValue && !validate(defaultValue)) {
            onMessage?.(new Error(invalidText))
        } else {
            onMessage?.(null)
        }
    }, [defaultValue])

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { value } = e.target

        setStateValue(value)

        if (!value) {
            onChange?.(value)

            return
        }

        if (validate(value)) {
            if (prepareToStore) {
                onChange?.(prepareToStore(value))

                return
            }

            onChange?.(value)
        }
    }

    const handleBlur = (e: FocusEvent<HTMLInputElement>) => {
        const { value } = e.target

        if (!value) { return onBlur?.(value) }

        if (validate(value)) {
            if (prepareToStore) {
                return onBlur?.(prepareToStore(value))
            }

            return onBlur?.(value)
        }

        if (clearOnBlur) {
            onChange?.(null)
            setStateValue('')

            return onBlur?.(null)
        }

        onMessage?.(new Error(invalidText))

        return null
    }

    return {
        stateValue,
        handleChange,
        handleBlur,
        inputClassName: classNames(COMMON_INPUT_CLASS_NAMES, className),
    }
}
