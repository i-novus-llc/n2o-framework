import React from 'react';
import cn from 'classnames';

function FixedContainer({
  name,
  className,
  setRef,
  fixed,
  isFixed,
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
          // 'n2o-page__fixed--animate-up':
          //   isFixed === false && fixedCurrentContainer,
        })}
        style={fixedCurrentContainer ? style : {}}
      >
        {children}
      </div>
    </div>
  );
}

export default FixedContainer;
