import React, { ReactElement, useMemo } from 'react'
import { Breadcrumb, BreadcrumbItem, BreadcrumbProps } from 'reactstrap'
import classNames from 'classnames'

import { breadcrumb } from './const'
import { Crumb } from './Crumb'

type ElementVisibility = {
    header: boolean,
    footer: boolean,
    breadcrumb: boolean,
}

type WindowType = Window & typeof globalThis & {
    N2O_ELEMENT_VISIBILITY: ElementVisibility
}

export function SimpleBreadcrumb({ items = [] }: { items: breadcrumb }): ReactElement<BreadcrumbProps> {
    return (
        <Breadcrumb className="n2o-breadcrumb">
            {
                items.map(({ label, path }) => {
                    const disabled = !path

                    return (
                        <BreadcrumbItem className={classNames({ disabled })}>
                            <Crumb label={label} path={path} />
                        </BreadcrumbItem>
                    )
                })
            }
        </Breadcrumb>
    )
}

export function DefaultBreadcrumb({ items = [] }: { items: breadcrumb }): ReactElement<BreadcrumbProps> | null {
    const { N2O_ELEMENT_VISIBILITY = { breadcrumb: true } as ElementVisibility } = window as WindowType

    const isBreadcrumbHidden = useMemo(
        () => (!items.length || !N2O_ELEMENT_VISIBILITY.breadcrumb),
        [N2O_ELEMENT_VISIBILITY.breadcrumb, items.length],
    )

    return isBreadcrumbHidden ? null : <SimpleBreadcrumb items={items} />
}

export default DefaultBreadcrumb
