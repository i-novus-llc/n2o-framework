import React, { ReactNode, ReactElement, CSSProperties } from 'react'
import { Switch, Route, RouteComponentProps } from 'react-router-dom'
import { ConnectedRouter } from 'connected-react-router'
import { pure } from 'recompose'
import { useTranslation } from 'react-i18next'

import history from '../../history'

import Page from './Page'

const errorStyle: CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    flexDirection: 'column',
    width: '100%',
    height: '100%',
    fontSize: '1.25rem',
    textAlign: 'center',
}

interface Props {
    children: ReactNode;
    embeddedRouting?: boolean
}

const Router = ({ children, embeddedRouting = true }: Props) => {
    const { t } = useTranslation()

    if (!embeddedRouting && !React.Children.count(children)) {
        return (
            <div style={errorStyle}>
                <p>
                    &#9888;
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
                {React.Children.map(children, (child) => {
                    const childElement = child as ReactElement<{ name?: string, custom?: boolean }>

                    return React.cloneElement(childElement, {
                        key: childElement.props.name,
                        custom: true,
                    })
                })}
                {embeddedRouting && (
                    <Route
                        path="/:pageUrl*"
                        render={(routeProps: RouteComponentProps) => <Page {...routeProps} needMetadata rootPage />}
                    />
                )}
            </Switch>
        </ConnectedRouter>
    )
}

export default pure(Router)
