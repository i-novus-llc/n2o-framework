import React from 'react';
import { Provider } from 'react-redux';
import { mount, shallow } from 'enzyme';
import { OverlayPages } from './OverlayPages';
import mockStore from 'redux-mock-store';

const setup = propsOverride => {
  const props = {
    overlays: [
      {
        pageId: 'test',
        name: 'test',
        visible: true,
        mode: 'modal',
      },
    ],
  };

  return mount(
    <Provider
      store={mockStore()({ pages: { test: {} }, overlays: [{ name: 'test' }] })}
    >
      <OverlayPages {...props} {...propsOverride} />
    </Provider>
  );
};

describe('<OverlayPages />', () => {
  it('компонент должен отрисоваться', () => {
    const wrapper = setup();

    expect(wrapper.find('.n2o-overlay-pages').exists()).toBeTruthy();
  });

  it('должен отрисоваться modalPage', () => {
    const wrapper = setup();

    expect(wrapper.find('ModalPage').exists()).toBeTruthy();
  });
});
