import React from 'react';
import { compose } from 'recompose';

import withRegisterDependency from './withRegisterDependency';
import withObserveDependency from './withObserveDependency';

const withDependency = (WrappedComponent, handleEventConfig) => {
  return compose(
    withRegisterDependency,
    withObserveDependency(handleEventConfig)
  )(WrappedComponent);
};

export default withDependency;
