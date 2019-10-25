import React from 'react';
import { mount, shallow } from 'enzyme';
import { BrowserRouter as Router } from 'react-router-dom';
import DefaultBreadcrumb from './DefaultBreadcrumb';

const setup = propsOverride => {
  const props = {
    items: [
      {
        label: 'First',
        path: '/first',
      },
      {
        label: 'Second',
        path: '/second',
      },
    ],
  };

  return mount(
    <Router>
      <DefaultBreadcrumb {...props} {...propsOverride} />
    </Router>
  );
};

describe('<DefaultBreadcrumb />', () => {
  it('Компонент должен отрисоваться', () => {
    const wrapper = setup();

    expect(wrapper.find('Breadcrumb').exists()).toBeTruthy();
  });

  it('должны отрисоваться 2 элемента', () => {
    const wrapper = setup();

    expect(wrapper.find('BreadcrumbItem').length).toBe(2);
  });

  it('ссылкой должен быть только первый элемент', () => {
    const wrapper = setup();

    expect(
      wrapper
        .find('BreadcrumbItem')
        .first()
        .find('Link')
        .exists()
    ).toBeTruthy();
    expect(
      wrapper
        .find('BreadcrumbItem')
        .at(1)
        .text()
    ).toBe('Second');
  });
});
