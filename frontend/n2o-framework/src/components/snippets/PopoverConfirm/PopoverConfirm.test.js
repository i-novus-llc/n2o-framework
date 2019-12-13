import React from 'react';
import { PopoverConfirm } from './PopoverConfirm';
import sinon from 'sinon';
import Actions from '../../actions/Actions.old';
import mockStore from 'redux-mock-store';
import { Provider } from 'react-redux';
import metaJson from './../../actions/PopoverConfirm/PopoverConfirm.meta';

const props = {
  component: {
    header: 'header',
  },
};

const setupAction = store => {
  return mount(
    <Provider store={mockStore()(store)}>
      <Actions
        actions={{
          dummy: {
            src: 'dummyImpl',
          },
        }}
        toolbar={[
          {
            buttons: [
              {
                title: 'Кнопка',
                actionId: 'dummy',
                confirm: {
                  target: 'test',
                  ...metaJson,
                },
              },
            ],
          },
        ]}
      />
    </Provider>
  );
};

const setupComponent = propsOverride => {
  return shallow(<PopoverConfirm {...props.component} {...propsOverride} />);
};

if (global.document)
  document.createRange = () => ({
    setStart: () => {},
    setEnd: () => {},
    commonAncestorContainer: {
      nodeName: 'BODY',
      ownerDocument: document,
    },
  });

describe('Тесты PopoverConfirm', () => {
  it('Отрисовывается, если передан header', () => {
    const wrapper = setupComponent();
    expect(wrapper.find('.n2o-popover').exists()).toEqual(true);
  });
  it('Показывает popover', () => {
    const onClick = sinon.spy();
    const wrapper = setupComponent({ toggle: onClick });
    wrapper.find('.toggle-popover').simulate('click');
    expect(onClick.called).toEqual(true);
  });
  it('Проверка onConfirm', () => {
    const onClick = sinon.spy();
    const wrapper = setupComponent({
      onConfirm: onClick,
    });
    wrapper
      .find('.btn-sm')
      .last()
      .simulate('click');
    expect(onClick.called).toEqual(true);
  });
  it('Проверка isOpen', () => {
    const onClick = sinon.spy();
    const div = document.createElement('div');
    div.setAttribute('id', 'test');
    document.body.appendChild(div);
    const wrapper = setupAction({ onClick: onClick });
    wrapper.find('.toggle-popover').simulate('click');
    expect(wrapper.find('PopoverConfirm').props().isOpen).toEqual(true);
  });
});
