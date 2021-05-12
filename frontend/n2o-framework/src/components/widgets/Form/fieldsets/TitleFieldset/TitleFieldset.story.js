import React from 'react'
import { storiesOf } from '@storybook/react'

import TitleFieldset from './TitleFieldset'
import meta from './TitleFieldset.meta.json'

const stories = storiesOf('UI Компоненты/TitleFieldset', module)

stories
    .add(
        'Компонент',
        () => (
            <TitleFieldset render={() => {}} title="Заголовок" showLine />
        ),
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
        },
    )
    .add(
        'Метаданные',
        () => <TitleFieldset render={() => {}} {...meta} />,
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
        },
    )
    .add(
        'Заголовок',
        () => <TitleFieldset render={() => {}} title="Заголовок" />,
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
        },
    )
    .add(
        'Заголовок и подзаголовок',
        () => (
            <TitleFieldset
                render={() => {}}
                title="Заголовок"
                subTitle="Подзаголовок"
            />
        ),
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
        },
    )
    .add(
        'Без заголовков',
        () => <TitleFieldset render={() => {}} showLine />,
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
        },
    )
