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
import cn from 'classnames';
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

let timeoutId = null;

function TopLeftRightPage({
  id,
  regions,
  width,
  setContainerRef,
  setFixedElementRef,
  fixed,
  isFixed,
  style,
  scrollTo,
  showScrollButton,
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
          name={FixedPlace.TOP}
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
            name={FixedPlace.LEFT}
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
            name={FixedPlace.RIGHT}
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
        <Button
          onClick={scrollTo}
          color="link"
          className={cn('n2o-page__scroll-to-top', {
            'n2o-page__scroll-to-top--show': showScrollButton,
          })}
        >
          <i className="fa fa-arrow-circle-up" />
        </Button>
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
  withState('showScrollButton', 'setShowScrollButton', false),
  mapProps(props => ({
    ...props,
    width: get(props, 'metadata.width', {}),
    fixed: get(props, 'metadata.fixed', false),
    fixedOffset: get(props, 'metadata.fixedOffset', 0),
    needScrollButton: get(props, 'metadata.needScrollButton', false),
  })),
  withHandlers({
    addScrollEvent: ({
      fixedOffset,
      setFixed,
      setStyle,
      fixedElementRef,
      setShowScrollButton,
      needScrollButton,
    }) => () => {
      if (timeoutId) clearTimeout(timeoutId);

      timeoutId = setTimeout(() => {
        if (needScrollButton) {
          setShowScrollButton(window.scrollY > 100);
        }
        const position = fixedElementRef.getBoundingClientRect();
        const translateY = Math.abs(position.top) + fixedOffset;
        let style = {
          transform: `translate(0, ${translateY}px)`,
        };

        setStyle(position.y <= 0 ? style : {});
        setFixed(position.y <= 0);
      }, 100);
    },
    scrollTo: () => () => {
      window.scrollTo({
        top: 0,
        behavior: 'smooth',
      });
    },
  }),
  lifecycle({
    componentDidUpdate() {
      const {
        eventListens,
        containerRef,
        fixedElementRef,
        addScrollEvent,
        setEventListens,
      } = this.props;

      if (!eventListens && containerRef && fixedElementRef) {
        window.addEventListener('scroll', addScrollEvent);

        setEventListens(true);
      }
    },

    componentWillUnmount() {
      const { addScrollEvent } = this.props;

      window.removeEventListener('scroll', addScrollEvent);
    },
  })
);

export { TopLeftRightPage };
export default enhance(TopLeftRightPage);
