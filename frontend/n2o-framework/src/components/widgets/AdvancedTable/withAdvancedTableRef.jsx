import React from 'react'

// eslint-disable-next-line react/prop-types
export default Component => ({ tableRef, ...rest }) => <Component ref={tableRef} {...rest} />
