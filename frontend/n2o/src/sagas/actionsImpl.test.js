import { resolveMapping, handleAction, handleInvoke } from './actionsImpl';
import { runSaga } from 'redux-saga';

describe('Проверка саги actionsImpl', () => {
  it('Проверка генератора handleAction', async () => {
    const disptatched = [];
    const action = {};
    const fakeStore = {
      getState: () => ({}),
      dispatch: action => disptatched.push(action)
    };
    const result = await runSaga(fakeStore, handleAction, action);
  });
  it('Проверка генератора resolveMapping', async () => {
    const dataProvider = {
      method: 'POST',
      pathMapping: {
        __patients_id: {
          link: "models.resolve['__patients'].id"
        }
      },
      url: 'n2o/data/patients/:__patients_id/vip'
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
