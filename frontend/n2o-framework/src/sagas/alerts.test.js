import {
  removeAlertSideEffect,
  getTimeout,
  addAlertSideEffect,
} from './alerts';
import { runSaga } from 'redux-saga';
import { removeAlert } from '../actions/alerts';
import { REMOVE } from '../constants/alerts';

const delay = ms => new Promise(r => setTimeout(r, ms));

describe('Проверка саги alerts', () => {
  it('getTimeout должен вернуть тайм-аут', () => {
    const timeout1 = getTimeout({
      timeout: 100,
    });
    const timeout2 = getTimeout(
      {
        severity: 'danger',
      },
      {
        timeout: {
          danger: 123,
        },
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
            id: 'test',
            data: {
              name: 'test',
            },
            severity: 'success',
            text: 'Success',
          },
        ],
      },
    };
    const dispatched = [];
    const fakeStore = {
      getState: () => ({
        alerts: {
          testKey: {
            some: 'value',
          },
        },
      }),
      dispatch: action => dispatched.push(action),
    };
    const result = await runSaga(fakeStore, removeAlertSideEffect, action, 1);
    await Promise.resolve(result.toPromise());
    expect(dispatched[0]).toEqual(removeAlert('testKey'));
  });

  it('Проверка генератора addAlertSideEffect', async () => {
    const dispatched = [];
    const fakeStore = {
      getState: () => ({
        alerts: {
          testAlert: {},
        },
      }),
      dispatch: action => dispatched.push(action),
    };
    const config = {};
    const action = {
      payload: {
        key: 'testAlert',
        alerts: [
          {
            id: 'alert1',
            timeout: 200,
          },
          {
            id: 'alert2',
            timeout: 200,
          },
        ],
      },
    };
    await runSaga(fakeStore, addAlertSideEffect, config, action);
    await delay(300);
    expect(dispatched[0].type).toBe(REMOVE);
    expect(dispatched[0].payload).toEqual({
      id: 'alert1',
      key: 'testAlert',
    });
    expect(dispatched[1].payload).toEqual({
      id: 'alert2',
      key: 'testAlert',
    });
  });
});
