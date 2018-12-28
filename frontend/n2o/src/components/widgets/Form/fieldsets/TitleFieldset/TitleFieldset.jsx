import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';

function TitleFieldset({ render, rows, title, showLine, className, subTitle }) {
  return (
    <div className={'title-fieldset'}>
      <div className={cn('title-fieldset-header', { [className]: className })}>
        {title && <span className="title-fieldset-text">{title}</span>}
        {subTitle && <small className="text-muted title-fieldset-subtitle">{subTitle}</small>}
        {showLine && <div className="title-fieldset-line" />}
      </div>
      {render(rows)}
    </div>
  );
}

TitleFieldset.propTypes = {
  render: PropTypes.func,
  rows: PropTypes.array,
  title: PropTypes.string,
  showLine: PropTypes.bool,
  className: PropTypes.string,
  subtitle: PropTypes.string
};

TitleFieldset.defaultProps = {
  showLine: true
};

export default TitleFieldset;
