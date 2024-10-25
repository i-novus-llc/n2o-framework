import React from 'react'
import { useTranslation } from 'react-i18next'

export const Page404 = () => {
    const { t } = useTranslation()

    return <h1>{t('pageNotFound')}</h1>
}

export default Page404
