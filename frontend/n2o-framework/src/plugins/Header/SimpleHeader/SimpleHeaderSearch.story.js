import React from 'react';
import { storiesOf } from '@storybook/react';

import { BrowserRouter } from 'react-router-dom';

import simpleHeaderMetadata from './simpleHeaderData.json';
import SimpleHeader from './SimpleHeader';
import Wireframe from '../../../components/snippets/Wireframe/Wireframe';

const stories = storiesOf('UI Компоненты/Меню сверху', module);

stories.addParameters({
  info: {
    propTablesExclude: [Wireframe],
  },
});
const menu = [
  {
    id: 'menuItem0',
    label: 'Контакты',
    href: '/proto',
    linkType: 'inner', // inner (внутри приложения) или outer (вне приложения)
    icon: 'fa fa-plus',
    description: 'some description',
  },
  {
    id: 'menuItem1',
    label: 'Модули',
    href: '/test',
    linkType: 'inner',
    icon: 'fa fa-check',
    description: 'some description 2',
  },
];
stories.add(
  'Компонент',
  () => {
    return (
      <div>
        <BrowserRouter>
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
          />
        </BrowserRouter>
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
