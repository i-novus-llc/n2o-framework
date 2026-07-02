export enum ModelPrefix {
    active = 'resolve',
    selected = 'multi',
    source = 'datasource',
    filter = 'filter',
    edit = 'edit',
}

export type FormModelPrefix = ModelPrefix.active | ModelPrefix.edit | ModelPrefix.filter
export type ListModelPrefix = ModelPrefix.source | ModelPrefix.selected

export type ModelPrefixes = 'resolve' | 'multi' | 'datasource' | 'filter' | 'edit'

export type ModelLink<Prefix extends ModelPrefix = ModelPrefix> = {
    prefix: Prefix
    id: string
    index?: number
}

export type FieldLink<Prefix extends ModelPrefix = ModelPrefix> = ModelLink<Prefix> & {
    field: string
}

export type ModelPath = `${ModelPrefix}['${string}']` | `${ListModelPrefix}[${string}][${number}]`
export type FieldPath = `${ModelPath}.${string}`
export type FullModelPath = `models.${ModelPath | FieldPath}`

export type Model = Record<string, unknown>

export type ModelTypeByPrefix<P extends ModelPrefix> = P extends ModelPrefix.source | ModelPrefix.selected ? Model[] : Model
