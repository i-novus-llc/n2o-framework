import React, { createContext, ReactNode, useContext, useEffect } from 'react'
import { bindActionCreators, Dispatch } from 'redux'
import get from 'lodash/get'
import keys from 'lodash/keys'
import { connect } from 'react-redux'
import numeral from 'numeral'
import { type i18n } from 'i18next'
import { Block } from '@i-novus/n2o-components/lib/display/Block'

import { ErrorContainer } from '../../core/error/Container'
import { type ErrorContainerError } from '../../core/error/types'
import { State } from '../../ducks/State'
import { requestConfig as requestConfigAction, registerLocales } from '../../ducks/global/store'
import { globalSelector } from '../../ducks/global/selectors'
import { Locale, type LocaleArray, type LocalesPreset } from '../../locales'
import { type SidebarProps } from '../../plugins/SideBar/types'
import { type SimpleHeaderBodyProps } from '../../plugins/Header/SimpleHeader/SimpleHeader'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'
import { Spinner, SpinnerType } from '../../factoryComponents/Spinner'

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
    locale: Locale
    error?: ErrorContainerError
    i18n: i18n
    localesPreset: LocalesPreset
    customLocales?: LocalesPreset
    render(): ReactNode
}

export interface ApplicationContextValue {
    getFromConfig(key: string): Config | undefined
    configLocale: Locale
}

export const ApplicationContext = createContext<ApplicationContextValue>({
    getFromConfig: () => undefined,
    configLocale: Locale.en,
})

function Application(props: ApplicationProps) {
    const {
        customLocales = EMPTY_OBJECT,
        i18n,
        registerLocales,
        requestConfig,
        localesPreset = EMPTY_OBJECT,
        locale,
        ready,
        loading,
        error,
        render,
    } = props

    // componentDidMount
    useEffect(() => {
        Object.entries(customLocales).forEach(([locale, value]) => {
            i18n.addResourceBundle(locale, 'translation', value)
        })

        registerLocales(keys({ ...localesPreset, ...customLocales }))
        requestConfig()
        numeral.locale(locale)
    }, [])

    useEffect(() => {
        i18n.changeLanguage(locale).catch(() => { /* ignore */ })
        numeral.locale(locale)
    }, [locale, i18n])

    const contextValue = {
        getFromConfig: (key: 'menu') => get(props, key),
        configLocale: locale,
    }

    return (
        <ApplicationContext.Provider value={contextValue}>
            <GlobalAlertsConnected />
            <ErrorContainer error={error}>
                <>
                    {!ready && <Spinner type={SpinnerType.cover} loading={loading} />}
                    {ready && <Block disabled={loading}>{render()}</Block>}
                </>
            </ErrorContainer>
        </ApplicationContext.Provider>
    )
}

const mapStateToProps = (state: State) => ({ ...globalSelector(state) })

const mapDispatchToProps = (dispatch: Dispatch) => ({
    requestConfig: bindActionCreators(requestConfigAction, dispatch),
    registerLocales: (locale: LocaleArray) => dispatch(registerLocales(locale)),
})

export default connect(mapStateToProps, mapDispatchToProps)(Application)
