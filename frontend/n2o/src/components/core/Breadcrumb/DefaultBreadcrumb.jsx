import React from 'react';
import { Link } from 'react-router-dom';
import { Breadcrumb, BreadcrumbItem } from 'reactstrap';

function DefaultBreadcrumb({ items }) {
  const crumbs = items.map(({ label, path }, index) => {
    return (
      <BreadcrumbItem active={index === items.length - 1}>
        {index !== items.length - 1 ? <Link to={path}>{label}</Link> : label}
      </BreadcrumbItem>
    );
  });
  return <Breadcrumb>{crumbs}</Breadcrumb>;
}

export default DefaultBreadcrumb;
