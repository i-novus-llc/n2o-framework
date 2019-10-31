import React from 'react';
import { compose } from 'recompose';

import withRegisterDependency from './withRegisterDependency';
import withObserveDependency from './withObserveDependency';

const withDependency = handleEventConfig => {
  return WrappedComponent => {
    return compose(
      withRegisterDependency,
      withObserveDependency(handleEventConfig)
    )(WrappedComponent);
  };
};

export default withDependency;
