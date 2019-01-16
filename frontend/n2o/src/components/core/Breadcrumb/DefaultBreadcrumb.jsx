import React from 'react';
import { Link } from 'react-router-dom';
import { Breadcrumb, BreadcrumbItem } from 'reactstrap';
import propsResolver from '../../../utils/propsResolver';

function DefaultBreadcrumb({ items }) {
  const crumbs = items.map(({ label, path, modelLink }, index) => {
    let resolveLabel = label;
    if (label && modelLink && modelLink.link) {
      resolveLabel = propsResolver(label, modelLink.link);
    }

    return (
      <BreadcrumbItem active={index === items.length - 1}>
        {index !== items.length - 1 ? <Link to={path}>{resolveLabel}</Link> : resolveLabel}
      </BreadcrumbItem>
    );
  });
  return <Breadcrumb>{crumbs}</Breadcrumb>;
}

export default DefaultBreadcrumb;
