import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import cn from 'classnames';
import { find, get } from 'lodash';
import { createStructuredSelector } from 'reselect';
import {
  compose,
  withPropsOnChange,
  branch,
  getContext,
  defaultProps,
  mapProps,
} from 'recompose';

import Factory from '../../core/factory/Factory';
import { PAGES } from '../../core/factory/factoryLevels';

import {
  makePageDisabledByIdSelector,
  makePageStatusByIdSelected,
} from '../../selectors/pages';
import { rootPageSelector } from '../../selectors/global';

import Root from './Root';
import withMetadata from './withMetadata';
import withActions from './withActions';
import { SimpleTemplate } from './templates';
import StandardPage from '../pages/StandardPage/StandardPage';
import Spinner from '../snippets/Spinner/Spinner';

function Page(props) {
  const {
    metadata,
    loading,
    disabled,
    status,
    defaultTemplate: Template = React.Fragment,
    defaultPage,
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
      <div className={cn({ 'n2o-disabled-page': disabled })}>
        <Factory
          level={PAGES}
          src={metadata && metadata.src ? metadata.src : defaultPage}
          errorPage={errorPage}
          id={get(metadata, 'id')}
          regions={get(metadata, 'regions', {})}
          {...this.props}
        />
      </div>
    );
  };

  return (
    <Spinner type="cover" loading={loading}>
      {rootPage ? (
        <Root>
          <Template>
            {page ? React.createElement(page, props) : renderDefaultBody()}
          </Template>
        </Root>
      ) : page ? (
        React.createElement(page, props)
      ) : (
        renderDefaultBody()
      )}
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
    pageUrl: props.pageUrl || get(props, 'match.params.pageUrl', ''),
  })),
  withPropsOnChange(
    ['pageId', 'pageUrl', 'rootPageId'],
    ({ pageId, pageUrl, rootPageId, rootPage }) => ({
      pageId: (rootPage && rootPageId) || pageId || pageUrl || null,
      pageUrl: pageUrl ? `/${pageUrl}` : '/',
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
    defaultPage: PropTypes.string,
  }),
  defaultProps({
    defaultTemplate: SimpleTemplate,
    defaultPage: StandardPage,
    metadata: {},
    loading: false,
    disabled: false,
  })
)(Page);
