import defaultTo from 'lodash/defaultTo'
import { all } from 'redux-saga/effects'

import { sagas as regionsSagas } from './ducks/regions/sagas'
import { sagas as wsSaga } from './sagas/notifications/notifications'
import pagesSagas from './ducks/pages/sagas'
import widgetsSagas from './ducks/widgets/sagas'
import dataSourceSagas from './ducks/datasource/sagas'
import actionsImplSagas from './sagas/actionsImpl'
import alertsSagas from './ducks/alerts/sagas'
import authSagas from './ducks/user/sagas'
import { formPluginSagas } from './ducks/form/sagas'
import { modelSagas } from './ducks/models/sagas'
import { fieldDependencySagas } from './sagas/fieldDependency'
import { metaSagas } from './sagas/meta'
import globalSagas from './ducks/global/sagas'
import { conditionsSaga } from './sagas/conditions'
import { widgetDependencySagas } from './sagas/widgetDependency'
import { overlaysSagas } from './ducks/overlays/sagas'
import toolbarSagas from './ducks/toolbar/sagas'
import { addComponent, removeComponent } from './ducks/datasource/store'
import { requestConfigSuccess } from './ducks/global/store'
import { updateModel } from './ducks/models/store'
import { sagas as apiSagas } from './ducks/api/sagas'
import { sagas as tableSagas } from './ducks/table/sagas'

const webSocketConfig = {
    observables: [addComponent, removeComponent, requestConfigSuccess],
    updater: updateModel,
    source: 'datasource',
    connected: 'components',
}

export default function generateSagas(dispatch, config) {
    return function* rootSaga() {
        yield all([
            ...pagesSagas(config.apiProvider),
            ...widgetsSagas(config.apiProvider),
            ...dataSourceSagas(config.apiProvider),
            ...actionsImplSagas(config.apiProvider, config.factories),
            ...alertsSagas(config.messages),
            ...formPluginSagas,
            ...modelSagas,
            ...fieldDependencySagas,
            ...authSagas(config.security),
            ...metaSagas,
            ...globalSagas(config.apiProvider),
            ...conditionsSaga,
            ...widgetDependencySagas,
            ...overlaysSagas,
            ...regionsSagas,
            ...toolbarSagas,
            ...tableSagas,
            ...defaultTo(config.customSagas, []),
            ...wsSaga(webSocketConfig),
            ...apiSagas,
        ])
    }
}
