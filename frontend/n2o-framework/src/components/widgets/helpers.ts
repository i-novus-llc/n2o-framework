import isEmpty from 'lodash/isEmpty'

import { type Mapping } from '../../ducks/datasource/Provider'

import { type FieldSetsProps, type FieldType } from './Form/types'

interface ReplaceOptions {
    replaced: string
    replace: string
}

function queryMapper(queryMapping: Mapping, replaceOptions: ReplaceOptions) {
    const mapped: Mapping = { ...queryMapping }
    const queryMappingKeys: string[] = Object.keys(mapped)

    for (const key of queryMappingKeys) {
        const mapping = queryMapping[key]
        const { link } = mapping

        if (link) {
            const { replaced, replace } = replaceOptions

            mapped[key] = { ...mapped[key], link: link.replace(replaced, replace) }
        }
    }

    return mapped
}

function fieldsMapper(fields: FieldType[], replaceOptions: ReplaceOptions) {
    return fields?.map((field) => {
        const { control } = field

        if (!control) { return field }

        const { dataProvider } = control

        if (!dataProvider || isEmpty(dataProvider)) { return field }

        const { queryMapping } = dataProvider

        if (!queryMapping || isEmpty(queryMapping)) { return field }

        const newQueryMapping: Mapping = queryMapper(queryMapping, replaceOptions)

        return { ...field,
            control: { ...control,
                dataProvider: {
                    ...dataProvider,
                    queryMapping: newQueryMapping,
                } } }
    })
}

/* Костыль в WidgetFilters идет вглубь филдов и заменяет link для DataProvider */
export function modelLinkMapper(
    fieldsets: FieldSetsProps,
    replaceOptions: ReplaceOptions = {
        replaced: 'filter',
        replace: 'edit',
    },
) {
    return fieldsets.map((fieldset) => {
        const { rows } = fieldset

        return {
            ...fieldset,
            rows: rows?.map((row) => {
                const { cols } = row

                return {
                    ...row,
                    cols: cols?.map((col) => {
                        const { fields } = col

                        return { ...col, fields: fieldsMapper(fields, replaceOptions) }
                    }),
                }
            }),
        }
    })
}
