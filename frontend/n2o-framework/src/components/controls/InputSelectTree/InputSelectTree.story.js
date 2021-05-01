import React from 'react';
import { storiesOf } from '@storybook/react';

import InputSelectTree, {
  InputSelectTree as InputSelectComponent,
} from './InputSelectTree';

const stories = storiesOf('Контролы/InputSelectTree', module);

stories.addParameters({
  info: {
    propTables: [InputSelectComponent],
    propTablesExclude: [InputSelectTree],
  },
});

stories.add(
  'Компонент ',
  () => {
    const data = [
      {
        id: '1',
        name: 'Алексей Николаев',
        icon: 'fa fa-address-card',
        image:
          'https://img.faceyourmanga.com/mangatars/0/2/2729/large_3206.png',
        dob: '11.09.1992',
        country: 'Россия',
      },
      {
        id: '2у',
        name: 'Игорь Николаев',
        icon: 'fa fa-address-card',
        image:
          'https://img.faceyourmanga.com/mangatars/0/2/2729/large_3156.png',
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
        image:
          'https://img.faceyourmanga.com/mangatars/0/0/39/large_140189.png',
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
        image:
          'https://img.faceyourmanga.com/mangatars/0/2/2729/large_3156.png',
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
        image:
          'https://img.faceyourmanga.com/mangatars/0/2/2729/large_3156.png',
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
      loading: false,
      disabled: false,
      placeholder: '',
      valueFieldId: 'id',
      labelFieldId: 'name',
      filter: 'includes',
      iconFieldId: 'icon',
      imageFieldId: 'image',
      multiSelect: false,
      parentFieldId: 'parentId',
      hasCheckboxes: false,
      hasChildrenFieldId: true,
      format: '',
      ajax: false,
      showCheckedStrategy: 'SHOW_CHILD',
      allowClear: true,
      showSearch: true,
      notFoundContent: 'Ничего не найдено',
      searchPlaceholder: 'Поиск',
      autoClearSearchValue: false,
      defaultValue: '',

      resetOnBlur: false,
      groupFieldId: '',
      closePopupOnSelect: true,
      data,
    };

    return (
      <InputSelectTree
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
    Компонент 'Выпадающий список-дерево'
    ~~~js
    import InputSelectTree from 'n2o-framework/lib/components/controls/InputSelectTree/InputSelectTree';
    
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
        name: 'Игорь Николаев',
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
    
    <InputSelectTree
        valueFieldId="id"
        labelFieldId="name"
        filter="includes"
        iconFieldId="icon"
        imageFieldId="image"
        parentFieldId="parentId"
        hasChildrenFieldId={true}
        showCheckedStrategy="SHOW_CHILD"
        allowClear={true}
        showSearch={true}
        notFoundContent="Ничего не найдено"
        searchPlaceholder="Поиск"
        closePopupOnSelect={true}
        data={data}
    />
    ~~~
    `,
    },
  }
);
