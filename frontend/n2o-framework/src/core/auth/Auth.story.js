import React from 'react'
import { Route, Link, Redirect, Switch } from 'react-router-dom'
import { storiesOf } from '@storybook/react'
import { connect } from 'react-redux'
import { getContext, compose } from 'recompose'
import fetchMock from 'fetch-mock'

import Wireframe from '../../components/snippets/Wireframe/Wireframe'
import Factory from '../factory/Factory'
import { WIDGETS } from '../factory/factoryLevels'
import { userLogin, userLogout as userLogoutAction } from '../../ducks/user/store'

import SecurityCheck from './SecurityCheck'
import AuthButtonContainer from './AuthLogin'
import authMeta from './Auth.meta.json'
import table from './err401.meta'

const ProtectedPage = props => (
    <SecurityCheck
        render={({ permissions }) => (permissions ? <Protected {...props} /> : <Forbidden />)
        }
    />
)

const AdminPage = props => (
    <SecurityCheck
        config={{ roles: ['admin'] }}
        render={({ permissions }) => (permissions ? <Admin {...props} /> : <Forbidden />)
        }
    />
)

const Home = () => (
    <Wireframe title="Главная страница" className="text-light bg-secondary" />
)
const Public = () => (
    <Wireframe title="Публичная страница" className="text-light bg-primary" />
)
const Protected = () => (
    <Wireframe title="Защищенная страница" className="text-light bg-success" />
)
const Admin = () => (
    <Wireframe title="Администрирование" className="bg-warning" />
)
const Forbidden = () => (
    <Wireframe title="Нет доступа" className="text-light bg-danger" />
)

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
        return (
            <div>
                <input
                    type="text"
                    placeholder="Логин"
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

storiesOf('Ограничение доступа', module)
    .add('Аутентификация и авторизация', () => (
        <div>
            <AuthButtonContainer />
            <small>
        Введите
                {' '}
                <mark>admin</mark>
, чтобы увидеть старницу
                {' '}
                <mark>Администрирование</mark>
            </small>
            <hr />
            <h5>Навигация</h5>
            <ul>
                <li>
                    <Link to="/">Главная страница</Link>
                </li>
                <li>
                    <Link to="/public">Публичная страница</Link>
                </li>
                <li>
                    <Link to="/protected">Защищенная страница</Link>
                </li>
                <SecurityCheck
                    config={{ roles: ['admin'] }}
                    render={({ permissions }) => (permissions ? (
                        <li>
                            <Link to="/admin">Администрирование</Link>
                        </li>
                    ) : null)
                    }
                />
            </ul>
            <hr />
            <div
                style={{
                    position: 'relative',
                    height: 200,
                }}
            >
                <Route path="/" exact component={Home} />
                <Route path="/public" component={Public} />
                <Route path="/protected" component={ProtectedPage} />
                <Route path="/admin" component={AdminPage} />
            </div>
        </div>
    ))
    .add('Использование в N2O', () => (
        <div>
            <AuthButtonContainer />
            <small>
        Введите
                {' '}
                <mark>admin</mark>
, чтобы увидеть скрытый виджет
            </small>
            <hr />
            <Factory level={WIDGETS} {...authMeta} />
        </div>
    ))
    .add('Обработка 401', () => {
        fetchMock.restore().get('begin:n2o/data', 401)
        return (
            <>
                <Switch>
                    <Route
                        path="/login"
                        component={() => (
                            <div>
                                <LoginContainer />
                                <small>Ошибка! Вы не авторизованы</small>
                            </div>
                        )}
                    />
                    <Route
                        path="/"
                        component={() => (
                            <Factory
                                level={WIDGETS}
                                {...table.Page_Table}
                                id="Page_Table"
                            />
                        )}
                    />
                </Switch>
            </>
        )
    })
