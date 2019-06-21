import { runSaga } from 'redux-saga';
import { getConfig } from './global';
import { REQUEST_CONFIG } from '../constants/global';
import * as api from '../core/api';
import { requestConfigSuccess, requestConfigFail } from '../actions/global';

describe('Проверка саги global', () => {
  it('Должен получить конфиг', async () => {
    const action = {
      meta: {},
      payload: {
        params: undefined,
      },
      type: REQUEST_CONFIG,
    };
    const config = {
      menu: {},
      page: {},
    };
    const dispatched = [];

    const fakeStore = {
      getState: () => ({
        global: {
          locale: 'ru_RU',
        },
      }),
      dispatch: action => dispatched.push(action),
    };

    api.default = jest.fn(() => Promise.resolve(config));

    await runSaga(fakeStore, getConfig, action);
    const requestConfigSuccessAction = requestConfigSuccess(config);
    expect(dispatched[2]).toEqual(requestConfigSuccessAction);
  });

  it('Должна выпасть ошибка', async () => {
    const errorObject = {
      label: 'Ошибка',
      text: 'Не удалось получить конфигурацию приложения',
      closeButton: false,
      severity: 'danger',
    };
    const gen = getConfig();
    gen.next();
    expect(gen.next().value.PUT.action).toEqual(requestConfigFail(errorObject));
  });
});
