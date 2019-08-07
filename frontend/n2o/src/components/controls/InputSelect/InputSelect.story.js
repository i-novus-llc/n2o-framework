import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';
import { action } from '@storybook/addon-actions';
import InputSelect from './InputSelect';

const stories = storiesOf('Контролы/InputSelect', module);

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);

stories.add('Компонент ', () => {
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
    loading: boolean('loading', false),
    disabled: boolean('disabled', false),
    placeholder: text('placeholder', 'Введите значение'),
    valueFieldId: text('valueFieldId', 'id'),
    labelFieldId: text('labelFieldId', 'id'),
    filter: select(
      'filter',
      ['includes', 'startsWith', 'endsWith'],
      'includes'
    ),
    resetOnBlur: boolean('resetOnBlur', false),
    iconFieldId: text('iconFieldId', 'icon'),
    imageFieldId: text('imageFieldId', 'image'),
    multiSelect: boolean('multiSelect', false),
    groupFieldId: text('groupFieldId', ''),
    hasCheckboxes: boolean('hasCheckboxes', false),
    closePopupOnSelect: boolean('closePopupOnSelect', true),
    format: text('format', ''),
    options: options,
    expandPopUp: boolean('expandPopUp', false),
  };

  return (
    <InputSelect
      {...props}
      onSearch={action('onSearch')}
      onSelect={action('onSelect')}
      onToggle={action('onToggle')}
      onInput={action('onInput')}
      onOpen={action('onOpen')}
      onClose={action('onClose')}
      onChange={action('onChange')}
      onScrollEnd={action('onScrollEnd')}
      onElementCreate={action('onElementCreate')}
    />
  );
});
