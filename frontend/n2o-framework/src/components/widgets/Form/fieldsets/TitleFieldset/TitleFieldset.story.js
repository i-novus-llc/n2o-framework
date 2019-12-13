import React from 'react';
import TitleFieldset from './TitleFieldset';
import { storiesOf } from '@storybook/react';

import meta from './TitleFieldset.meta.json';

const stories = storiesOf('UI Компоненты/TitleFieldset', module);

stories
  .add(
    'Компонент',
    () => {
      return (
        <TitleFieldset render={() => {}} title={'Заголовок'} showLine={true} />
      );
    },
    {
      info: {
        text: `
      Компонент 'TitleFieldset'
      ~~~js
      import TitleFieldset from 'n2o-framework/lib/components/widgets/Form/fieldsets/TitleFieldset/TitleFielset';
      
      <TitleFieldset render={render} title="Заголовок" showLine={true} />
      ~~~
      `,
      },
    }
  )
  .add(
    'Метаданные',
    () => {
      return <TitleFieldset render={() => {}} {...meta} />;
    },
    {
      info: {
        text: `
      Компонент 'TitleFieldset'
      ~~~js
      import TitleFieldset from 'n2o-framework/lib/components/widgets/Form/fieldsets/TitleFieldset/TitleFielset';
      
      <TitleFieldset 
          render={render}
          title="Тестовый заголовок"
          subTitle="Подзаголовок"
          showLine={true}
          className="h3"
       />
      ~~~
      `,
      },
    }
  )
  .add(
    'Заголовок',
    () => {
      return <TitleFieldset render={() => {}} title={'Заголовок'} />;
    },
    {
      info: {
        text: `
      Компонент 'TitleFieldset'
      ~~~js
      import TitleFieldset from 'n2o-framework/lib/components/widgets/Form/fieldsets/TitleFieldset/TitleFielset';
      
      <TitleFieldset 
          render={render}
          title="Заголовок"
       />
      ~~~
      `,
      },
    }
  )
  .add(
    'Заголовок и подзаголовок',
    () => {
      return (
        <TitleFieldset
          render={() => {}}
          title={'Заголовок'}
          subTitle={'Подзаголовок'}
        />
      );
    },
    {
      info: {
        text: `
      Компонент 'TitleFieldset'
      ~~~js
      import TitleFieldset from 'n2o-framework/lib/components/widgets/Form/fieldsets/TitleFieldset/TitleFielset';
      
      <TitleFieldset 
          render={render}
          title="Заголовок"
          subTitle="Подзаголовок"
       />
      ~~~
      `,
      },
    }
  )
  .add(
    'Без заголовков',
    () => {
      return <TitleFieldset render={() => {}} showLine={true} />;
    },
    {
      info: {
        text: `
      Компонент 'TitleFieldset'
      ~~~js
      import TitleFieldset from 'n2o-framework/lib/components/widgets/Form/fieldsets/TitleFieldset/TitleFielset';
      
      <TitleFieldset 
          render={render}
          showLine={true}
       />
      ~~~
      `,
      },
    }
  );
