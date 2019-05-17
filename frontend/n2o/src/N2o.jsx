import '@babel/polyfill';
import 'whatwg-fetch';

import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Provider } from 'react-redux';
import { Switch, Route } from 'react-router-dom';
import { ConnectedRouter } from 'connected-react-router';
import { pick, keys } from 'lodash';
import { compose, withContext, defaultProps, withProps } from 'recompose';
import { IntlProvider, addLocaleData } from 'react-intl';

import history from './history';
import configureStore from './store';

import FactoryProvider from './core/factory/FactoryProvider';
import createFactoryConfig, {
  factories,
} from './core/factory/createFactoryConfig';
import factoryConfigShape from './core/factory/factoryConfigShape';

import SecurityProvider from './core/auth/SecurityProvider';

import RootPage from './components/core/RootPage';

import ruLocaleData from 'react-intl/locale-data/ru';
import Application from './components/core/Application';
import { HeaderFooterTemplate } from './components/core/templates';
import DefaultBreadcrumb from './components/core/Breadcrumb/DefaultBreadcrumb';
import globalFnDate from './utils/globalFnDate';
import configureErrorPages from './components/errors';

addLocaleData(ruLocaleData);

class N2o extends Component {
  constructor(props) {
    super(props);
    const config = {
      security: props.security,
      messages: props.messages,
      customReducers: props.customReducers,
      customSagas: props.customSagas,
    };
    this.store = configureStore({}, history, config);
    globalFnDate.addFormat(props.formats);
  }

  generateCustomConfig() {
    return pick(this.props, keys(factories));
  }

  render() {
    const { routes, security } = this.props;

    const config = createFactoryConfig(this.generateCustomConfig());
    const errorPages = configureErrorPages(config.customErrorPages);

    return (
      <Provider store={this.store}>
        <SecurityProvider {...security}>
          <Application
            render={(locale, messages) => (
              <IntlProvider locale={locale} messages={messages}>
                <FactoryProvider
                  config={config}
                  securityBlackList={['actions']}
                >
                  <ConnectedRouter history={history}>
                    <Switch>
                      {routes.map(route => (
                        <Route {...route} />
                      ))}
                      {errorPages.map(route => (
                        <Route {...route} />
                      ))}
                      <Route path="/:pageUrl*" render={RootPage} />
                    </Switch>
                  </ConnectedRouter>
                </FactoryProvider>
              </IntlProvider>
            )}
          />
        </SecurityProvider>
      </Provider>
    );
  }
}

N2o.propTypes = {
  ...factoryConfigShape,
  defaultTemplate: PropTypes.element,
  defaultBreadcrumb: PropTypes.element,
  defaultPromptMessage: PropTypes.string,
  formats: PropTypes.shape({
    dateFormat: PropTypes.string,
    timeFormat: PropTypes.string,
  }),
  routes: PropTypes.arrayOf(
    PropTypes.shape({
      path: PropTypes.string,
      component: PropTypes.element,
      exact: PropTypes.bool,
      strict: PropTypes.bool,
    })
  ),
  security: PropTypes.shape({
    authProvider: PropTypes.func,
    redirectPath: PropTypes.string,
    externalLoginUrl: PropTypes.string,
    loginComponent: PropTypes.element,
    userMenuComponent: PropTypes.element,
    forbiddenComponent: PropTypes.element,
  }),
  messages: PropTypes.shape({
    timeout: PropTypes.shape({
      error: PropTypes.number,
      success: PropTypes.number,
      warning: PropTypes.number,
      info: PropTypes.number,
    }),
  }),
  customReducers: PropTypes.object,
  customSagas: PropTypes.array,
};

const EnhancedN2O = compose(
  defaultProps({
    defaultTemplate: HeaderFooterTemplate,
    defaultBreadcrumb: DefaultBreadcrumb,
    defaultPromptMessage:
      'Все несохраненные данные будут утеряны, вы уверены, что хотите уйти?',
    formats: {
      dateFormat: 'DD.MM.YYYY',
      timeFormat: 'HH:mm:ss',
    },
    routes: [],
    security: {},
    messages: {},
    customReducers: {},
    customSagas: [],
  }),
  withContext(
    {
      defaultTemplate: PropTypes.element,
      defaultBreadcrumb: PropTypes.element,
      defaultPromptMessage: PropTypes.string,
    },
    props => ({
      defaultTemplate: props.defaultTemplate,
      defaultBreadcrumb: props.defaultBreadcrumb,
      defaultPromptMessage: props.defaultPromptMessage,
    })
  ),
  withProps(props => ({
    ref: props.forwardedRef,
  }))
)(N2o);

// This works! Because forwardedRef is now treated like a regular prop.
export default React.forwardRef(({ ...props }, ref) => (
  <EnhancedN2O {...props} forwardedRef={ref} />
));
