import React from 'react';
import { Link } from 'react-router-dom';
import { withState, compose } from 'recompose';
import isEmpty from 'lodash/isEmpty';
import has from 'lodash/has';
import isUndefined from 'lodash/isUndefined';
import isNull from 'lodash/isNull';
import Breadcrumb from 'reactstrap/lib/Breadcrumb';
import BreadcrumbItem from 'reactstrap/lib/BreadcrumbItem';

function DefaultBreadcrumb({ items, title, setTitle }) {
  const findInitTitle = items => {
    if (!isEmpty(items)) {
      const initItem = items[items.length - 1];
      return has(initItem, 'title') ? initItem.title : '';
    }
    return '';
  };
  const initTitle = findInitTitle(items);
  const crumbs = items.map(({ label, path, title }, index) => {
    return (
      <BreadcrumbItem key={index} active={index === items.length - 1}>
        {path && index !== items.length - 1 ? (
          <Link
            onClick={() =>
              !isUndefined(title) ? setTitle(title) : setTitle('')
            }
            to={path}
            key={index}
          >
            {label}
          </Link>
        ) : (
          label
        )}
      </BreadcrumbItem>
    );
  });
  return (
    <>
      <Breadcrumb>{crumbs}</Breadcrumb>
      {!isNull(title) ? <h1>{title}</h1> : <h1>{initTitle}</h1>}
    </>
  );
}

export default compose(withState('title', 'setTitle', null))(DefaultBreadcrumb);
