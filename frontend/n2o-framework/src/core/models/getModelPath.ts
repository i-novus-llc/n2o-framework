import { FieldLink, FieldPath, FullModelPath, ModelLink, ModelPath, ModelPrefix } from './types'

export function getModelPath({
    id,
    prefix,
    index,
}: ModelLink): ModelPath {
    if ((typeof index === 'number') && (prefix === ModelPrefix.source || prefix === ModelPrefix.selected)) {
        return `${prefix}['${id}'][${index}]`
    }

    return `${prefix}['${id}']`
}

export function getFieldPath(link: FieldLink): FieldPath {
    return `${getModelPath(link)}.${link.field}`
}

export const getFullModelPath = (link: ModelLink | FieldLink): FullModelPath => (
    'field' in link && typeof link.field === 'string'
        ? `models.${getFieldPath(link)}`
        : `models.${getModelPath(link)}`
)
