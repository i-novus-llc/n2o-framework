import React from 'react'
import { connect } from 'react-redux'

import { getLocales, localeSelector } from '../../../selectors/global'
import { changeLocale } from '../../../actions/global'

function LocaleSelectContainer(WrappedComponent) {
    const Component = function (props) {
        return <WrappedComponent {...props} />
    }

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
