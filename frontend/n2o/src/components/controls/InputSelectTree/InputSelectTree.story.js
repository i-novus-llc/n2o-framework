import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';
import { action } from '@storybook/addon-actions';
import InputSelectTree from './InputSelectTree';

const stories = storiesOf('Контролы/InputSelectTree', module);

stories.addDecorator(withKnobs);

stories.add('Компонент ', () => {
  const data = [
    {
      id: '1',
      name: 'Алексей Николаев',
      icon: 'fa fa-address-card',
      image: 'https://img.faceyourmanga.com/mangatars/0/2/2729/large_3206.png',
      dob: '11.09.1992',
      country: 'Россия',
    },
    {
      id: '2у',
      name: 'Игонь Николаев',
      icon: 'fa fa-address-card',
      image: 'https://img.faceyourmanga.com/mangatars/0/2/2729/large_3156.png',
      dob: '24.04.1891',
      country: 'Россия',
    },
    {
      id: '3',
      name: 'Владимир Серпухов',
      icon: 'fa fa-address-card',
      image: 'https://img.faceyourmanga.com/mangatars/0/0/39/large_511.png',
      dob: '03.12.1981',
      country: 'США',
      parentId: '2у',
    },
    {
      id: '4',
      name: 'Анатолий Петухов',
      icon: 'fa fa-address-card',
      image: 'https://img.faceyourmanga.com/mangatars/0/0/39/large_140189.png',
      dob: '11.11.2003',
      country: 'США',
    },
    {
      id: '5',
      name: 'Николай Патухов',
      icon: 'fa fa-address-card',
      image: 'https://img.faceyourmanga.com/mangatars/0/0/39/large_9319.png',
      dob: '20.11.1991',
      country: 'Беларусь',
      parentId: '4',
    },
    {
      id: '6',
      name: 'Сергей Медведев',
      icon: 'fa fa-address-card',
      image: 'https://img.faceyourmanga.com/mangatars/0/2/2729/large_3156.png',
      dob: '11.11.2003',
      country: 'США',
    },
    {
      id: '7',
      name: 'Павел Милешин',
      icon: 'fa fa-address-card',
      image: 'https://img.faceyourmanga.com/mangatars/0/0/39/large_9319.png',
      dob: '20.11.1991',
      country: 'Беларусь',
      parentId: '6',
    },
    {
      id: '8',
      name: 'Анатолий Попов',
      icon: 'fa fa-address-card',
      image: 'https://img.faceyourmanga.com/mangatars/0/2/2729/large_3156.png',
      dob: '11.11.2003',
      country: 'США',
    },
    {
      id: '9',
      name: 'Андрей Мельников',
      icon: 'fa fa-address-card',
      image: 'https://img.faceyourmanga.com/mangatars/0/0/39/large_9319.png',
      dob: '20.11.1991',
      country: 'Беларусь',
      parentId: '8',
    },
  ];

  const props = {
    loading: boolean('loading', false),
    disabled: boolean('disabled', false),
    placeholder: text('placeholder', ''),
    valueFieldId: text('valueFieldId', 'id'),
    labelFieldId: text('labelFieldId', 'name'),
    filter: select(
      'filter',
      ['includes', 'startsWith', 'endsWith', 'server'],
      'includes'
    ),
    iconFieldId: text('iconFieldId', 'icon'),
    imageFieldId: text('imageFieldId', 'image'),
    multiSelect: boolean('multiSelect', false),
    parentFieldId: text('parentFieldId', 'parentId'),
    hasCheckboxes: boolean('hasCheckboxes', false),
    hasChildrenFieldId: boolean('hasChildrenFieldId', true),
    format: text('format', ''),
    ajax: boolean('ajax', false),
    showCheckedStrategy: select(
      'showCheckedStrategy',
      ['SHOW_PARENT', 'SHOW_CHILD', 'SHOW_ALL'],
      'SHOW_CHILD'
    ),
    allowClear: boolean('allowClear', true),
    showSearch: boolean('showSearch', true),
    notFoundContent: text('notFoundContent', 'Ничего не найдено'),
    searchPlaceholder: text('searchPlaceholder', 'Поиск'),
    autoClearSearchValue: boolean('autoClearSearchValue', false),
    defaultValue: text('defaultValue', ''),

    resetOnBlur: boolean('resetOnBlur', false),
    groupFieldId: text('groupFieldId', ''),
    closePopupOnSelect: boolean('closePopupOnSelect', true),
    data,
  };

  return (
    <InputSelectTree
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
