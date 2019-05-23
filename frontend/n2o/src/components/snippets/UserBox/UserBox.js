import React from 'react';
import cn from 'classnames';
import { compose, withHandlers, withState, lifecycle } from 'recompose';
import { id } from '../../../utils/id';
import { UncontrolledTooltip } from 'reactstrap';

function UserBox({
  id,
  image,
  title,
  subTitle,
  children,
  isOpen,
  onToggle,
  compressed,
}) {
  return (
    <div
      className={cn('n2o-user-box', { 'n2o-user-box--compressed': compressed })}
    >
      {compressed && (title || subTitle) && (
        <UncontrolledTooltip placement="right" target={id}>
          <div>{title}</div>
          <small>{subTitle}</small>
        </UncontrolledTooltip>
      )}
      <div
        id={id}
        className="n2o-user-box__image d-flex justify-content-center"
      >
        <img
          className="d-block"
          src={image}
          alt={title}
          width="70"
          height="70"
        />
      </div>
      {compressed && (
        <button onClick={onToggle} className="n2o-user-box__title">
          <i
            className={cn({
              'fa fa-chevron-up': isOpen,
              'fa fa-chevron-down': !isOpen,
            })}
          />
        </button>
      )}
      {!compressed && (
        <React.Fragment>
          <button
            onClick={onToggle}
            className="n2o-user-box__title pl-2 pr-2 text-center"
          >
            <span
              className={cn({
                'n2o-user-box__title--chevron': children,
                'n2o-user-box__title--chevron-up': isOpen,
              })}
            >
              {title}
            </span>
          </button>
          {subTitle && (
            <small className="n2o-user-box__sub-title d-block pl-2 pr-2 text-center">
              {subTitle}
            </small>
          )}
        </React.Fragment>
      )}
      {isOpen && children && (
        <ul className="n2o-user-box__items">{children}</ul>
      )}
    </div>
  );
}

export default compose(
  withState('isOpen', 'toggle', false),
  withHandlers({
    onToggle: ({ isOpen, toggle }) => () => toggle(!isOpen),
  }),
  lifecycle({
    componentDidMount() {
      this.setState({ id: id() });
    },
  })
)(UserBox);
