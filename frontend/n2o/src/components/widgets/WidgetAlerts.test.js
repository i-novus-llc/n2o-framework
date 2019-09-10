import React from 'react';
import WidgetAlerts from './WidgetAlerts';
import configureMockStore from 'redux-mock-store';
import { Provider } from 'react-redux';

const mockStore = configureMockStore();

// const setup = (storeObj, propOverrides = {}) => {
//   const props = Object.assign({}, propOverrides);
//
//   const store = mockStore(storeObj || defaultStateObj);
//
//   const wrapper = mount(
//     <Provider store={store}>
//       <WidgetAlerts {...props} />
//     </Provider>
//   );
//
//   return {
//     props,
//     wrapper,
//     store,
//   };
// };
