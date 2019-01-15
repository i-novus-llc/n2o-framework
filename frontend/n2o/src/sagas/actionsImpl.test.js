import { resolveMapping, handleAction, handleInvoke, fetchInvoke, validate } from './actionsImpl';
import { runSaga } from 'redux-saga';
import { CALL_ACTION_IMPL } from '../constants/toolbar';
import * as api from './fetch';
import * as fetch from './actionsImpl';
import * as apiPovider from '../core/api';
import mockStore from 'redux-mock-store';

const store = mockStore()({
  widgets: {
    testKey: {
      validation: {}
    }
  },
  form: {
    testKey: {
      values: {
        some: 'value'
      }
    }
  }
});

const dataProvider = {
  method: 'POST',
  pathMapping: {
    __patients_id: {
      link: "models.resolve['__patients'].id"
    }
  },
  url: 'n2o/data/patients/:__patients_id/vip'
};

const action = {
  meta: {},
  payload: {
    modelLink: '',
    widgetId: '',
    dataProvider,
    data: {}
  }
};

const state = {
  models: {
    resolve: {
      __patients: {
        id: 111
      }
    }
  }
};

describe('Проверка саги actionsImpl', () => {
  it('Проверка генератора fetchInvoke', async () => {
    const fakeStore = {
      getState: () => state
    };
    api.default = jest.fn(() => Promise.resolve({ response: 'response from server' }));
    const promise = await runSaga(fakeStore, fetchInvoke, dataProvider, { id: 12345 }).done;
    const result = await Promise.resolve(promise);
    expect(result).toEqual({
      response: 'response from server'
    });
  });

  it('Проверка генератора resolveMapping', async () => {
    const fakeState = {
      getState: () => ({
        models: {
          resolve: {
            __patients: {
              id: 111
            }
          }
        }
      })
    };
    const promise = await runSaga(fakeState, resolveMapping, dataProvider, state);
    const result = await promise.done;
    expect(result).toEqual('n2o/data/patients/111/vip');
  });
});
