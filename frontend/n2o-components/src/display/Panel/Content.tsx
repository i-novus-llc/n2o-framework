import React from 'react'
import classNames from 'classnames'

import { ContentProps } from './types'

export const Content = ({
    children,
    isActive,
    openAnimation,
    forceRender,
    destroyInactivePanel,
    contentRef,
}: ContentProps) => {
    const shouldRenderChildren = forceRender || isActive || !destroyInactivePanel

    return (
        <div
            ref={contentRef}
            className={classNames('collapse-panel-content', {
                'collapse-panel-content-active': isActive,
                'collapse-panel-content-inactive': !isActive,
            })}
            aria-hidden={!isActive}
            style={{
                transition: `height ${openAnimation.duration}ms ${openAnimation.easing}, opacity ${openAnimation.duration}ms ${openAnimation.easing}`,
            }}
        >
            <div className="collapse-panel-content-box">
                {shouldRenderChildren ? children : null}
            </div>
        </div>
    )
}
