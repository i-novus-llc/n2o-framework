import React from 'react';
import { Provider } from 'react-redux';
import { WidgetAlerts } from './WidgetAlerts';
import configureMockStore from 'redux-mock-store';
import sinon from 'sinon';

const store = configureMockStore();

const props = {
  onDismiss: () => {},
  alerts: [
    {
      text: 'test-alert',
      id: 'test',
      position: 'absolute',
      visible: true,
    },
  ],
};

const setup = propOverrides => {
  return mount(
    <Provider store={store}>
      <WidgetAlerts {...props} {...propOverrides} />
    </Provider>
  );
};

describe('<WidgetAlerts />', () => {
  it('проверяет props', () => {
    const wrapper = setup();
    const props = wrapper.find('Alert').props();
    expect(props.id).toEqual('test');
    expect(props.position).toEqual('absolute');
    expect(props.visible).toEqual(true);
  });
  it('проверяет onDismiss', () => {
    const onDismiss = sinon.spy();
    const wrapper = setup({ onDismiss: onDismiss });
    wrapper.find('.close.n2o-alert-close').simulate('click');
    expect(onDismiss.calledOnce).toEqual(true);
  });
});
