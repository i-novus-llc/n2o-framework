import React from 'react'
import { connect } from 'react-redux'
import { compose } from 'recompose'
import { withTranslation } from 'react-i18next'

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
        this.props.userLogin({ username, roles: [username] })
    }

    render() {
        const { t } = this.props
        return (
            <div>
                <input
                    type="text"
                    placeholder={t('login')}
                    value={this.state.username}
                    onChange={this.handleChange}
                />
                {' '}
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
    <SecurityCheck
        render={({ permissions, user }) => (permissions ? (
            <p>

          Привет,
                {user.username}
!
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

export default compose(
    withTranslation(),
    connect(
        null,
        { userLogout: userLogoutAction },
    ),
)(AuthButton)
