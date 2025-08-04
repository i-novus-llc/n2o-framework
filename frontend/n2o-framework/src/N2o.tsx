import React, {
    Component,
    ComponentType,
    ReactNode,
    LegacyRef,
    useContext,
    forwardRef,
} from 'react'
import { Provider } from 'react-redux'
import pick from 'lodash/pick'
import { withTranslation, WithTranslation } from 'react-i18next'
import { type Store } from 'redux'

import './i18n'

// @ts-ignore TODO если включить разрешающее правило, сломается build
import packageJson from '../package.json'

import history from './history'
import configureStore from './store'
import { FactoryProvider } from './core/factory/FactoryProvider'
import factoryPoints from './core/factory/factoryPoints'
import apiProvider from './core/api'
import Router from './components/core/Router'
import Application from './components/core/Application'
import { Template } from './components/core/templates'
import globalFnDate from './utils/globalFnDate'
import { errorTemplates } from './components/errors/errorTemplates'
import { type LocalesPreset, defaultLocalesPreset } from './locales'
import { ErrorHandlersProvider } from './core/error/Container'
import { ExpressionContext } from './core/Expression/Context'
import '@fortawesome/fontawesome-free/css/all.css'
import functions from './utils/functions'
import { defaultProvider } from './core/auth/Provider'
import { MarkdownFieldMappers } from './components/widgets/Form/fields/MarkdownField/helpers'
import { State } from './ducks/State'
import { TemplateProps } from './components/core/templates/types'
import { WindowType } from './components/core/WindowType'
import { ErrorContainerProviderProps } from './core/error/types'
import { EMPTY_OBJECT } from './utils/emptyTypes'

const { version } = packageJson

export interface N2OProps extends WithTranslation {
    formats?: {
        dateFormat: string
        timeFormat: string
    };
    security?: {
        provider: Function
        authUrl?: string
    };
    messages?: {
        timeout?: {
            error?: number
            success?: number
            warning?: number
            info?: number
        };
    };
    customReducers?: object
    customSagas?: Function[]
    apiProvider?: Function
    evalContext?: object
    children?: ReactNode
    locales?: LocalesPreset
    initialState?: object
    extraDefaultErrorPages?: Record<number | string, React.ComponentType>
    defaultTemplate?: ComponentType<TemplateProps>
    forwardedRef?: LegacyRef<HTMLDivElement>
    // eslint-disable-next-line react/no-unused-prop-types
    markdownFieldMappers?: MarkdownFieldMappers
}

export interface N2OState {
    isOnline: boolean
}

export interface N2OContextProps {
    defaultTemplate?: ComponentType<TemplateProps>
    extraDefaultErrorPages?: object
    markdownFieldMappers?: MarkdownFieldMappers
    version: string
}

export const N2OContext = React.createContext<N2OContextProps>({
    markdownFieldMappers: {},
    defaultTemplate: Template,
    extraDefaultErrorPages: {},
    version,
})

class N2oBody extends Component<N2OProps, N2OState> {
    static defaultProps = {
        defaultTemplate: Template,
        extraDefaultErrorPages: {},
        formats: {
            dateFormat: 'YYYY-MM-DD',
            timeFormat: 'HH:mm:ss',
        },
        security: {
            provider: defaultProvider,
        },
        messages: {},
        customReducers: {},
        customSagas: [],
        apiProvider,
        evalContext: {},
        locales: {},
        initialState: {},
        markdownFieldMappers: {},
    }

    store: Store<State>

    constructor(props: N2OProps) {
        super(props)
        this.state = { isOnline: true }
        const config = {
            security: {
                provider: defaultProvider,
                ...props.security,
            },
            messages: props.messages,
            customReducers: props.customReducers,
            customSagas: props.customSagas,
            apiProvider: props.apiProvider,
            factories: this.generateConfig(),
        }

        // @ts-ignore FIXME configureStore не типизирован
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
            children,
            i18n,
            locales: customLocalesPreset = EMPTY_OBJECT,
            evalContext,
            extraDefaultErrorPages,
            defaultTemplate,
            forwardedRef,
        } = this.props

        const { isOnline } = this.state

        const config = this.generateConfig()
        const handlers = [errorTemplates(extraDefaultErrorPages, isOnline)] as unknown as ErrorContainerProviderProps['value']

        const { _n2oEvalContext = {} } = window as WindowType

        const context = { ..._n2oEvalContext, ...functions, ...(evalContext || {}) }

        return (
            <Provider store={this.store}>
                <div ref={forwardedRef}>
                    <ExpressionContext.Provider value={context}>
                        <ErrorHandlersProvider value={handlers} isOnline={isOnline}>
                            <FactoryProvider config={config}>
                                <Application
                                    i18n={i18n}
                                    localesPreset={defaultLocalesPreset}
                                    customLocales={customLocalesPreset}
                                    render={() => <Router defaultTemplate={defaultTemplate}>{children}</Router>}
                                />
                            </FactoryProvider>
                        </ErrorHandlersProvider>
                    </ExpressionContext.Provider>
                </div>
            </Provider>
        )
    }
}

export const withN2OContext = <P extends object>(WrappedComponent: React.ComponentType<P>) => (props: P) => {
    const context = useContext(N2OContext)

    return <WrappedComponent {...props} {...context} />
}

const EnhancedN2O = withTranslation()(
    forwardRef<HTMLDivElement, N2OProps>((props: N2OProps, ref?: LegacyRef<HTMLDivElement>) => {
        return (
            <N2OContext.Provider
                value={{
                    defaultTemplate: props.defaultTemplate,
                    extraDefaultErrorPages: props.extraDefaultErrorPages,
                    markdownFieldMappers: props.markdownFieldMappers,
                    version,
                }}
            >
                <N2oBody {...props} forwardedRef={ref} />
            </N2OContext.Provider>
        )
    }),
)

export const N2O = forwardRef<HTMLDivElement, N2OProps>((props, ref) => (
    <EnhancedN2O {...props} ref={ref} />
))

export default N2O
