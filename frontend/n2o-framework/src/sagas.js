import { defaultTo } from 'lodash';
import { all } from 'redux-saga/effects';
import pagesSagas from './sagas/pages';
import widgetsSagas from './sagas/widgets';
import actionsImplSagas from './sagas/actionsImpl';
import alertsSagas from './sagas/alerts';
import authSagas from './sagas/auth';
import { formPluginSagas } from './sagas/formPlugin';
import { fieldDependencySagas } from './sagas/fieldDependency';
import { metaSagas } from './sagas/meta';
import globalSagas from './sagas/global';
import { toolbarSagas } from './sagas/toolbar';
import { widgetDependencySagas } from './sagas/widgetDependency';
import { modalsSagas } from './sagas/modals';

export default function generateSagas(dispatch, config) {
  return function* rootSaga() {
    yield all([
      ...pagesSagas(config.apiProvider),
      ...widgetsSagas(config.apiProvider),
      ...actionsImplSagas(config.apiProvider),
      ...alertsSagas(config.messages),
      ...formPluginSagas,
      ...fieldDependencySagas.map(saga => saga(dispatch)),
      ...authSagas(config.security),
      ...metaSagas,
      ...globalSagas(config.apiProvider),
      ...toolbarSagas,
      ...widgetDependencySagas,
      ...modalsSagas,
      ...defaultTo(config.customSagas, []),
    ]);
  };
}
