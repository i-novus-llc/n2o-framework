import React from 'react';
import getElementType from './getElementType';

function Test({ as }) {
  return React.createElement(as, {});
}

describe('Проверка getElementType', () => {
  it('вернет тип компонента', () => {
    const component = <Test as={'span'} />;
    expect(getElementType(component, { as: 'span' })).toEqual('span');
    expect(getElementType(component, { as: 'span' }, () => 'span'));
    expect(getElementType(component, {})).toEqual('div');
  });
});
