import React from 'react'
import { Helmet } from 'react-helmet'

import { ModelPrefix } from '../../core/datasource/const'
import propsResolver from '../../utils/propsResolver'

import { useModel } from './hooks/useModel'

interface Props {
    title?: string,
    htmlTitle?: string,
    className?: string,
    datasource?: string,
    modelPrefix?: ModelPrefix,
    titleLayout?: boolean,
}

/** Renders the main-title or html title of the page **/
export function PageTitle({
    title,
    htmlTitle,
    modelPrefix,
    className,
    datasource,
    titleLayout = true,
}: Props) {
    const model = useModel(datasource, modelPrefix)

    if (title) {
        const resolvedTitle = propsResolver(title, model) || ''

        if (titleLayout) {
            return (
                <h1 className={className}>{resolvedTitle}</h1>
            )
        }

        // eslint-disable-next-line react/jsx-no-useless-fragment
        return <>{resolvedTitle}</>
    }

    if (htmlTitle) {
        const resolvedTitle = propsResolver(htmlTitle, model) || ''

        return <Helmet title={resolvedTitle} />
    }

    return null
}
