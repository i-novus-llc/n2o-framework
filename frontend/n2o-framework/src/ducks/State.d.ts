import type { State as DataSourceState } from './datasource/DataSource'
import type { State as ModelsState } from './models/Models'

export interface State {
    datasource: DataSourceState
    models: ModelsState
}
