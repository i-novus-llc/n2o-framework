import { getFormFieldsByName } from '../../ducks/form/selectors'
import type { State as GlobalState } from '../../ducks/State'

import { ValidationResult } from './types'

/**
 * Добавляет сообщения валидации форматирования поля в список ошибок валидаций источника данных,
 * чтобы заблокировать в дальнейшем выполнение действий при невалидных масках
 * FIXME: Вынести сообщения об ошибках из формы в датасурс и не смешавать вот так
 */
export function addFieldMessages(
    datasourceId: string,
    messages: Record<string, ValidationResult[]>,
    state: GlobalState,
): Record<string, ValidationResult[]> {
    const withFieldMessages = { ...messages }

    Object.entries(getFormFieldsByName(datasourceId)(state))
        .forEach(([fieldName, field]) => {
            const mess = field.message

            if (mess) {
                withFieldMessages[fieldName] = withFieldMessages[fieldName]
                    ? [mess, ...withFieldMessages[fieldName]]
                    : [mess]
            }
        })

    return withFieldMessages
}
