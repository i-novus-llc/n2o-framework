import type { State as DataSourceState } from './datasource/DataSource'
import type { State as ModelsState } from './models/Models'
import { TGlobalState } from './global/TGlobalState'

export interface State {
    datasource: DataSourceState
    models: ModelsState
    global: TGlobalState
}
