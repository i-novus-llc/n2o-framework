import isUndefined from 'lodash/isUndefined'
import classNames from 'classnames'

// dashed стиль лейбла tooltip или нет
export const triggerClassName = (labelDashed, type) => classNames({
    'list-text-cell__trigger_dashed': labelDashed,
    'list-text-cell__trigger': !labelDashed || isUndefined(labelDashed),
    'd-flex': type === 'mapProps(StandardButton)',
})

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
