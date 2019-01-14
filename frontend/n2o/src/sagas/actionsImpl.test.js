import { resolveMapping, handleAction, handleInvoke } from './actionsImpl';
import { runSaga } from 'redux-saga';
import { CALL_ACTION_IMPL } from '../constants/toolbar';

describe('Проверка саги actionsImpl', () => {
  it('Проверка генератора handleAction', async () => {
    const disptatched = [];
    const action = {
      id: 'test',
      payload: {
        actionSrc: 'perform',
        options: {
          containerKey: 'testWidget'
        }
      },
      type: CALL_ACTION_IMPL
    };
    const fakeStore = {
      getState: () => ({
        widgets: {
          testWidget: {
            validation: 'value'
          }
        },
        form: {
          testWidget: {
            values: {
              some: 'value'
            }
          }
        }
      }),
      dispatch: action => disptatched.push(action)
    };
    const gen = handleAction(action);
    console.log(gen.next().value);
    console.log(gen.next().value);
    console.log(gen.next().value);
    console.log(gen.next().value);
    console.log(gen.next().value);
    const result = await runSaga(fakeStore, handleAction, action);
    console.log(result.done);
    console.log(disptatched);
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
