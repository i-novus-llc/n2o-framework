import React from 'react';

import Base from '../Base';

function Paragraph({ ...rest }) {
  const tag = 'div';

  return <Base tag={tag} {...rest} />;
}

export default Paragraph;
