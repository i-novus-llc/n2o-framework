import React from 'react';
import sinon from 'sinon';
import * as hocs from './TreeContainer';

const NullComponent = () => null;

function setup(props, hocName) {
  const TestComponent = hocs[hocName](NullComponent);
  return mount(<TestComponent {...props} />);
}

describe('Тесты TreeContainer', () => {
  it('Проверка withWidgetHandlers props', () => {
    const wrapper = setup(
      {
        datasource: [
          { id: 1, label: 'datasource' },
          { id: 2, label: 'datasource' },
        ],
        valueFieldId: 1,
        multiselect: false,
        dispatch: sinon.spy(),
      },
      'withWidgetHandlers'
    );
    const props = wrapper.find(NullComponent).props();
    expect(props.datasource).toEqual([
      { id: 1, label: 'datasource' },
      { id: 2, label: 'datasource' },
    ]);
    expect(props.valueFieldId).toEqual(1);
    expect(props.multiselect).toEqual(false);
  });
  it('Проверка withWidgetHandlers -> onResolve', () => {
    const onResolve = sinon.spy();
    const wrapper = setup(
      {
        onResolve,
      },
      'withWidgetHandlers'
    );
    wrapper
      .find(NullComponent)
      .props()
      .onResolve();
    expect(onResolve.calledOnce).toBe(true);
  });
  it('Проверка withWidgetHandlers -> onRowClickAction', () => {
    const dispatch = sinon.spy();
    const wrapper = setup(
      {
        dispatch,
        rowClick: { src: 'dummy' },
      },
      'withWidgetHandlers'
    );
    wrapper
      .find(NullComponent)
      .props()
      .onRowClickAction();
    expect(dispatch.calledOnce).toBe(true);
  });
});
