import React from 'react';
import PropTypes from 'prop-types';
import { getContext } from 'recompose';
import ErrorPage from './ErrorPage';
import DocumentTitle from '../core/DocumentTitle';

const NotFoundPage = () =>
  getContext({ t: PropTypes.func })(
    <React.Fragment>
      <DocumentTitle title={this.props.t('pageNotFound')} />
      <ErrorPage status={404} error={this.props.t('pageNotFound')} />
    </React.Fragment>
  );
const ForbiddenPage = () =>
  getContext({ t: PropTypes.func })(
    <React.Fragment>
      <DocumentTitle title={this.props.t('accessDenied')} />
      <ErrorPage status={403} error={this.props.t('accessDenied')} />
    </React.Fragment>
  );
const ServerErrorPage = () =>
  getContext({ t: PropTypes.func })(
    <React.Fragment>
      <DocumentTitle title={this.props.t('innerAppError')} />
      <ErrorPage status={500} error={this.props.t('innerAppError')} />
    </React.Fragment>
  );

const createErrorPages = ({
  notFound = NotFoundPage,
  forbidden = ForbiddenPage,
  serverError = ServerErrorPage,
} = {}) => {
  return [
    {
      status: 403,
      component: forbidden,
    },
    {
      status: 404,
      component: notFound,
    },
    {
      status: 500,
      component: serverError,
    },
  ];
};

export default createErrorPages;
