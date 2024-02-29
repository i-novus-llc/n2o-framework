import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Provider } from 'react-redux'
import pick from 'lodash/pick'
import { compose, withContext, defaultProps, withProps } from 'recompose'
import { withTranslation } from 'react-i18next'

import './i18n'
import packageJson from '../package.json'

import history from './history'
import configureStore from './store'
import { FactoryProvider } from './core/factory/FactoryProvider'
import factoryPoints from './core/factory/factoryPoints'
import factoryConfigShape from './core/factory/factoryConfigShape'
import apiProvider from './core/api'
import SecurityProvider from './core/auth/SecurityProvider'
import Router from './components/core/Router'
import Application from './components/core/Application'
import { Template } from './components/core/templates'
import { DefaultBreadcrumb } from './components/core/Breadcrumb/DefaultBreadcrumb'
import globalFnDate from './utils/globalFnDate'
import { errorTemplates } from './components/errors/errorTemplates'
import locales from './locales'
import { Tooltip } from './components/snippets/Tooltip/Tooltip'
import { ErrorHandlersProvider } from './core/error/Container'
import '@fortawesome/fontawesome-free/css/all.css'

const { version } = packageJson

class N2o extends Component {
    constructor(props) {
        super(props)
        this.state = { isOnline: true }
        const config = {
            security: props.security,
            messages: props.messages,
            customReducers: props.customReducers,
            customSagas: props.customSagas,
            apiProvider: props.apiProvider,
            factories: this.generateConfig(),
        }

        // eslint-disable-next-line no-underscore-dangle
        window._n2oEvalContext = props.evalContext

        this.store = configureStore(props.initialState, history, config)
        globalFnDate.addFormat(props.formats)
    }

    generateConfig() {
        return pick(this.props, factoryPoints)
    }

    componentDidMount() {
        window.addEventListener('offline', this.handleOffline)
        window.addEventListener('online', this.handleOnline)
    }

    componentWillUnmount() {
        window.removeEventListener('offline', this.handleOffline)
        window.removeEventListener('online', this.handleOnline)
    }

    handleOffline = () => { this.setState({ isOnline: false }) }

    handleOnline = () => { this.setState({ isOnline: true }) }

    render() {
        const {
            security,
            realTimeConfig,
            embeddedRouting,
            children,
            i18n,
            locales: customLocales = {},
            extraDefaultErrorPages,
        } = this.props

        const { isOnline } = this.state

        const config = this.generateConfig()
        const handlers = [errorTemplates(extraDefaultErrorPages, isOnline)]

        return (
            <Provider store={this.store}>
                <ErrorHandlersProvider value={handlers} isOnline={isOnline}>
                    <SecurityProvider {...security}>
                        <FactoryProvider config={config} securityBlackList={['actions']}>
                            <Application
                                i18n={i18n}
                                locales={locales}
                                customLocales={customLocales}
                                realTimeConfig={realTimeConfig}
                                render={() => <Router embeddedRouting={embeddedRouting}>{children}</Router>}
                            />
                        </FactoryProvider>
                    </SecurityProvider>
                </ErrorHandlersProvider>
            </Provider>
        )
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
    formats: PropTypes.shape({
        dateFormat: PropTypes.string,
        timeFormat: PropTypes.string,
    }),
    security: PropTypes.shape({
        authProvider: PropTypes.func,
        authUrl: PropTypes.string,
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
    initialState: PropTypes.object,
}

const EnhancedN2O = compose(
    withTranslation(),
    defaultProps({
        defaultTemplate: Template,
        defaultBreadcrumb: DefaultBreadcrumb,
        defaultPage: 'StandardPage',
        extraDefaultErrorPages: {},
        defaultTooltip: Tooltip,
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
        initialState: {},
        markdownFieldMappers: {},
    }),
    withContext(
        {
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
            extraDefaultErrorPages: PropTypes.arrayOf(
                PropTypes.oneOfType([PropTypes.node, PropTypes.element, PropTypes.func]),
            ),
            defaultTooltip: PropTypes.oneOfType([PropTypes.node, PropTypes.element, PropTypes.func]),
            version: PropTypes.string,
            markdownFieldMappers: PropTypes.object,
        },
        props => ({
            defaultTemplate: props.defaultTemplate,
            defaultBreadcrumb: props.defaultBreadcrumb,
            defaultPage: props.defaultPage,
            extraDefaultErrorPages: props.extraDefaultErrorPages,
            defaultTooltip: props.defaultTooltip,
            markdownFieldMappers: props.markdownFieldMappers,
            version,
        }),
    ),
    withProps(props => ({
        ref: props.forwardedRef,
    })),
)(N2o)

// This works! Because forwardedRef is now treated like a regular prop.
export default React.forwardRef(({ ...props }, ref) => (
    <EnhancedN2O {...props} forwardedRef={ref} />
))
