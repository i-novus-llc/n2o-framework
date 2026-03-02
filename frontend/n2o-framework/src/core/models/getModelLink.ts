import { ModelLink, ModelPrefix } from './types'

export const getModelLink = (prefix: ModelPrefix, id: string, field?: string): ModelLink => (
    field
        ? `models.${prefix}['${id}'].${field}`
        : `models.${prefix}['${id}']`
)
