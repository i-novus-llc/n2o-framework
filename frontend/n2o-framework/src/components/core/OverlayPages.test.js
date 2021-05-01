import React from 'react';
import { Provider } from 'react-redux';
import { mount, shallow } from 'enzyme';
import { OverlayPages } from './OverlayPages';
import mockStore from 'redux-mock-store';

const setup = propsOverride => {
  return mount(
    <Provider
      store={mockStore()({ pages: { test: {} }, overlays: [{ name: 'test' }] })}
    >
      <OverlayPages {...propsOverride} />
    </Provider>
  );
};

describe('<OverlayPages />', () => {
  it('Мод modal', () => {
    const wrapper = setup({
      overlays: [
        {
          pageId: 'test',
          name: 'test',
          visible: true,
          mode: 'modal',
        },
      ],
    });

    expect(wrapper.find('.n2o-overlay-pages').exists()).toBeTruthy();
  });
});

describe('<OverlayPages />', () => {
  it('Мод dialog', () => {
    const wrapper = setup({
      overlays: [
        {
          pageId: 'test',
          name: 'test',
          visible: true,
          mode: 'dialog',
        },
      ],
    });

    expect(wrapper.find('.n2o-overlay-pages').exists()).toBeTruthy();
  });
});

describe('<OverlayPages />', () => {
  it('Мод drawer', () => {
    const wrapper = setup({
      overlays: [
        {
          pageId: 'test',
          name: 'test',
          visible: true,
          mode: 'drawer',
        },
      ],
    });

    expect(wrapper.find('.n2o-overlay-pages').exists()).toBeTruthy();
  });
});
