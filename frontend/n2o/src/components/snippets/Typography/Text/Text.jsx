import React from 'react';

import Base from '../Base';

function Text({ ...rest }) {
  const tag = `p`;

  return <Base tag={tag} {...rest} />;
}

export default Text;
