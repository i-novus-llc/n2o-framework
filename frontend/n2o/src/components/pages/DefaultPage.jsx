import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty, has, get, map } from 'lodash';
import Alert from '../snippets/Alerts/Alert';
import DocumentTitle from '../core/DocumentTitle';
import BreadcrumbContainer from '../core/Breadcrumb/BreadcrumbContainer';
import Actions from '../actions/Actions';

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
          defaultBreadcrumb={context.defaultBreadcrumb}
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

DefaultPage.contextTypes = {
  defaultBreadcrumb: PropTypes.element,
};

export default DefaultPage;
