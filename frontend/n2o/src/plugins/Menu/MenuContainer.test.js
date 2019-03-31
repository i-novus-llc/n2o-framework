import React from 'react';
import { MenuContainer } from './MenuContainer';

const setup = () => {
  return mount(<MenuContainer render={() => null} />);
};

describe('Проверка MenuContainer', () => {
  it('правильно удаляет subItems', () => {
    const wrapper = setup();
    wrapper.setState({
      items: [
        {
          id: 'menuItem0',
          label: 'ссылка',
          href: '/',
          type: 'link',
          icon: 'icon',
          linkType: 'outer',
          security: {
            roles: ['admin'],
          },
        },
        {
          id: 'menuItem1',
          label: 'список',
          href: '/pageRoute',
          type: 'dropdown',
          linkType: 'inner',
          subItems: [
            {
              id: 'menuItem2',
              label: 'Название страницы',
              href: '/pageRoute',
              type: 'link',
              linkType: 'inner',
            },
            {
              id: 'menuItem3',
              label: 'элемент списка №2',
              href: '/pageRoute1',
              type: 'link',
              linkType: 'inner',
            },
          ],
        },
      ],
      extraItems: [],
    });
    wrapper.instance().setSubItem(
      {
        id: 'menuItem2',
        label: 'Название страницы',
        href: '/pageRoute',
        type: 'link',
        linkType: 'inner',
      },
      'items',
      'menuItem1'
    );
    expect(wrapper.state().items[1].subItems).toEqual([
      {
        id: 'menuItem3',
        label: 'элемент списка №2',
        href: '/pageRoute1',
        type: 'link',
        linkType: 'inner',
      },
    ]);
  });

  it('Правильно удаляет пустые dropdown', () => {
    const wrapper = setup();
    wrapper.setState({
      items: [
        {
          id: 'menuItem0',
          label: 'ссылка',
          href: '/',
          type: 'link',
          icon: 'icon',
          linkType: 'outer',
          security: {
            roles: ['admin'],
          },
        },
        {
          id: 'menuItem1',
          label: 'список',
          href: '/pageRoute',
          type: 'dropdown',
          linkType: 'inner',
          subItems: [],
        },
      ],
    });

    expect(wrapper.instance().mapRenderProps().items).toEqual([
      {
        id: 'menuItem0',
        label: 'ссылка',
        href: '/',
        type: 'link',
        icon: 'icon',
        linkType: 'outer',
        security: {
          roles: ['admin'],
        },
      },
    ]);
  });
});
