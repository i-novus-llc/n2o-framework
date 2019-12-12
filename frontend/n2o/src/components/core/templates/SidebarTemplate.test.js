import React from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter as Router } from 'react-router-dom';
import mockStore from 'redux-mock-store';
import { mount, shallow } from 'enzyme';
import SidebarTemplate from './SidebarTemplate';

const setup = propsOverride => {
  const props = {};

  return mount(
    <Provider store={mockStore()({})}>
      <Router>
        <SidebarTemplate {...props} {...propsOverride} />
      </Router>
    </Provider>
  );
};

describe('<SidebarTemplate />', () => {
  it('компонент должен отрисоваться', () => {
    const wrapper = setup();

    expect(wrapper.find('.application').exists()).toBeTruthy();
  });

  it('должны отрисоваться вложенные компоненты', () => {
    const wrapper = setup();

    expect(wrapper.find('.body-container').exists()).toBeTruthy();
    expect(wrapper.find('SideBar').exists()).toBeTruthy();
    expect(wrapper.find('.application-body').exists()).toBeTruthy();
    expect(wrapper.find('Footer').exists()).toBeTruthy();
  });
});
