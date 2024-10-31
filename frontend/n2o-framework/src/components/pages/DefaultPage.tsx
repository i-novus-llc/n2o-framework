import React, { CSSProperties, ReactNode } from 'react'
import classNames from 'classnames'
import { Dispatch } from 'redux'

import Alert from '../snippets/Alerts/Alert'
import { PageTitle, PageTitle as DocumentTitle } from '../core/PageTitle'
import { BreadcrumbContainer } from '../core/Breadcrumb/BreadcrumbContainer'
import Toolbar, { ToolbarProps } from '../buttons/Toolbar'
import { ModelPrefix } from '../../core/datasource/const'
import { breadcrumb } from '../core/Breadcrumb/const'

import { usePageRegister } from './usePageRegister'

interface DefaultPageProps {
    metadata?: {
        style?: CSSProperties
        className?: string
        datasources?: string[]
        id?: string
        page: {
            title?: string
            htmlTitle?: string
            datasource?: string
            model: ModelPrefix
        };
        breadcrumb: breadcrumb
    };
    toolbar?: {
        topLeft?: ToolbarProps
        topCenter?: ToolbarProps
        topRight?: ToolbarProps
        bottomLeft?: ToolbarProps
        bottomCenter?: ToolbarProps
        bottomRight?: ToolbarProps
    };
    entityKey?: string;
    error?: false | { [key: string]: string }
    children?: ReactNode
    disabled?: boolean
    dispatch: Dispatch
}

const DefaultPage: React.FC<DefaultPageProps> = ({
    metadata,
    toolbar,
    entityKey,
    error,
    children,
    disabled,
    dispatch,
}) => {
    const { style, className, datasources, id: pageId, page, breadcrumb } = metadata || {}
    const { title, htmlTitle, datasource, model: modelPrefix } = page || {}

    usePageRegister(datasources, dispatch, pageId)

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
