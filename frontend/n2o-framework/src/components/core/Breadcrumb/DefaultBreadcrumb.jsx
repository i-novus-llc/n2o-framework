import React from 'react';
import { Link } from 'react-router-dom';
import Breadcrumb from 'reactstrap/lib/Breadcrumb';
import BreadcrumbItem from 'reactstrap/lib/BreadcrumbItem';

function DefaultBreadcrumb({ items }) {
  const crumbs = items.map(({ label, path, title, modelLink }, index) => {
    const caption = title ? title : label;
    return (
      <BreadcrumbItem key={index} active={index === items.length - 1}>
        {path && index !== items.length - 1 ? (
          <Link to={path}>{caption}</Link>
        ) : (
          caption
        )}
      </BreadcrumbItem>
    );
  });
  return <Breadcrumb>{crumbs}</Breadcrumb>;
}

export default DefaultBreadcrumb;
