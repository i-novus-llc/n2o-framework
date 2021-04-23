import map from 'lodash/map'
import pull from 'lodash/pull'

/**
 * Функция для преобразования данных с помоцию ключей указывающих на значение
 * @param value - текущее значение
 * @param data - массив данных
 * @param labelFieldId - ключ лейбла
 * @param valueFieldId - - ключ value
 * @returns {*}
 */
export const convertData = (value, data, { labelFieldId, valueFieldId }) => {
    const prepareData = item => ({
        label: item[labelFieldId],
        id: item[valueFieldId],
        active: value.includes(item[valueFieldId]),
    })

    return map(data, prepareData)
}

/**
 * Функция для подготовки выбранных данных
 * @param currentValue - текущее значение
 * @param id - новое значение
 * @param multiSelect флаг множественного выбора
 * @returns {*}
 */
export const prepareValue = (
    currentValue,
    id,
    { multiSelect = false } = {},
) => {
    if (multiSelect) {
        if (currentValue.includes(id)) {
            pull(currentValue, id)
            return currentValue
        }
        currentValue.push(id)
        return currentValue
    }
    if (currentValue.includes(id)) {
        return []
    }
    return [id]
}
