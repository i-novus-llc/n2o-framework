import type { FindOptionToAdd } from '../types'

import { getOptionById, getOptionByFieldName } from './common'

// Выбор опций по Enter
export function findOptionToAdd({ activeValueId, options, inputValue, labelFieldId, enabledFieldId }: FindOptionToAdd) {
    // 1. Попытка выбрать опцию по активному ID (навигация стрелками)
    const byActiveId = getOptionById(activeValueId, options)

    if (byActiveId) { return byActiveId }

    // 2. Попытка найти опцию по тексту в поле ввода
    if (inputValue) {
        const byInput = getOptionByFieldName(labelFieldId, inputValue, options)

        if (byInput) { return byInput }
    }

    // 3. Если доступна только одна опция — выбираем её (с учётом enabledFieldId)
    if (options.length === 1) {
        const option = options[0]

        if (enabledFieldId) { return option[enabledFieldId] !== false ? option : null }

        return option
    }

    return null
}
