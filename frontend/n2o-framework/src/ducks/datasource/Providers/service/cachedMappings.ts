import get from 'lodash/get'

const DIVISION = '.'

export enum Key { PATH = 'path', QUERY = 'query' }

export type Mapping = Record<string, unknown>
// a string with dynamic param like 'path.param' or 'query.param'
type InvalidateParams = string[]

export interface Mappings {
    [Key.PATH]: Mapping
    [Key.QUERY]: Mapping
    [key: string]: Mapping
}

export function checkInvalidateParams(
    invalidateParams: InvalidateParams,
    cachedMappings: Mappings,
    mappings: Mappings,
) {
    return invalidateParams.every((path) => {
        const [key, id] = path.split(DIVISION)

        return cachedMappings[key][id] === mappings[key][id]
    })
}

export function createCachedMappings(
    invalidateParams: InvalidateParams,
    mappings: Mappings,
) {
    const cachedMappings: Mappings = { [Key.PATH]: {}, [Key.QUERY]: {} }

    for (const path of invalidateParams) {
        const [key, id] = path.split(DIVISION)

        cachedMappings[key][id] = get(mappings, `${key}.${id}`)
    }

    return cachedMappings
}
