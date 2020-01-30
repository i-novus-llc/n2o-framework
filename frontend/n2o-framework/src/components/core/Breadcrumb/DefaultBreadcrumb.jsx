import React from 'react';
import { Link } from 'react-router-dom';
import isUndefined from 'lodash/isUndefined';
import last from 'lodash/last';
import filter from 'lodash/filter';
import Breadcrumb from 'reactstrap/lib/Breadcrumb';
import BreadcrumbItem from 'reactstrap/lib/BreadcrumbItem';

function DefaultBreadcrumb({ items }) {
  const itemsWithLabels = filter(items, item => !isUndefined(item.label));
  const title = last(itemsWithLabels).title;
  const renderTitle = !isUndefined(title) && <h1>{title}</h1>;
  const crumbs = itemsWithLabels.map(({ label, path }, index) => {
    return (
      <BreadcrumbItem key={index} active={index === itemsWithLabels.length - 1}>
        {path && index !== itemsWithLabels.length - 1 ? (
          <Link to={path} key={index}>
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
      {renderTitle}
    </>
  );
}

export default DefaultBreadcrumb;
