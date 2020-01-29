import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import cn from 'classnames';
import isEmpty from 'lodash/isEmpty';
import find from 'lodash/find';
import has from 'lodash/has';
import get from 'lodash/get';
import { createStructuredSelector } from 'reselect';
import {
  compose,
  withPropsOnChange,
  branch,
  getContext,
  defaultProps,
  mapProps,
} from 'recompose';

import Section from '../layouts/Section';
import Factory from '../../core/factory/Factory';
import { LAYOUTS, REGIONS } from '../../core/factory/factoryLevels';
import BreadcrumbContainer from './Breadcrumb/BreadcrumbContainer';
import DocumentTitle from './DocumentTitle';
import Toolbar from '../buttons/Toolbar';

import {
  makePageDisabledByIdSelector,
  makePageStatusByIdSelected,
} from '../../selectors/pages';
import { rootPageSelector } from '../../selectors/global';
import withMetadata from './withMetadata';
import withActions from './withActions';
import Alert from '../snippets/Alerts/Alert';
import Spinner from '../snippets/Spinner/Spinner';
import { SimpleTemplate } from './templates';
import Root from './Root';

function Page(props) {
  const {
    pageId,
    metadata,
    loading,
    error,
    disabled,
    status,
    toolbar,
    defaultTemplate: Template = React.Fragment,
    defaultBreadcrumb,
    defaultErrorPages,
    page,
    rootPage,
  } = props;

  const getErrorPage = () => {
    return get(
      find(defaultErrorPages, page => page.status === status),
      'component',
      null
    );
  };

  const errorPage = getErrorPage();

  const renderDefaultBody = () => {
    return errorPage ? (
      React.createElement(errorPage)
    ) : (
      <div className={cn('n2o-page-body', { 'n2o-disabled-page': disabled })}>
        {error && <Alert {...error} visible />}
        {!isEmpty(metadata) && metadata.page && (
          <DocumentTitle {...metadata.page} />
        )}
        {!isEmpty(metadata) && metadata.breadcrumb && (
          <BreadcrumbContainer
            defaultBreadcrumb={defaultBreadcrumb}
            items={metadata.breadcrumb}
          />
        )}
        {toolbar && (toolbar.topLeft || toolbar.topRight) && (
          <div className="n2o-page-actions">
            <Toolbar entityKey={pageId} toolbar={toolbar.topLeft} />
            <Toolbar entityKey={pageId} toolbar={toolbar.topRight} />
          </div>
        )}
        <div className="n2o-page">
          {has(metadata, 'layout') && (
            <Factory
              level={LAYOUTS}
              src={metadata.layout.src}
              {...metadata.layout}
            >
              {Object.keys(metadata.layout.regions).map((place, i) => {
                return (
                  <Section place={place} key={'section' + i}>
                    {metadata.layout.regions[place].map((region, j) => (
                      <Factory
                        key={`region-${place}-${j}`}
                        level={REGIONS}
                        {...region}
                        pageId={metadata.id}
                      />
                    ))}
                  </Section>
                );
              })}
            </Factory>
          )}
        </div>
        {toolbar && (toolbar.bottomLeft || toolbar.bottomRight) && (
          <div className="n2o-page-actions">
            <Toolbar entityKey={pageId} toolbar={toolbar.bottomLeft} />
            <Toolbar entityKey={pageId} toolbar={toolbar.bottomRight} />
          </div>
        )}
      </div>
    );
  };

  return rootPage ? (
    <Root>
      <Template>
        <Spinner type="cover" loading={loading}>
          {page ? React.createElement(page, props) : renderDefaultBody()}
        </Spinner>
      </Template>
    </Root>
  ) : (
    <Spinner type="cover" loading={loading}>
      {page ? React.createElement(page, props) : renderDefaultBody()}
    </Spinner>
  );
}

Page.propTypes = {
  pageId: PropTypes.string,
  metadata: PropTypes.object,
  loading: PropTypes.bool,
  disabled: PropTypes.bool,
  error: PropTypes.object,
  status: PropTypes.number,
  page: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
};

export { Page };

const mapStateToProps = createStructuredSelector({
  disabled: (state, { pageId }) => makePageDisabledByIdSelector(pageId)(state),
  status: (state, { pageId }) => makePageStatusByIdSelected(pageId)(state),
  rootPageId: rootPageSelector,
});

export default compose(
  connect(mapStateToProps),
  mapProps(props => ({
    ...props,
    pageUrl: props.pageUrl || '/' + get(props, 'match.params.pageUrl', ''),
  })),
  withPropsOnChange(
    ['pageId', 'pageUrl', 'rootPageId'],
    ({ pageId, pageUrl, rootPageId, rootPage }) => ({
      pageId: (rootPage && rootPageId) || pageId || pageUrl || null,
      pageUrl: pageUrl || '/',
    })
  ),
  branch(({ needMetadata }) => needMetadata, withMetadata),
  branch(({ rootPage }) => rootPage, withActions),
  getContext({
    defaultBreadcrumb: PropTypes.oneOfType([
      PropTypes.func,
      PropTypes.element,
      PropTypes.node,
    ]),
    defaultErrorPages: PropTypes.arrayOf(
      PropTypes.oneOfType([PropTypes.node, PropTypes.element, PropTypes.func])
    ),
    defaultTemplate: PropTypes.oneOfType([
      PropTypes.func,
      PropTypes.element,
      PropTypes.node,
    ]),
  }),
  defaultProps({
    defaultTemplate: SimpleTemplate,
    metadata: {},
    loading: false,
    disabled: false,
  })
)(Page);
