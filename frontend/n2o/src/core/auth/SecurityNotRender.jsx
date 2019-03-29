import React from 'react';
import { isEmpty } from 'lodash';
import SecurityCheck from './SecurityCheck';
import PropTypes from 'prop-types';

/**
 * Вспомогательная функция для SecurityCheck
 * @param config
 * @param component
 * @param rest
 * @returns {*}
 * @constructor
 */
function SecurityNotRender({ component, config, ...rest }) {
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

SecurityNotRender.propTypes = {
  authProvider: PropTypes.func,
  config: PropTypes.object,
  user: PropTypes.object,
  onPermissionsSet: PropTypes.func
};

export default SecurityNotRender;
