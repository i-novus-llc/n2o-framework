import React, { useContext } from 'react'
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
import { Button } from 'reactstrap'

import { REGIONS } from '../../../core/factory/factoryLevels'
// eslint-disable-next-line import/no-named-as-default
import Factory from '../../../core/factory/Factory'
import DefaultPage from '../DefaultPage'
import { ScrollContext } from '../../snippets/ScrollContainer/ScrollContainer'

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
        <DefaultPage
            {...rest}
        >
            <div
                className="n2o-page n2o-page__top-left-right-layout"
            >
                <FixedContainer
                    className="n2o-page__top"
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

const withContext = (Context, mapper) => Component => (props) => {
    const contextValue = useContext(Context)
    const mappedValue = mapper(contextValue)

    return (<Component {...props} {...mappedValue} />)
}

const enhance = compose(
    withContext(ScrollContext, scrollContext => ({ scrollContext })),
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
            setShowScrollButton,
            needScrollButton,
        }) => (scrollContext) => {
            if (timeoutId) { clearTimeout(timeoutId) }

            timeoutId = setTimeout(() => {
                if (needScrollButton) {
                    setShowScrollButton(scrollContext.scrollTop > 100)
                }
            }, 100)
        },
        scrollTo: ({ setShowScrollButton, scrollContext }) => () => {
            scrollContext.scrollTo({
                top: 0,
                behavior: 'smooth',
            })
            setShowScrollButton(false)
        },
    }),
    lifecycle({
        componentDidUpdate({ scrollContext: prevScrollContext }) {
            const {
                addScrollEvent,
                scrollContext,
            } = this.props

            if (scrollContext.scrollTop !== prevScrollContext.scrollTop) {
                addScrollEvent(scrollContext)
            }
        },
    }),
)

export { TopLeftRightPage }
export default enhance(TopLeftRightPage)
