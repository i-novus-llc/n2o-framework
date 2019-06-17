import React from 'react';
import ErrorPage from './ErrorPage';

const NotFoundPage = () => (
  <ErrorPage status={404} error="Страница не найдена" />
);
const ForbiddenPage = () => <ErrorPage status={403} error="Доступ запрещён" />;
const ServerErrorPage = () => (
  <ErrorPage status={500} error="Внутренняя ошибка приложения" />
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
