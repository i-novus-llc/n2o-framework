import React from 'react';
import { isEmpty } from 'lodash';
import SecurityCheck from './SecurityCheck';
import PropTypes from 'prop-types';

/**
 * Вспомогательная функция для SecurityCheck
 * @param security
 * @param component
 * @returns {*}
 * @constructor
 */
function Security({ component, config, ...rest }) {
  return isEmpty(config) ? (
    component
  ) : (
    <SecurityCheck
      config={config}
      {...rest}
      render={({ permissions }) => {
        return permissions ? component : null;
      }}
    />
  );
}

Security.propTypes = {
  authProvider: PropTypes.func,
  config: PropTypes.object,
  user: PropTypes.object,
  onPermissionsSet: PropTypes.func
};

export default Security;
