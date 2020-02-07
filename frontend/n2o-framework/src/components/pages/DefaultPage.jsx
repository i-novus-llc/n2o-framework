import React from 'react';
import PropTypes from 'prop-types';
import isEmpty from 'lodash/isEmpty';
import cn from 'classnames';
import Alert from '../snippets/Alerts/Alert';
import DocumentTitle from '../core/DocumentTitle';
import DefaultBreadcrumb from '../core/Breadcrumb/DefaultBreadcrumb';
import BreadcrumbContainer from '../core/Breadcrumb/BreadcrumbContainer';
import Toolbar from '../buttons/Toolbar';

/**
 * Стандартное наполнение страницы
 * @param metadata
 * @param toolbar
 * @param actions
 * @param containerKey
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
    containerKey,
    error,
    pageId,
    regions,
    children,
    disabled,
  },
  context
) {
  return (
    <div className={cn('n2o-page-body', { 'n2o-disabled-page': disabled })}>
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
      {toolbar && (toolbar.topLeft || toolbar.topRight) && (
        <div className="n2o-page-actions">
          <Toolbar entityKey={containerKey} toolbar={toolbar.topLeft} />
          <Toolbar entityKey={containerKey} toolbar={toolbar.topRight} />
        </div>
      )}
      {children}
      {toolbar && (toolbar.bottomLeft || toolbar.bottomRight) && (
        <div className="n2o-page-actions">
          <Toolbar entityKey={containerKey} toolbar={toolbar.bottomLeft} />
          <Toolbar entityKey={containerKey} toolbar={toolbar.bottomRight} />
        </div>
      )}
    </div>
  );
}

DefaultPage.propTypes = {
  metadata: PropTypes.object,
  toolbar: PropTypes.object,
  actions: PropTypes.object,
  containerKey: PropTypes.string,
  error: PropTypes.object,
  pageId: PropTypes.string,
  regions: PropTypes.object,
  children: PropTypes.oneOfType([
    PropTypes.node,
    PropTypes.func,
    PropTypes.element,
  ]),
};

DefaultPage.defaultProps = {
  toolbar: {},
  actions: {},
};

export default DefaultPage;
