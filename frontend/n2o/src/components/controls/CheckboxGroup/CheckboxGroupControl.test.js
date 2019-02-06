import React from 'react';
import sinon from 'sinon';
import { CheckboxGroupControl } from './CheckboxGroupControl';
import setupFormTest, { toMathInCollection } from '../../../utils/formTestHelper';
import { focus, blur } from 'redux-form';

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

describe('Работа с reduxForm', () => {
  it('creates checkboxes', () => {
    const { wrapper, store, actions } = setupFormTest({
      src: 'CheckboxGroup',
      data: [{ id: 1, label: 'test' }, { id: 2, label: 'test2' }]
    });

    wrapper
      .find('input[type="checkbox"]')
      .at(0)
      .simulate('focus');

    expect(toMathInCollection(actions, focus('Page_Form', 'testControl'))).toBe(true);

    wrapper
      .find('input[type="checkbox"]')
      .at(0)
      .simulate('blur');

    expect(toMathInCollection(actions, blur('Page_Form', 'testControl', '', true))).toBe(true);
  });
});
