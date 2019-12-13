import React from 'react';
import { Provider } from 'react-redux';
import { mount, shallow } from 'enzyme';
import { ModalPages } from './ModalPages';
import mockStore from 'redux-mock-store';

const setup = propsOverride => {
  const props = {
    modals: [
      {
        pageId: 'test',
        name: 'test',
        visible: true,
      },
    ],
  };

  return mount(
    <Provider
      store={mockStore()({ pages: { test: {} }, modals: { name: 'test' } })}
    >
      <ModalPages {...props} {...propsOverride} />
    </Provider>
  );
};

describe('<ModalPages />', () => {
  it('компонент должен отрисоваться', () => {
    const wrapper = setup();

    expect(wrapper.find('.n2o-modal-pages').exists()).toBeTruthy();
  });

  it('должен отрисоваться modalPage', () => {
    const wrapper = setup();

    expect(wrapper.find('ModalPage').exists()).toBeTruthy();
  });
});
