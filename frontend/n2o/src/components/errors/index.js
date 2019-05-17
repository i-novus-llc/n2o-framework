import React from 'react';
import ErrorPage from './ErrorPage';

export default ({
  notFound = () => <ErrorPage status={404} error="Страница не найдена" />,
  forbidden = () => <ErrorPage status={403} error="Доступ запрещён" />,
  serverError = () => <ErrorPage status={500} error="Сервер не доступен" />,
} = {}) => [
  {
    status: 403,
    path: '/403',
    component: forbidden,
  },
  {
    status: 404,
    path: '/404',
    component: notFound,
  },
  {
    status: 500,
    path: '/500',
    component: serverError,
  },
];
