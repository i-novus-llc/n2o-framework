import React from 'react';

import Base from '../Base';

function Title({ level, ...rest }) {
  const tag = `h${level}`;

  return <Base tag={tag} {...rest} copiable={false} />;
}

export default Title;
