import React from 'react'
import PropTypes from 'prop-types'
import { bindActionCreators, Dispatch } from 'redux'
import { i18n } from 'i18next'
import get from 'lodash/get'
import map from 'lodash/map'
import keys from 'lodash/keys'
import { connect } from 'react-redux'
import {
    compose,
    withContext,
    lifecycle,
    withHandlers,
    getContext,
} from 'recompose'
import numeral from 'numeral'

import 'numeral/locales/ru'

import { Block } from '../snippets/Block/Block'
import { State } from '../../ducks/State'
import { DataSourceState } from '../../ducks/datasource/DataSource'
import {
    requestConfig as requestConfigAction,
    setReady as setReadyAction,
    registerLocales,
    globalSelector,
// @ts-ignore ignore import error from js file
} from '../../ducks/global/store'
// @ts-ignore ignore import error from js file
import { errorController } from '../errors/errorController'

export interface IApplicationProps {
    i18n: i18n
    ready: boolean
    loading: boolean
    realTimeConfig: boolean
    locale: string
    menu: {
        datasources: Record<string, DataSourceState>
    }
    error: Record<string, DataSourceState>
    defaultErrorPages: React.ReactNode[]
    render(): React.ReactNode
}

/* data sources register on the app layer are executing in the meta.js sagas */
function Application(props: IApplicationProps) {
    const {
        ready,
        locale,
        loading,
        error,
        defaultErrorPages,
        render,
    } = props

    numeral.locale(locale)

    if (error) {
        const { status } = error
        const errorPage = errorController(status, defaultErrorPages)

        return React.createElement(errorPage)
    }

    return (
        <Block disabled={loading}>
            {ready && render()}
        </Block>
    )
}

const mapStateToProps = (state: State) => ({
    ...globalSelector(state),
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setReady: bindActionCreators(setReadyAction, dispatch),
    requestConfig: bindActionCreators(requestConfigAction, dispatch),
    registerLocales: (locales: string[]) => dispatch(registerLocales(locales)),
})

export default compose(
    connect(
        mapStateToProps,
        mapDispatchToProps,
    ),
    withContext(
        {
            getFromConfig: PropTypes.func,
            configLocale: PropTypes.string,
        },
        (props: IApplicationProps) => ({
            getFromConfig: (key: string) => get(props, key),
            configLocale: props.locale,
        }),
    ),
    getContext({
        defaultErrorPages: PropTypes.arrayOf(
            PropTypes.oneOfType([PropTypes.node, PropTypes.element, PropTypes.func]),
        ),
    }),
    withHandlers({
        // @ts-ignore нет смысла типизировать, будет переделано
        addCustomLocales: ({ i18n, customLocales }) => () => {
            map(keys(customLocales), (locale) => {
                i18n.addResourceBundle(locale, 'translation', customLocales[locale])
            })
        },
    }),
    lifecycle({
        componentDidMount() {
            const {
                // @ts-ignore нет смысла типизировать, будет переделано
                realTimeConfig, requestConfig, setReady, locales = {}, customLocales, registerLocales, addCustomLocales,
            } = this.props

            addCustomLocales()
            registerLocales(keys({ ...locales, ...customLocales }))

            if (realTimeConfig) {
                requestConfig()
            } else {
                setReady()
            }
        },
        componentDidUpdate(prevProps) {
            // @ts-ignore нет смысла типизировать, будет переделано
            const { locale, i18n } = this.props

            // @ts-ignore нет смысла типизировать, будет переделано
            if (prevProps.locale !== locale) {
                i18n.changeLanguage(locale)
            }
        },
    }),
// @ts-ignore нет смысла типизировать, будет переделано
)(Application)
