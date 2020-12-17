import React from 'react';
import configureStore from '../../../store';
import { Provider } from 'react-redux';
import CardsContainer from './CardsContainer';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import metadata from './CardsWidget.meta';
import history from '../../../history';

const store = configureStore({}, history, {});

const setup = propsOverride => {
  return mount(
    <Provider store={store}>
      <CardsContainer
        level={WIDGETS}
        {...metadata['Page_Cards']}
        {...propsOverride}
        id="Page_Cards"
      />
    </Provider>
  );
};

describe('<CardsContainer />', () => {
  it('отрисовка', () => {
    const wrapper = setup();
    expect(wrapper).toMatchSnapshot();
  });
});
