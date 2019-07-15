import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty, has, get, map } from 'lodash';
import Alert from '../snippets/Alerts/Alert';
import DocumentTitle from '../core/DocumentTitle';
import DefaultBreadcrumb from '../core/Breadcrumb/DefaultBreadcrumb';
import BreadcrumbContainer from '../core/Breadcrumb/BreadcrumbContainer';
import Actions from '../actions/Actions';

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
  },
  context
) {
  return (
    <React.Fragment>
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
    </React.Fragment>
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
