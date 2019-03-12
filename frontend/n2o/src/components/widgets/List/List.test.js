import React from 'react';
import sinon from 'sinon';
import List from './List';

const setup = propsOverride => {
  const props = {
    data: [
      {
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
    ]
  };

  return mount(<List {...props} {...propsOverride} />);
};

describe('Проверка List', () => {
  it('Компонент отрисовывается', () => {
    const wrapper = setup();
    expect(wrapper.find('.n2o-widget-list').exists()).toEqual(true);
  });

  it('Секции отрисовываются', () => {
    const wrapper = setup();
    expect(wrapper.find('.n2o-widget-list-item-image').exists()).toEqual(true);
    expect(wrapper.find('.n2o-widget-list-item-header').exists()).toEqual(true);
    expect(wrapper.find('.n2o-widget-list-item-subheader').exists()).toEqual(true);
    expect(wrapper.find('.n2o-widget-list-item-body').exists()).toEqual(true);
    expect(wrapper.find('.n2o-widget-list-item-right-top').exists()).toEqual(true);
    expect(wrapper.find('.n2o-widget-list-item-right-bottom').exists()).toEqual(true);
    expect(wrapper.find('.n2o-widget-list-item-extra').exists()).toEqual(true);
  });

  it('Срабатывает клик по строке', () => {
    const onChange = sinon.spy();
    const wrapper = setup({
      onItemClick: onChange,
      data: [
        {
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
        },
        {
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
      ]
    });
    wrapper
      .find('.n2o-widget-list-item')
      .at(1)
      .simulate('click');
    expect(onChange.calledOnce).toEqual(true);
    expect(wrapper.find('List').state().selectedIndex).toEqual(1);
  });

  it('Загружается по клику "Загрузить еще"', () => {
    const fetchMore = sinon.spy();
    const wrapper = setup({
      onFetchMore: fetchMore,
      hasMoreButton: true
    });
    wrapper.find('.n2o-widget-list-more-button button').simulate('click');

    expect(fetchMore.calledOnce).toEqual(true);
  });

  it('Загружается при скролле', () => {
    const fetchMore = sinon.spy();
    const data = [];
    for (let i = 0; i < 10; i++) {
      data.push({
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
      });
    }
    const wrapper = setup({
      fetchOnScroll: true,
      onFetchMore: fetchMore,
      data,
      maxHeight: 300
    });
    console.log(wrapper.find('.n2o-widget-list').instance().clientHeight);
  });
});
