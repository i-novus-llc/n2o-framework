import React, { useContext } from 'react'
import PropTypes from 'prop-types'
import { bindActionCreators, Dispatch } from 'redux'
import get from 'lodash/get'
import map from 'lodash/map'
import keys from 'lodash/keys'
import { connect } from 'react-redux'
import {
    compose,
    withContext,
    lifecycle,
    withHandlers,
} from 'recompose'
import numeral from 'numeral'

import { ErrorContainer } from '../../core/error/Container'
import { Block } from '../snippets/Block/Block'
import { State } from '../../ducks/State'
import {
    requestConfig as requestConfigAction,
    setReady as setReadyAction,
    registerLocales,
} from '../../ducks/global/store'
import { globalSelector } from '../../ducks/global/selectors'
import { FactoryLevels } from '../../core/factory/factoryLevels'
import { FactoryContext } from '../../core/factory/context'

// @ts-ignore ignore import error from js file
import { GlobalAlertsConnected } from './GlobalAlerts'

export interface ApplicationProps {
    ready: boolean
    loading: boolean
    locale: string
    error: Error
    render(): React.ReactNode
}

/* data sources register on the app layer are executing in the meta.js sagas */
function Application({
    ready,
    locale,
    loading,
    error,
    render,
}: ApplicationProps) {
    const { getComponent } = useContext(FactoryContext)
    const FactorySpinner = getComponent('Spinner', FactoryLevels.SNIPPETS)

    numeral.locale(locale)

    return (
        <>
            <GlobalAlertsConnected />
            {/* @ts-ignore FIXME разобраться в типизации */}
            <ErrorContainer error={error}>
                <>
                    {!ready && FactorySpinner
                        ? <FactorySpinner type="cover" loading={loading} />
                        : null }

                    {ready ? (
                        <Block disabled={loading}>
                            {render()}
                        </Block>
                    ) : null}
                </>
            </ErrorContainer>
        </>
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
        (props: ApplicationProps) => ({
            getFromConfig: (key: string) => get(props, key),
            configLocale: props.locale,
        }),
    ),
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
                requestConfig, locales = {}, customLocales, registerLocales, addCustomLocales,
            } = this.props

            addCustomLocales()
            registerLocales(keys({ ...locales, ...customLocales }))

            requestConfig()
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
