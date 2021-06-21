import defaultTo from 'lodash/defaultTo'
import { all } from 'redux-saga/effects'

import pagesSagas from './sagas/pages'
import widgetsSagas from './sagas/widgets'
import actionsImplSagas from './sagas/actionsImpl'
import alertsSagas from './ducks/alerts/sagas'
import authSagas from './sagas/auth'
import { formPluginSagas } from './sagas/formPlugin'
import { fieldDependencySagas } from './sagas/fieldDependency'
import { metaSagas } from './sagas/meta'
import globalSagas from './sagas/global'
import { conditionsSaga } from './sagas/conditions'
import { widgetDependencySagas } from './sagas/widgetDependency'
import regionsSagas from './ducks/regions/sagas'
import { overlaysSagas } from './sagas/overlays'
import toolbarSagas from './ducks/toolbar/sagas'

export default function generateSagas(dispatch, config) {
    return function* rootSaga() {
        yield all([
            ...pagesSagas(config.apiProvider),
            ...widgetsSagas(config.apiProvider),
            ...actionsImplSagas(config.apiProvider, config.factories),
            ...alertsSagas(config.messages),
            ...formPluginSagas,
            ...fieldDependencySagas.map(saga => saga(dispatch)),
            ...authSagas(config.security),
            ...metaSagas,
            ...globalSagas(config.apiProvider),
            ...conditionsSaga,
            ...widgetDependencySagas,
            ...overlaysSagas,
            ...regionsSagas,
            ...toolbarSagas,
            ...defaultTo(config.customSagas, []),
        ])
    }
}
