import React, { useCallback, useContext, useEffect, useState, Context, ReactComponentElement, ReactElement } from 'react'
import classNames from 'classnames'
import { ScrollContext, type State as ScrollContextProps } from '@i-novus/n2o-components/lib/layouts/ScrollContainer'

import { DefaultPage } from '../DefaultPage'
import { TopLeftRightPageProps, FIXED_PLACE, Places } from '../types'
import { EMPTY_OBJECT } from '../../../utils/emptyTypes'

import { Region } from './Region'
import { FixedContainer } from './FixedContainer'

let timeout: NodeJS.Timeout | null = null

const getStyle = (place: FIXED_PLACE, places?: Places) => {
    if (!places) { return {} }

    const { fixed, width } = places[place]

    return { fixed, width }
}

function TopLeftRightPageBody({
    id,
    metadata,
    scrollContext,
    regions = EMPTY_OBJECT,
    isFixed = false,
    style,
    ...rest
}: TopLeftRightPageProps) {
    const [showScrollButton, setShowScrollButton] = useState<boolean>(false)

    const { top, left, right } = regions

    const { places, needScrollButton = false } = metadata || {}

    const scrollEvent = useCallback(() => {
        if (timeout) { clearTimeout(timeout) }

        timeout = setTimeout(() => {
            if (needScrollButton && scrollContext?.scrollTop) {
                setShowScrollButton(scrollContext.scrollTop > 100)
            }
        }, 100)
    }, [needScrollButton, scrollContext.scrollTop])

    const scroll = useCallback(() => {
        if (scrollContext?.scrollTo) {
            scrollContext.scrollTo({ top: 0, behavior: 'smooth' })
        }

        setShowScrollButton(false)
    }, [scrollContext])

    useEffect(() => {
        scrollEvent()
    }, [scrollContext.scrollTop, scrollEvent])

    return (
        <DefaultPage metadata={metadata} {...rest}>
            <div className="n2o-page n2o-page__top-left-right-layout">
                <FixedContainer
                    className="n2o-page__top"
                    fixed={isFixed}
                    style={style}
                    {...getStyle(FIXED_PLACE.TOP, places)}
                >
                    <Region region={top} pageId={id} />
                </FixedContainer>

                <div className="n2o-page__left-right-layout">
                    <FixedContainer
                        className="n2o-page__left"
                        fixed={isFixed}
                        style={style}
                        {...getStyle(FIXED_PLACE.LEFT, places)}
                    >
                        <Region region={left} pageId={id} />
                    </FixedContainer>

                    <FixedContainer
                        className="n2o-page__right"
                        fixed={isFixed}
                        style={style}
                        {...getStyle(FIXED_PLACE.RIGHT, places)}
                    >
                        <Region region={right} pageId={id} />
                    </FixedContainer>
                </div>
                <i
                    onClick={scroll}
                    color="link"
                    className={classNames('n2o-page__scroll-to-top fa fa-arrow-circle-up', {
                        'n2o-page__scroll-to-top--show': showScrollButton,
                    })}
                />
            </div>
        </DefaultPage>
    )
}

type Mapper = (context: ScrollContextProps) => Record<string, unknown>

const withContext = (Context: Context<ScrollContextProps>, mapper: Mapper) => {
    return (Component: React.ComponentType<TopLeftRightPageProps>) => {
        return (props: TopLeftRightPageProps) => {
            const contextValue = useContext(Context)
            const mappedValue = mapper(contextValue)

            return <Component {...props} {...mappedValue} />
        }
    }
}

export { TopLeftRightPageBody }
export const TopLeftRightPage = withContext(ScrollContext, (scrollContext: ScrollContextProps) => ({ scrollContext }))(TopLeftRightPageBody)
export default TopLeftRightPage
