import React from 'react'

export default Component => ({ tableRef, ...rest }) => <Component ref={tableRef} {...rest} />
