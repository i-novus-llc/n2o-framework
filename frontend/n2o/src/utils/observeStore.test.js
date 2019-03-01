import mockStore from 'redux-mock-store';
import sinon from 'sinon';
import { setModel } from '../actions/models';
import reducers from '../reducers';
import observeStore from './observeStore';

describe('Проверка observeStore', () => {
  it('Подписывается на изменение', () => {
    const store = mockStore(reducers)({
      models: {
        resolve: {
          widgetName: {
            anyValue: {}
          }
        }
      }
    });
    const onChange = sinon.spy();
    const select = state => {
      return state.models.resolve.widgetName;
    };
    const observer = observeStore(store, select, onChange);
    store.dispatch(setModel('resolve', 'widgetName', { anotherValue: 'value' }));
    expect(onChange.calledOnce).toEqual(true);
    observer();
  });
});
