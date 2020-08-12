import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Provider } from 'react-redux';
import pick from 'lodash/pick';
import { compose, withContext, defaultProps, withProps } from 'recompose';
import { IntlProvider, addLocaleData } from 'react-intl';
import { withTranslation } from 'react-i18next';
import './i18n';

import history from './history';
import configureStore from './store';
import packageJson from '../package';

import FactoryProvider from './core/factory/FactoryProvider';
import factoryPoints from './core/factory/factoryPoints';
import factoryConfigShape from './core/factory/factoryConfigShape';

import apiProvider from './core/api';
import SecurityProvider from './core/auth/SecurityProvider';

import Router from './components/core/Router';

import ruLocaleData from 'react-intl/locale-data/ru';
import Application from './components/core/Application';
import { HeaderFooterTemplate } from './components/core/templates';
import DefaultBreadcrumb from './components/core/Breadcrumb/DefaultBreadcrumb';
import globalFnDate from './utils/globalFnDate';
import configureErrorPages from './components/errors';
import locales from './locales';

addLocaleData(ruLocaleData);

const { version } = packageJson;

class N2o extends Component {
  constructor(props) {
    super(props);
    const config = {
      security: props.security,
      messages: props.messages,
      customReducers: props.customReducers,
      customSagas: props.customSagas,
      apiProvider: props.apiProvider,
      factories: this.generateConfig(),
    };

    window._n2oEvalContext = props.evalContext;

    this.store = configureStore({}, history, config);
    globalFnDate.addFormat(props.formats);
  }

  generateConfig() {
    return pick(this.props, factoryPoints);
  }

  render() {
    const {
      security,
      realTimeConfig,
      embeddedRouting,
      children,
      i18n,
      locales: customLocales = {},
    } = this.props;

    const config = this.generateConfig();

    return (
      <Provider store={this.store}>
        <SecurityProvider {...security}>
          <Application
            i18n={i18n}
            locales={locales}
            customLocales={customLocales}
            realTimeConfig={realTimeConfig}
            render={({ locale, messages }) => (
              <IntlProvider locale={locale} messages={messages}>
                <FactoryProvider
                  config={config}
                  securityBlackList={['actions']}
                >
                  <Router embeddedRouting={embeddedRouting}>{children}</Router>
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
  defaultTemplate: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.element,
    PropTypes.node,
  ]),
  defaultBreadcrumb: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.element,
    PropTypes.node,
  ]),
  defaultPromptMessage: PropTypes.string,
  formats: PropTypes.shape({
    dateFormat: PropTypes.string,
    timeFormat: PropTypes.string,
  }),
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
  customErrorPages: PropTypes.object,
  apiProvider: PropTypes.func,
  realTimeConfig: PropTypes.bool,
  embeddedRouting: PropTypes.bool,
  evalContext: PropTypes.object,
  children: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.node),
    PropTypes.node,
  ]),
  version: PropTypes.string,
  locales: PropTypes.object,
};

const EnhancedN2O = compose(
  withTranslation(),
  defaultProps({
    defaultTemplate: HeaderFooterTemplate,
    defaultBreadcrumb: DefaultBreadcrumb,
    defaultPage: 'StandardPage',
    // key from locale translation
    defaultPromptMessage: 'defaultPromptMessage',
    defaultErrorPages: configureErrorPages(),
    formats: {
      dateFormat: 'YYYY-MM-DD',
      timeFormat: 'HH:mm:ss',
    },
    security: {},
    messages: {},
    customReducers: {},
    customSagas: [],
    apiProvider,
    realTimeConfig: true,
    embeddedRouting: true,
    evalContext: {},
    locales: {},
  }),
  withContext(
    {
      t: PropTypes.func,
      i18n: PropTypes.func,
      defaultTemplate: PropTypes.oneOfType([
        PropTypes.func,
        PropTypes.element,
        PropTypes.node,
      ]),
      defaultBreadcrumb: PropTypes.oneOfType([
        PropTypes.func,
        PropTypes.element,
        PropTypes.node,
      ]),
      defaultPage: PropTypes.oneOfType([
        PropTypes.func,
        PropTypes.element,
        PropTypes.node,
      ]),
      defaultPromptMessage: PropTypes.string,
      defaultErrorPages: PropTypes.arrayOf(
        PropTypes.oneOfType([PropTypes.node, PropTypes.element, PropTypes.func])
      ),
      version: PropTypes.string,
    },
    props => ({
      t: props.t,
      i18n: props.i18n,
      defaultTemplate: props.defaultTemplate,
      defaultBreadcrumb: props.defaultBreadcrumb,
      defaultPromptMessage: props.defaultPromptMessage,
      defaultPage: props.defaultPage,
      defaultErrorPages: props.defaultErrorPages,
      version: version,
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
