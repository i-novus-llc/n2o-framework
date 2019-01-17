import { removeAlertSideEffect, getTimeout } from './alerts';
import { select } from 'redux-saga/effects';
import { runSaga } from 'redux-saga';
import { makeAlertsByKeySelector } from '../selectors/alerts';
import { removeAlerts } from '../actions/alerts';

describe('Проверка саги alerts', () => {
  it('getTimeout должен вернуть тайм-аут', () => {
    const timeout1 = getTimeout({
      timeout: 100
    });
    const timeout2 = getTimeout(
      {
        severity: 'danger'
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

  it('Проверка генератора removeAlertSideEffect', async () => {
    const key = 'testKey';
    const action = {
      meta: {},
      payload: {
        key: 'testKey',
        alerts: [
          {
            data: {
              name: 'test'
            },
            severity: 'success',
            text: 'Success'
          }
        ]
      }
    };
    const dispatched = [];
    const fakeStore = {
      getState: () => ({
        alerts: {
          testKey: {
            some: 'value'
          }
        }
      }),
      dispatch: action => dispatched.push(action)
    };
    const result = await runSaga(fakeStore, removeAlertSideEffect, action, 1);
    await Promise.resolve(result.done);
    expect(dispatched[0]).toEqual(removeAlerts('testKey'));
  });
});
