import React from 'react';
import isUndefined from 'lodash/isUndefined';

function PageTitle({ title }) {
  return !isUndefined(title) && <h1 className="n2o-page-title">{title}</h1>;
}

export default PageTitle;
