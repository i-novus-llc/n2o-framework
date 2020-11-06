import React from 'react';
import TimePicker from './TimePicker';
import sinon from 'sinon';
const setup = propOverrides => {
  const props = Object.assign(
    {
      // use this to assign some default props
    },
    propOverrides
  );

  const wrapper = mount(<TimePicker {...props} />);

  return {
    props,
    wrapper,
  };
};

it('проверка значений digit hh:mm:ss "mode": ["hours", "minutes", "seconds"]', () => {
  const { wrapper } = setup({
    format: 'digit',
    mode: ['hours', 'minutes', 'seconds'],
    dataFormat: 'hh:mm:ss',
    defaultValue: '01:20:00',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(3);
  expect(wrapper.state().value).toBe('01:20:00');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('01:20:00');
  expect(wrapper).toMatchSnapshot();
});

it('проверка значений digit hh:mm:ss "mode": ["minutes", "seconds"]', () => {
  const { wrapper } = setup({
    format: 'digit',
    mode: ['minutes', 'seconds'],
    dataFormat: 'hh:mm:ss',
    defaultValue: '01:20:00',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(2);
  expect(wrapper.state().value).toBe('01:20:00');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('01:20:00');
  expect(wrapper).toMatchSnapshot();
});

it('проверка значений digit hh:ss "mode": ["hours", "seconds"]', () => {
  const { wrapper } = setup({
    format: 'digit',
    mode: ['hours', 'seconds'],
    dataFormat: 'HH:ss',
    defaultValue: '12:20',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(2);
  expect(wrapper.state().value).toBe('12:20');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('12:20');
  expect(wrapper).toMatchSnapshot();
});

it('проверка значений digit hh:mm:ss "mode": ["minutes"]', () => {
  const { wrapper } = setup({
    format: 'digit',
    mode: ['minutes'],
    dataFormat: 'hh:mm:ss',
    defaultValue: '01:20:00',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(1);
  expect(wrapper.state().value).toBe('01:20:00');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('01:20:00');
  expect(wrapper).toMatchSnapshot();
});

it('проверка значений digit ss "mode": ["seconds"]', () => {
  const { wrapper } = setup({
    format: 'digit',
    mode: ['seconds'],
    dataFormat: 'ss',
    defaultValue: '10',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(1);
  expect(wrapper.state().value).toBe('10');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('10');
  expect(wrapper).toMatchSnapshot();
});

it('проверка значений digit mm:ss "mode": ["minutes"]', () => {
  const { wrapper } = setup({
    format: 'digit',
    mode: ['seconds'],
    dataFormat: 'mm:ss',
    defaultValue: '10:20',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(1);
  expect(wrapper.state().value).toBe('10:20');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('10:20');
  expect(wrapper).toMatchSnapshot();
});

it('проверка значений symbols hh:mm:ss "mode": ["hours", "minutes", "seconds"]', () => {
  const { wrapper } = setup({
    format: 'symbols',
    mode: ['hours', 'minutes', 'seconds'],
    dataFormat: 'HH:mm:ss',
    defaultValue: '10:20:30',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(3);
  expect(wrapper.state().value).toBe('10:20:30');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('10 ч 20 мин 30 сек');
  expect(wrapper).toMatchSnapshot();
});

it('проверка значений symbols hh:mm:ss "mode": ["minutes", "seconds"]', () => {
  const { wrapper } = setup({
    format: 'symbols',
    mode: ['minutes', 'seconds'],
    dataFormat: 'HH:mm:ss',
    defaultValue: '10:20:30',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(2);
  expect(wrapper.state().value).toBe('10:20:30');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('20 мин 30 сек');
  expect(wrapper).toMatchSnapshot();
});

it('проверка значений symbols hh:mm:ss "mode": ["minutes"]', () => {
  const { wrapper } = setup({
    format: 'symbols',
    mode: ['minutes'],
    dataFormat: 'HH:mm:ss',
    defaultValue: '10:20:10',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(1);
  expect(wrapper.state().value).toBe('10:20:10');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('20 мин');
  expect(wrapper).toMatchSnapshot();
});

it('проверка значений symbols hh:mm:ss "mode": ["hours", "seconds"]', () => {
  const { wrapper } = setup({
    format: 'symbols',
    mode: ['hours', 'seconds'],
    dataFormat: 'HH:ss',
    defaultValue: '10:20',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(2);
  expect(wrapper.state().value).toBe('10:20');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('10 ч 20 сек');
  expect(wrapper).toMatchSnapshot();
});

it('проверка значений symbols hh:mm:ss "mode": ["seconds"]', () => {
  const { wrapper } = setup({
    format: 'symbols',
    mode: ['seconds'],
    dataFormat: 'ss',
    defaultValue: '10',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(1);
  expect(wrapper.state().value).toBe('10');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('10 сек');
  expect(wrapper).toMatchSnapshot();
});

it('проверка значений symbols mm:ss "mode": ["minutes"]', () => {
  const { wrapper } = setup({
    format: 'symbols',
    mode: ['minutes'],
    dataFormat: 'mm:ss',
    defaultValue: '10:20',
  });
  const input = wrapper.find('.rc-time-picker-input');
  input.simulate('click');
  expect(wrapper.find('.rc-time-picker-panel-select').length).toBe(1);
  expect(wrapper.state().value).toBe('10:20');
  expect(
    wrapper
      .find('input')
      .first()
      .props().value
  ).toBe('10 мин');
  expect(wrapper).toMatchSnapshot();
});
