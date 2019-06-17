import React from 'react';
import ErrorPage from './ErrorPage';
import DocumentTitle from '../core/DocumentTitle';

const PAGE_NOT_FOUND = "Страница не найдена";
const ACCESS_DENIED = "Доступ запрещён";
const INNER_APPLICATION_ERROR = "Внутренняя ошибка приложения";

const NotFoundPage = () => (
  <React.Fragment>
    <DocumentTitle title={PAGE_NOT_FOUND} />
    <ErrorPage status={404} error={PAGE_NOT_FOUND} />
  </React.Fragment>
);
const ForbiddenPage = () => (
 <React.Fragment>
   <DocumentTitle title={ACCESS_DENIED} />
   <ErrorPage status={403} error={ACCESS_DENIED} />
 </React.Fragment>
);
const ServerErrorPage = () => (
  <React.Fragment>
    <DocumentTitle title={INNER_APPLICATION_ERROR} />
    <ErrorPage status={500} error={INNER_APPLICATION_ERROR} />
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
