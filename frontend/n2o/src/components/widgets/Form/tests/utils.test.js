import React from 'react';

import { flatFields, getAutoFocusId } from '../utils';

const fieldsets = [
  {
    src: 'StandardFieldset',
    rows: [
      {
        cols: [
          {
            fields: [
              {
                src: 'StandardField',
                id: 'surname2',
                visible: false,
                label: 'Фамилия',
                name: 'surname1',
                control: 'Input'
              },
              {
                id: 'name2',
                name: 'name1',
                label: 'Имя',
                control: 'Input'
              }
            ]
          }
        ]
      }
    ]
  }
];

const fields = [
  {
    src: 'StandardField',
    id: 'surname2',
    visible: false,
    label: 'Фамилия',
    name: 'surname1',
    control: 'Input'
  },
  {
    id: 'name2',
    name: 'name1',
    label: 'Имя',
    control: 'Input'
  }
];

describe('Тест утильных функций', () => {
  it('flatFields возвращает массив из филдов', () => {
    expect(flatFields(fieldsets)).toEqual(fields);
  });
  it('getAutoFocusId возвращает id поля, которое будет в фокусе при автофокусе', () => {
    expect(getAutoFocusId(fields)).toEqual('name2');
  });
});
