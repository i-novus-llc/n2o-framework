import React from 'react'
import { useTranslation } from 'react-i18next'

import DocumentTitle from '../core/DocumentTitle'

import ErrorPage from './ErrorPage'

const NotFoundPage = () => {
    const { t } = useTranslation()
    return (
        <>
            <DocumentTitle title={t('pageNotFound')} />
            <ErrorPage status={404} error={t('pageNotFound')} />
        </>
    )
}
const ForbiddenPage = () => {
    const { t } = useTranslation()
    return (
        <>
            <DocumentTitle title={t('accessDenied')} />
            <ErrorPage status={403} error={t('accessDenied')} />
        </>
    )
}

const ServerErrorPage = () => {
    const { t } = useTranslation()
    return (
        <>
            <DocumentTitle title={t('innerAppError')} />
            <ErrorPage status={500} error={t('innerAppError')} />
        </>
    )
}

const createErrorPages = ({
    notFound = NotFoundPage,
    forbidden = ForbiddenPage,
    serverError = ServerErrorPage,
} = {}) => [
    {
        status: 403,
        component: forbidden,
    },
    {
        status: 404,
        component: notFound,
    },
    {
        status: 500,
        component: serverError,
    },
]

export default createErrorPages
