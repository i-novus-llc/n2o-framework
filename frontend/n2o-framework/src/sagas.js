import defaultTo from 'lodash/defaultTo'
import { all } from 'redux-saga/effects'
import { sagas as wsSaga } from 'n2o-notifications'

import pagesSagas from './ducks/pages/sagas'
import widgetsSagas from './ducks/widgets/sagas'
import dataSourceSagas from './ducks/datasource/sagas'
import actionsImplSagas from './sagas/actionsImpl'
import alertsSagas from './ducks/alerts/sagas'
import authSagas from './ducks/user/sagas'
import { formPluginSagas } from './ducks/form/sagas'
import { fieldDependencySagas } from './sagas/fieldDependency'
import { metaSagas } from './sagas/meta'
import globalSagas from './ducks/global/sagas'
import { conditionsSaga } from './sagas/conditions'
import { widgetDependencySagas } from './sagas/widgetDependency'
import regionsSagas from './ducks/regions/sagas'
import { overlaysSagas } from './ducks/overlays/sagas'
import toolbarSagas from './ducks/toolbar/sagas'
import { addComponent, removeComponent } from './ducks/datasource/store'
import { updateModel } from './ducks/models/store'

/* FIXME remove mock wsUrl */

const webSocketConfig = {
    observables: [addComponent, removeComponent],
    updater: updateModel,
    source: 'datasource',
    connected: 'components',
    wsUrl: 'http://yandex.develop:8140/ws/?access_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjhhZDRhMDBkLTRlMGItNDA2MC1iYTViLWNjNjZlZTQ0YTgwMCJ9.eyJyb2xlcyI6WyJVU0VSIiwiQURNSU4iXSwiY2xpZW50X2lkIjoiY2ciLCJzaWQiOiIyNkQyNEI3MkE3ODEwODRFMzc3RDIyMkZFNDQ5REEwNyIsInBhdHJvbnltaWMiOiLQkC4iLCJzeXN0ZW1zIjpbImFjY2VzcyIsImNnIl0sInN1cm5hbWUiOiLQnNGD0YHQutCw0YLQvtCy0LAiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwibmFtZSI6ItCe0LvRjNCz0LAiLCJyZWdpb24iOiIxNiIsImV4cCI6MTY0MDI2MTQwOCwiZW1haWwiOiJtdXNrYXRvdmFAbWFpbC5ydSIsImp0aSI6IjQzMWU3MDY2LTZmZjctNDgwYy1iYTgxLWMxYWE3ZmVjNTBlMyIsInVzZXJuYW1lIjoiNDQzLTA5NS02NjQgODcifQ.uo3LSLK1gfdw02Wt3TiZuB0eQhS6C3WaCwSHhnN-fEfgIac__KQ1lbam6z3ODchMrRl6kTjmA0GV6Z_5NwBJjfHI11dNC5EvghubXDsDr3_Pv4c-t1CoWA_-9lIfs2sLanb7MKUh_NsEsWuaEgPPECQVwm6FRxGYFFAzsj6etvcNKUNmaONVcaiL9ErZd02jxCfs-9p5muZLxF95tQFbDdKyaiL6QUs7Kn8uZmpf_aHpLBj2d3OzcPEKCNEI1TmMc6FNkIk-vjSyftPq-0YXUobFvJfDZOI-4cUfI4gwSJA6vtr9sb-y44WCXdmuAhegRXvutGiaCqINTA6nyMaabQ',
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
            ...wsSaga(webSocketConfig),
        ])
    }
}
