import React from 'react'
import PropTypes from 'prop-types'
import { Switch, Route } from 'react-router-dom'
import { ConnectedRouter } from 'connected-react-router'
import { pure } from 'recompose'
import { useTranslation } from 'react-i18next'

import history from '../../history'

// eslint-disable-next-line import/no-named-as-default
import Page from './Page'

const errorStyle = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    flexDirection: 'column',
    width: '100%',
    height: '100%',
    fontSize: '1.25rem',
    textAlign: 'center',
}

function Router({ embeddedRouting, children }) {
    const { t } = useTranslation()

    if (!embeddedRouting && !React.Children.count(children)) {
        return (
            <div style={errorStyle}>
                {/* eslint-disable-next-line react/jsx-one-expression-per-line */}
                <p>
                    &#9888;
                    {/* eslint-disable-next-line react/jsx-one-expression-per-line */}
                    {t('pagesNotConfigured')}
.
                </p>
                <p>
                    {t('routerError', {
                        page: <code>&lt;Page&gt;</code>,
                        n2o: <code>&lt;N2O&gt;</code>,
                        embeddedRouting: <code>embeddedRouting</code>,
                    })}
                </p>
            </div>
        )
    }

    return (
        <ConnectedRouter history={history}>
            <Switch>
                {React.Children.map(children, child => React.cloneElement(child, {
                    key: child.props.name,
                    custom: true,
                }))}
                {embeddedRouting ? (
                    <Route
                        path="/:pageUrl*"
                        render={routeProps => (
                            <Page {...routeProps} needMetadata rootPage />
                        )}
                    />
                ) : null}
            </Switch>
        </ConnectedRouter>
    )
}

Router.propTypes = {
    children: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.node),
        PropTypes.node,
    ]),
    embeddedRouting: PropTypes.bool,
}

Router.defaultProps = {
    embeddedRouting: true,
}

export default pure(Router)
