import { runSaga } from 'redux-saga';

import { changeColumnVisiblity } from '../actions/columns';
import { resolveColumn } from './column';

const state = {
  models: {
    resolve: {
      someWidget: {
        name: 'Лада',
      },
    },
  },
};

describe('Проверка саги column', () => {
  it('генератор resolveColumn должен показать колонку', async () => {
    const dispatched = [];
    const fakeStore = {
      getState: () => Object.assign({}, state),
      dispatch: action => dispatched.push(action),
    };

    await runSaga(fakeStore, resolveColumn, {
      key: 'someWidget',
      columnId: 'surname',
      conditions: {
        visible: [
          {
            expression: "name !== 'Лада'",
            modelLink: 'models.resolve.someWidget',
          },
        ],
      },
    });
    expect(dispatched[0]).toEqual(
      changeColumnVisiblity('someWidget', 'surname', false)
    );
  });

  it('генератор resolveColumn должен скрыть колонку', async () => {
    const dispatched = [];
    const fakeStore = {
      getState: () => Object.assign({}, state),
      dispatch: action => dispatched.push(action),
    };

    await runSaga(fakeStore, resolveColumn, {
      key: 'someWidget',
      columnId: 'surname',
      conditions: {
        visible: [
          {
            expression: "name === 'Лада'",
            modelLink: 'models.resolve.someWidget',
          },
        ],
      },
    });
    expect(dispatched[0]).toEqual(
      changeColumnVisiblity('someWidget', 'surname', true)
    );
  });
});
