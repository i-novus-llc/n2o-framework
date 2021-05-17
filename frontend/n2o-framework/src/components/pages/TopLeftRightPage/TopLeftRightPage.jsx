import React from 'react'
import PropTypes from 'prop-types'
import {
    compose,
    withState,
    mapProps,
    withHandlers,
    lifecycle,
} from 'recompose'
import get from 'lodash/get'
import map from 'lodash/map'
import classNames from 'classnames'
import Button from 'reactstrap/lib/Button'

import { REGIONS } from '../../../core/factory/factoryLevels'
// eslint-disable-next-line import/no-named-as-default
import Factory from '../../../core/factory/Factory'
import DefaultPage from '../DefaultPage'

import FixedContainer from './FixedContainer'

const FixedPlace = {
    TOP: 'top',
    LEFT: 'left',
    RIGHT: 'right',
}

let timeoutId = null

function TopLeftRightPage({
    id,
    regions,
    setContainerRef,
    setFixedRef,
    isFixed,
    style,
    scrollTo,
    showScrollButton,
    places,
    ...rest
}) {
    const topRegion = get(regions, 'top', null)
    const leftRegion = get(regions, 'left', null)
    const rightRegion = get(regions, 'right', null)

    return (
        <DefaultPage {...rest}>
            <div
                className="n2o-page n2o-page__top-left-right-layout"
                ref={setContainerRef}
            >
                <FixedContainer
                    className="n2o-page__top"
                    setRef={setFixedRef(FixedPlace.TOP)}
                    isFixed={isFixed}
                    style={style[FixedPlace.TOP]}
                    {...places[FixedPlace.TOP]}
                >
                    {map(topRegion, (region, index) => (
                        <Factory key={index} level={REGIONS} {...region} pageId={id} />
                    ))}
                </FixedContainer>

                <div className="n2o-page__left-right-layout">
                    <FixedContainer
                        className="n2o-page__left"
                        setRef={setFixedRef(FixedPlace.LEFT)}
                        isFixed={isFixed}
                        style={style[FixedPlace.LEFT]}
                        {...places[FixedPlace.LEFT]}
                    >
                        {map(leftRegion, (region, index) => (
                            <Factory key={index} level={REGIONS} {...region} pageId={id} />
                        ))}
                    </FixedContainer>

                    <FixedContainer
                        className="n2o-page__right"
                        name={FixedPlace.RIGHT}
                        setRef={setFixedRef(FixedPlace.RIGHT)}
                        isFixed={isFixed}
                        style={style[FixedPlace.RIGHT]}
                        {...places[FixedPlace.RIGHT]}
                    >
                        {map(rightRegion, (region, index) => (
                            <Factory key={index} level={REGIONS} {...region} pageId={id} />
                        ))}
                    </FixedContainer>
                </div>
                <Button
                    onClick={scrollTo}
                    color="link"
                    className={classNames('n2o-page__scroll-to-top', {
                        'n2o-page__scroll-to-top--show': showScrollButton,
                    })}
                >
                    <i className="fa fa-arrow-circle-up" />
                </Button>
            </div>
        </DefaultPage>
    )
}

TopLeftRightPage.propTypes = {
    id: PropTypes.string,
    regions: PropTypes.object,
    width: PropTypes.object,
    setContainerRef: PropTypes.func,
    setFixedRef: PropTypes.func,
    isFixed: PropTypes.bool,
    style: PropTypes.object,
    scrollTo: PropTypes.func,
    showScrollButton: PropTypes.bool,
    places: PropTypes.object,
}

TopLeftRightPage.defaultProps = {
    regions: {},
    width: {},
}

const enhance = compose(
    withState('containerRef', 'setContainerRef', null),
    withState('fixedElementRef', 'setFixedElementRef', {}),
    withState('eventListens', 'setEventListens', false),
    withState('isFixed', 'setFixed', null),
    withState('style', 'setStyle', {}),
    withState('showScrollButton', 'setShowScrollButton', false),
    mapProps(props => ({
        ...props,
        places: get(props, 'metadata.places', {}),
        needScrollButton: get(props, 'metadata.needScrollButton', false),
    })),
    withHandlers({
        addScrollEvent: ({
            places,
            setFixed,
            setStyle,
            fixedElementRef,
            setShowScrollButton,
            needScrollButton,
        }) => (e) => {
            if (timeoutId) { clearTimeout(timeoutId) }

            timeoutId = setTimeout(() => {
                let allStyles = {}
                let isFixed = false

                if (needScrollButton) {
                    setShowScrollButton(window.scrollY > 100)
                }

                map(fixedElementRef, (ref, key) => {
                    const offset = get(places, [key, 'offset'], 0)
                    const { body } = e.target
                    const html = e.target.documentElement
                    const position = ref.getBoundingClientRect()
                    const translateY = Math.abs(position.top) + offset
                    const bodyHeight = Math.max(
                        body.scrollHeight,
                        body.offsetHeight,
                        html.clientHeight,
                        html.scrollHeight,
                        html.offsetHeight,
                    )
                    const xEndPosition =
            ref.clientHeight + translateY + Math.abs(ref.offsetTop)

                    if (bodyHeight < xEndPosition) { return }

                    const style = {
                        transform: `translate(0, ${translateY}px)`,
                    }

                    isFixed = position.y <= 0

                    allStyles = {
                        ...allStyles,
                        [key]: position.y <= 0 ? style : {},
                    }
                })

                if (fixedElementRef.top) {
                    allStyles = {
                        ...allStyles,
                        left: allStyles.top,
                        right: allStyles.top,
                    }
                }

                setStyle(allStyles)
                setFixed(isFixed)
            }, 100)
        },
        scrollTo: ({ setShowScrollButton }) => () => {
            window.scrollTo({
                top: 0,
                behavior: 'smooth',
            })
            setShowScrollButton(false)
        },
        setFixedRef: ({ fixedElementRef, setFixedElementRef }) => name => (ref) => {
            if (ref && !fixedElementRef[name]) {
                setFixedElementRef({ ...fixedElementRef, [name]: ref })
            }
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
            } = this.props

            if (!eventListens && containerRef && fixedElementRef) {
                window.addEventListener('scroll', addScrollEvent)

                setEventListens(true)
            }
        },

        componentWillUnmount() {
            const { addScrollEvent } = this.props

            window.removeEventListener('scroll', addScrollEvent)
        },
    }),
)

export { TopLeftRightPage }
export default enhance(TopLeftRightPage)
