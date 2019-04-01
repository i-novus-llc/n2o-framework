import { all } from 'redux-saga/effects';
import { pagesSagas } from './sagas/pages';
import { widgetsSagas } from './sagas/widgets';
import { actionsImplSagas } from './sagas/actionsImpl';
import alertsSagas from './sagas/alerts';
import authSagas from './sagas/auth';
import { formPluginSagas } from './sagas/formPlugin';
import { dependenciesSagas } from './sagas/dependency';
import { metaSagas } from './sagas/meta';
import { globalSagas } from './sagas/global';
import { toolbarSagas } from './sagas/toolbar';
import { modalsSagas } from './sagas/modals';

export default function generateSagas(dispatch, config) {
  return function* rootSaga() {
    yield all([
      ...pagesSagas,
      ...widgetsSagas,
      ...actionsImplSagas,
      ...alertsSagas(config.messages),
      ...formPluginSagas,
      ...dependenciesSagas.map(saga => saga(dispatch)),
      ...authSagas(config.security),
      ...metaSagas,
      ...globalSagas,
      ...toolbarSagas,
      ...modalsSagas,
    ]);
  };
}
