import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';

function FixedContainer({
  name,
  className,
  setRef,
  fixed,
  width,
  style,
  children,
}) {
  const fixedCurrentContainer = fixed === name;
  const ref = fixedCurrentContainer ? setRef : undefined;

  return (
    <div
      className={className}
      ref={ref}
      style={{
        width: width[name],
        height: fixedCurrentContainer && style.height,
      }}
    >
      <div
        className={cn('n2o-page__fixed-container', {
          'n2o-page__fixed-container--fixed': fixedCurrentContainer,
        })}
        style={fixedCurrentContainer ? style : {}}
      >
        {children}
      </div>
    </div>
  );
}

FixedContainer.propTypes = {
  name: PropTypes.string,
  className: PropTypes.string,
  setRef: PropTypes.func,
  fixed: PropTypes.string,
  width: PropTypes.object,
  style: PropTypes.object,
  children: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.node,
    PropTypes.element,
  ]),
};

FixedContainer.defaultProps = {
  width: {},
};

export default FixedContainer;
