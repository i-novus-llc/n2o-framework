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

describe('<IconCell />', () => {
  it('проверяет создание элемента IconText', () => {
    const wrapper = shallow(<IconCell {...props} />);

    expect(
      wrapper
        .find('div')
        .first()
        .prop('title')
    ).toEqual(props.model[props.id]);
  });

  it('проверяет класс иконки', () => {
    const wrapper = shallow(<IconCell {...props} />);
    expect(wrapper.children().getElements()[0].props.icon).toEqual(props.icon);
  });

  it('проверяет расположение текста', () => {
    props.textPlace = textPlaceTypes.LEFT;
    const wrapper = shallow(<IconCell {...props} />);
    expect(
      wrapper
        .find('.n2o-cell-text')
        .getElements()
        .pop().props.style.float
    ).toEqual('left');
  });

  it('проверяет типы ячейки', () => {
    let wrapper = shallow(<IconCell {...props} />);
    expect(wrapper.find('.n2o-cell-text').exists()).toBeTruthy();

    props.type = iconCellTypes.ICON;
    wrapper = shallow(<IconCell {...props} />);
    expect(wrapper.find('.n2o-cell-text').exists()).toBeFalsy();
  });
});
