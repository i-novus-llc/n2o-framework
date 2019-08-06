import React from 'react';
import { N2OPopover } from './Popover';
import sinon from 'sinon';
import Actions from '../../actions/Actions';
import mockStore from 'redux-mock-store';
import { Provider } from 'react-redux';

const props = {
  help: {
    id: 'test',
    help: 'подсказка',
  },
  component: {
    body: 'body',
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
                id: 'test',
                title: 'Кнопка',
                actionId: 'dummy',
                popoverConfirm: true,
              },
            ],
          },
        ]}
        containerKey="test"
      />
    </Provider>
  );
};

const setupHelp = propsOverride => {
  return shallow(<N2OPopover {...props.help} {...propsOverride} />);
};

const setupComponent = propsOverride => {
  return shallow(<N2OPopover {...props.component} {...propsOverride} />);
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

describe('Тесты Popover', () => {
  it('Отрисовывается, если переданы id и help', () => {
    const wrapper = setupHelp();
    expect(wrapper.find('.n2o-popover').exists()).toEqual(true);
  });
  it('Отрисовывается, если переданы header и body', () => {
    const wrapper = setupComponent();
    expect(wrapper.find('.n2o-popover').exists()).toEqual(true);
  });
  it('Показывает подсказку', () => {
    const onClick = sinon.spy();
    const wrapper = setupHelp({ onToggle: onClick });
    wrapper.find('.toggle-popover').simulate('click');
    expect(onClick.called).toEqual(true);
  });
  it('Показывает popover', async () => {
    const onClick = sinon.spy();
    const wrapper = setupComponent({ onToggle: onClick });
    wrapper.find('.toggle-popover').simulate('click');
    expect(onClick.called).toEqual(true);
  });
  it('Проверка popoverConfirm', () => {
    const onClick = sinon.spy();
    const wrapper = setupComponent({
      popoverConfirm: 'true',
      onClickYes: onClick,
    });
    wrapper
      .find('.btn-sm')
      .last()
      .simulate('click');
    expect(onClick.called).toEqual(true);
  });
  it('проверка popoverConfirm', () => {
    const onClick = sinon.spy();
    const div = document.createElement('div');
    div.setAttribute('id', 'test');
    document.body.appendChild(div);
    const wrapper = setupAction({ onClick: onClick });
    wrapper.find('.toggle-popover').simulate('click');
    expect(wrapper.find('Popover').props().isOpen).toEqual(true);
  });
});
