import React from 'react';
import sinon from 'sinon';
import { CheckboxGroupControl } from './CheckboxGroupControl';
import setupFormTest, {
  toMathInCollection,
  getField,
} from '../../../../test/formTestHelper';
import { focus, blur, change } from 'redux-form';

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
      data: [{ id: 1, label: 'label' }, { id: 2, label: 'label' }],
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
        'sorting.label': 'ASC',
      })
    ).toBe(true);
  });
  it('проверка лодера ', () => {
    const wrapper = setup({
      isLoading: true,
    });
    expect(wrapper.find('Spinner')).toBeTruthy();
  });
});

describe('Работа с reduxForm', () => {
  it('onFocus && onBlur', () => {
    const { wrapper, store, actions } = setupFormTest({
      src: 'CheckboxGroup',
      data: [{ id: 1, label: 'test' }, { id: 2, label: 'test2' }],
    });

    expect(getField(store)).toBe(false);
    wrapper
      .find('input[type="checkbox"]')
      .at(0)
      .simulate('focus');

    expect(toMathInCollection(actions, focus('Page_Form', 'testControl'))).toBe(
      true
    );
    expect(getField(store)).toEqual({ visited: true, active: true });

    wrapper
      .find('input[type="checkbox"]')
      .at(0)
      .simulate('blur');

    expect(
      toMathInCollection(actions, blur('Page_Form', 'testControl', '', true))
    ).toBe(true);
    expect(getField(store)).toEqual({ visited: true, touched: true });
  });
  it('Эмуляция onChange', () => {
    const { wrapper, store, actions } = setupFormTest({
      src: 'CheckboxGroup',
      data: [{ id: 1, label: 'test' }, { id: 2, label: 'test2' }],
    });
    wrapper
      .find('input[type="checkbox"]')
      .at(0)
      .simulate('focus');

    wrapper
      .find('input[type="checkbox"]')
      .at(0)
      .simulate('change');

    expect(
      toMathInCollection(
        actions,
        change(
          'Page_Form',
          'testControl',
          [{ id: 1, label: 'test' }],
          false,
          false
        )
      )
    ).toBe(true);
  });
});
