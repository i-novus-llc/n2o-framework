import type { ModelPrefix } from '../../core/datasource/const'

export type State<TModel extends object = object, TFilter extends object = TModel> = {
    [ModelPrefix.source]: Record<string, TModel[]>
    [ModelPrefix.selected]: Record<string, TModel[]>
    [ModelPrefix.active]: Record<string, Partial<TModel>>
    [ModelPrefix.edit]: Record<string, Partial<TModel>>
    [ModelPrefix.filter]: Record<string, Partial<TFilter>>
}
