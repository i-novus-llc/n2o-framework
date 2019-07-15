import React from 'react';

import N2o from '../N2o';

import EmbeddedRoute from './core/EmbeddedRoute';
import createConfig from './createConfig';

function Framework() {
  return (
    <N2o {...createConfig()}>
      <EmbeddedRoute />
    </N2o>
  );
}

export default Framework;
