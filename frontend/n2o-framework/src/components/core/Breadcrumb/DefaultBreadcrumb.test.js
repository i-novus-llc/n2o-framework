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

const setup2 = propsOverride => {
  const props = {
    items: [
      {
        label: 'First',
        path: '/first',
        title: 'FirstTitle',
      },
      {
        label: 'Second',
      },
      {
        label: 'Third',
        path: '/third',
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
  it('Компонент должен отрисоваться, тест на setup2', () => {
    const wrapper = setup2();

    expect(wrapper.find('Breadcrumb').exists()).toBeTruthy();
  });
  it('если отсутствует path, элемент не ссылка', () => {
    const wrapper = setup2();

    expect(
      wrapper
        .find('BreadcrumbItem')
        .at(1)
        .find('Link')
        .exists()
    ).toBeFalsy();
  });
  it('если отсутствует path элемент не ссылка', () => {
    const wrapper = setup2();

    expect(
      wrapper
        .find('BreadcrumbItem')
        .at(1)
        .find('Link')
        .exists()
    ).toBeFalsy();
  });
});
