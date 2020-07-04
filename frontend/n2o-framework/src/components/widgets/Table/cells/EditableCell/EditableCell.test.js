import React from 'react';
import sinon from 'sinon';
import { EditableCell } from './EditableCell';
import InputText from '../../../../controls/InputText/InputText';

const action = {
  id: 'update',
  type: 'n2o/actionImpl/START_INVOKE',
  payload: {
    widgetId: '__patients',
    dataProvider: {
      url: 'n2o/data/update',
      pathMapping: {},
      method: 'POST',
    },
    modelLink: "models.resolve['__patients']",
  },
  meta: {
    success: {
      refresh: {
        type: 'widget',
        options: {
          widgetId: '__patients',
        },
      },
    },
    fail: {},
  },
};

const setup = propsOverride => {
  const props = {
    visible: true,
    editable: true,
    value: 'test',
  };

  return mount(<EditableCell {...props} {...propsOverride} />);
};

describe('Тесты EditableCell', function() {
  it('компонент отрисовывается', () => {
    const wrapper = setup();
    expect(wrapper.find('.n2o-editable-cell').exists()).toEqual(true);
  });
  it('компонент не отрисовывается по visible = false', () => {
    const wrapper = setup({ visible: false });
    expect(wrapper.find('.n2o-editable-cell').exists()).toEqual(false);
  });
  it('отрисовывается контрол', () => {
    const wrapper = setup({
      control: {
        component: () => <div>test</div>,
      },
    });
    expect(wrapper.find('.n2o-editable-cell-control').exists()).toEqual(false);
    wrapper.setState({ editing: true });
    expect(wrapper.find('.n2o-editable-cell-control').exists()).toEqual(true);
  });
  it('срабатывает onChange', () => {
    const dispatch = sinon.spy();
    const callAction = sinon.spy();
    const wrapper = setup({
      control: {
        component: InputText,
      },
      editFieldId: 'name',
      onResolve: () => {},
      dispatch,
      callAction,
    });
    expect(wrapper.state().model).toEqual({});
    wrapper
      .find('.n2o-editable-cell-text')
      .at(0)
      .simulate('click');
    wrapper
      .find('input')
      .at(0)
      .simulate('change', { target: { value: 'Ivan' } });
    expect(wrapper.state().model).toEqual({
      name: 'Ivan',
    });
    wrapper
      .find('input')
      .at(0)
      .simulate('blur');
    wrapper
      .find('.n2o-editable-cell-text')
      .at(0)
      .simulate('click');
    wrapper
      .find('input')
      .at(0)
      .simulate('change', { target: { value: 'Sergey' } });
    expect(wrapper.state().prevModel).toEqual({
      name: 'Ivan',
    });
    expect(wrapper.state().model).toEqual({
      name: 'Sergey',
    });
    expect(callAction.called).toBeTruthy();
  });
  it('срабатывает onBlur', () => {
    const wrapper = setup({
      control: {
        component: InputText,
      },
    });

    expect(wrapper.state().editing).toEqual(false);
    wrapper
      .find('.n2o-editable-cell-text')
      .at(0)
      .simulate('click');
    expect(wrapper.state().editing).toEqual(true);
    wrapper
      .find('input')
      .at(0)
      .simulate('blur');
    expect(wrapper.state().editing).toEqual(false);
  });
  it('правильно работает логика изменения значения', () => {
    const onResolve = sinon.spy();
    const onSetSelectedId = sinon.spy();
    const dispatch = sinon.spy();
    const callAction = sinon.spy();

    const wrapper = setup({
      control: {
        component: InputText,
      },
      editFieldId: 'name',
      model: {
        id: 1,
        surname: 'Ivanov',
        name: 'Ivan',
        part: 'Ivanovich',
      },
      action,
      callAction,
      onResolve,
      onSetSelectedId,
      dispatch,
    });

    wrapper
      .find('.n2o-editable-cell-text')
      .at(0)
      .simulate('click');
    wrapper
      .find('input')
      .at(0)
      .simulate('change', { target: { value: 'Sergey' } });
    wrapper
      .find('input')
      .at(0)
      .simulate('blur');

    expect(onResolve.called).toEqual(true);
    expect(onSetSelectedId.called).toEqual(true);
    expect(callAction.calledOnce).toBeTruthy();
  });
});
