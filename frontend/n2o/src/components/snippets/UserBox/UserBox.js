import React from 'react';
import { compose, withHandlers, withState } from 'recompose';
import { Button } from 'reactstrap';

function UserBox({ image, title, subTitle, children, isOpen, onToggle }) {
  return (
    <div className="n2o-user-box">
      <div className="n2o-user-box__image d-flex justify-content-center">
        <img
          className="d-block mt-2 mb-2"
          src={image}
          alt={title}
          width="100"
          height="100"
        />
      </div>
      <button
        onClick={onToggle}
        className="n2o-user-box__title pl-2 pr-2 text-center"
      >
        {title}
        {children && <i className="fa fa-chevron-down ml-2" />}
      </button>
      {subTitle && (
        <small className="n2o-user-box__sub-title d-block pl-2 pr-2 mb-3 text-center">
          {subTitle}
        </small>
      )}
      {isOpen &&
        React.Children.map(children, child => (
          <div className="n2o-user-box__dropdown-item pl-2 pr-2 text-center">
            {child}
          </div>
        ))}
    </div>
  );
}

export default compose(
  withState('isOpen', 'toggle', false),
  withHandlers({
    onToggle: ({ isOpen, toggle }) => () => toggle(!isOpen),
  })
)(UserBox);
