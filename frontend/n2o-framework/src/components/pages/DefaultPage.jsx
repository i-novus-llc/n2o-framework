import React from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'

import Alert from '../snippets/Alerts/Alert'
import DocumentTitle from '../core/DocumentTitle'
import PageTitle from '../core/PageTitle'
import DefaultBreadcrumb from '../core/Breadcrumb/DefaultBreadcrumb'
import BreadcrumbContainer from '../core/Breadcrumb/BreadcrumbContainer'
import Toolbar from '../buttons/Toolbar'

/**
 * Стандартное наполнение страницы
 * @param metadata
 * @param toolbar
 * @param actions
 * @param entityKey
 * @param error
 * @param pageId
 * @param regions
 * @param children
 * @param disabled
 * @param context
 * @return {*}
 * @constructor
 */
function DefaultPage(
    {
        metadata,
        toolbar,
        actions,
        entityKey,
        error,
        pageId,
        regions,
        children,
        disabled,
    },
    context,
) {
    const { style, className } = metadata

    return (
        <div className={classNames('n2o-page-body', className, { 'n2o-disabled-page': disabled })} style={style}>
            {error && <Alert {...error} visible />}
            {!isEmpty(metadata) && metadata.page && (
                <DocumentTitle {...metadata.page} />
            )}
            {!isEmpty(metadata) && metadata.breadcrumb && (
                <BreadcrumbContainer
                    defaultBreadcrumb={DefaultBreadcrumb}
                    items={metadata.breadcrumb}
                />
            )}
            {!isEmpty(metadata) && metadata.page && <PageTitle {...metadata.page} />}
            {toolbar && (toolbar.topLeft || toolbar.topRight || toolbar.topCenter) && (
                <div className="n2o-page-actions">
                    <Toolbar entityKey={entityKey} toolbar={toolbar.topLeft} />
                    <Toolbar entityKey={entityKey} toolbar={toolbar.topCenter} />
                    <Toolbar entityKey={entityKey} toolbar={toolbar.topRight} />
                </div>
            )}
            {children}
            {toolbar &&
        (toolbar.bottomLeft || toolbar.bottomRight || toolbar.bottomCenter) && (
        <div className="n2o-page-actions">
                    <Toolbar entityKey={entityKey} toolbar={toolbar.bottomLeft} />
                    <Toolbar entityKey={entityKey} toolbar={toolbar.bottomCenter} />
                    <Toolbar entityKey={entityKey} toolbar={toolbar.bottomRight} />
                </div>
            )}
        </div>
    )
}

DefaultPage.propTypes = {
    metadata: PropTypes.object,
    toolbar: PropTypes.object,
    actions: PropTypes.object,
    entityKey: PropTypes.string,
    error: PropTypes.object,
    pageId: PropTypes.string,
    regions: PropTypes.object,
    children: PropTypes.oneOfType([
        PropTypes.node,
        PropTypes.func,
        PropTypes.element,
    ]),
}

DefaultPage.defaultProps = {
    toolbar: {},
    actions: {},
}

export default DefaultPage
