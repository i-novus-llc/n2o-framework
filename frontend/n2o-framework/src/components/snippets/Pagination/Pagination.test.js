import React from 'react';
import Pagination from './Pagination';

const WrapperComp = props => (
  <Pagination {...props} />
);

const setup = (propOverrides, method) => {
  const props = Object.assign(
    {
      // use this to assign some default props
    },
    propOverrides
  );

  let wrapper;

  if (method == 'mount') {
    wrapper = mount(<WrapperComp {...props} />);
  } else {
    wrapper = shallow(<WrapperComp {...props} />);
  }

  return {
    props,
    wrapper,
  };
};

const classicN2oProps = {
  activePage: 1,
  count: 151,
  size: 10,
  maxButtons: 4,
  stepIncrement: 10,
};

describe('<Pagination />', () => {
  it('проверяет рендер с дефолтными настройками', () => {
    const { wrapper } = setup();
    expect(wrapper).toMatchSnapshot();
  });

  it('проверяет рендер с полным функционалом', () => {
    const { wrapper } = setup({
      prev: true,
      next: true,
      first: true,
      last: true,
      maxButtons: 4,
      stepIncrement: 10,
      count: 151,
      size: 10,
      activePage: 1,
    });
    expect(wrapper).toMatchSnapshot();
  });

  it('проверяет рендер с классическими настрйоками n2o', () => {
    const { wrapper } = setup(classicN2oProps);
    expect(wrapper).toMatchSnapshot();
  });

  // it('проверяет состояние на разных страницах', () => {
  //   const values = [3, 4, 5, 6, 7, 13, 14, 15, 16, 1];
  //   const { wrapper } = setup(classicN2oProps, 'mount');
  //   values.forEach(v => {
  //     expect(wrapper).toMatchSnapshot();
  //     wrapper
  //       .find('.page-item')
  //       .at(v - 1)
  //       .simulate('click');
  //     expect(wrapper).toMatchSnapshot();
  //   });
  // });

  it('проверяет работы вызова callback при смене страницы', () => {
    const value = 3;
    const onSelect = jest.fn();
    const { wrapper } = setup(
      { ...classicN2oProps, onSelect: onSelect },
      'mount'
    );

    expect(wrapper).toMatchSnapshot();

    wrapper
      .find('.page-item')
      .at(value - 1)
      .simulate('click');

    expect(onSelect).toHaveBeenCalledWith(3, expect.anything());
  });

  it('проверяет установку класса active для номера страницы', () => {
    const { wrapper } = setup({ ...classicN2oProps }, 'mount');
    expect(wrapper.find('.page-item.active > .page-link').text()).toEqual('1');
    wrapper.setProps({ activePage: 3 });
    wrapper.update();
    expect(wrapper.find('.page-item.active > .page-link').text()).toEqual('3');
  });
});
