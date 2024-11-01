import React from 'react'
import classNames from 'classnames'

import Alert from '../snippets/Alerts/Alert'
import { PageTitle, PageTitle as DocumentTitle } from '../core/PageTitle'
import { BreadcrumbContainer } from '../core/Breadcrumb/BreadcrumbContainer'
import Toolbar from '../buttons/Toolbar'
import { ModelPrefix } from '../../core/datasource/const'

import { type DefaultPageProps } from './types'
import { usePageRegister } from './usePageRegister'

export const DefaultPage = ({
    metadata,
    toolbar,
    entityKey,
    error,
    children,
    disabled,
    dispatch,
}: DefaultPageProps) => {
    const { style, className, datasources, id: pageId, page, breadcrumb } = metadata || {}
    const { title, htmlTitle, datasource, model: modelPrefix } = page || {}

    usePageRegister(dispatch, datasources, pageId)

    return (
        <div className={classNames('n2o-page-body', className, { 'n2o-disabled-page': disabled })} style={style}>
            {error && <Alert {...error} visible />}
            <DocumentTitle
                htmlTitle={htmlTitle}
                datasource={datasource}
                modelPrefix={modelPrefix || ModelPrefix.active}
            />
            <BreadcrumbContainer
                breadcrumb={breadcrumb}
                datasource={datasource}
                modelPrefix={modelPrefix}
            />
            <PageTitle title={title} datasource={datasource} modelPrefix={modelPrefix} className="n2o-page__title" />
            {toolbar && (toolbar.topLeft || toolbar.topRight || toolbar.topCenter) && (
                <div className="n2o-page-actions">
                    <Toolbar entityKey={entityKey} toolbar={toolbar.topLeft} />
                    <Toolbar entityKey={entityKey} toolbar={toolbar.topCenter} />
                    <Toolbar entityKey={entityKey} toolbar={toolbar.topRight} />
                </div>
            )}
            {children}
            {
                toolbar && (toolbar.bottomLeft || toolbar.bottomRight || toolbar.bottomCenter) &&
                (
                    <div className="n2o-page-actions">
                        <Toolbar entityKey={entityKey} toolbar={toolbar.bottomLeft} />
                        <Toolbar entityKey={entityKey} toolbar={toolbar.bottomCenter} />
                        <Toolbar entityKey={entityKey} toolbar={toolbar.bottomRight} />
                    </div>
                )
            }
        </div>
    )
}

DefaultPage.defaultProps = {
    toolbar: {},
}

export default DefaultPage
