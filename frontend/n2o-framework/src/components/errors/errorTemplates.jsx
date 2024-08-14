import React from 'react'

import translation from '../../locales/ru/translation'
import { PageTitle as DocumentTitle } from '../core/PageTitle'

import ErrorPage from './ErrorPage'

const ForbiddenPage = () => (
    <>
        <DocumentTitle htmlTitle={translation.accessDenied} />
        <ErrorPage status={403} error={translation.accessDenied} />
    </>
)

const NotFoundPage = () => (
    <>
        <DocumentTitle htmlTitle={translation.pageNotFound} />
        <ErrorPage status={404} error={translation.pageNotFound} />
    </>
)

const ServerErrorPage = () => (
    <>
        <DocumentTitle htmlTitle={translation.innerAppError} />
        <ErrorPage status={500} error={translation.innerAppError} />
    </>
)

const BadGatewayPage = () => (
    <>
        <DocumentTitle htmlTitle={translation.badGateway} />
        <ErrorPage status={502} error={translation.badGateway} />
    </>
)

const ServiceUnavailablePage = () => (
    <>
        <DocumentTitle htmlTitle={translation.serviceUnavailable} />
        <ErrorPage status={503} error={translation.serviceUnavailable} />
    </>
)

const NetworkErrorPage = () => (
    <>
        <DocumentTitle htmlTitle={translation.networkError} />
        <ErrorPage status={null} error={translation.networkError} />
    </>
)

const InternalErrorPage = ({ error }) => {
    const { text } = error

    return (
        <>
            <DocumentTitle htmlTitle={text} />
            <ErrorPage status={null} error={text} />
        </>
    )
}
const defaultComponents = {
    403: ForbiddenPage,
    404: NotFoundPage,
    500: ServerErrorPage,
    502: BadGatewayPage,
    503: ServiceUnavailablePage,
    networkError: NetworkErrorPage,
    internalError: InternalErrorPage,
}

/* example config = {404: CustomComponent} */
// eslint-disable-next-line @typescript-eslint/default-param-last
export const errorTemplates = (config = {}, isOnline) => {
    const components = {
        ...defaultComponents,
        ...config,
    }

    return (error) => {
        if (error.status) { return components[error.status] }
        if (!isOnline) { return components.networkError }

        return components.internalError
    }
}

export default errorTemplates
