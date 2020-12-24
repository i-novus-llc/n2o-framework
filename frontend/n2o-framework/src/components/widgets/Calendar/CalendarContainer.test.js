import React from 'react';
import { Provider } from 'react-redux';
import CalendarContainer from './CalendarContainer';
import metadata from './CalendarWidget.meta';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import configureStore from '../../../store';
import history from '../../../history';

const store = configureStore({}, history, {});

const setup = propsOverride => {
  return mount(
    <Provider store={store}>
      <CalendarContainer
        level={WIDGETS}
        {...metadata['Page_Calendar']}
        {...propsOverride}
        id="Page_Calendar"
      />
    </Provider>
  );
};

describe('<CalendarContainer />', () => {
  it('отрисовка', () => {
    const wrapper = setup();
    expect(wrapper.find(CalendarContainer).exists()).toBe(true);
  });
  // it('срабатывает action на клик по ячейке', () => {
  //   const event = { disabled: false };
  //   const dispatch = sinon.spy();
  //
  //   const wrapper = setup({ defaultView: 'week', dispatch: dispatch });
  //   wrapper.find(timeSlotWrapper).first().simulate('select', event);
  //   expect(dispatch.calledOnce).toBeTruthy();
  // });
});
