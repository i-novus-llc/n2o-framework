import React from 'react';
import sinon from 'sinon';
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import reducers from '../../reducers';
import withObserveDependency from './withObserveDependency';
import { registerFieldDependency } from '../../actions/formPlugin';

const setup = (store, props = {}, onChange) => {
  const Component = withObserveDependency({
    onChange
  })(() => <div>test</div>);
  return mount(
    <Provider store={store}>
      <Component {...props} />
    </Provider>
  );
};

describe('Проверка хока withObserveDependency', () => {
  it('подписывается на события', () => {
    const store = createStore(reducers);
    store.dispatch(
      registerFieldDependency('testForm', 'testField', [
        {
          type: 'reRender',
          on: ['anotherTestField']
        }
      ])
    );
    const wrapper = setup(
      store,
      {
        id: 'testField',
        form: 'testForm',
        dependency: [
          {
            type: 'reRender',
            on: ['testForm']
          }
        ]
      },
      () => {}
    );
    // expect(wrapper._observers).toEqual([])
  });
});
