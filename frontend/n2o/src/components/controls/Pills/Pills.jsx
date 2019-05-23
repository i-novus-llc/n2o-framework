import React from 'react';
import PropTypes from 'prop-types';
import { Nav, NavItem, NavLink } from 'reactstrap';
import { map } from 'lodash';

/**
 * Контролл Pills
 * @param data - данные
 * @param rest
 * @returns {*}
 * @constructor
 */
function Pills({ data, onClick, ...rest }) {
  const handleOnClick = id => e => {
    onClick(e, id);
  };

  const renderPills = ({ id, label, active }) => (
    <NavItem>
      <NavLink href="#" active={active} onClick={handleOnClick(id)}>
        {label}
      </NavLink>
    </NavItem>
  );

  return (
    <Nav pills {...rest}>
      {map(data, renderPills)}
    </Nav>
  );
}

Pills.propTypes = {
  data: PropTypes.array,
};
Pills.defaultProps = {
  multiSelect: [],
};

export default Pills;
