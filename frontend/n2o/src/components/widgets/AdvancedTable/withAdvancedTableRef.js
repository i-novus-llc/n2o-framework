import React from 'react';

export default Component => ({ tableRef, ...rest }) => {
  return <Component ref={tableRef} {...rest} />;
};
