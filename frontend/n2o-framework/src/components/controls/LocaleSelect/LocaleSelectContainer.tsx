import React from 'react'
import { connect } from 'react-redux'
import { Dispatch } from 'redux'

import { changeLocale } from '../../../ducks/global/store'
import { localeSelector, getLocales } from '../../../ducks/global/selectors'
import { State } from '../../../ducks/State'

export interface Props {
    locales: string[]
    value: string
    changeLocale(locale: string): void
    className?: string
    direction?: 'left' | 'right'
}

export function LocaleSelectContainer(WrappedComponent: React.FunctionComponent<Props>) {
    const Component = (props: Props) => (<WrappedComponent {...props} />)

    const mapStateToProps = (state: State) => ({
        locales: getLocales(state),
        value: localeSelector(state),
    })

    const mapDispatchToProps = (dispatch: Dispatch) => ({
        changeLocale: (locale: string) => dispatch(changeLocale(locale)),
    })

    return connect(mapStateToProps, mapDispatchToProps)(Component)
}

export default LocaleSelectContainer
