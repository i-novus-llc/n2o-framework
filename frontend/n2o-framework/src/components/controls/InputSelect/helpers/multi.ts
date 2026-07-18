import type { GetPlaceholderParams, Options, SummaryFormat } from '../types'
import type { TagsMeta } from '../../../snippets/Tags/types'
import { SUMMARY_TAG_ID } from '../const'

import { getField } from './common'

/**
 * Возвращает текст-плейсхолдер для поля выбора на основе количества выбранных элементов.
 * Если ничего не выбрано – возвращает пустую строку.
 * Если заданы все три формы (one, few, many) – использует getNoun для склонения.
 * Иначе подставляет количество в selectFormat.
 */
export function getPlaceholder({
    selected,
    selectFormat = 'Выбрано: {size}',
    selectFormatOne = '',
    selectFormatFew = '',
    selectFormatMany = '',
    getNoun = defaultGetNoun,
}: GetPlaceholderParams): string {
    const count = selected.length

    if (count < 1) { return '' }

    const hasCustomForms =
        selectFormatOne !== '' && selectFormatFew !== '' && selectFormatMany !== ''

    if (hasCustomForms) {
        const result = getNoun(count, selectFormatOne, selectFormatFew, selectFormatMany)

        return result.replace(/{size}/g, String(count))
    }

    return selectFormat.replace(/{size}/g, String(count))
}

function defaultGetNoun(count: number, one: string, few: string, many: string): string {
    const mod10 = count % 10
    const mod100 = count % 100

    if (mod100 >= 11 && mod100 <= 19) {
        return many
    }
    if (mod10 === 1) {
        return one
    }
    if (mod10 >= 2 && mod10 <= 4) {
        return few
    }

    return many
}

// Создание тэгов
export const createTags = (selected: Options, inputLabelFieldId: string, enabledFieldId?: string) => selected.map((option) => {
    return {
        id: option.id,
        value: String(getField<string | number>(option, inputLabelFieldId)),
        enabled: getField<boolean>(option, enabledFieldId) as boolean,
    }
})

// Создание лимитированных тэгов c плейсхолдерами
export const createLimitedTags = (
    selected: Options,
    inputLabelFieldId: string,
    enabledFieldId?: string,
    maxTagCount?: number,
    // Опции для форматирования суммарных тегов
    // selectFormat для общего количества (maxTagCount === 0)
    summaryFormat?: SummaryFormat,
    // Опции для форматирования остатка (maxTagCount > 0)
    // selectFormat по умолчанию '+{size}...'
    remainingFormat?: SummaryFormat,
): TagsMeta => {
    // Без ограничений или количество выбранных не превышает лимит
    if (maxTagCount === undefined || selected.length <= maxTagCount) {
        return createTags(selected, inputLabelFieldId, enabledFieldId)
    }

    // Случай maxTagCount === 0: только один суммарный тег
    if (maxTagCount === 0) {
        const placeholder = getPlaceholder({
            selected,
            selectFormat: summaryFormat?.selectFormat ?? 'Выбрано: {size}',
            selectFormatOne: summaryFormat?.selectFormatOne ?? '',
            selectFormatFew: summaryFormat?.selectFormatFew ?? '',
            selectFormatMany: summaryFormat?.selectFormatMany ?? '',
            getNoun: summaryFormat?.getNoun ?? defaultGetNoun,
        })

        return [
            {
                id: SUMMARY_TAG_ID,
                value: placeholder,
                isSummary: true,
                enabled: false,
            },
        ]
    }

    // Случай maxTagCount > 0: берём первые maxTagCount тегов и добавляем суммарный с остатком
    const visibleTags = createTags(
        selected.slice(0, maxTagCount),
        inputLabelFieldId,
        enabledFieldId,
    )
    const remainingCount = selected.length - maxTagCount

    // Для остатка создаём фиктивный массив длиной remainingCount,
    // чтобы getPlaceholder мог использовать его длину.
    const remainingArray = Array(remainingCount).fill(null) as Options

    const remainingPlaceholder = getPlaceholder({
        selected: remainingArray,
        selectFormat: remainingFormat?.selectFormat ?? '+{size}...',
        selectFormatOne: remainingFormat?.selectFormatOne ?? '',
        selectFormatFew: remainingFormat?.selectFormatFew ?? '',
        selectFormatMany: remainingFormat?.selectFormatMany ?? '',
        getNoun: remainingFormat?.getNoun ?? defaultGetNoun,
    })

    const summaryTag = {
        id: SUMMARY_TAG_ID,
        value: remainingPlaceholder,
        isSummary: true,
        enabled: false,
    }

    return [...visibleTags, summaryTag]
}
