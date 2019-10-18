import React from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter as Router } from 'react-router-dom';
import mockStore from 'redux-mock-store';
import { mount, shallow } from 'enzyme';
import HeaderFooterTemplate from './HeaderFooterTemplate';

const setup = propsOverride => {
  const props = {};

  return mount(
    <Provider store={mockStore()({})}>
      <Router>
        <HeaderFooterTemplate {...props} {...propsOverride} />
      </Router>
    </Provider>
  );
};

describe('<HeaderFooterTemplate />', () => {
  it('компонент должен отрисоваться', () => {
    const wrapper = setup();

    expect(wrapper.find('.application').exists()).toBeTruthy();
  });

  it('должны отрисоваться вложенные компоненты', () => {
    const wrapper = setup();

    expect(wrapper.find('SimpleHeader').exists()).toBeTruthy();
    expect(wrapper.find('.application-body').exists()).toBeTruthy();
    expect(wrapper.find('Footer').exists()).toBeTruthy();
  });
});
