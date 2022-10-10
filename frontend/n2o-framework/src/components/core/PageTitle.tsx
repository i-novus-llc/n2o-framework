import React from 'react'
import { Helmet, HelmetProps } from 'react-helmet'

// @ts-ignore ignore import error from js file
import { WithDataSource } from '../../core/datasource/WithDataSource'
import { IDataSourceModels, ModelPrefix } from '../../core/datasource/const'

import { textResolver } from './textResolver'

interface IPageTitle {
    title?: string,
    htmlTitle?: string,
    className?: string,
    models: IDataSourceModels,
    modelPrefix?: ModelPrefix,
    titleLayout: boolean,
}

/** Renders the main-title or html title of the page **/

function PageTitleBody({
    title,
    htmlTitle,
    models,
    modelPrefix,
    className,
    titleLayout = true,
}: IPageTitle): JSX.Element | React.Component<HelmetProps> | string | null {
    if (title) {
        const resolvedTitle = textResolver(models, title, modelPrefix) || ''

        if (titleLayout) {
            return (
                <h1 className={className}>{resolvedTitle}</h1>
            )
        }

        return resolvedTitle
    }

    if (htmlTitle) {
        const resolvedTitle = textResolver(models, htmlTitle, modelPrefix) || ''

        return <Helmet title={resolvedTitle} />
    }

    return null
}

export const PageTitle = WithDataSource(PageTitleBody)
