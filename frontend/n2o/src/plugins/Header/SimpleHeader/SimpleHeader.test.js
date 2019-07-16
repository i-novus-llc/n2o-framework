import React from 'react';
import SimpleHeader from './SimpleHeader';
import { mount } from 'enzyme';
import { BrowserRouter as Router } from 'react-router-dom';
import { shallowToJson } from 'enzyme-to-json';

const props = {
  items: [
    {
      id: 'link',
      label: 'link',
      href: '/link',
      type: 'link',
      linkType: 'inner',
    },
    {
      id: 'dropdown',
      label: 'dropdown',
      type: 'dropdown',
      subItems: [
        {
          id: 'link1',
          label: 'link1',
          href: 'link1.ru',
          linkType: 'outer',
          badge: 'badge1',
          badgeColor: 'danger',
        },
      ],
    },
  ],
  extraItems: [
    {
      id: 'text',
      label: 'text',
      type: 'text',
    },
  ],
  brand: 'N2O',
  search: true,
  brandImage: '/image.svg',
};

const setup = (propOverrides, defaultProps = props) => {
  const props = Object.assign({}, defaultProps, propOverrides);

  const wrapper = mount(
    <Router>
      <SimpleHeader {...props} />
    </Router>
  );

  return {
    props,
    wrapper,
  };
};

describe('<SimpleHeader />', () => {
  it('проверяет создание компонента', () => {
    const { wrapper } = setup();
    wrapper.update();
    expect(shallowToJson(wrapper)).toMatchSnapshot();
  });
  it('проверяет props', () => {
    const { wrapper } = setup();
    const expectedValue = props.items[1];
    wrapper.update();
    expect(wrapper.find('NavItemContainer').get(1).props.item).toBe(
      expectedValue
    );
  });
  it('проверяет открытие меню', () => {
    const { wrapper } = setup();
    wrapper.update();
    wrapper.find('.dropdown-toggle').simulate('click');
    expect(wrapper.find('.dropdown-menu').hasClass('show')).toBeTruthy();
  });
  // it('проверяет, что активный элемент устанавливается правильно', () => {
  //   const { wrapper } = setup();
  //   wrapper.update();
  //   wrapper
  //     .find('.dropdown-menu')
  //     .first()
  //     .simulate('click');
  //   console.log(wrapper.find('.dropdown-item').debug());
  //   expect(
  //     wrapper
  //       .find('a.nav-link')
  //       .first()
  //       .hasClass('active')
  //   ).toBeTruthy();
  // });
  it('проверяет баджи', () => {
    const { wrapper } = setup();
    wrapper.update();
    expect(wrapper.find('Badge').exists()).toBeTruthy();
  });
});
