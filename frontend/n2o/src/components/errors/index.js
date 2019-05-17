import NotFoundPage from './NotFoundPage';
import ForbiddenPage from './ForbiddenPage';
import ServerErrorPage from './ServerErrorPage';

export { NotFoundPage, ForbiddenPage, ServerErrorPage };

export default ({
  notFound = NotFoundPage,
  forbidden = ForbiddenPage,
  serverError = ServerErrorPage,
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
