import React, { Component, ContextType, createContext, ReactNode } from 'react'
import { bindActionCreators, Dispatch } from 'redux'
import get from 'lodash/get'
import map from 'lodash/map'
import keys from 'lodash/keys'
import { connect } from 'react-redux'
import numeral from 'numeral'
import { type i18n } from 'i18next'
import { Block } from '@i-novus/n2o-components/lib/display/Block'

import { ErrorContainer } from '../../core/error/Container'
import { State } from '../../ducks/State'
import {
    requestConfig as requestConfigAction,
    setReady as setReadyAction,
    registerLocales,
} from '../../ducks/global/store'
import { globalSelector } from '../../ducks/global/selectors'
import { FactoryLevels } from '../../core/factory/factoryLevels'
import { FactoryContext } from '../../core/factory/context'
import { ErrorContainerProps } from '../../core/error/types'
import { locales } from '../../locales'
import { type SidebarProps } from '../../plugins/SideBar/types'
import { type SimpleHeaderBodyProps } from '../../plugins/Header/SimpleHeader/SimpleHeader'

import { GlobalAlertsConnected } from './GlobalAlerts'

export interface Config {
    datasources: Record<string, unknown>
    header: SimpleHeaderBodyProps
    layout: Record<string, unknown>
    sidebars: SidebarProps[]
}

export interface ApplicationProps extends ReturnType<typeof mapDispatchToProps> {
    ready?: boolean
    loading: boolean
    locale: string
    error?: ErrorContainerProps['error']
    i18n: i18n
    locales?: typeof locales
    customLocales?: Record<string, unknown>
    render(): ReactNode
    // eslint-disable-next-line react/no-unused-prop-types
    menu: Config
}

export interface ApplicationContextValue {
    getFromConfig(key: string): Config | undefined
    configLocale: string
}

export const ApplicationContext = createContext<ApplicationContextValue>({
    getFromConfig: () => undefined,
    configLocale: 'en',
})

class Application extends Component<ApplicationProps> {
    static contextType = FactoryContext

    context!: ContextType<typeof FactoryContext>

    addCustomLocales = () => {
        const { customLocales = {}, i18n } = this.props

        map(keys(customLocales), (locale) => {
            i18n.addResourceBundle(locale, 'translation', customLocales[locale])
        })
    }

    componentDidMount() {
        const {
            requestConfig,
            locales = {},
            customLocales = {},
            registerLocales,
        } = this.props

        this.addCustomLocales()
        registerLocales(keys({ ...locales, ...customLocales }))
        requestConfig()
    }

    componentDidUpdate(prevProps: ApplicationProps) {
        const { locale, i18n } = this.props

        if (prevProps.locale !== locale) {
            // eslint-disable-next-line
            i18n.changeLanguage(locale)
        }
    }

    render() {
        const { ready, locale, loading, error, render } = this.props
        const { getComponent } = this.context
        const FactorySpinner = getComponent('Spinner', FactoryLevels.SNIPPETS)

        numeral.locale(locale)

        const contextValue = {
            getFromConfig: (key: 'menu') => get(this.props, key),
            configLocale: locale,
        }

        return (
            <ApplicationContext.Provider value={contextValue}>
                <GlobalAlertsConnected />
                <ErrorContainer error={error}>
                    <>
                        {!ready && FactorySpinner && <FactorySpinner type="cover" loading={loading} />}
                        {ready && <Block disabled={loading}>{render()}</Block>}
                    </>
                </ErrorContainer>
            </ApplicationContext.Provider>
        )
    }
}

const mapStateToProps = (state: State) => ({
    ...globalSelector(state),
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    // eslint-disable-next-line react/no-unused-prop-types
    setReady: bindActionCreators(setReadyAction, dispatch),
    requestConfig: bindActionCreators(requestConfigAction, dispatch),
    registerLocales: (locales: string[]) => dispatch(registerLocales(locales)),
})

// @ts-ignore TODO объеденить типы ErrorContainerProps['error'] и error из Global
export default connect(mapStateToProps, mapDispatchToProps)(Application)
