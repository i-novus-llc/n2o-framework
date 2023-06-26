import React from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'

import { registerFieldDependency } from '../../ducks/form/store'

export default (Component) => {
    class DependencyContainer extends React.Component {
        constructor(props) {
            super(props)
            this.initIfNeeded()
        }

        /**
         * Регистрация дополнительных свойств поля
         */
        initIfNeeded() {
            const { dependency } = this.props

            if (dependency) {
                this.registerDependency()
            }
        }

        registerDependency() {
            const { id, form, dependency, registerFieldDependency } = this.props

            registerFieldDependency(form, id, dependency)
        }

        render() {
            return <Component {...this.props} />
        }
    }
    const mapDispatchToProps = dispatch => ({
        registerFieldDependency: (form, id, dependency) => (
            dispatch(registerFieldDependency(form, id, dependency))
        ),
    })

    DependencyContainer.propTypes = {
        dependency: PropTypes.any,
        id: PropTypes.string,
        form: PropTypes.any,
        registerFieldDependency: PropTypes.func,
    }

    return connect(
        null,
        mapDispatchToProps,
    )(DependencyContainer)
}
