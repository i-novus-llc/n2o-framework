import React from 'react';
import PropTypes from 'prop-types';
import {
  compose,
  withState,
  mapProps,
  withHandlers,
  lifecycle,
} from 'recompose';
import get from 'lodash/get';
import map from 'lodash/map';
import Button from 'reactstrap/lib/Button';

import { REGIONS } from '../../../core/factory/factoryLevels';
import FixedContainer from './FixedContainer';
import Factory from '../../../core/factory/Factory';
import DefaultPage from '../DefaultPage';

const FixedPlace = {
  TOP: 'top',
  LEFT: 'left',
  RIGHT: 'right',
};

function TopLeftRightPage({
  id,
  regions,
  width,
  setContainerRef,
  setFixedElementRef,
  fixed,
  isFixed,
  style,
  needScrollButton,
  ...rest
}) {
  const topRegion = get(regions, 'top', null);
  const leftRegion = get(regions, 'left', null);
  const rightRegion = get(regions, 'right', null);

  return (
    <DefaultPage {...rest}>
      <div
        className="n2o-page n2o-page__top-left-right-layout"
        ref={setContainerRef}
      >
        <FixedContainer
          className="n2o-page__top"
          name="top"
          setRef={setFixedElementRef}
          width={width}
          fixed={fixed}
          isFixed={isFixed}
          style={style}
        >
          {map(topRegion, (region, index) => (
            <Factory key={index} level={REGIONS} {...region} pageId={id} />
          ))}
        </FixedContainer>

        <div className="n2o-page__left-right-layout">
          <FixedContainer
            className="n2o-page__left"
            name="left"
            setRef={setFixedElementRef}
            width={width}
            fixed={fixed}
            isFixed={isFixed}
            style={style}
          >
            {map(leftRegion, (region, index) => (
              <Factory key={index} level={REGIONS} {...region} pageId={id} />
            ))}
          </FixedContainer>

          <FixedContainer
            className="n2o-page__right"
            name="right"
            setRef={setFixedElementRef}
            width={width}
            fixed={fixed}
            isFixed={isFixed}
            style={style}
          >
            {map(rightRegion, (region, index) => (
              <Factory key={index} level={REGIONS} {...region} pageId={id} />
            ))}
          </FixedContainer>
        </div>
        {needScrollButton && (
          <Button color="link" className="n2o-page__scroll-to-top">
            <i className="fa fa-arrow-circle-up" />
          </Button>
        )}
      </div>
    </DefaultPage>
  );
}

TopLeftRightPage.propTypes = {
  id: PropTypes.string,
  regions: PropTypes.object,
  width: PropTypes.object,
};

TopLeftRightPage.defaultProps = {
  regions: {},
  width: {},
};

const enhance = compose(
  withState('containerRef', 'setContainerRef', null),
  withState('fixedElementRef', 'setFixedElementRef', null),
  withState('eventListens', 'setEventListens', false),
  withState('isFixed', 'setFixed', null),
  withState('style', 'setStyle', {}),
  mapProps(props => ({
    ...props,
    width: get(props, 'metadata.width', {}),
    fixed: get(props, 'metadata.fixed', false),
    fixedOffset: get(props, 'metadata.fixedOffset', 0),
    needScrollButton: get(props, 'metadata.needScrollButton', false),
  })),
  withHandlers({
    addScrollEvent: ({
      fixed,
      fixedOffset,
      setFixed,
      setEventListens,
      setStyle,
    }) => (element, containerRef) => {
      let timeoutId = null;

      window.addEventListener('scroll', () => {
        if (timeoutId) clearTimeout(timeoutId);

        timeoutId = setTimeout(() => {
          // const containerPosition = containerRef.getBoundingClientRect();
          const position = element.getBoundingClientRect();
          // const rightPosition =
          //   containerPosition.right - containerPosition.width;
          // const leftPosition = containerPosition.left;
          const translateY = Math.abs(position.top) + fixedOffset;
          console.log('point');
          console.log(position.top);
          console.log(Math.abs(position.top));
          console.log(fixedOffset);
          console.log(translateY);
          let style = {
            // position: 'relative',
            // width: position.width,
            // height: position.height,
            // top: 0,
            transform: `translate(0, ${translateY}px)`,
          };

          // if (fixed === FixedPlace.RIGHT) {
          //   set(style, 'right', rightPosition);
          // } else {
          //   set(style, 'left', leftPosition);
          // }

          setStyle(position.y <= 0 ? style : {});
          setFixed(position.y <= 0);
        }, 50);
      });

      setEventListens(true);
    },
  }),
  lifecycle({
    componentDidUpdate() {
      const {
        eventListens,
        containerRef,
        fixedElementRef,
        addScrollEvent,
      } = this.props;

      if (!eventListens && containerRef && fixedElementRef) {
        addScrollEvent(fixedElementRef, containerRef);
      }
    },
  })
);

export { TopLeftRightPage };
export default enhance(TopLeftRightPage);
