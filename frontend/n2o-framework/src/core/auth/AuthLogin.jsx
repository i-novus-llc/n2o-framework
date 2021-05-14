import React from 'react'
import { connect } from 'react-redux'
import { compose } from 'recompose'
import { withTranslation } from 'react-i18next'
import PropTypes from 'prop-types'

import { userLogin, userLogout as userLogoutAction } from '../../actions/auth'

import SecurityCheck from './SecurityCheck'

class Login extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            username: '',
        }
        this.handleChange = this.handleChange.bind(this)
        this.handleLogin = this.handleLogin.bind(this)
    }

    handleChange(e) {
        this.setState({ username: e.target.value })
    }

    handleLogin() {
        const { username } = this.state
        const { userLogin } = this.props

        userLogin({ username, roles: [username] })
    }

    render() {
        const { t } = this.props
        const { username } = this.state

        return (
            <div>
                <input
                    type="text"
                    placeholder={t('login')}
                    value={username}
                    onChange={this.handleChange}
                />
                {' '}
                {/* eslint-disable-next-line react/button-has-type */}
                <button onClick={this.handleLogin}>Войти</button>
            </div>
        )
    }
}

const LoginContainer = connect(
    null,
    { userLogin },
)(Login)

const AuthButton = ({ userLogout }) => (
    /* eslint-disable react/jsx-one-expression-per-line */
    <SecurityCheck
        render={({ permissions, user }) => (permissions ? (
            <p>

          Привет,
                {user.username}

!
                {/* eslint-disable-next-line react/button-has-type */}
                <button onClick={userLogout}>Выйти</button>
            </p>
        ) : (
            <LoginContainer />
        ))
        }
    />
)

Login.defaultProps = {
    t: () => {},
}
Login.propTypes = {
    userLogin: PropTypes.func,
    t: PropTypes.func,
}
AuthButton.propTypes = {
    userLogout: PropTypes.func,
}

export default compose(
    withTranslation(),
    connect(
        null,
        { userLogout: userLogoutAction },
    ),
)(AuthButton)
