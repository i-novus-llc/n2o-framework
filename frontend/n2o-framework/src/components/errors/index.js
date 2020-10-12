import React from 'react';
import PropTypes from 'prop-types';
import { getContext } from 'recompose';
import ErrorPage from './ErrorPage';
import DocumentTitle from '../core/DocumentTitle';

const NotFoundPage = t => (
  <React.Fragment>
    <DocumentTitle title={t('pageNotFound')} />
    <ErrorPage status={404} error={t('pageNotFound')} />
  </React.Fragment>
);
const ForbiddenPage = t => (
  <React.Fragment>
    <DocumentTitle title={t('accessDenied')} />
    <ErrorPage status={403} error={t('accessDenied')} />
  </React.Fragment>
);
const ServerErrorPage = t => (
  <React.Fragment>
    <DocumentTitle title={t('innerAppError')} />
    <ErrorPage status={500} error={t('innerAppError')} />
  </React.Fragment>
);

const createErrorPages = ({
  t,
  notFound = NotFoundPage(t),
  forbidden = ForbiddenPage(t),
  serverError = ServerErrorPage(t),
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

export default getContext({ t: PropTypes.func })(createErrorPages);
