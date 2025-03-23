import { QueryResult } from '../../Provider'

export type CacheData = QueryResult & {
    timestamp: string
    mappings: Record<'baseQuery' | 'pathParams', Record<string, string | number>>
}
