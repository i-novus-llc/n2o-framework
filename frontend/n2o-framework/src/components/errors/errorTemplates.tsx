import React, { ComponentType } from 'react'

import translation from '../../locales/ru/translation'

import ErrorPage from './ErrorPage'

const ForbiddenPage = () => <ErrorPage status={403} error={translation.accessDenied} />
const NotFoundPage = () => <ErrorPage status={404} error={translation.pageNotFound} />
const ServerErrorPage = () => <ErrorPage status={500} error={translation.innerAppError} />
const BadGatewayPage = () => <ErrorPage status={502} error={translation.badGateway} />
const ServiceUnavailablePage = () => <ErrorPage status={503} error={translation.serviceUnavailable} />
const NetworkErrorPage = () => <ErrorPage status={null} error={translation.networkError} />

type Error = { error?: Error & { status?: number, text?: string } }

const InternalErrorPage = ({ error }: Error) => {
    const { text, status } = error || {}

    return <ErrorPage status={status || 'Внутренняя ошибка приложения'} error={text || String(error)} />
}
const defaultComponents: Record<number | string, ComponentType<Error>> = {
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
export const errorTemplates = (config: Record<number | string, ComponentType> = {}, isOnline: boolean) => {
    const components: Record<number | string, ComponentType> = { ...defaultComponents, ...config }

    return (error: { status: number }) => {
        if (components[error?.status]) { return components[error.status] }
        if (!isOnline) { return components.networkError }

        return components.internalError
    }
}

export default errorTemplates
