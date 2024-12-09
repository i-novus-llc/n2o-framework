import classNames from 'classnames'

// dashed стиль лейбла tooltip или нет
export const triggerClassName = (labelDashed?: boolean, type?: string) => classNames({
    'list-text-cell__trigger_dashed': labelDashed,
    'list-text-cell__trigger': !labelDashed,
    'd-flex': type === 'mapProps(StandardButton)',
})

// ищет placeholder {size} в label, заменяет на длину массива
const PLACEHOLDER_SIZE = '{size}'

export const replacePlaceholder = (label: string | undefined, listLength: number): string | number => {
    if (label === undefined) { return listLength }

    if (label.includes(PLACEHOLDER_SIZE)) {
        return label.replace(new RegExp(PLACEHOLDER_SIZE, 'g'), String(listLength))
    }

    return label
}
