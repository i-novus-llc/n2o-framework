import React from 'react';
import { useTranslation } from 'react-i18next';

import ErrorPage from './ErrorPage';
import DocumentTitle from '../core/DocumentTitle';

const NotFoundPage = () => {
  const { t } = useTranslation();
  return (
    <React.Fragment>
      <DocumentTitle title={t('pageNotFound')} />
      <ErrorPage status={404} error={t('pageNotFound')} />
    </React.Fragment>
  );
};
const ForbiddenPage = () => {
  const { t } = useTranslation();
  return (
    <React.Fragment>
      <DocumentTitle title={t('accessDenied')} />
      <ErrorPage status={403} error={t('accessDenied')} />
    </React.Fragment>
  );
};

const ServerErrorPage = () => {
  const { t } = useTranslation();
  return (
    <React.Fragment>
      <DocumentTitle title={t('innerAppError')} />
      <ErrorPage status={500} error={t('innerAppError')} />
    </React.Fragment>
  );
};

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
