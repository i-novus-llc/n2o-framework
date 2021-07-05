import React from 'react'
import { connect } from 'react-redux'

import { getLocales, localeSelector, changeLocale } from '../../../ducks/global/store'

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
