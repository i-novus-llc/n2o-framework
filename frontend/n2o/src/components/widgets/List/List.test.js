import React from 'react';
import List from './List';

const setup = propsOverride => {
  const props = {
    data: {
      image: {
        src: 'google.com',
        alt: 'test'
      },
      header: 'header',
      subHeader: 'subHeader',
      rightTop: 'rightTop',
      rightBottom: 'rightBottom',
      body: 'body',
      extra: 'extra'
    }
  };

  return mount(<List {...props} {...propsOverride} />);
};

describe('Проверка List', () => {
  it('Компонент отрисовывается', () => {
    const wrapper = setup();
    expect(wrapper.find('.n2o-widget-list').exists()).toEqual(true);
  });

  it('Секции отрисовываются', () => {
    // const wrapper = setup();
    // expect(wrapper.find('.n2o-widget-list-item-image').exists()).toEqual(true);
    // expect(wrapper.find('.n2o-widget-list-item-header').exists()).toEqual(true);
    // expect(wrapper.find('.n2o-widget-list-item-subheader').exists()).toEqual(true);
    // expect(wrapper.find('.n2o-widget-list-item-body').exists()).toEqual(true);
    // expect(wrapper.find('.n2o-widget-list-item-right-top').exists()).toEqual(true);
    // expect(wrapper.find('.n2o-widget-list-item-right-bottom').exists()).toEqual(true);
    // expect(wrapper.find('.n2o-widget-list-item-extra').exists()).toEqual(true);
  });
});
