import React from 'react';
import configureStore from '../../../store';
import { Provider } from 'react-redux';
import TilesContainer from './TilesContainer';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import metadata from './TilesWidget.meta';
import history from '../../../history';

const store = configureStore({}, history, {});

const setup = propsOverride => {
  return mount(
    <Provider store={store}>
      <TilesContainer
        level={WIDGETS}
        {...metadata['Page_Tiles']}
        {...propsOverride}
        id="Page_Tiles"
      />
    </Provider>
  );
};

describe('<TilesContainer />', () => {
  it('отрисовка', () => {
    const wrapper = setup();
    expect(wrapper).toMatchSnapshot();
  });
});
