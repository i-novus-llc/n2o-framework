import React from 'react';
import sinon from 'sinon';

import Checkbox from '../Checkbox/Checkbox';
import { CheckboxGroupControl } from './CheckboxGroupControl';

const setup = overrideProps => {
  const props = Object.assign(
    {
      // use this to assign some default props
    },
    overrideProps
  );

  return mount(<CheckboxGroupControl {...props} />);
};

describe('<CheckboxGroupControl />', () => {
  it('creates checkboxes ', () => {
    const wrapper = setup({
      data: [{ id: 1, label: 'label' }, { id: 2, label: 'label' }]
    });
    expect(wrapper.find('Checkbox')).toHaveLength(2);
  });
  it('Проверка вызова _fetchData ', () => {
    const _fetchData = sinon.spy();
    setup({ _fetchData, size: 10 });
    expect(_fetchData.calledOnce).toEqual(true);
    expect(
      _fetchData.calledWith({
        size: 10,
        'sorting.label': 'ASC'
      })
    ).toBe(true);
  });
  it('проверка лодера ', () => {
    const wrapper = setup({
      isLoading: true
    });
    expect(wrapper.find('Spinner')).toBeTruthy();
  });
});
