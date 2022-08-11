import React from 'react'

import translation from '../../locales/ru/translation'
import DocumentTitle from '../core/DocumentTitle'

import ErrorPage from './ErrorPage'

const ForbiddenPage = () => (
    <>
        <DocumentTitle title={translation.accessDenied} />
        <ErrorPage status={403} error={translation.accessDenied} />
    </>
)

const NotFoundPage = () => (
    <>
        <DocumentTitle title={translation.pageNotFound} />
        <ErrorPage status={404} error={translation.pageNotFound} />
    </>
)

const ServerErrorPage = () => (
    <>
        <DocumentTitle title={translation.innerAppError} />
        <ErrorPage status={500} error={translation.innerAppError} />
    </>
)

const BadGatewayPage = () => (
    <>
        <DocumentTitle title={translation.badGateway} />
        <ErrorPage status={502} error={translation.badGateway} />
    </>
)

const defaultComponents = {
    403: ForbiddenPage,
    404: NotFoundPage,
    500: ServerErrorPage,
    502: BadGatewayPage,
}

/* example config = {404: CustomComponent} */
export const errorTemplates = (config = {}) => {
    const components = {
        ...defaultComponents,
        ...config,
    }

    return Object.entries(components).map(([status, component]) => ({
        status,
        component,
    }))
}

export default errorTemplates
