import React from 'react';
import get from 'lodash/get';
import { Link } from 'react-router-dom';

function OutputListItem(props) {
  const { labelFieldId, linkFieldId, target, separator, isLast } = props;
  const label = <>{get(props, labelFieldId)}</>;
  const href = get(props, linkFieldId);
  const isInnerLink = target === 'application';

  return (
    <li>
      {isInnerLink ? (
        <Link to={href} className="n2o-output-list__link" target={target}>
          {label}
        </Link>
      ) : (
        <a href={href} target={target} className="n2o-output-list__link">
          {label}
        </a>
      )}
      {!isLast ? separator : ''}
    </li>
  );
}

export default OutputListItem;
