import React from 'react'
import { connect } from 'react-redux'

import { changeLocale } from '../../../ducks/global/store'
import { localeSelector, getLocales } from '../../../ducks/global/selectors'

function LocaleSelectContainer(WrappedComponent) {
    const Component = props => (<WrappedComponent {...props} />)

    const mapStateToProps = state => ({
        locales: getLocales(state),
        value: localeSelector(state),
    })

    const mapDispatchToProps = dispatch => ({
        changeLocale: locale => dispatch(changeLocale(locale)),
    })

    return connect(
        mapStateToProps,
        mapDispatchToProps,
    )(Component)
}

export default LocaleSelectContainer
