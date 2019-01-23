import React from 'react';
import sinon from 'sinon';
import LinkCell from './LinkCell';
import meta from './LinkCell.meta';
import { Provider } from 'react-redux';
import configureMockStore from 'redux-mock-store';

const setupLinkCell = propsOverride => {
  const props = {
    ...meta
  };
  return mount(
    <Provider store={configureMockStore()({})}>
      <LinkCell {...props} {...propsOverride} />
    </Provider>
  );
};

describe('Тесты LinkCell', () => {
  it('Отрисовывается', () => {
    const wrapper = setupLinkCell();
    expect(wrapper.find('Button').exists()).toEqual(true);
  });
  it('Отрисовывается icon', () => {
    const wrapper = setupLinkCell({
      icon: 'fa fa-plus',
      type: 'icon'
    });
    expect(wrapper.find('.fa.fa-plus').exists()).toEqual(true);
  });
  it('Проверка onClick', () => {
    const callActionImpl = sinon.spy();
    const model = { a: 1 };
    const wrapper = setupLinkCell({
      icon: 'fa fa-plus',
      model: model,
      callActionImpl
    });

    wrapper
      .find('Button')
      .at(0)
      .simulate('click', {
        nativeEvent: {
          stopPropagation: () => {}
        }
      });
    expect(callActionImpl.withArgs().calledOnce).toEqual(true);
  });

  it('Отрисовыается ссылка по таргету "application"', () => {
    const wrapper = setupLinkCell({
      url: '/n2o/test',
      target: 'application'
    });
    expect(wrapper.find('a[href="/n2o/test"]').exists()).toEqual(true);
  });
  it('Отрисовывается ссылка по таргету "self"', () => {
    const wrapper = setupLinkCell({
      url: '/n2o/self/test',
      target: 'self'
    });
    expect(wrapper.find('a[href="/n2o/self/test"]').exists()).toEqual(true);
  });
  it('Отрисовывается ссылка по таргету "newWindow"', () => {
    const wrapper = setupLinkCell({
      url: 'https://google.com',
      target: 'newWindow'
    });
    expect(wrapper.find('a[target="_blank"]').exists()).toEqual(true);
  });
});
