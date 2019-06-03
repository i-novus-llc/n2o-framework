import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import cn from 'classnames';
import {
  isEmpty,
  filter,
  find,
  isEqual,
  has,
  get,
  clone,
  setWith,
  curry,
} from 'lodash';
import { createStructuredSelector } from 'reselect';
import { compose, withPropsOnChange, branch } from 'recompose';
import pathToRegexp from 'path-to-regexp';
import Factory from '../../core/factory/Factory';
import { metadataRequest, resetPage, mapUrl } from '../../actions/pages';
import {
  makePageMetadataByIdSelector,
  makePageLoadingByIdSelector,
  makePageErrorByIdSelector,
  makePageDisabledByIdSelector,
} from '../../selectors/pages';
import { getLocation } from '../../selectors/global';
import withActions from '../core/withActions';
import { PAGES } from '../../core/factory/factoryLevels';

class PageContainer extends React.Component {
  componentDidMount() {
    this.props.getMetadata();
  }

  componentWillUnmount() {
    this.props.reset(this.props.pageId);
  }

  componentDidUpdate(prevProps) {
    const { metadata, getMetadata, routeMap, reset, error } = this.props;

    if (!isEmpty(metadata) && this.shouldGetPageMetadata(prevProps)) {
      reset(prevProps.pageId);
      getMetadata();
    } else if (
      this.isEqualPageId(prevProps) &&
      !this.isEqualPageUrl(prevProps)
    ) {
      routeMap();
      if (error) {
        getMetadata();
      }
    }
  }

  shouldGetPageMetadata(prevProps) {
    const {
      metadata,
      location: { pathname, state = {} },
    } = this.props;

    if (!isEmpty(metadata) && !isEmpty(metadata.routes)) {
      const findedRoutes = filter(metadata.routes.list, route => {
        const re = pathToRegexp(route.path);
        return re.test(pathname);
      });
      const isNewPage = find(findedRoutes, route => {
        return route.isOtherPage;
      });
      return (
        (isNewPage ||
          (this.isEqualPageId(prevProps) &&
            !this.isEqualPageUrl(prevProps) &&
            isEmpty(findedRoutes))) &&
        !state.silent
      );
    }
    return false;
  }

  isEqualPageId(prevProps) {
    return this.props.pageId === prevProps.pageId;
  }

  isEqualPageUrl(prevProps) {
    return this.props.pageUrl === prevProps.pageUrl;
  }

  isEqualLocation(prevProps) {
    return isEqual(this.props.location, prevProps.location);
  }

  render() {
    const {
      metadata,
      defaultTemplate: Template = React.Fragment,
      disabled,
    } = this.props;

    const { defaultPage } = this.context;

    return (
      <div className={cn({ 'n2o-disabled-page': disabled })}>
        <Factory
          level={PAGES}
          src={metadata && metadata.src ? metadata.src : defaultPage}
          {...this.props}
        />
      </div>
    );
  }
}

PageContainer.contextTypes = {
  defaultPage: PropTypes.string,
};

PageContainer.propTypes = {
  metadata: PropTypes.object,
  loading: PropTypes.bool,
  pageId: PropTypes.string,
  pageUrl: PropTypes.string,
  pageMapping: PropTypes.object,
  rootPage: PropTypes.bool,
};

PageContainer.defaultProps = {
  rootPage: false,
};

const mapStateToProps = createStructuredSelector({
  metadata: (state, props) => {
    return makePageMetadataByIdSelector(props.pageId)(state, props);
  },
  loading: (state, props) => {
    return makePageLoadingByIdSelector(props.pageId)(state, props);
  },
  error: (state, { pageId }) => {
    return makePageErrorByIdSelector(pageId)(state);
  },
  disabled: (state, { pageId }) => {
    return makePageDisabledByIdSelector(pageId)(state);
  },
  location: getLocation,
});

function mapDispatchToProps(
  dispatch,
  { pageId, rootPage, pageUrl, pageMapping }
) {
  return {
    getMetadata: () => {
      dispatch(metadataRequest(pageId, rootPage, pageUrl, pageMapping));
    },
    reset: pageId => dispatch(resetPage(pageId)),
    routeMap: () => dispatch(mapUrl(pageId)),
  };
}

export { PageContainer };

export default compose(
  branch(({ rootPage }) => rootPage, withActions),
  withPropsOnChange(['pageId', 'pageUrl'], ({ pageId, pageUrl }) => ({
    pageId: pageId ? pageId : pageUrl ? pageUrl : null,
  })),
  connect(
    mapStateToProps,
    mapDispatchToProps
  )
)(PageContainer);
