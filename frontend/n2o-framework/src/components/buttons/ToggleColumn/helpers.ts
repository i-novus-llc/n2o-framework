export function getLabel(columnId: string, label?: string, icon?: string) {
    if (!label && !icon) { return columnId }
    if (!label) { return null }

    return label
}

export function parseIds(ids: string) {
    if (!ids) { return [] }

    return ids.replace(/\s/g, '').split(',')
}
