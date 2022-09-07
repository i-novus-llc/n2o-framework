import isUndefined from 'lodash/isUndefined'

// ищет placeholder {size} в label, заменяет на длину массива
export const replacePlaceholder = (label, listLength) => {
    const placeholder = '{size}'
    const hasPlaceholder = !isUndefined(label) && label.match(placeholder)

    if (isUndefined(label)) {
        return listLength
    } if (hasPlaceholder) {
        return label.replace(/{size}/gm, listLength)
    }

    return label
}
