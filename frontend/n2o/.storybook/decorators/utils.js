import configureStore from "../../src/store";
import authProviderExample from "../auth/authProviderExample";
import {createMemoryHistory as createHistory} from 'history';

const securityConfig = {
  authProvider: authProviderExample,
  redirectPath: '/login',
};
const alertsConfig = {
  timeout: {
    error: 0,
    success: 5000,
  }
};
export const history = createHistory();
const config = {
  security: securityConfig,
  messages: alertsConfig,
  customSagas: []
};
export const store = configureStore({}, history, config);

export const makeStore = () => {
  return { store, history, securityConfig };
}