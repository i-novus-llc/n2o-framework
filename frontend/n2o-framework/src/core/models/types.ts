export enum ModelPrefix {
    active = 'resolve',
    selected = 'multi',
    source = 'datasource',
    filter = 'filter',
    edit = 'edit',
}

export type FormModelPrefix = ModelPrefix.active | ModelPrefix.edit | ModelPrefix.filter

export type ModelPrefixes = 'resolve' | 'multi' | 'datasource' | 'filter' | 'edit'

export type ModelLink = `models.${ModelPrefix}['${string}']` | `models.${ModelPrefix}[${string}].${string}`
