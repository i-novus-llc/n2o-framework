import React from 'react';
import { storiesOf } from '@storybook/react';

import InputSelect, {
  InputSelect as InputSelectComponent,
} from './InputSelect';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('Контролы/InputSelect', module);

stories.addParameters({
  info: {
    propTables: [InputSelectComponent],
    propTablesExclude: [InputSelect, Factory],
  },
});

stories.add(
  'Компонент ',
  () => {
    const options = [
      {
        id: 'Алексей Николаев',
        icon: 'fa fa-square',
        dob: '11.09.1992',
        country: 'Россия',
      },
      {
        id: 'Игонь Николаев',
        icon: 'fa fa-plus',
        dob: '24.04.1891',
        country: 'Россия',
      },
      {
        id: 'Владимир Серпухов',
        icon: 'fa fa-square',
        dob: '03.12.1981',
        country: 'США',
      },
      {
        id: 'Анатолий Петухов',
        icon: 'fa fa-square',
        dob: '11.11.2003',
        country: 'США',
      },
      {
        id: 'Николай Патухов',
        icon: 'fa fa-plus',
        dob: '20.11.1991',
        country: 'Беларусь',
      },
      {
        id: 'Алексей Николаев1',
        icon: 'fa fa-square',
        dob: '11.09.1992',
        country: 'Россия',
      },
      {
        id: 'Игонь Николаев1',
        icon: 'fa fa-plus',
        dob: '24.04.1891',
        country: 'Россия',
      },
      {
        id: 'Владимир Серпухов1',
        icon: 'fa fa-square',
        dob: '03.12.1981',
        country: 'США',
      },
      {
        id: 'Анатолий Петухов1',
        icon: 'fa fa-square',
        dob: '11.11.2003',
        country: 'США',
      },
      {
        id: 'Николай Патухов1',
        icon: 'fa fa-plus',
        dob: '20.11.1991',
        country: 'Беларусь',
      },
      {
        id: 'Алексей Николаев2',
        icon: 'fa fa-square',
        dob: '11.09.1992',
        country: 'Россия',
      },
      {
        id: 'Игонь Николаев2',
        icon: 'fa fa-plus',
        dob: '24.04.1891',
        country: 'Россия',
      },
      {
        id: 'Владимир Серпухов2',
        icon: 'fa fa-square',
        dob: '03.12.1981',
        country: 'США',
      },
      {
        id: 'Анатолий Петухов2',
        icon: 'fa fa-square',
        dob: '11.11.2003',
        country: 'США',
      },
      {
        id: 'Николай Патухов2',
        icon: 'fa fa-plus',
        dob: '20.11.1991',
        country: 'Беларусь',
      },
    ];

    const props = {
      loading: false,
      disabled: false,
      placeholder: 'Введите значение',
      valueFieldId: 'id',
      labelFieldId: 'id',
      filter: 'includes',
      resetOnBlur: false,
      iconFieldId: 'icon',
      imageFieldId: 'image',
      multiSelect: false,
      groupFieldId: '',
      hasCheckboxes: false,
      closePopupOnSelect: true,
      format: '',
      options: options,
      expandPopUp: false,
    };

    return (
      <InputSelect
        {...props}
        onSearch={() => {}}
        onSelect={() => {}}
        onToggle={() => {}}
        onInput={() => {}}
        onOpen={() => {}}
        onClose={() => {}}
        onChange={() => {}}
        onScrollEnd={() => {}}
        onElementCreate={() => {}}
      />
    );
  },
  {
    info: {
      text: `
      Компонент 'Выпадающий список'
      ~~~js
      import InputSelect from 'n2o-framework/lib/components/controls/InputSelect/InputSelect';
      
      <InputSelect 
          placeholder="Введите значение"
          valueFieldId="id"
          labelFieldId="id"
          filter="includes"
          resetOnBlur={false}
          iconFieldId="icon"
          imageFieldId="image"
          multiSelect={false}
          groupFieldId=""
          closePopupOnSelect={true}
          options={options}
      />
      ~~~
      `,
    },
  }
);
