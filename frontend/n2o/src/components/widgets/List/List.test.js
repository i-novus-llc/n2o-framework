import React from 'react';
import sinon from 'sinon';
import List from './List';

const setup = propsOverride => {
  const props = {
    data: [
      {
        image: {
          src: 'google.com',
          alt: 'test',
        },
        header: 'header',
        subHeader: 'subHeader',
        rightTop: 'rightTop',
        rightBottom: 'rightBottom',
        body: 'body',
        extra: 'extra',
      },
    ],
  };

  return mount(<List {...props} {...propsOverride} />);
};

describe('Проверка List', () => {
  it('Компонент отрисовывается', () => {
    const wrapper = setup();
    expect(wrapper.find('.n2o-widget-list').exists()).toEqual(true);
  });

  it('renderRow отрисовывает строки', () => {
    const wrapper = setup();
    const rowItem = wrapper
      .instance()
      .renderRow({ index: 0, key: 0, style: {}, parent: {} });
    expect(React.isValidElement(rowItem)).toEqual(true);
  });
  // it('fetchOnScroll', () => {
  //   const fetchMore = sinon.spy();
  //   const wrapper = setup({ fetchOnScroll: true, fetchMore });
  // wrapper.dispatchEvent(new window.Event('scroll'));
  // wrapper.scrollTo(window.scrollX, window.scrollY + 100);
  // window.addEventListener('scroll', this.onScroll);
  // window.scrollTo(window.scrollX, window.scrollY + 100);
  // window.dispatchEvent(new window.UIEvent('scroll', { detail: 0 }));
  // window.dispatchEvent(new Event('scroll'));

  //   console.log(wrapper.debug())
  //   expect(fetchMore.calledOnce).toEqual(true);
  // });
  // it('componentDidUpdate', () => {
  //   const wrapper = setup({
  //     hasMoreButton: true,
  //     maxHeight: true,
  //   });
  //   console.log(wrapper.props())
  //   expect(wrapper.props()).toEqual(true);
  // });
});
