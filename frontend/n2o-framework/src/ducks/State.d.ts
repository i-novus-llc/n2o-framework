import type { State as DataSourceState } from './datasource/DataSource'
import type { State as ModelsState } from './models/Models'
import { FormsState } from './form/types'
import { TColumnsState } from './columns/types'
import { TOverlayState } from './overlays/types'
import { TPageState } from './pages/types'
import { TToolbarState } from './toolbar/types'
import { TUserState } from './user/types'
import { TWidgetState } from './widgets/types'
import { TGlobalState } from './global/TGlobalState'

export interface State {
    form?: FormsState
    datasource: DataSourceState
    models: ModelsState
    columns: TColumnsState
    overlays: TOverlayState
    pages: TPageState
    toolbar: TToolbarState
    user: TUserState
    widgets: TWidgetState
    global: TGlobalState
}
