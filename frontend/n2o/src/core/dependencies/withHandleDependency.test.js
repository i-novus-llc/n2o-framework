import React from 'react';
import { Provider } from 'react-redux';
import mockStore from 'redux-mock-store';
import withHandleDependency from './withHandleDependency';
import sinon from 'sinon';
import * as obs from '../../utils/observeStore';

class Wrapped extends React.Component {
  render() {
    return <div />;
  }
}

const setup = (component, onChange, store, props = {}) => {
  const Component = withHandleDependency({
    onChange
  })(component);
  return mount(
    <Provider store={store}>
      <Component {...props} />
    </Provider>
  );
};

describe('Проверка хока withHandleDependency', () => {
  it('hoc подпишется на изменение стора', () => {
    const fake = sinon.spy();
    obs.default = fake;
    const store = mockStore()({});
    const wrapper = setup(Wrapped, () => {}, store);
    expect(fake.calledOnce).toEqual(true);
  });
});
