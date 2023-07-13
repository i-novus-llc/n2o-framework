import isEmpty from 'lodash/isEmpty'

type QueryMapping = Record<string, { link?: string }>
type DataProviderType = { queryMapping?: QueryMapping }
type ControlType = {dataProvider?: DataProviderType}
type FieldType = { control: ControlType}
type ColType = { fields: FieldType[] }
type RowType = { cols: ColType[] }
type Fieldset = { rows: RowType[] }
type Fieldsets = Fieldset[]
interface IReplaceOptions {
    replaced: string
    replace: string
}

function queryMapper(queryMapping: QueryMapping, replaceOptions: IReplaceOptions) {
    const mapped: QueryMapping = { ...queryMapping }
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

function fieldsMapper(fields: FieldType[], replaceOptions: IReplaceOptions) {
    return fields.map((field) => {
        const { control } = field

        if (!control) { return field }

        const { dataProvider } = control

        if (!dataProvider || isEmpty(dataProvider)) { return field }

        const { queryMapping } = dataProvider

        if (!queryMapping || isEmpty(queryMapping)) { return field }

        const newQueryMapping: QueryMapping = queryMapper(queryMapping, replaceOptions)

        return {
            ...field,
            control: {
                ...control,
                dataProvider: {
                    ...dataProvider,
                    queryMapping: newQueryMapping,
                } } }
    })
}

/* Костыль в WidgetFilters идет вглубь филдов и заменяет link для DataProvider */
export function modelLinkMapper(
    fieldsets: Fieldsets,
    replaceOptions: IReplaceOptions = {
        replaced: 'filter',
        replace: 'edit',
    },
) {
    return fieldsets.map((fieldset) => {
        const { rows } = fieldset

        return {
            ...fieldset,
            rows: rows.map((row) => {
                const { cols } = row

                return {
                    ...row,
                    cols: cols.map((col) => {
                        const { fields } = col

                        return { ...col, fields: fieldsMapper(fields, replaceOptions) }
                    }),
                }
            }),
        }
    })
}
