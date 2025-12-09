import React, { CSSProperties, ReactNode } from 'react'
import classNames from 'classnames'

import { Spinner, SpinnerType } from '../../../factoryComponents/Spinner'

export enum PLACES {
    top = 'top',
    left = 'left',
    right = 'right',
    center = 'center',
    topLeft = 'topLeft',
    topRight = 'topRight',
    topCenter = 'topCenter',
    bottomLeft = 'bottomLeft',
    bottomRight = 'bottomRight',
    bottomCenter = 'bottomCenter',
}

export interface WidgetLayoutProps {
    className?: string
    style?: CSSProperties
    filterComponent: JSX.Element | ReactNode
    filterPlace: PLACES
    topToolbars: JSX.Element[] | ReactNode[]
    bottomToolbars: JSX.Element[] | ReactNode[]
    scrollbar: JSX.Element | ReactNode
    stickyFooter: boolean
    loading: boolean
    children: ReactNode | null
}

/** Разметка компонентов виджета */
export function WidgetLayout({
    className,
    style,
    filterComponent,
    filterPlace,
    topToolbars,
    bottomToolbars,
    scrollbar,
    stickyFooter,
    loading,
    children,
}: WidgetLayoutProps) {
    return (
        <div className={className} style={style}>
            {filterPlace === PLACES.left && (
                <div className="n2o-standard-widget-layout-aside n2o-standard-widget-layout-aside--left">
                    {filterComponent}
                </div>
            )}
            <div className="n2o-standard-widget-layout-center">
                {filterPlace === PLACES.top && (
                    <div className="n2o-standard-widget-layout-center-filter">
                        {filterComponent}
                    </div>
                )}
                {!!topToolbars.length && (
                    <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar-top">
                        {topToolbars}
                    </div>
                )}
                <div className="n2o-standard-widget-layout-content">
                    <Spinner loading={loading} type={SpinnerType.cover}>
                        {children}
                    </Spinner>
                </div>
                {(!!bottomToolbars.length || !!scrollbar) && (
                    <div className={classNames(
                        'n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar-bottom',
                        { 'sticky-footer': stickyFooter },
                    )}
                    >
                        {scrollbar}
                        {bottomToolbars}
                    </div>
                )}
            </div>
            {filterPlace === PLACES.right && (
                <div className="n2o-standard-widget-layout-aside n2o-standard-widget-layout-aside--right">
                    {filterComponent}
                </div>
            )}
        </div>
    )
}
