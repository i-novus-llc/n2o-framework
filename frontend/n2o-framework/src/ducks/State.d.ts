import type { State as DataSourceState } from './datasource/DataSource'
import type { State as ModelsState } from './models/Models'
import { FormsState } from './form/types'
import { State as ColumnsState } from './columns/Columns'
import { State as OverlaysState } from './overlays/Overlays'
import { State as RegionsState } from './regions/Regions'
import { State as PagesState } from './pages/Pages'
import { State as ToolbarState } from './toolbar/Toolbar'
import { State as UserState } from './user/User'
import { State as WidgetsState } from './widgets/Widgets'
import { State as GlobalState, RouterState } from './global/Global'
import { State as AlertsState } from './alerts/Alerts'

export interface State {
    form?: FormsState
    alerts: AlertsState
    datasource: DataSourceState
    models: ModelsState
    columns: ColumnsState
    overlays: OverlaysState
    pages: PagesState
    toolbar: ToolbarState
    user: UserState
    widgets: WidgetsState
    global: GlobalState
    router: RouterState
    regions: RegionsState
}
