import React, { Children, Component, createContext } from 'react'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import isEqual from 'lodash/isEqual'
import PropTypes from 'prop-types'

import { userSelector } from '../../ducks/user/selectors'
import { makePageMetadataByIdSelector } from '../../ducks/pages/selectors'
import { rootPageSelector } from '../../ducks/global/store'

import { SECURITY_CHECK, SECURITY_INITIALIZE } from './authTypes'

export const SecurityContext = createContext({
    user: null,
    checkSecurity() {},
    params: {},
})

class SecurityProvider extends Component {
    getChildContext() {
        return this.state
    }

    constructor(props) {
        super(props)

        this.state = {
            // eslint-disable-next-line react/no-unused-state
            user: null,
            // eslint-disable-next-line react/no-unused-state
            checkSecurity: this.checkSecurity,
            params: {},
        }
    }

    componentDidUpdate(prevProps) {
        const { user, pageMetadata } = this.props

        if (!isEqual(prevProps.user, user)) {
            this.setState({
                // eslint-disable-next-line react/no-unused-state
                user,
            })
        }

        // TODO кажется, что pages в redux излишни, ведь они ничего не хранят полезного, было бы
        // логично хранить только состояние одной страницы, кроме того, возникает следующая проблема:
        // когда метаданные страницы еше не получены, мы не знает ее id и в redux страница регистрируется по pageUrl,
        // в следствии чего возникает проблема с отслеживанием состояния (loading, metadata и тд)
        // с другой стороны, хранить все полученные пейджы интересно, ведь метаданные не меняются и можно оптимизировать
        // работу n2o
        if (!prevProps.pageMetadata && pageMetadata) {
            this.initSecurity()
        }
    }

    initSecurity = async () => {
        try {
            const { authProvider, ...rest } = this.props

            const initializeParams = await authProvider(SECURITY_INITIALIZE, { ...rest, ...this.state })

            if (initializeParams) {
                this.setParamsToContext(initializeParams)
            }
        } catch (err) {
            // eslint-disable-next-line no-console
            console.error(err)
        }
    }

    setParamsToContext = (params) => {
        this.setState(prevState => ({
            ...prevState,
            params,
        }))
    }

    checkSecurity = (securityConfig) => {
        const { authProvider, ...rest } = this.props

        return authProvider(SECURITY_CHECK, {
            ...rest,
            ...this.state,
            config: securityConfig,
        })
    }

    render() {
        const { children } = this.props

        return (
            <SecurityContext.Provider value={this.state}>
                {Children.only(children)}
            </SecurityContext.Provider>
        )
    }
}

SecurityProvider.propTypes = {
    authProvider: PropTypes.func,
    store: PropTypes.object,
    user: PropTypes.object,
    pageMetadata: PropTypes.object,
    children: PropTypes.node,
}

SecurityProvider.childContextTypes = {
    user: PropTypes.object,
    checkSecurity: PropTypes.func,
    params: PropTypes.object,
}

const mapStateToProps = createStructuredSelector({
    store: state => state,
    pageMetadata: state => makePageMetadataByIdSelector(rootPageSelector(state))(state),
    user: userSelector,
})

export default connect(mapStateToProps, null)(SecurityProvider)
