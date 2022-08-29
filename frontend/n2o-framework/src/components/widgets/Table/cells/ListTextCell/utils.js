import classNames from 'classnames'
import isUndefined from 'lodash/isUndefined'

const triggerClassName = labelDashed => classNames({
    'list-text-cell__trigger_dashed': labelDashed,
    'list-text-cell__trigger': !labelDashed || isUndefined(labelDashed),
})

const tooltipContainerClassName = (validTooltipList, theme) => classNames({
    'list-text-cell__tooltip-container dark':
        validTooltipList && theme === 'dark',
    'list-text-cell__tooltip-container light':
        validTooltipList && theme === 'light',
    'list-text-cell__tooltip-container_empty': !validTooltipList,
})

const arrowClassName = theme => classNames({
    'tooltip-arrow light': theme === 'light',
    'tooltip-arrow dark': theme === 'dark',
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
