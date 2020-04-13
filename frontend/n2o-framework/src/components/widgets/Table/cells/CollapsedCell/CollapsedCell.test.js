import React from 'react';
import { mount } from 'enzyme';

import CollapsedCell from './CollapsedCell';

const setup = (propOverrides = {}) => {
  const props = Object.assign(
    {
      tooltipFieldId: 'tooltip',
      model: {
        data: ['Казань', 'Москва', 'Токио', 'Берлин', 'Париж', 'Лондон'],
        tooltip: ['tooltip', 'body'],
      },
      fieldKey: 'data',
      amountToGroup: 3,
    },
    propOverrides
  );

  const wrapper = mount(<CollapsedCell {...props} />);

  return {
    props,
    wrapper,
  };
};

describe('<InputSelectContainer />', () => {
  it('проверяет создание элемента', () => {
    const { wrapper } = setup();

    expect(wrapper.find('span.badge').exists()).toBeTruthy();
  });

  it('Cell обернут тултипом', () => {
    const { wrapper } = setup();

    expect(wrapper.find('.list-text-cell__trigger').exists()).toEqual(true);
  });

  it('проверяет количество элементов', () => {
    const { wrapper, props } = setup();

    expect(wrapper.find('span.badge')).toHaveLength(props.amountToGroup);
  });

  it('проверяет кнопку раскрытия и скрытия', () => {
    const { wrapper, props } = setup();

    // раскрытие
    wrapper
      .find('a.collapsed-cell-control')
      .last()
      .simulate('click');
    expect(wrapper.find('span.badge')).toHaveLength(
      props.model[props.fieldKey].length
    );

    // скрытие
    wrapper
      .find('a.collapsed-cell-control')
      .last()
      .simulate('click');
    expect(wrapper.find('span.badge')).toHaveLength(props.amountToGroup);
  });

  it('проверяет цвета', () => {
    const { wrapper, props } = setup({ color: 'info' });

    expect(wrapper.find(`span.badge-${props.color}`).exists()).toBeTruthy();
  });

  it('проверяет отсутствие кнопки раскрытия при маленьком количестве элементов', () => {
    const { wrapper, props } = setup({
      amountToGroup: 1,
      fieldKey: 'data',
      model: {
        data: ['testData'],
      },
    });

    expect(wrapper.find('span.badge')).toHaveLength(
      props.model[props.fieldKey].length
    );
  });
});
