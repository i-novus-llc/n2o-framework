import { combineReducers } from 'redux'
import { connectRouter } from 'connected-react-router'

import widgets from './ducks/widgets/store'
import models from './ducks/models/store'
import global from './ducks/global/store'
import pages from './ducks/pages/store'
import datasource from './ducks/datasource/store'
import overlays from './ducks/overlays/store'
import columns from './ducks/columns/store'
import toolbar from './ducks/toolbar/store'
import form from './ducks/form/store'
import user from './ducks/user/store'
import alerts from './ducks/alerts/store'
import regions from './ducks/regions/store'

export default (history, customReducers = {}) => combineReducers({
    alerts,
    columns,
    datasource,
    form,
    global,
    models,
    overlays,
    pages,
    regions,
    toolbar,
    user,
    router: connectRouter(history),
    widgets,
    ...customReducers,
})
