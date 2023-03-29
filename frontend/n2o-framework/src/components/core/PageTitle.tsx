import React from 'react'
import { Helmet } from 'react-helmet'

import { ModelPrefix } from '../../core/datasource/const'

import { useModel } from './hooks/useModel'
import { textResolver } from './textResolver'

interface IPageTitle {
    title?: string,
    htmlTitle?: string,
    className?: string,
    datasource?: string,
    modelPrefix?: ModelPrefix,
    titleLayout: boolean,
}

/** Renders the main-title or html title of the page **/
export function PageTitle({
    title,
    htmlTitle,
    modelPrefix,
    className,
    datasource,
    titleLayout = true,
}: IPageTitle) {
    const model = useModel(datasource, modelPrefix)

    if (title) {
        const resolvedTitle = textResolver(model, title) || ''

        if (titleLayout) {
            return (
                <h1 className={className}>{resolvedTitle}</h1>
            )
        }

        return resolvedTitle
    }

    if (htmlTitle) {
        const resolvedTitle = textResolver(model, htmlTitle) || ''

        return <Helmet title={resolvedTitle} />
    }

    return null
}
