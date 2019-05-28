import React from 'react';
import { compose, withState, withHandlers } from 'recompose';

function SidebarDropdown({ label, children, isOpen, toggle }) {
  return (
    <div className="n2o-sidebar__item-dropdown">
      <div className="n2o-sidebar__item-dropdown-label" onClick={toggle}>
        <span>{label}</span>
      </div>
      {isOpen && <div className="n2o-sidebar__subitems">{children}</div>}
    </div>
  );
}

export default compose(
  withState('isOpen', 'setOpen', false),
  withHandlers({
    toggle: ({ isOpen, setOpen }) => () => setOpen(!isOpen),
  })
)(SidebarDropdown);
