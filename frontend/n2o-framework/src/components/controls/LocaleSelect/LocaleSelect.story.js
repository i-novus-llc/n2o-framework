import React from 'react';
import { storiesOf } from '@storybook/react';
import LocaleSelect from './LocaleSelect';

const stories = storiesOf('Контролы/Выбор локализации', module);

stories.add(
  'N2O вариант',
  () => {
    return <LocaleSelect />;
  },
  {
    info: {
      text: `
    Компонент 'Выпадающий список N2O'
    ~~~js
    import N2OSelect from 'n2o-framework/lib/components/controls/N2OSelect/N2OSelect';
    
    const options = [
        {
          id: 'Алексей',
          icon: 'fa fa-square',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Игорь',
          icon: 'fa fa-plus',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Владимир',
          icon: 'fa fa-square',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Анатолий',
          icon: 'fa fa-square',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Николай',
          icon: 'fa fa-plus',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
     ];
     
    <N2OSelect 
        placeholder="Введите"
        valueFieldId="id"
        labelFieldId="id"
        iconFieldId="icon"
        imageFieldId="image"
        groupFieldId="icon"
        filter="includes"
        hasCheckboxes={true}
        closePopupOnSelect={true}
        format="\`id + ' ' + id\`"
        hasSearch={true}
        cleanable={true}
    />
    ~~~
    `,
    },
  }
);
