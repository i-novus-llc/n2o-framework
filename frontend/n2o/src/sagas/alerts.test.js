import { addAlertSideEffect, getTimeout } from './alerts';
import { runSaga } from 'redux-saga';
import * as alerts from './alerts';

describe('Проверка саги alerts', () => {
  it('getTimeout должен вернуть тайм-аут', () => {
    const timeout1 = getTimeout({
      payload: {
        alerts: [
          {
            timeout: 100
          }
        ]
      }
    });
    const timeout2 = getTimeout(
      {
        payload: {
          alerts: [
            {
              severity: 'danger'
            }
          ]
        }
      },
      {
        timeout: {
          danger: 123
        }
      }
    );

    expect(timeout1).toEqual(100);
    expect(timeout2).toEqual(123);
  });

  it('Проверка генератора addAlertSideEffect', async () => {
    const dispatched = [];
    const fakeStore = {
      getState: () => ({
        alerts: {
          testAlert: {}
        }
      }),
      dispatch: action => dispatched.push(action)
    };
    alerts.getTimeout = jest.fn(() => Promise.resolve({ test: 'test' }));
    const config = {
      timeout: {
        danger: 1234
      }
    };
    const action = {
      payload: {
        key: 'testAlert',
        alerts: [
          {
            timeout: 100,
            severity: 'danger'
          }
        ]
      }
    };

    let gen = addAlertSideEffect(config, action);
    const result = await runSaga(fakeStore, addAlertSideEffect, config, action);

    const timeout = true;
    console.log(gen.next());
    console.log(gen.next());
    console.log(gen.next());
    console.log(gen.next());
  });
});
