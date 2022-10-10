import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import Alert from '../snippets/Alerts/Alert'
import { PageTitle, PageTitle as DocumentTitle } from '../core/PageTitle'
import { BreadcrumbContainer } from '../core/Breadcrumb/BreadcrumbContainer'
import Toolbar from '../buttons/Toolbar'

import { usePageRegister } from './usePageRegister'

/**
 * Стандартное наполнение страницы
 * @param metadata
 * @param toolbar
 * @param entityKey
 * @param error
 * @param children
 * @param disabled
 * @param dispatch
 * @return {*}
 * @constructor
 */
function DefaultPage({
    metadata = {},
    toolbar,
    entityKey,
    error,
    children,
    disabled,
    dispatch,
}) {
    const { style, className, datasources, id: pageId, page = {}, breadcrumb } = metadata
    const { title, htmlTitle, datasource, model: modelPrefix = 'resolve' } = page

    usePageRegister(datasources, dispatch, pageId)

    return (
        <div className={classNames('n2o-page-body', className, { 'n2o-disabled-page': disabled })} style={style}>
            {error && <Alert {...error} visible />}
            <DocumentTitle htmlTitle={htmlTitle} datasource={datasource} modelPrefix={modelPrefix} />
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

DefaultPage.propTypes = {
    metadata: PropTypes.object,
    toolbar: PropTypes.object,
    entityKey: PropTypes.string,
    error: PropTypes.oneOfType([PropTypes.object, PropTypes.oneOf([false])]),
    children: PropTypes.oneOfType([
        PropTypes.node,
        PropTypes.func,
        PropTypes.element,
    ]),
    disabled: PropTypes.bool,
    dispatch: PropTypes.func,
}

DefaultPage.defaultProps = {
    toolbar: {},
}

export default DefaultPage
