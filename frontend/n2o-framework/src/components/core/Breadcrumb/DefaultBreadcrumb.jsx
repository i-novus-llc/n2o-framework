import React from 'react';
import { Link } from 'react-router-dom';
import Breadcrumb from 'reactstrap/lib/Breadcrumb';
import BreadcrumbItem from 'reactstrap/lib/BreadcrumbItem';

function DefaultBreadcrumb({ items }) {
  const crumbs = items.map(({ label, path, modelLink }, index) => {
    return (
      <BreadcrumbItem key={index} active={index === items.length - 1}>
        {index !== items.length - 1 ? <Link to={path}>{label}</Link> : label}
      </BreadcrumbItem>
    );
  });
  return <Breadcrumb>{crumbs}</Breadcrumb>;
}

export default DefaultBreadcrumb;
