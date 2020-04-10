import React from 'react';
import { shallow } from 'enzyme';

import IconCell from './IconCell';
import { iconCellTypes, textPlaceTypes } from './cellTypes';

const props = {
  id: 'name',
  model: {
    name: 'text',
    age: '12',
  },
  type: iconCellTypes.ICONANDTEXT,
  textPlace: textPlaceTypes.RIGHT,
  icon: 'fa fa-minus',
};

const propsWithTooltip = {
  id: 'name',
  model: {
    name: 'text',
    age: '12',
    tooltipFieldId: ['tooltip', 'body'],
  },
  type: iconCellTypes.ICONANDTEXT,
  textPlace: textPlaceTypes.RIGHT,
  icon: 'fa fa-minus',
};

describe('<IconCell />', () => {
  it('проверяет создание элемента IconText', () => {
    const wrapper = mount(<IconCell {...props} />);
    expect(
      wrapper
        .find('span')
        .first()
        .prop('title')
    ).toEqual(props.model[props.id]);
  });

  it('проверяет класс иконки', () => {
    const wrapper = mount(<IconCell {...props} />);
    expect(wrapper.children().getElements()[0].props.icon).toEqual(props.icon);
  });

  it('проверяет расположение текста', () => {
    props.textPlace = textPlaceTypes.LEFT;
    const wrapper = mount(<IconCell {...props} />);
    expect(
      wrapper
        .find('.n2o-cell-text')
        .getElements()
        .pop().props.style.float
    ).toEqual('left');
  });

  it('проверяет типы ячейки', () => {
    let wrapper = mount(<IconCell {...props} />);
    expect(wrapper.find('.n2o-cell-text').exists()).toBeTruthy();

    props.type = iconCellTypes.ICON;
    wrapper = mount(<IconCell {...props} />);
    expect(wrapper.find('.n2o-cell-text').exists()).toBeFalsy();
  });
  it('Cell обернут тултипом', () => {
    let wrapper = mount(<IconCell {...propsWithTooltip} />);
    expect(wrapper.find('.list-text-cell__trigger').exists()).toEqual(true);
  });
});
