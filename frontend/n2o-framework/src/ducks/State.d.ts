import { State as DataSourceState } from './datasource/DataSource'
import { State as ModelsState } from './models/Models'

export interface State {
    datasource: DataSourceState
    models: ModelsState
}