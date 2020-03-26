import React from 'react';
import { storiesOf } from '@storybook/react';

import simpleHeaderMetadata from './simpleHeaderData.json';
import SimpleHeader from './SimpleHeader';
import Wireframe from '../../../components/snippets/Wireframe/Wireframe';

const stories = storiesOf('UI Компоненты/Меню сверху', module);

stories.addParameters({
  info: {
    propTablesExclude: [Wireframe],
  },
});

stories.add(
  'Компонент',
  () => {
    return (
      <div>
        <SimpleHeader
          brandImage={
            'https://avatars0.githubusercontent.com/u/25926683?s=200&v=4'
          }
          activeId={'link'}
          fixed={false}
          items={simpleHeaderMetadata.items}
          extraItems={simpleHeaderMetadata.extraItems}
          color="inverse"
          search={true}
          list={[
            {
              id: 'menuItem1',
              label: 'Контакты',
              href: '/proto',
              linkType: 'inner',
              icon: 'fa fa-plus',
              description: 'some description',
            },
            {
              id: 'menuItem2',
              label: 'Модули',
              href: '/test',
              linkType: 'inner',
              icon: 'fa fa-check',
              description: 'some description 2',
            },
            {
              id: 'menuItem3',
              label: 'Контент',
              href: '/test',
              linkType: 'inner',
              icon: 'fa fa-check',
            },
            {
              id: 'menuItem4',
              label: 'Комбинация',
              href: '/test',
              linkType: 'inner',
              icon: 'fa fa-database',
              description: 'some description 2',
            },
            {
              id: 'menuItem5',
              label: 'Мокап',
              href: '/test',
              linkType: 'inner',
              icon: 'fa fa-book',
              description: 'some description 2',
            },
          ]}
        />
        <div
          style={{
            padding: '200px',
            position: 'relative',
          }}
        >
          <Wireframe className="n2o" title="Тело страницы" />
        </div>
      </div>
    );
  },
  {
    info: {
      text: `
    Компонент 'Меню'
    ~~~js
    import SimpleHeader from 'n2o-framework/lib/plugins/Header/SimpleHeader/SimpleHeader';
    
    <SimpleHeader
      brandImage="https://avatars0.githubusercontent.com/u/25926683?s=200&v=4"
      activeId={'link'}
      fixed={false}
      items={simpleHeaderMetadata.items}
      extraItems={simpleHeaderMetadata.extraItems}
      color="inverse"
    />
    ~~~
    `,
    },
  }
);

SimpleHeader.defaultProps = {
  color: 'default',
  fixed: true,
  collapsed: true,
  className: '',
  items: [],
  extraItems: [],
  itemsWithBadges: [],
  extraItemsWithBadges: [],
  search: false,
  style: {},
};
