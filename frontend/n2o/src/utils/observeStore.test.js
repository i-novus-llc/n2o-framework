import mockStore from 'redux-mock-store';
import sinon from 'sinon';
import { setModel } from '../actions/models';
import reducers from '../reducers';
import history from '../history';
import observeStore from './observeStore';
import { createStore } from 'redux';

const store = createStore(reducers(history), {
  models: {
    resolve: {
      test: {
        value: 'test value',
      },
      widgetName: {
        value: 'value',
      },
    },
  },
});

describe('Проверка observeStore', () => {
  it('Подписывается на изменение', () => {
    const onChange = sinon.spy();
    const select = state => {
      return state.models.resolve.widgetName;
    };
    const observer = observeStore(store, select, onChange);
    store.dispatch(
      setModel('resolve', 'widgetName', { anotherValue: 'value' })
    );
    expect(onChange.called).toEqual(true);
    observer();
  });
});
