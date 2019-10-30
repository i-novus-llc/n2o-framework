import React from 'react';
import sinon from 'sinon';

import {
  flatFields,
  getAutoFocusId,
  fetchIfChangeDependencyValue,
} from '../utils';

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
                control: 'Input',
              },
              {
                id: 'name2',
                name: 'name1',
                label: 'Имя',
                control: 'Input',
              },
            ],
          },
        ],
      },
    ],
  },
];

const fields = [
  {
    src: 'StandardField',
    id: 'surname2',
    visible: false,
    label: 'Фамилия',
    name: 'surname1',
    control: 'Input',
  },
  {
    id: 'name2',
    name: 'name1',
    label: 'Имя',
    control: 'Input',
  },
];

describe('Тест утильных функций', () => {
  it('flatFields возвращает массив из филдов', () => {
    expect(flatFields(fieldsets)).toEqual(fields);
  });
  it('getAutoFocusId возвращает id поля, которое будет в фокусе при автофокусе', () => {
    expect(getAutoFocusId(fields)).toEqual('name2');
  });
  it('fetchIfChangeDependencyValue запрашивает даные при измененеии зависимости', () => {
    const _fetchData = sinon.spy();
    const notFetch = sinon.spy();
    fetchIfChangeDependencyValue(
      { type: 1 },
      { type: 2 },
      { props: { _fetchData } }
    );
    expect(_fetchData.calledOnce).toEqual(true);
    fetchIfChangeDependencyValue(
      { type: 1 },
      { type: 1 },
      { props: { notFetch } }
    );
    fetchIfChangeDependencyValue({ type: 1 }, { type: 2 });
    fetchIfChangeDependencyValue({ type: 1 }, { type: 2 }, { props: {} });
    expect(notFetch.calledOnce).toEqual(false);
  });
});
