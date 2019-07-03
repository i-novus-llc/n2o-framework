import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import NavItemContainer from './NavItemContainer';

const setup = (itemOverride = {}) => {
  const props = {
    item: {
      id: 'menuItem1',
      label: 'список',
      href: '/pageRoute',
      type: 'link',
      icon: 'fa fa-user',
      linkType: 'inner',
      ...itemOverride,
    },
  };

  return mount(
    <Router>
      <Route path="*" render={() => <NavItemContainer {...props} />} />
    </Router>
  );
};

describe('<NavItemContainer/>', () => {
  it('Компонент отрисовывается', () => {
    const wrapper = setup();
    expect(wrapper.find('a').exists()).toEqual(true);
  });

  it('Отрисовывается иконка во внутренней ссылке', () => {
    const wrapper = setup();
    expect(wrapper.find('.fa-user').exists()).toEqual(true);
  });

  it('Отрисовывается иконка во внешней ссылке', () => {
    const wrapper = setup({ linkType: 'outer' });
    expect(wrapper.find('.fa-user').exists()).toEqual(true);
  });

  it('Отрисовывается иконка в dropdown ссылке', () => {
    const wrapper = setup({
      type: 'dropdown',
      subItems: [
        {
          id: 'menuItem2',
          label: 'Название страницы',
          href: '/pageRoute',
          type: 'link',
          icon: 'fa fa-user',
          linkType: 'inner',
        },
        {
          id: 'menuItem3',
          label: 'элемент списка №2',
          href: '/pageRoute1',
          type: 'link',
          icon: 'fa fa-user',
          linkType: 'inner',
        },
      ],
    });
    expect(wrapper.find('.fa-user').length).toEqual(3);
  });
});
