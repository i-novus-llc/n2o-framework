import React from 'react';
import PropTypes from 'prop-types';
import isEmpty from 'lodash/isEmpty';
import Alert from '../snippets/Alerts/Alert';
import DocumentTitle from '../core/DocumentTitle';
import DefaultBreadcrumb from '../core/Breadcrumb/DefaultBreadcrumb';
import BreadcrumbContainer from '../core/Breadcrumb/BreadcrumbContainer';
import Actions from '../actions/Actions';
import cn from 'classnames';

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
          <Actions
            toolbar={toolbar.topLeft}
            actions={actions}
            containerKey={containerKey}
            pageId={pageId}
          />
          <Actions
            toolbar={toolbar.topRight}
            actions={actions}
            containerKey={containerKey}
            pageId={pageId}
          />
        </div>
      )}
      {children}
      {toolbar && (toolbar.bottomLeft || toolbar.bottomRight) && (
        <div className="n2o-page-actions">
          <Actions
            toolbar={toolbar.bottomLeft}
            actions={actions}
            containerKey={containerKey}
            pageId={pageId}
          />
          <Actions
            toolbar={toolbar.bottomRight}
            actions={actions}
            containerKey={containerKey}
            pageId={pageId}
          />
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
